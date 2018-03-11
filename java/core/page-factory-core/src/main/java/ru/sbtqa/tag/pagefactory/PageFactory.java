package ru.sbtqa.tag.pagefactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.support.properties.Configuration;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

/**
 * Общая информация о контексте теста
 */
public class PageFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PageFactory.class);

    private static final Map<Class<? extends Page>, Map<Field, String>> PAGES_REPOSITORY = new HashMap<>();

    private static Actions actions;
    private static PageManager pageManager;
    private static VideoRecorder videoRecorder;
    private static boolean aspectsDisabled = false;

    private static final Configuration PROPERTIES = Properties.getProperties();


    public static void initElements(WebDriver driver, Object page) {
        org.openqa.selenium.support.PageFactory.initElements(driver, page);
    }

    public static void initElements(FieldDecorator decorator, Object page) {
        org.openqa.selenium.support.PageFactory.initElements(decorator, page);
    }

    public static PageManager getInstance() {
        if (null == pageManager) {
            pageManager = new PageManager(getPagesPackage());
        }
        return pageManager;
    }

    public static String getPagesPackage() {
        return PROPERTIES.getPagesPackage();
    }

    public static Actions getActions() {
        if (null == actions) {
            actions = new Actions(PageContext.getCurrentPage().getDriver());
        }
        return actions;
    }
    
    public static int getTimeOutInSeconds() {
        return getTimeOut() / 1000;
    }

    public static int getTimeOut() {
        return PROPERTIES.getTimeout();
    }


    public static Map<Class<? extends Page>, Map<Field, String>> getPageRepository() {
        return PAGES_REPOSITORY;
    }

    /**
     * Affects click and sendKeys aspects only
     *
     * @return the aspectsDisabled default false
     */
    public static boolean isAspectsDisabled() {
        return aspectsDisabled;
    }

    /**
     * Affects click and sendKeys aspects only
     *
     * @param aAspectsDisabled default false
     */
    public static void setAspectsDisabled(boolean aAspectsDisabled) {
        aspectsDisabled = aAspectsDisabled;
    }

    public static void setVideoRecorderToNull() {
        videoRecorder = null;
    }
}
