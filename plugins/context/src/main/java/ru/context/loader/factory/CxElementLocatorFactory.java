package ru.context.loader.factory;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import ru.context.annotations.MustBeVisible;
import ru.context.loader.decorator.CxElementFieldAnnotationsHandler;
import ru.sbtqa.tag.pagefactory.environment.Environment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CxElementLocatorFactory implements ElementLocatorFactory {
    private final SearchContext searchContext;

    public CxElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public ElementLocator createLocator(Field field) {
        long timeout = Environment.getDriverService().implicitlyWaitTime();
        List<Function<WebElement, String>> checks = compileChecks(field);
        return new CxAjaxElementLocator(searchContext, timeout, checks, new CxElementFieldAnnotationsHandler(field));
    }

    private List<Function<WebElement, String>> compileChecks(Field field) {
        List<Function<WebElement, String>> result = new ArrayList<>();
        if (field.isAnnotationPresent(MustBeVisible.class)) {
            result.add(x -> {
                if (!x.isDisplayed()) {
                    return "Element is not visible";
                }
                return null;
            });
        }
        return result;
    }
}
