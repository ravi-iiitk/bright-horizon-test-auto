package com.brighthorizon.test.web.pagebojects;


import com.brighthorizon.test.automation.framework.utils.selenium.ElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class BrightHorizonsHomePage {

    private WebDriver driver;

    public BrightHorizonsHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // WebElements
    @FindBy(css = ".nav-link-search.track_nav_interact[href='#subnav-search-desktop-top']")
    private WebElement searchIcon;

    @FindBy(xpath = "//nav[@id='subnav-search-desktop-top']//input[@id='search-field']")
    private WebElement searchField;

    @FindBy(css = "nav[id='subnav-search-desktop-top'] button[type='submit']")
    private WebElement searchButton;

    @FindBy(css = ".txt-nav-search-title")
    private WebElement searchTitle;

    @FindBy(xpath = "//h3[normalize-space()='Employee Education in 2018: Strategies to Watch']") // Replace with actual locator for the first search result
    private WebElement firstSearchResult;

    @FindBy(id = "onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;

    @FindBy(css = "h3[class='title']")
    private WebElement noSearchResults;



    // Actions
    public void clickSearchIcon() {
        ElementUtils.clickElement(searchIcon, driver);
    }

    public boolean isSearchFieldVisible() {
        return ElementUtils.isElementDisplayed(searchField, driver);
    }

    public void enterSearchText(String searchText) {
        ElementUtils.enterText(searchField, searchText, driver);
    }

    public void clickSearchButton() {
        ElementUtils.clickElement(searchButton, driver);
    }

    public void verifyNoResultsMessage(String expectedMessage) {
        String actualMessage = ElementUtils.getText(noSearchResults, driver);
        if (!actualMessage.contains(expectedMessage)) {
            throw new AssertionError("Expected 'No results' message: '" + expectedMessage + "', but found: '" + actualMessage + "'");
        }
    }

    public void verifySearchResultsDisplayed() {
        List<WebElement> searchResults = driver.findElements(By.cssSelector("a.search-result h3.title")); // Update with the correct locator

        if (searchResults.isEmpty()) {
            throw new AssertionError("No search results were displayed, but results were expected.");
        }
    }


    public void verifyEmptySearchMessage(String expectedMessage) {

        String actualMessage = ElementUtils.getText(firstSearchResult, driver);
        if (!actualMessage.contains(expectedMessage)) {
            throw new AssertionError("Expected 'Empty search' message: '" + expectedMessage + "', but found: '" + actualMessage + "'");
        }
    }

    public String getFirstSearchResultText() {
        return ElementUtils.getText(firstSearchResult, driver);
    }

    public String getSearchTitle() {
        return ElementUtils.getText(searchTitle, driver);
    }


    public void verifyFirstSearchResult(String expectedText) {
        String actualText = ElementUtils.getText(firstSearchResult, driver);
        if (!actualText.equals(expectedText)) {
            throw new AssertionError("Expected search result: '" + expectedText + "', but found: '" + actualText + "'");
        }
    }


    public void verifyFirstSearchResultDynamically(String expectedText, int resultIndex) {
        List<WebElement> searchResults = driver.findElements(By.cssSelector("a.search-result h3.title")); // Update with correct locator

        if (searchResults.isEmpty()) {
            throw new AssertionError("No search results found on the page.");
        }

        String actualText = ElementUtils.getText(searchResults.get(resultIndex), driver);

        if (!actualText.equals(expectedText)) {
            throw new AssertionError("Expected first search result: '" + expectedText + "', but found: '" + actualText + "'");
        }
    }

    public void clearSearchField() {
        ElementUtils.enterText(searchField, "", driver); // Clears the field
    }

    public void acceptCookes() {
        if (ElementUtils.isElementDisplayed(acceptCookiesButton, driver)) {
            ElementUtils.clickElement(acceptCookiesButton, driver);
        }
    }

}
