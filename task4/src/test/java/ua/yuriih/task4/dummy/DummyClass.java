package ua.yuriih.task4.dummy;

import java.util.Arrays;
import java.util.Objects;

public class DummyClass {
    public int dummyInt;
    private String dummyString;
    float[] dummyFloatArray;

    private DummyClass() {

    }
    
    public DummyClass(int dummyInt, String dummyString, float[] dummyFloatArray) {
        this.dummyInt = dummyInt;
        this.dummyString = dummyString;
        this.dummyFloatArray = dummyFloatArray;
    }

    public String getDummyString() {
        return dummyString;
    }

    public void setDummyString(String dummyString) {
        this.dummyString = dummyString;
    }

    public float getDummyFloat(int index) {
        return dummyFloatArray[index];
    }


    @Override
    public String toString() {
        return "DummyClass{" +
                "dummyInt=" + dummyInt +
                ", dummyString='" + dummyString + '\'' +
                ", dummyFloatArray=" + Arrays.toString(dummyFloatArray) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyClass that = (DummyClass) o;
        return dummyInt == that.dummyInt &&
                Objects.equals(dummyString, that.dummyString) &&
                Arrays.equals(dummyFloatArray, that.dummyFloatArray);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dummyInt, dummyString);
        result = 31 * result + Arrays.hashCode(dummyFloatArray);
        return result;
    }
}
