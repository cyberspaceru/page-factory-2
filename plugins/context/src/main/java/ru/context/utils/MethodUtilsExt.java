package ru.context.utils;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodUtilsExt extends MethodUtils {
    public static List<Method> getDeclaredMethodsWithInheritance(Class clazz) {
        List<Method> fields = new ArrayList<>();
        for (Class supp = clazz; supp != Object.class; supp = supp.getSuperclass()) {
            fields.addAll(Arrays.asList(supp.getDeclaredMethods()));
        }
        return fields;
    }
}
