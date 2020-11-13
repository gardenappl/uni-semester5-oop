package ua.yuriih.task4;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class CustomClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = '/' + name.replace('.', File.separatorChar) + ".class";
        InputStream stream = getClass().getResourceAsStream(fileName);

        try {
            byte[] data = stream.readAllBytes();
            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NullPointerException ignored) {
            throw new ClassNotFoundException("Class not found: " + name);
        }

        //return Class.forName(name);
    }
}
