package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;

/**
 * Basic step definitions, that should be available on every project Notations
 * used in this class: Page - a class that extends {@link ru.sbtqa.tag.pagefactory.Page} and has
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation Action -
 * a method with {@link ru.sbtqa.tag.pagefactory.annotations.ActionTitle}
 * annotation in page object List - list of objects's with
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation on page
 * object
 * <p>
 * To pass a Cucumber {@link cucumber.api.DataTable} as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | header 1| header 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a {@link cucumber.api.DataTable} object.
 * First line is not enforced to be a header.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class CoreGenericSteps extends CoreSetupSteps {

    private static final Logger LOG = LoggerFactory.getLogger(CoreGenericSteps.class);
    
    /**
     * Initialize a page with corresponding title (defined via
     * {@link ru.sbtqa.tag.pagefactory.annotations.PageEntry} annotation)
     * User|he keywords are optional
     *
     * @param title of the page to initialize
     * @throws PageInitializationException if page initialization failed
     */
    public CoreGenericSteps openPage(String title) throws PageInitializationException {
        PageManager.getPage(title);
        return this;
    }

    /**
     * Execute action with no parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userActionNoParams(String action) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action);
        return this;
    }

    /**
     * Execute action with one parameter User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userActionOneParam(String action, String param) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action, param);
        return this;
    }

    /**
     * Execute action with two parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userActionTwoParams(String action, String param1, String param2) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action, param1, param2);
        return this;
    }

    /**
     * Execute action with three parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @param param3 third parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userActionThreeParams(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action, param1, param2, param3);
        return this;
    }

    /**
     * Execute action with parameters from given {@link DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userActionTableParam(String action, DataTable dataTable) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action, dataTable);
        return this;
    }

    /**
     * Execute action with string parameter and {@link DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @param dataTable table of parameters
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userDoActionWithObject(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action, param, dataTable);
        return this;
    }

    /**
     * Execute action with parameters taken from list User|he keywords are
     * optional
     *
     * @param action title of the action to execute
     * @param list parameters list
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreGenericSteps userActionListParam(String action, List<String> list) throws NoSuchMethodException {
        ReflectionUtils.executeMethodByTitle(PageContext.getCurrentPage(), action, list);
        return this;
    }

    /**
     * Element is focused
     *
     * @param element element to focus on
     */
    public void isElementFocused(String element) {
        LOG.warn("Note that isElementFocused method is still an empty!");
    }

    /**
     * Current step will be replaced with steps of specified scenario
     *
     * @param fragmentName scenario name to insert instead of this step
     */
    public void userInsertsFragment(String fragmentName) throws FragmentException {
        throw new FragmentException("The fragment-needed step must be replaced, but this did not happened");
    }
}
