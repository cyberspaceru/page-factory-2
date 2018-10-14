package ru.context.loader.decorator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.context.base.CxElement;
import ru.context.loader.decorator.proxyhandlers.CxElementListProxyHandler;
import ru.context.loader.decorator.proxyhandlers.CxWebElementNamedProxyHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

import static ru.context.loader.CxElementLoader.createCxElement;
import static ru.context.utils.ElementUtils.*;

public class CxElementDecorator implements FieldDecorator {
    private final ElementLocatorFactory factory;

    public CxElementDecorator(ElementLocatorFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        try {
            if (isCxElement(field)) {
                return decorateCxElement(loader, field);
            }
            if (isCxElementList(field)) {
                return decorateCxElementList(loader, field);
            }
            return null;
        } catch (ClassCastException ignore) {
            return null;
        }
    }

    protected WebElement decorateWebElement(ClassLoader loader, Field field) {
        InvocationHandler handler = new CxWebElementNamedProxyHandler(locator(field), field);
        return createWebElementProxy(loader, handler);
    }

    @SuppressWarnings("unchecked")
    protected <T extends CxElement> T decorateCxElement(ClassLoader loader, Field field) {
        WebElement elementToWrap = decorateWebElement(loader, field);
        return createCxElement((Class<T>) field.getType(), elementToWrap, name(field), by(field), 0);
    }

    protected <T extends CxElement> List<T> decorateCxElementList(ClassLoader loader, Field field) {
        @SuppressWarnings("unchecked")
        Class<T> elementClass = (Class<T>) getGenericParameterClass(field);
        InvocationHandler handler = new CxElementListProxyHandler<>(elementClass, by(field), locator(field), name(field));
        return createTypifiedElementListProxy(loader, handler);
    }

    @SuppressWarnings("unchecked")
    protected <T extends WebElement> T createWebElementProxy(ClassLoader loader, InvocationHandler handler) {
        Class<?>[] interfaces = new Class[]{WebElement.class, WrapsElement.class, Locatable.class};
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    @SuppressWarnings("unchecked")
    protected <T extends CxElement> List<T> createTypifiedElementListProxy(ClassLoader loader, InvocationHandler handler) {
        return (List<T>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }

    private ElementLocator locator(Field field) {
        return factory.createLocator(field);
    }

    private String name(Field field) {
        return getElementName(field);
    }

    private By by(Field field) {
        return new CxElementFieldAnnotationsHandler(field).buildBy();
    }
}
