package com.brighthorizon.test.web.stepdefs;

import com.brighthorizon.test.automation.framework.core.DriverManager;
import com.brighthorizon.test.web.pagebojects.BrightHorizonsFindCenterPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class FindCenterSteps {

    private WebDriver driver;
    private BrightHorizonsFindCenterPage findCenterPage;

    public FindCenterSteps() {
        this.driver = DriverManager.getDriver();
        this.findCenterPage = new BrightHorizonsFindCenterPage(driver);
    }

    @When("I click on Find a Center option in the top header")
    public void iClickOnFindACenterOption() {
        findCenterPage.clickFindCenterOption();
    }

    @Then("I should see that the newly opened page contains {string} in the URL")
    public void iShouldSeeThatTheNewPageContainsInURL(String expectedURL) {
        assertTrue("The page URL does not contain expected text", findCenterPage.isCorrectURLDisplayed());
    }

    @When("I type {string} into the search box and press Enter")
    public void iTypeIntoTheSearchBoxAndPressEnter(String location) {
        findCenterPage.enterLocationAndVerify(location);
    }

    @Then("I verify if the number of found centers is the same as the number of centers displayed in the list")
    public void iVerifyNumberOfCentersMatchesDisplayedCount() {
        findCenterPage.verifyNumberOfResults();
    }


    @When("I click on the first center in the list")
    public void iClickOnTheFirstCenterInTheList() {
        findCenterPage.clickFirstCenter();
    }

    @Then("I verify that the center name and address match between the list and the popup")
    public void iVerifyThatCenterNameAndAddressMatch() {
        assertTrue("Center name and address do not match", findCenterPage.isCenterNameAndAddressMatching());
    }
}
