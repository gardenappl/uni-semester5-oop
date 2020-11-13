package ua.yuriih.task4;

import org.junit.jupiter.api.Test;
import ua.yuriih.task4.dummy.DummyClass;

import static org.junit.jupiter.api.Assertions.*;

class CustomClassLoaderTest {
    @Test
    void findClass() {
        CustomClassLoader customClassLoader = new CustomClassLoader();

        final String dummyClassName = "ua.yuriih.task4.dummy.DummyClass";
        assertDoesNotThrow(() ->
                assertEquals(DummyClass.class, customClassLoader.findClass(dummyClassName))
        );
        assertThrows(ClassNotFoundException.class, () ->
                customClassLoader.findClass("ua.yuriih.task4.NonExistentClass")
        );
    }
}