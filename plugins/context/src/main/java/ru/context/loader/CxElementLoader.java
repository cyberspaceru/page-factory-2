package ru.context.loader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.context.base.CxAnchor;
import ru.context.base.CxElement;
import ru.context.exceptions.CxElementException;

import java.lang.reflect.InvocationTargetException;

import ru.context.utils.ElementUtils;

public class CxElementLoader {
    public static <T extends CxElement> T createCxElement(Class<T> elementClass, WebElement elementToWrap,
                                                          String name, By by, int index) {
        try {
            CxAnchor anchor = new CxAnchor(name, by, index);
            return ElementUtils.newInstance(elementClass, elementToWrap, anchor);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new CxElementException(e);
        }
    }
}
