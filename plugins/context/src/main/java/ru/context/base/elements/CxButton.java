package ru.context.base.elements;

import org.openqa.selenium.WebElement;
import ru.context.base.CxAnchor;
import ru.context.base.CxElement;

public class CxButton extends CxElement {
    public CxButton(WebElement wrappedElement, CxAnchor anchor) {
        super(wrappedElement, anchor);
    }
}
