package khernyo.gson.nullsafe;

import com.google.common.base.Optional;

import javax.annotation.Nullable;

@NullSafeNullable
class DataString {
    public final String s;

    DataString(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataString that = (DataString) o;

        if (!s.equals(that.s)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }
}

@NullSafeNullable
class DataStringNullable {
    @Nullable
    public final String s;

    DataStringNullable(@Nullable String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataStringNullable that = (DataStringNullable) o;

        if (s != null ? !s.equals(that.s) : that.s != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return s != null ? s.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DataStringNullable{" +
                "s='" + s + '\'' +
                '}';
    }
}

@NullSafeOptional
class DataStringOptional {
    public final Optional<String> s;

    DataStringOptional(Optional<String> s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataStringOptional that = (DataStringOptional) o;

        if (!s.equals(that.s)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }

    @Override
    public String toString() {
        return "DataStringOptional{" +
                "s=" + s +
                '}';
    }
}
