package ru.context.base;

import org.openqa.selenium.By;

public class CxAnchor {
    private final String name;
    private final By locator;
    private final int index;

    public CxAnchor(String name, By locator, int index) {
        this.name = name;
        this.locator = locator;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public By getLocator() {
        return locator;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        String locatorText = locator + (index == 0 ? "" : " [" + index + "]");
        return String.format("'%s' by '%s'", name, locatorText);
    }
}
