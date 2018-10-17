package ru.context;

import ru.context.annotations.ContextEntry;
import ru.context.annotations.ContextEntryProvider;
import ru.context.utils.MethodUtilsExt;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static ru.sbtqa.tag.qautils.reflect.FieldUtilsExt.getDeclaredFieldsWithInheritance;

public class ContextData {
    private final Class<? extends Page> pageClass;
    private final String title;
    private final Map<String, Field> contextEntryFields;
    private final Map<String, Method> contextEntryProviderMethods;

    ContextData(Class<? extends Page> pageClass) {
        this.pageClass = pageClass;
        this.title = this.pageClass.getAnnotation(PageEntry.class).title();
        this.contextEntryFields = findContextEntryFields(this.pageClass);
        this.contextEntryProviderMethods = findContextEntryProviderMethods(this.pageClass);
    }

    public Map<String, Field> getEntryFields() {
        return contextEntryFields;
    }

    public Field getField(String title) {
        return contextEntryFields.get(title);
    }

    public Method getMethod(String title) {
        return contextEntryProviderMethods.get(title);
    }

    public Class<? extends Page> getPageClass() {
        return pageClass;
    }

    public String getTitle() {
        return title;
    }

    private static Map<String, Field> findContextEntryFields(Class<? extends Page> pageClass) {
        Map<String, Field> result = new HashMap<>();
        getDeclaredFieldsWithInheritance(pageClass).forEach(field -> {
            ContextEntry contextEntry = field.getAnnotation(ContextEntry.class);
            if (contextEntry != null) {
                result.put(contextEntry.value(), field);
            }
        });
        return result;
    }

    private static Map<String, Method> findContextEntryProviderMethods(Class<? extends Page> pageClass) {
        Map<String, Method> result = new HashMap<>();
        MethodUtilsExt.getDeclaredMethodsWithInheritance(pageClass).forEach(method -> {
            ContextEntryProvider contextEntry = method.getAnnotation(ContextEntryProvider.class);
            if (contextEntry != null) {
                result.put(contextEntry.value(), method);
            }
        });
        return result;
    }
}
