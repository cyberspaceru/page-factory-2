package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

@Aspect
public class ScrollToElement {

    private static final WebConfiguration properties = ConfigFactory.create(WebConfiguration.class);

    @Pointcut("call(* org.openqa.selenium.WebElement.click()) && if()")
    public static boolean isScrollToElementEnabled() {
        return properties.isScrollToElementEnabled();
    }

    @Around("isScrollToElementEnabled()")
    public void highlight(ProceedingJoinPoint joinPoint) throws Throwable {
        WebElement webElement = (WebElement) joinPoint.getTarget();
        WebDriver webDriver = Environment.getDriverService().getDriver();

        Dimension size = webDriver.manage().window().getSize();
        Point elementLocation = (webElement).getLocation();
        Dimension elementSize = (webElement).getSize();

        if (size.getHeight() < (elementLocation.getY() + elementSize.getHeight() + 200)) {
            ((JavascriptExecutor) webDriver).
                    executeScript("window.scroll(" + elementLocation.getX() + ","
                            + (elementLocation.getY() - 200) + ");");
        }
    }

}
