package ru.context.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.text.WordUtils;
import ru.context.base.CxElement;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;

import java.lang.reflect.*;
import java.util.List;

import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;

public class ElementUtils {

    private ElementUtils() {
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) throws IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            Class outerClass = clazz.getDeclaringClass();
            Object outerObject = outerClass.newInstance();
            return invokeConstructor(clazz, Lists.asList(outerObject, args).toArray());
        }
        return invokeConstructor(clazz, args);
    }

    public static boolean isCxElement(Field field) {
        return isCxElement(field.getType());
    }

    public static boolean isCxElement(Class<?> clazz) {
        return CxElement.class.isAssignableFrom(clazz);
    }

    public static boolean isCxElementList(Field field) {
        if (!isParametrizedList(field)) {
            return false;
        }
        Class listParameterClass = getGenericParameterClass(field);
        return isCxElement(listParameterClass);
    }

    public static Class getGenericParameterClass(Field field) {
        Type genericType = field.getGenericType();
        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    private static boolean isParametrizedList(Field field) {
        return isList(field) && hasGenericParameter(field);
    }

    private static boolean isList(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    private static boolean hasGenericParameter(Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }

    public static String getElementName(Field field) {
        if (field.isAnnotationPresent(ElementTitle.class)) {
            return field.getAnnotation(ElementTitle.class).value();
        }
        if (field.getType().isAnnotationPresent(ElementTitle.class)) {
            return field.getType().getAnnotation(ElementTitle.class).value();
        }
        return splitCamelCase(field.getName());
    }

    public static <T> String getElementName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(ElementTitle.class)) {
            return clazz.getAnnotation(ElementTitle.class).value();
        }
        return splitCamelCase(clazz.getSimpleName());
    }

    private static String splitCamelCase(String camel) {
        return WordUtils.capitalizeFully(camel.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        ));
    }
}
