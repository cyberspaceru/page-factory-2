package ru.context.loader.decorator.proxyhandlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.context.base.CxElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static ru.context.loader.CxElementLoader.createCxElement;

public class CxElementListProxyHandler<T extends CxElement> implements InvocationHandler {
    private final Class<T> elementClass;
    private final By by;
    private final ElementLocator locator;
    private final String name;

    public CxElementListProxyHandler(Class<T> elementClass, By by, ElementLocator locator, String name) {
        this.elementClass = elementClass;
        this.by = by;
        this.locator = locator;
        this.name = name;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return name;
        }
        List<T> elements = new LinkedList<>();
        List<WebElement> found = locator.findElements();
        for (int i = 0; i < found.size(); i++) {
            String newName = String.format("%s [%d]", name, i);
            T typified = createCxElement(elementClass, found.get(i), newName, by,0);
            elements.add(typified);
        }
        return method.invoke(elements, objects);
    }
}
