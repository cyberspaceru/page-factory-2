package ru.context.base.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ISelect;
import org.openqa.selenium.support.ui.Select;
import ru.context.base.CxAnchor;
import ru.context.base.CxElement;

import java.util.List;

public class CxSelect extends CxElement implements ISelect {
    private final Select nativeSelect;

    public CxSelect(WebElement wrappedElement, CxAnchor anchor) {
        super(wrappedElement, anchor);
        this.nativeSelect = new Select(wrappedElement);
    }

    @Override
    public void selectByIndex(int option) {
        nativeSelect.selectByIndex(option);
    }

    @Override
    public void selectByValue(String value) {
        nativeSelect.selectByValue(value);
    }

    @Override
    public void deselectAll() {
        nativeSelect.deselectAll();
    }

    @Override
    public void deselectByValue(String value) {
        nativeSelect.deselectByValue(value);
    }

    @Override
    public void deselectByIndex(int index) {
        nativeSelect.deselectByIndex(index);
    }

    @Override
    public void deselectByVisibleText(String text) {
        nativeSelect.deselectByVisibleText(text);
    }

    @Override
    public void selectByVisibleText(String option) {
        nativeSelect.selectByVisibleText(option);
    }


    @Override
    public boolean isMultiple() {
        return nativeSelect.isMultiple();
    }

    @Override
    public List<WebElement> getOptions() {
        return nativeSelect.getOptions();
    }

    @Override
    public List<WebElement> getAllSelectedOptions() {
        return nativeSelect.getAllSelectedOptions();
    }

    @Override
    public WebElement getFirstSelectedOption() {
        return nativeSelect.getFirstSelectedOption();
    }

    @Override
    public String getText() {
        return getFirstSelectedOption().getText();
    }
}
