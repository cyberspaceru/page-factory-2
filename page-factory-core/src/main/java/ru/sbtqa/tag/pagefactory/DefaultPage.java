package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.Keys;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.annotations.PageEntry;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public abstract class DefaultPage<A extends PageActions, C extends  PageChecks> implements Page {
    private A actions;
    private C checks;

    public DefaultPage(A actions, C checks) {
        this.actions = actions;
        this.checks = checks;
    }

    /**
     * Get title of current page object
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return this.getClass().getAnnotation(PageEntry.class).title();
    }

    public A getActions() {
        return actions;
    }

    public C getChecks() {
        return checks;
    }

    /**
     * Fill specified element with text
     *
     * @param elementTitle element to fill
     * @param text text to enter
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    @ActionTitle("заполняет поле")
    public void fill(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        actions.fill(element, text);
    }

    /**
     * Click specified element
     *
     * @param elementTitle title of the element to click
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    @ActionTitles({
            @ActionTitle("кликает по ссылке"),
            @ActionTitle("нажимает кнопку")})
    public void click(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        actions.click(element);
    }

    /**
     * Press key on keyboard
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     */
    @ActionTitle("нажимает клавишу")
    public void pressKey(String keyName) {
        actions.press(null, keyName);
    }

    /**
     * Press key on keyboard with focus on specified element
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @param elementTitle title of element that accepts key commands
     * @throws PageException if couldn't find element with required title
     */
    @ActionTitle("нажимает клавишу")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        actions.press(element, keyName);
    }

    /**
     * Select specified option in select-element
     *
     * @param elementTitle element that is supposed to be selectable
     * @param option option to select
     * @throws PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    @ActionTitle("выбирает")
    public void select(String elementTitle, String option) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        actions.select(element, option);
    }

    /**
     * Set checkbox element to selected state
     *
     * @param elementTitle element that is supposed to represent checkbox
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    @ActionTitle("отмечает чекбокс")
    public void setCheckBox(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        actions.setCheckbox(element, true);
    }


    /**
     * Check that the element's value is equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @throws ElementNotFoundException if couldn't find element by given title, or current page isn't initialized
     */
    @ActionTitle("проверяет значение")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!checks.checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is not equal with '" + text + "'");
        }
    }

    /**
     * Check that the element's value is not equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @throws PageException if current page wasn't initialized, or element with required title was not found
     */
    @ActionTitle("проверяет несовпадение значения")
    public void checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (checks.checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is equal with '" + text + "'");
        }
    }

    /**
     * Check that the element's value is not empty
     *
     * @param elementTitle title of the element to check
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    @ActionTitle("проверяет что поле непустое")
    public void checkNotEmpty(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (checks.checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is empty");
        }
    }

    /**
     * Check that the element's value is empty
     *
     * @param elementTitle title of the element to check
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    @ActionTitle("проверяет что поле пустое")
    public void checkEmpty(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!checks.checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is not empty");
        }
    }
}
