package ua.yuriih.task4;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassInfoPrinter {
    private final CustomClassLoader customClassLoader;

    public ClassInfoPrinter() {
        customClassLoader = new CustomClassLoader();
    }

    public String getInfo(String className) throws ClassNotFoundException {
        Class<?> aClass = customClassLoader.findClass(className);

        StringBuilder sb = new StringBuilder()
                .append("Canonical name: ")
                .append(aClass.getCanonicalName())
                .append("\nSuperclass: ")
                .append(aClass.getSuperclass())
                .append("\nInterfaces: ")
                .append(Arrays.toString(aClass.getInterfaces()))
                .append("\nModifiers: ")
                .append(Modifier.toString(aClass.getModifiers()))
                .append("\nAnnotations: ")
                .append(Arrays.toString(aClass.getAnnotations()));
        
        for (Field field : aClass.getDeclaredFields())
            appendInfo(sb, field);
        for (Constructor constructor : aClass.getDeclaredConstructors())
            appendInfo(sb, constructor);
        for (Method method : aClass.getDeclaredMethods())
            appendInfo(sb, method);

        return sb.toString();
    }
    
    private StringBuilder appendInfo(StringBuilder sb, Constructor constructor) {
        sb.append("\nConstructor: ")
                .append(Arrays.toString(constructor.getParameterTypes()))
                .append("\n\tModifiers: ")
                .append(Modifier.toString(constructor.getModifiers()))
                .append('\n');
        return sb;
    }

    private StringBuilder appendInfo(StringBuilder sb, Method method) {
        sb.append("\nMethod: ")
                .append(method.getName())
                .append("\n\tReturn type: ")
                .append(method.getReturnType().getCanonicalName())
                .append("\n\tParameters: ")
                .append(Arrays.toString(method.getParameterTypes()))
                .append("\n\tModifiers: ")
                .append(Modifier.toString(method.getModifiers()))
                .append('\n');
        return sb;
    }

    private StringBuilder appendInfo(StringBuilder sb, Field field) {
        sb.append("\nField: ")
                .append(field.getName())
                .append("\n\tType: ")
                .append(field.getType().getCanonicalName())
                .append("\n\tModifiers: ")
                .append(Modifier.toString(field.getModifiers()))
                .append('\n');
        return sb;
    }
}
