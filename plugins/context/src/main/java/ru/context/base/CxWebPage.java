package ru.context.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.context.base.interactions.CxActions;
import ru.context.base.interactions.CxChecks;
import ru.context.loader.decorator.CxElementDecorator;
import ru.context.loader.factory.CxElementLocatorFactory;
import ru.context.utils.ContextUtils;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.WebPage;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class CxWebPage<A extends CxActions, C extends CxChecks> extends WebPage<A, C> {
    static {
        PageManager.subscribeOnPageBootstrappedEvent(ContextUtils::merdge);
    }

    public CxWebPage(A actions, C checks) {
        super(newFieldDecorator(), actions, checks);
    }

    public CxWebPage(FieldDecorator decorator, A actions, C checks) {
        super(decorator, actions, checks);
    }

    private static FieldDecorator newFieldDecorator() {
        WebDriver driver = Environment.getDriverService().getDriver();
        return new CxElementDecorator(new CxElementLocatorFactory(driver));
    }
}
