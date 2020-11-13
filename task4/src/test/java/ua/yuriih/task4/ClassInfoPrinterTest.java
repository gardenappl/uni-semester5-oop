package ua.yuriih.task4;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ClassInfoPrinterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassInfoPrinterTest.class);

    @Test
    public void getInfo() {
        ClassInfoPrinter classInfoPrinter = new ClassInfoPrinter();

        AtomicReference<String> stringRef = new AtomicReference<>();
        assertDoesNotThrow(() -> 
                stringRef.set(classInfoPrinter.getInfo("ua.yuriih.task4.dummy.DummyClass"))
        );
        String info = stringRef.get();

        LOGGER.info(info);

        assert(info.contains(
                "Canonical name: ua.yuriih.task4.dummy.DummyClass\n" +
                "Superclass: class java.lang.Object\n" +
                "Interfaces: [interface java.io.Serializable]\n" +
                "Modifiers: public\n" +
                "Annotations: [@java.lang.Deprecated(forRemoval=false, since=\"\")]\n"
        ));
        assert(info.contains(
                "Field: dummyInt\n" +
                "\tType: int\n" +
                "\tModifiers: public\n"
        ));
        assert(info.contains(
                "Field: dummyString\n" +
                "\tType: java.lang.String\n" +
                "\tModifiers: private\n"
        ));
        assert(info.contains(
                "Field: dummyFloatArray\n" +
                "\tType: float[]\n" +
                "\tModifiers: \n"
        ));
        assert(info.contains(
                "Constructor: []\n" +
                "\tModifiers: private\n"
        ));
        assert(info.contains(
                "Constructor: [int, class java.lang.String, class [F]\n" +
                "\tModifiers: public\n"
        ));
        assert(info.contains(
                "Method: getDummyString\n" +
                "\tReturn type: java.lang.String\n" +
                "\tParameters: []\n" +
                "\tModifiers: public\n"
        ));
        assert(info.contains(
                "Method: setDummyString\n" +
                "\tReturn type: void\n" +
                "\tParameters: [class java.lang.String]\n" +
                "\tModifiers: public\n"
        ));
        assert(info.contains(
                "Method: getDummyFloat\n" +
                "\tReturn type: float\n" +
                "\tParameters: [int]\n" +
                "\tModifiers: public\n"
        ));
        assert(info.contains(
                "Method: toString\n" +
                "\tReturn type: java.lang.String\n" +
                "\tParameters: []\n" +
                "\tModifiers: public\n"
        ));
    }
}
