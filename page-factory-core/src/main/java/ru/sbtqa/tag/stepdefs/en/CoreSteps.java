package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import java.util.List;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

public class CoreSteps extends CoreGenericSteps {

    @Override
    @Before(order = 0)
    public void preSetUp(Scenario scenario) {
        super.preSetUp(scenario);
    }

    @Override
    @Before(order = 99999)
    public void setUp(Scenario scenario) {
        super.setUp(scenario);
    }

    @Override
    @After(order = 99999)
    public void tearDown() {
        super.tearDown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he |)(?:is on the page|page is being opened|master tab is being opened) \"(.*?)\"$")
    public CoreSteps openPage(String title) throws PageInitializationException {
        super.openPage(title);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\)$")
    public CoreSteps userActionNoParams(String action) throws NoSuchMethodException {
        super.userActionNoParams(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with param |)\"([^\"]*)\"$")
    public CoreSteps userActionOneParam(String action, String param) throws NoSuchMethodException {
        super.userActionOneParam(action, param);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\"$")
    public CoreSteps userActionTwoParams(String action, String param1, String param2) throws NoSuchMethodException {
        super.userActionTwoParams(action, param1, param2);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) (?:with the parameters |)\"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public CoreSteps userActionThreeParams(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        super.userActionThreeParams(action, param1, param2, param3);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) data$")
    public CoreSteps userActionTableParam(String action, DataTable dataTable) throws NoSuchMethodException {
        super.userActionTableParam(action, dataTable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) [^\"]*\"([^\"]*) data$")
    public CoreSteps userDoActionWithObject(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        super.userDoActionWithObject(action, param, dataTable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user \\((.*?)\\) from the list$")
    public CoreSteps userActionListParam(String action, List<String> list) throws NoSuchMethodException {
        super.userActionListParam(action, list);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^element \"([^\"]*)\" is focused$")
    public void isElementFocused(String element) {
        super.isElementFocused(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:user |he |)inserts fragment \"([^\"]*)\"$")
    public void userInsertsFragment(String fragmentName) throws FragmentException {
        super.userInsertsFragment(fragmentName);
    }
}
