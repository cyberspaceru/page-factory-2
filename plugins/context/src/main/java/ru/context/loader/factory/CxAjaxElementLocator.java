package ru.context.loader.factory;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CxAjaxElementLocator extends CxDefaultElementLocator {
    protected final long timeOutInSeconds;
    private final List<Function<WebElement, String>> checks;

    public CxAjaxElementLocator(SearchContext context, long timeOutInSeconds, List<Function<WebElement, String>> checks, AbstractAnnotations annotations) {
        super(context, annotations);
        this.timeOutInSeconds = timeOutInSeconds;
        this.checks = checks;
    }

    @Override
    public WebElement findElement() {
        SlowLoadingElement loadingElement = new SlowLoadingElement(timeOutInSeconds);
        WebElement element = loadingElement.get().getElement();
        if (element == null) {
            throw loadingElement.lastException;
        }
        return element;
    }

    @Override
    public List<WebElement> findElements() {
        SlowLoadingElementList list = new SlowLoadingElementList(timeOutInSeconds);
        return list.get().getElements();
    }

    private String available(WebElement webElement) {
        for (Function<WebElement, String> check : checks) {
            String errorMessage = check.apply(webElement);
            if (errorMessage != null) {
                return errorMessage;
            }
        }
        return null;
    }

    protected long sleepFor() {
        return 250;
    }

    private class SlowLoadingElement extends SlowLoadableComponent<SlowLoadingElement> {
        private NoSuchElementException lastException;
        private WebElement element;

        public SlowLoadingElement(long timeOutInSeconds) {
            super(new SystemClock(), (int) timeOutInSeconds);
        }

        @Override
        protected void load() {
            // Does nothing
        }

        @Override
        protected long sleepFor() {
            return CxAjaxElementLocator.this.sleepFor();
        }

        @Override
        protected void isLoaded() throws Error {
            try {
                element = CxAjaxElementLocator.super.findElement();
                String message = available(element);
                if (message != null) {
                    throw new NoSuchElementException(message);
                }
            } catch (NoSuchElementException e) {
                element = null;
                lastException = e;
            }
        }

        public NoSuchElementException getLastException() {
            return lastException;
        }

        public WebElement getElement() {
            return element;
        }
    }

    private class SlowLoadingElementList extends SlowLoadableComponent<SlowLoadingElementList> {
        private NoSuchElementException lastException;
        private List<WebElement> elements = new ArrayList<>();

        public SlowLoadingElementList(long timeOutInSeconds) {
            super(new SystemClock(), (int) timeOutInSeconds);
        }

        @Override
        protected void load() {
            // Does nothing
        }

        @Override
        protected long sleepFor() {
            return CxAjaxElementLocator.this.sleepFor();
        }

        @Override
        protected void isLoaded() throws Error {
            try {
                elements = CxAjaxElementLocator.super.findElements();
            } catch (NoSuchElementException e) {
                lastException = e;
            }
        }

        public NoSuchElementException getLastException() {
            return lastException;
        }

        public List<WebElement> getElements() {
            return elements;
        }
    }
}