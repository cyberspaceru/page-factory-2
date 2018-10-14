package ru.context.loader.decorator.proxyhandlers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static ru.context.utils.ElementUtils.getElementName;

public class CxLocatingElementHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final String name;

    CxLocatingElementHandler(ElementLocator locator, Field field) {
        this.locator = locator;
        this.name = getElementName(field);
    }

    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return name;
        }
        WebElement element = locator.findElement();
        return method.invoke(element, objects);
    }
}
