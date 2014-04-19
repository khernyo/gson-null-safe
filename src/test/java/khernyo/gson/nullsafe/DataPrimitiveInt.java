package khernyo.gson.nullsafe;

import javax.annotation.Nullable;

@NullSafeNullable
class DataPrimitiveInt {
    public final int i;

    DataPrimitiveInt(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPrimitiveInt that = (DataPrimitiveInt) o;

        if (i != that.i) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i;
    }

    @Override
    public String toString() {
        return "DataPrimitiveInt{" +
                "i=" + i +
                '}';
    }
}

@NullSafeNullable
class DataPrimitiveIntNullable {
    @Nullable
    public final int i;

    DataPrimitiveIntNullable(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPrimitiveIntNullable that = (DataPrimitiveIntNullable) o;

        if (i != that.i) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i;
    }

    @Override
    public String toString() {
        return "DataPrimitiveIntNullable{" +
                "i=" + i +
                '}';
    }
}

@NullSafeOptional
class DataPrimitiveIntOptional {
    public final int i;

    DataPrimitiveIntOptional(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPrimitiveIntOptional that = (DataPrimitiveIntOptional) o;

        if (i != that.i) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i;
    }

    @Override
    public String toString() {
        return "DataPrimitiveIntOptional{" +
                "i=" + i +
                '}';
    }
}
