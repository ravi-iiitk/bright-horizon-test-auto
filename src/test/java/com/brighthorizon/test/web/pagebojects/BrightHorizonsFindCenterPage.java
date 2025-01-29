package com.brighthorizon.test.web.pagebojects;

import com.brighthorizon.test.automation.framework.utils.selenium.ElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrightHorizonsFindCenterPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BrightHorizonsFindCenterPage.class);

    public BrightHorizonsFindCenterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // WebElements
    @FindBy(xpath = "//nav[@class='nav-shared txt-nav-hierarchy nav-top js-nav-shared js-nav-top']//li[@class='nav-item displayed-desktop']//a[@class='btn-nav btn btn-large btn-hollow color-nileblue global_header_findcenter track_cta_click'][normalize-space()='Find a Center']")
    private WebElement findCenterOption;

    @FindBy(id = "addressInput")
    private WebElement searchBox;

    @FindBy(id = "center-results-container")
    private WebElement centerResultsContainer;

    @FindBy(css = "span.resultsNumber")
    private WebElement resultsNumber;

    @FindBy(css = "div.centerResult")
    private List<WebElement> centerList;

    public void clickFindCenterOption() {
        logger.info("Clicking on 'Find a Center' option");
        ElementUtils.clickElement(findCenterOption, driver);
    }

    public boolean isCorrectURLDisplayed() {
        String currentURL = driver.getCurrentUrl();
        logger.info("Current URL: {}", currentURL);
        return currentURL.contains("/child-care-locator");
    }

    public void enterLocationAndVerify(String location) {
        logger.info("Entering location: {}", location);
        ElementUtils.clickElement(searchBox, driver);
        searchBox.clear();
        searchBox.sendKeys(location);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        logger.info("Waiting for autocomplete suggestions to appear");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".pac-item")));

        List<WebElement> suggestions = driver.findElements(By.cssSelector(".pac-item"));
        if (!suggestions.isEmpty()) {
            logger.info("Selecting the first suggestion");
            suggestions.get(0).click();
        }

        logger.info("Waiting for location input update");
        wait.until(ExpectedConditions.attributeContains(searchBox, "value", location));
        searchBox.sendKeys(Keys.ENTER);

        logger.info("Verifying search results are displayed");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("center-results-container")));
    }

    public int getDisplayedCenterCount() {
        int count = centerList.size();
        logger.info("Displayed centers count: {}", count);
        return count;
    }

    public int getExpectedCenterCount() {
        int expectedCount = Integer.parseInt(ElementUtils.getText(resultsNumber, driver));
        logger.info("Expected centers count: {}", expectedCount);
        return expectedCount;
    }

    public void clickFirstCenter() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("Waiting for the first center in the list to be clickable");
        List<WebElement> centers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@id='center-results-container']//div[contains(@class, 'centerResult')]")
        ));

        if (!centers.isEmpty()) {
            logger.info("Clicking on the first center");
            ElementUtils.clickElement(centers.get(0), driver);
        } else {
            logger.error("No centers found in the search results");
            throw new AssertionError("No centers found in the search results.");
        }
    }

    public void verifyNumberOfResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("Waiting for search results to be displayed");
        boolean isResultsDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("center-results-container"))).isDisplayed();

        if (!isResultsDisplayed) {
            logger.error("Search results did not load after entering location");
            throw new AssertionError("Search results did not load after entering location.");
        }

        int displayedCenters = getDisplayedCenterCount();
        int expectedCenters = getExpectedCenterCount();
        logger.info("Comparing displayed ({}) and expected ({}) centers", displayedCenters, expectedCenters);
        assertEquals("Mismatch between found and displayed center counts", expectedCenters, displayedCenters);
    }

    public boolean isCenterNameAndAddressMatching() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("Fetching first center details from the results list");
        WebElement firstCenter = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@id='center-results-container']//div[contains(@class, 'centerResult')][1]")));

        String listCenterName = firstCenter.findElement(By.xpath(".//h3[contains(@class, 'centerResult__name')]")).getText().trim();
        String listCenterAddress = firstCenter.findElement(By.xpath(".//span[contains(@class, 'centerResult__address')]")).getText().trim();

        logger.info("Fetching details from the pop-up");
        WebElement popup = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'gm-style-iw')]"))); // Ensure the pop-up is visible

        String popupName = popup.findElement(By.xpath(".//span[contains(@class, 'mapTooltip__headline')]")).getText().trim();
        WebElement popupAddressElement = popup.findElement(By.xpath(".//div[contains(@class, 'mapTooltip__address')]"));
        String popupAddress = popupAddressElement.getText().replaceAll("\n", " ").trim();

        logger.info("listCenterName: {}", listCenterName);
        logger.info("listCenterAddress: {}", listCenterAddress);
        logger.info("popupName: {}", popupName);
        logger.info("popupAddress: {}", popupAddress);

        boolean isMatch = listCenterName.equalsIgnoreCase(popupName) && listCenterAddress.equalsIgnoreCase(popupAddress);
        if (!isMatch) {
            logger.error("Center name and address do not match");
        }
        return isMatch;
    }
}
