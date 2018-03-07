package ru.sbtqa.tag.pagefactory;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.drivers.TagMobileDriver;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.qautils.properties.Props;

public class DriverManager {

    private static final String ENVIRONMENT = Props.get("driver.environment");
    private static final String ENVIRONMENT_WEB = "web";
    private static final String ENVIRONMENT_MOBILE = "mobile";

    public static WebDriver getWebDriver() {
        return getDriver();
    }

    public static AppiumDriver getMobileDriver() {
        return (AppiumDriver) getDriver();
    }


    public static WebDriver getDriver() {
        switch (getEnvironment()) {
            case WEB:
                return TagWebDriver.getDriver();
            case MOBILE:
                return TagMobileDriver.getDriver();
            default:
                throw new FactoryRuntimeException("Failed to get driver");
        }
    }

    public static void dispose() {
        switch (getEnvironment()) {
            case WEB:
                TagWebDriver.dispose();
                break;
            case MOBILE:
                TagMobileDriver.dispose();
                break;
            default:
                throw new FactoryRuntimeException("Failed to dispose");
        }
    }

    public static Environment getEnvironment() {
        switch (ENVIRONMENT) {
            case ENVIRONMENT_WEB:
                return Environment.WEB;
            case ENVIRONMENT_MOBILE:
                return Environment.MOBILE;
            default:
                throw new FactoryRuntimeException("Environment '" + ENVIRONMENT + "' is not supported");
        }
    }
}
