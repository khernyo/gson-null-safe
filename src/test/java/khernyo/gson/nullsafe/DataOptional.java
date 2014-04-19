package khernyo.gson.nullsafe;

import com.google.common.base.Optional;

import javax.annotation.Nullable;

class DataOptional {
    public final Optional<Integer> i;

    DataOptional(Optional<Integer> i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataOptional that = (DataOptional) o;

        if (!i.equals(that.i)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return i.hashCode();
    }

    @Override
    public String toString() {
        return "DataOptional{" +
                "i=" + i +
                '}';
    }
}
