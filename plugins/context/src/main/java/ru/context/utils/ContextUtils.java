package ru.context.utils;

import org.reflections.Reflections;
import ru.context.Context;
import ru.context.base.CxWebPage;
import ru.context.annotations.ContextEntry;
import ru.context.annotations.ContextEntryProvider;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sbtqa.tag.qautils.reflect.FieldUtilsExt.getDeclaredFieldsWithInheritance;

public class ContextUtils {
    private static List<ContextData> CACHE = new ArrayList<>();

    static {
        updateCache();
    }

    public static void merdge(Page page) {
        System.out.println(1);
    }

    public static ContextData getReprByPageClass(Class<? extends CxWebPage> pageClass) {
        return CACHE.stream().filter(x -> x.getPageClass().equals(pageClass))
                .findFirst()
                .orElse(null);
    }

    public static ContextData getReprByPageTitle(String title) {
        return CACHE.stream().filter(x -> x.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public static void updateCache() {
        CACHE = findPages().stream().map(x -> (Class<? extends CxWebPage>) x)
                .map(ContextData::new)
                .collect(Collectors.toList());
    }

    public static Set<Class<?>> findPages() {
        String pagesPackage = Context.webConfig().getPagesPackage();
        Reflections reflections = new Reflections(pagesPackage);
        return reflections.getTypesAnnotatedWith(PageEntry.class);
    }

    public static List<Method> getDeclaredMethodsWithInheritance(Class clazz) {
        List<Method> fields = new ArrayList<>();
        for (Class supp = clazz; supp != Object.class; supp = supp.getSuperclass()) {
            fields.addAll(Arrays.asList(supp.getDeclaredMethods()));
        }
        return fields;
    }

    public static class ContextData {
        private final Class<? extends CxWebPage> pageClass;
        private final String title;
        private final Map<String, ValueHolder<Field>> contextEntryFields;
        private final Map<String, Method> contextEntryProviderMethods;

        private ContextData(Class<? extends CxWebPage> pageClass) {
            this.pageClass = pageClass;
            PageEntry entry = this.pageClass.getAnnotation(PageEntry.class);
            this.title = entry.title();
            this.contextEntryFields = new HashMap<>();
            getDeclaredFieldsWithInheritance(this.pageClass).forEach(field -> {
                ContextEntry contextEntry = field.getAnnotation(ContextEntry.class);
                if (contextEntry != null) {
                    ValueHolder<Field> holder = new ValueHolder<>(field);
                    this.contextEntryFields.put(contextEntry.value(), holder);
                }
            });
            this.contextEntryProviderMethods = new HashMap<>();
            getDeclaredMethodsWithInheritance(this.pageClass).forEach(method -> {
                ContextEntryProvider contextEntry = method.getAnnotation(ContextEntryProvider.class);
                if (contextEntry != null) {
                    this.contextEntryProviderMethods.put(contextEntry.value(), method);
                }
            });
        }

        public ValueHolder<Field> getFieldHolder(String title) {
            return contextEntryFields.get(title);
        }

        public Method getMethod(String title) {
            return contextEntryProviderMethods.get(title);
        }

        public Class<? extends CxWebPage> getPageClass() {
            return pageClass;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class ValueHolder<T extends Member> {
        private final T member;
        private ThreadLocal<Object> value = new ThreadLocal<>();

        private ValueHolder(T member) {
            this.member = member;
        }

        public T getMember() {
            return member;
        }

        public Object getValue() {
            return value.get();
        }

        public void setValue(Object value) {
            this.value.set(value);
        }
    }
}
