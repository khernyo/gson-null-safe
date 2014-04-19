package khernyo.gson.nullsafe;

import com.google.common.base.Optional;

import javax.annotation.Nullable;

@NullSafeNullable
class DataInteger {
    public final Integer i;

    DataInteger(Integer i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataInteger that = (DataInteger) o;

        if (!i.equals(that.i)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i.hashCode();
    }

    @Override
    public String toString() {
        return "DataInteger{" +
                "i=" + i +
                '}';
    }
}

@NullSafeNullable
class DataIntegerNullable {
    @Nullable
    public final Integer i;

    DataIntegerNullable(@Nullable Integer i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataIntegerNullable that = (DataIntegerNullable) o;

        if (i != null ? !i.equals(that.i) : that.i != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i != null ? i.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DataIntegerNullable{" +
                "i=" + i +
                '}';
    }
}

@NullSafeOptional
class DataIntegerOptional {
    public final Optional<Integer> i;

    DataIntegerOptional(Optional<Integer> i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataIntegerOptional that = (DataIntegerOptional) o;

        if (!i.equals(that.i)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i.hashCode();
    }

    @Override
    public String toString() {
        return "DataIntegerOptional{" +
                "i=" + i +
                '}';
    }
}
