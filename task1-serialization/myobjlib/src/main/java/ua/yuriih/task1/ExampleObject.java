package ua.yuriih.task1;

import java.io.Serializable;
import java.util.Objects;

public class ExampleObject implements Serializable {
    private final int exampleInt;
    private final String exampleString;
    private transient final int otherInt;
    
    public ExampleObject(int exampleInt, String exampleString, int otherInt) {
        this.exampleInt = exampleInt;
        this.exampleString = exampleString;
        this.otherInt = otherInt;
    }

    public int getExampleInt() {
        return exampleInt;
    }

    public String getExampleString() {
        return exampleString;
    }

    public int getOtherInt() {
        return otherInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleObject object = (ExampleObject) o;
        return getExampleInt() == object.getExampleInt() &&
                getOtherInt() == object.getOtherInt() &&
                Objects.equals(getExampleString(), object.getExampleString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExampleInt(), getExampleString(), getOtherInt());
    }

    @Override
    public String toString() {
        return "ExampleObject{" +
                "exampleInt=" + exampleInt +
                ", exampleString='" + exampleString + '\'' +
                ", otherInt=" + otherInt +
                '}';
    }
}
