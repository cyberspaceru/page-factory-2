package ru.context.base.elements;

import org.openqa.selenium.WebElement;
import ru.context.base.CxAnchor;
import ru.context.base.CxElement;

public class CxWebElement extends CxElement {
    public CxWebElement(WebElement wrappedElement, CxAnchor anchor) {
        super(wrappedElement, anchor);
    }
}
