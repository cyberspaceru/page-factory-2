package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;
import ru.sbtqa.tag.pagefactory.web.utils.WebExpectedConditionsUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

/**
 * Contains basic ru.sbtqa.tag.pagefactory.mobile.actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class WebPage<A extends WebPageActions, C extends WebPageChecks> extends DefaultPage<A, C> {

    public WebPage(A actions, C checks) {
        super(actions, checks);
        PageFactory.initElements((WebDriver) Environment.getDriverService().getDriver(), this);
    }

    public WebPage(FieldDecorator decorator, A actions, C checks) {
        super(actions, checks);
        PageFactory.initElements(decorator, this);
    }

    /**
     * Wait for an alert with specified text, and accept it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     *                       timeout
     */
    @ActionTitle("принимает уведомление")
    public void acceptAlert(String text) throws WaitException {
        getActions().acceptAlert();
    }

    /**
     * Wait for an alert with specified text, and dismiss it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     *                       timeout
     */
    @ActionTitle("отклоняет уведомление")
    public void dismissAlert(String text) throws WaitException {
        getActions().dismissAlert();
    }

    /**
     * Wait for appearance of the required text in current DOM model. Text will
     * be space-trimmed, so only non-space characters will matter.
     *
     * @param text text to search
     * @throws WaitException if text didn't appear on the page during the
     *                       timeout
     */
    @ActionTitle("текст появляется на странице")
    public void checkTextAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, true);
    }

    /**
     * Check whether specified text is absent on the page. Text is being
     * space-trimmed before assertion, so only non-space characters will matter
     *
     * @param text text to search for
     */
    @ActionTitle("текст отсутствует на странице")
    public void checkTextIsNotPresent(String text) {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, false);
    }

    /**
     * Wait for a new browser window, then wait for a specific text inside the
     * appeared window List of previously opened windows is being saved before
     * each click, so if modal window appears without click, this method won't
     * catch it. Text is being waited by {@link #checkTextAppears}, so it will
     * be space-trimmed as well
     *
     * @param text text that will be searched inside of the window
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException if
     */
    @ActionTitle("появляется модальное окно с текстом")
    public void checkModalWindowAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForModalWindowWithText(text);
    }

    /**
     * Perform a check that there is an element with required text on current
     * page
     *
     * @param text a {@link java.lang.String} object.
     */
    @ActionTitles({@ActionTitle("существует элемент с текстом"), @ActionTitle("отображается текст")})
    public void checkElementWithTextIsPresent(String text) {
        if (!WebExpectedConditionsUtils.checkElementWithTextIsPresent(text)) {
            throw new AutotestError("Text '" + text + "' is not present");
        }
    }
}