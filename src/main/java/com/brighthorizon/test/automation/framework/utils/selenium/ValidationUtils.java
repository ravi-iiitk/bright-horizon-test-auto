package com.brighthorizon.test.automation.framework.utils.selenium;

import org.openqa.selenium.WebElement;

public class ValidationUtils {

    public static boolean isTextPresent(WebElement element, String expectedText) {
        return element.getText().contains(expectedText);
    }

    public static boolean isAttributeValueEqual(WebElement element, String attribute, String expectedValue) {
        return element.getAttribute(attribute).equals(expectedValue);
    }
}
