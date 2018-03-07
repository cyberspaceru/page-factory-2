package ru.sbtqa.tag.pagefactory;


import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;

/**
 * Entry point to the page-factory
 */
public class Page {

    private WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    private Page(){}

    /**
     * Get title of current page object
     *
     * @return the title
     */
    public String getTitle() {
        return this.getClass().getAnnotation(PageEntry.class).title();
    }

    /**
     * Get web driver
     *
     * @return webDriver
     */
    public WebDriver getDriver() {
        return driver;
    }
}
