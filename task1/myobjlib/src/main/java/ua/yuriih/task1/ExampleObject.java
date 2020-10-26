package ua.yuriih.task1;

import java.io.Serializable;
import java.util.Objects;

public class ExampleObject implements Serializable {
    private final int exampleInt;
    private final String exampleString;
    
    public ExampleObject(int exampleInt, String exampleString) {
        this.exampleInt = exampleInt;
        this.exampleString = exampleString;
    }

    public int getExampleInt() {
        return exampleInt;
    }

    public String getExampleString() {
        return exampleString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleObject that = (ExampleObject) o;
        return getExampleInt() == that.getExampleInt() &&
                Objects.equals(getExampleString(), that.getExampleString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExampleInt(), getExampleString());
    }
}
