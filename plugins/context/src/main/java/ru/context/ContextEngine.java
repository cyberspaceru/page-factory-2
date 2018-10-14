package ru.context;

import javafx.util.Pair;
import org.reflections.Reflections;
import ru.context.Context;
import ru.context.base.CxWebPage;
import ru.context.annotations.ContextEntry;
import ru.context.annotations.ContextEntryProvider;
import ru.context.exceptions.ContextException;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.sbtqa.tag.qautils.reflect.FieldUtilsExt.getDeclaredFieldsWithInheritance;

public class ContextEngine {
    private static final ThreadLocal<Page> LAST_PAGE = new ThreadLocal<>();
    private static List<ContextData> CACHE = new ArrayList<>();
    private static final Map<String, ThreadLocal<Object>> VALUES = new ConcurrentHashMap<>();

    static {
        updateCache();
    }

    public static void merge(Page page) {
        Page prev = LAST_PAGE.get();
        if (prev != null) {
            update(prev);
            assign(page);
        }
        LAST_PAGE.set(page);
    }

    private static void assign(Page page) {
        ContextData data = getReprByPageClass(page.getClass());
        for (Entry<String, Field> entry : data.getEntryFields().entrySet()) {
            String name = entry.getKey();
            Field field = entry.getValue();
            try {
                ThreadLocal<Object> threadLocal = VALUES.get(name);
                if (threadLocal != null) {
                    field.set(page, threadLocal.get());
                }
            } catch (Exception e) {
                throw new ContextException("Can't assign context values", e);
            }
        }
    }

    private static void update(Page page) {
        ContextData data = getReprByPageClass(page.getClass());
        for (Entry<String, Field> entry : data.getEntryFields().entrySet()) {
            String name = entry.getKey();
            Field field = entry.getValue();
            try {
                Object value = field.get(page);
                if (!VALUES.containsKey(name)) {
                    VALUES.put(name, new ThreadLocal<>());
                }
                VALUES.get(name).set(value);
            } catch (Exception e) {
                throw new ContextException("Can't update context values", e);
            }
        }
    }

    public static ContextData getReprByPageClass(Class<? extends Page> pageClass) {
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
        CACHE = findPages().stream().map(x -> (Class<? extends Page>) x)
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
        private final Class<? extends Page> pageClass;
        private final String title;
        private final Map<String, Field> contextEntryFields;
        private final Map<String, Method> contextEntryProviderMethods;

        private ContextData(Class<? extends Page> pageClass) {
            this.pageClass = pageClass;
            PageEntry entry = this.pageClass.getAnnotation(PageEntry.class);
            this.title = entry.title();
            this.contextEntryFields = new HashMap<>();
            getDeclaredFieldsWithInheritance(this.pageClass).forEach(field -> {
                ContextEntry contextEntry = field.getAnnotation(ContextEntry.class);
                if (contextEntry != null) {
                    this.contextEntryFields.put(contextEntry.value(), field);
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
    }
}
