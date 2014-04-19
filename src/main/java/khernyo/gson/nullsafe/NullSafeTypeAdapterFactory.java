package khernyo.gson.nullsafe;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class NullSafeTypeAdapterFactory implements TypeAdapterFactory {

    private final Excluder excluder = Excluder.DEFAULT;
    private final ConstructorConstructor constructorConstructor =
            new ConstructorConstructor(ImmutableMap.<Type, InstanceCreator<?>>of());
    private final ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory =
            new ReflectiveTypeAdapterFactory(constructorConstructor, FieldNamingPolicy.IDENTITY, excluder);
    private final OptionalTypeAdapterFactory optionalTypeAdapterFactory = new OptionalTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> optionalTypeAdapter = optionalTypeAdapterFactory.create(gson, type);
        if (optionalTypeAdapter != null) {
            return optionalTypeAdapter;
        }

        final boolean nullable = type.getRawType().getAnnotation(NullSafeNullable.class) != null;
        final boolean optional = type.getRawType().getAnnotation(NullSafeOptional.class) != null;
        if (!nullable && !optional) {
            return null;
        } else if (nullable && optional) {
            throw new IllegalArgumentException(
                    format("Class %s has both %s and %s annotations. At most one of them can be used on a class.",
                            type,
                            NullSafeNullable.class.getSimpleName(),
                            NullSafeOptional.class.getSimpleName()));
        }

        final Class<?> annotation = nullable ? NullSafeNullable.class : NullSafeOptional.class;
        checkAllReferenceTypes(annotation, type);

        return nullable ? createNullableTypeAdapter(gson, type) : createOptionalTypeAdapter(gson, type);
    }

    private <T> TypeAdapter<T> createNullableTypeAdapter(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> typeAdapter = reflectiveTypeAdapterFactory.create(gson, type);
        return new NullSafeNullableTypeAdapter<>(typeAdapter);
    }

    private <T> TypeAdapter<T> createOptionalTypeAdapter(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> typeAdapter = reflectiveTypeAdapterFactory.create(gson, type);
        return new NullSafeOptionalTypeAdapter<>(typeAdapter);
    }

    private boolean excludeField(Field field) {
        // FIXME what about serialization?
        return excluder.excludeClass(field.getType(), false) || excluder.excludeField(field, false);
    }

    private List<Field> collectFields(Class<?> clazz) {
        final ArrayList<Field> result = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            final Field[] fields = c.getDeclaredFields();
            for (Field field: fields) {
                if (!excludeField(field)) {
                    field.setAccessible(true);
                    result.add(field);
                }
            }
        }
        return result;
    }

    private void checkAllReferenceTypes(Class<?> annotation, TypeToken<?> type) {
        final Class<?> rawType = type.getRawType();
        final List<Field> fields = collectFields(rawType);
        for (Field field: fields) {
            if (!isReferenceType(field)) {
                throw new IllegalArgumentException(
                        format("@%s annotated %s contains non-reference typed field: %s %s",
                                annotation.getSimpleName(),
                                rawType,
                                field.getType(),
                                field.getName()));
            }
        }
    }

    private boolean isReferenceType(Field field) {
        return Object.class.isAssignableFrom(field.getType());
    }

    private Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldValue(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkFieldNotNull(Field field, Object obj) {
        if (getFieldValue(field, obj) == null) {
            throw new JsonParseException(
                    format("Field %s of %s is null after deserialization",
                            field.getName(),
                            field.getType()));
        }
    }

    private class NullSafeNullableTypeAdapter<T> extends TypeAdapter<T> {
        private final TypeAdapter<T> typeAdapter;

        public NullSafeNullableTypeAdapter(TypeAdapter<T> typeAdapter) {
            this.typeAdapter = typeAdapter;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            typeAdapter.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            final T value = typeAdapter.read(in);
            validate(value);
            return value;
        }

        private void validate(T value) {
            for (Field field: collectFields(value.getClass())) {
                final boolean nullable = field.getAnnotation(Nullable.class) != null;
                if (!nullable) {
                    checkFieldNotNull(field, value);
                }
            }
        }
    }

    private class NullSafeOptionalTypeAdapter<T> extends TypeAdapter<T> {
        private final TypeAdapter<T> typeAdapter;

        public NullSafeOptionalTypeAdapter(TypeAdapter<T> typeAdapter) {
            this.typeAdapter = typeAdapter;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            typeAdapter.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            final T value = typeAdapter.read(in);
            fixOptionalFields(value);
            validate(value);
            return value;
        }

        private void fixOptionalFields(T value) {
            for (Field field: collectFields(value.getClass())) {
                final boolean optional = Optional.class.isAssignableFrom(field.getType());
                if (optional && getFieldValue(field, value) == null) {
                    setFieldValue(field, value, Optional.absent());
                }
            }
        }

        private void validate(T value) {
            for (Field field: collectFields(value.getClass())) {
                checkFieldNotNull(field, value);
            }
        }
    }
}
