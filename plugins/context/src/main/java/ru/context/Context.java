package ru.context;

import org.aeonbits.owner.ConfigFactory;
import org.reflections.Reflections;
import ru.context.exceptions.ContextException;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    private static final WebConfiguration WEB_CONFIGURATION = ConfigFactory.create(WebConfiguration.class);
    private static final ThreadLocal<Page> LAST_PAGE = new ThreadLocal<>();
    private static final Map<String, ThreadLocal<Object>> VALUES = new ConcurrentHashMap<>();
    private static final List<ContextData> CACHE;

    static {
        CACHE = loadContextData();
    }

    private Context() {
    }

    public static WebConfiguration getWebConfig() {
        return WEB_CONFIGURATION;
    }

    public static void merge(Page page) {
        Page prev = LAST_PAGE.get();
        if (prev != null) {
            update(prev);
            assign(page);
        }
        LAST_PAGE.set(page);
    }

    public static ContextData findByClass(Class<? extends Page> pageClass) {
        for (ContextData data : CACHE) {
            if (data.getPageClass().equals(pageClass)) {
                return data;
            }
        }
        return null;
    }

    public static ContextData findByTitle(String title) {
        for (ContextData data : CACHE) {
            if (data.getTitle().equals(title)) {
                return data;
            }
        }
        return null;
    }

    private static void assign(Page page) {
        ContextData data = findByClass(page.getClass());
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
        ContextData data = findByClass(page.getClass());
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

    private static List<ContextData> loadContextData() {
        List<ContextData> result = new ArrayList<>();
        String pagesPackage = Context.getWebConfig().getPagesPackage();
        Reflections reflections = new Reflections(pagesPackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(PageEntry.class);
        for (Class<?> clazz : classes) {
            if (Page.class.isAssignableFrom(clazz)) {
                ContextData data = new ContextData((Class<? extends Page>) clazz);
                result.add(data);
            } else {
                String message = String.format("Class '%s' contains '%s' annotation but is not extended '%s'",
                        clazz.getCanonicalName(), PageEntry.class.getSimpleName(), PageEntry.class.getSimpleName());
                throw new ContextException(message);
            }
        }
        return result;
    }
}
