package ru.context.loader.decorator.proxyhandlers;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.context.utils.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
import static ru.sbtqa.tag.pagefactory.environment.Environment.getDriverService;

public class CxWebElementNamedProxyHandler extends CxLocatingElementHandler {

    public CxWebElementNamedProxyHandler(ElementLocator locator, Field field) {
        super(locator, field);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        long timeOutInSeconds = getTimeoutInSeconds();
        long end = currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeOutInSeconds);
        Exception last;
        do {
            try {
                return super.invoke(o, method, objects);
            } catch (Exception e) {
                last = e;
            }
            TestUtils.sleep(200, "Wait for refreshing of an element");
        } while (currentTimeMillis() < end);
        throw last;
    }

    private long getTimeoutInSeconds() {
        long timeoutInSeconds = getDriverService().implicitlyWaitTime();
        if (timeoutInSeconds == -1) {
            throw new TimeoutException("Timeout is not specified");
        }
        return timeoutInSeconds;
    }
}
