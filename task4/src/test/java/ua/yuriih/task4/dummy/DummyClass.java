package ua.yuriih.task4.dummy;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Deprecated
public class DummyClass implements Serializable {
    public int dummyInt;
    @NotNull
    private String dummyString;
    float[] dummyFloatArray;

    private DummyClass() {

    }
    
    public DummyClass(int dummyInt, String dummyString, float[] dummyFloatArray) {
        this.dummyInt = dummyInt;
        this.dummyString = dummyString;
        this.dummyFloatArray = dummyFloatArray;
    }

    @NotNull
    public String getDummyString() {
        return dummyString;
    }

    public void setDummyString(@NotNull String dummyString) {
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
}
