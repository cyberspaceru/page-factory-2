package pagefactory.pages.webelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.sbtqa.tag.pagefactory.WebPage;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;

public class AbstractPage extends WebPage<WebPageActions, WebPageChecks> {

    @ElementTitle("Contact")
    @FindBy(xpath = "//a[text()='Contact']")
    protected WebElement contactButton;

    @ElementTitle("ContactRedirect")
    @FindBy(xpath = "//a[text()='Contact']")
    private WebElement contactButtonWithRedirect;

    @ElementTitle("Home")
    @FindBy(xpath = "//a[text()='Home']")
    protected WebElement homeButton;

    @ElementTitle("HomeRedirect")
    @FindBy(xpath = "//a[text()='Home']")
    private WebElement homeButtonWithRedirect;

    public AbstractPage() {
        super(new WebPageActions(), new WebPageChecks());
    }

    @ActionTitle("go to page")
    public void goToPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "home":
                homeButton.click();
                break;

            case "contact":
                contactButton.click();
                break;
        }
    }
}
