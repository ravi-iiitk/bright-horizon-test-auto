package com.brighthorizon.test.web.pagebojects;

import com.brighthorizon.test.automation.framework.utils.selenium.ElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class BrightHorizonsHomePage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BrightHorizonsHomePage.class);

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

    @FindBy(xpath = "//h3[normalize-space()='Employee Education in 2018: Strategies to Watch']")
    private WebElement firstSearchResult;

    @FindBy(id = "onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;

    @FindBy(css = "h3[class='title']")
    private WebElement noSearchResults;

    // Actions
    public void clickSearchIcon() {
        logger.info("Clicking on search icon");
        ElementUtils.clickElement(searchIcon, driver);
    }

    public boolean isSearchFieldVisible() {
        boolean visible = ElementUtils.isElementDisplayed(searchField, driver);
        logger.info("Search field visibility: {}", visible);
        return visible;
    }

    public void enterSearchText(String searchText) {
        logger.info("Entering search text: {}", searchText);
        ElementUtils.enterText(searchField, searchText, driver);
    }

    public void clickSearchButton() {
        logger.info("Clicking on search button");
        ElementUtils.clickElement(searchButton, driver);
    }

    public void verifyNoResultsMessage(String expectedMessage) {
        String actualMessage = ElementUtils.getText(noSearchResults, driver);
        logger.info("Expected message: '{}', Actual message: '{}'", expectedMessage, actualMessage);
        if (!actualMessage.contains(expectedMessage)) {
            logger.error("Mismatch in 'No results' message.");
            throw new AssertionError("Expected 'No results' message: '" + expectedMessage + "', but found: '" + actualMessage + "'");
        }
    }

    public void verifySearchResultsDisplayed() {
        List<WebElement> searchResults = driver.findElements(By.cssSelector("a.search-result h3.title"));
        logger.info("Number of search results displayed: {}", searchResults.size());
        if (searchResults.isEmpty()) {
            logger.error("No search results were displayed.");
            throw new AssertionError("No search results were displayed, but results were expected.");
        }
    }

    public void verifyEmptySearchMessage(String expectedMessage) {
        String actualMessage = ElementUtils.getText(firstSearchResult, driver);
        logger.info("Expected message: '{}', Actual message: '{}'", expectedMessage, actualMessage);
        if (!actualMessage.contains(expectedMessage)) {
            logger.error("Mismatch in 'Empty search' message.");
            throw new AssertionError("Expected 'Empty search' message: '" + expectedMessage + "', but found: '" + actualMessage + "'");
        }
    }

    public String getFirstSearchResultText() {
        String resultText = ElementUtils.getText(firstSearchResult, driver);
        logger.info("First search result text: {}", resultText);
        return resultText;
    }

    public String getSearchTitle() {
        String title = ElementUtils.getText(searchTitle, driver);
        logger.info("Search title: {}", title);
        return title;
    }

    public void verifyFirstSearchResult(String expectedText) {
        String actualText = ElementUtils.getText(firstSearchResult, driver);
        logger.info("Verifying first search result. Expected: '{}', Actual: '{}'", expectedText, actualText);
        if (!actualText.equals(expectedText)) {
            logger.error("Mismatch in first search result.");
            throw new AssertionError("Expected search result: '" + expectedText + "', but found: '" + actualText + "'");
        }
    }

    public void verifyFirstSearchResultDynamically(String expectedText, int resultIndex) {
        List<WebElement> searchResults = driver.findElements(By.cssSelector("a.search-result h3.title"));
        logger.info("Total search results found: {}", searchResults.size());
        if (searchResults.isEmpty()) {
            logger.error("No search results found.");
            throw new AssertionError("No search results found on the page.");
        }
        String actualText = ElementUtils.getText(searchResults.get(resultIndex), driver);
        logger.info("Verifying dynamic search result at index {}. Expected: '{}', Actual: '{}'", resultIndex, expectedText, actualText);
        if (!actualText.equals(expectedText)) {
            logger.error("Mismatch in dynamic search result.");
            throw new AssertionError("Expected first search result: '" + expectedText + "', but found: '" + actualText + "'");
        }
    }

    public void clearSearchField() {
        logger.info("Clearing search field");
        ElementUtils.enterText(searchField, "", driver);
    }

    public void acceptCookies() {
        if (ElementUtils.isElementDisplayed(acceptCookiesButton, driver)) {
            logger.info("Accepting cookies");
            ElementUtils.clickElement(acceptCookiesButton, driver);
        } else {
            logger.info("Cookies banner not displayed.");
        }
    }
}
