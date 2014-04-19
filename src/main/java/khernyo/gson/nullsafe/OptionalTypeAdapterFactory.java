package khernyo.gson.nullsafe;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class OptionalTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        return Optional.class.isAssignableFrom(type.getRawType()) ? new OptionalTypeAdapter<>(gson, type) : null;
    }
}

class OptionalTypeAdapter<T> extends TypeAdapter<T> {
    private final Gson gson;
    private final Type wrappedType;

    public OptionalTypeAdapter(Gson gson, TypeToken<T> type) {
        this.gson = gson;
        this.wrappedType = ((ParameterizedType) type.getType()).getActualTypeArguments()[0];
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        final Optional<?> optional = (Optional<?>) value;
        gson.toJson(optional.isPresent() ? optional.get() : null, wrappedType, out);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        final Object value = gson.fromJson(in, wrappedType);
        @SuppressWarnings("unchecked")
        final T result = (T) Optional.fromNullable(value);
        return result;
    }
}
