gson-null-safe
==============

Null-safe JSON deserialization with Gson. It's working! Kind of.

The deserialization is done by `ReflectiveTypeAdapterFactory`
included in Gson with checks and fixes done by
`NullSafeTypeAdapterFactory`.

It is not a complete solution because the `ReflectiveTypeAdapterFactory`
is not properly set up. E.g. the `excluder` is the default one which
does not contain the user specified field and class exclusions. There
are probably other problems, too.

To use it, register `NullSafeTypeAdapterFactory` and annotate your
classes with either `@NullSafeNullable` or `@NullSafeOptional`:

- `@NullSafeNullable` means that a `JsonParseException` will be thrown if
after deserialization any of the fields are `null` except for `@Nullable`
annotated fields.
- `@NullSafeOptional` means that a `JsonParseException` will be thrown if
after deserialization any of the fields are `null`. You can specify
optional fields using the `Optional<T>` type from Guava, and these fields
will be properly populated with `Optional.absent()` if the JSON had a
`null` value for the given field or it did not contain the field at all.
