package com.brighthorizon.test.web.stepdefs;


import com.brighthorizon.test.automation.framework.core.DriverManager;
import com.brighthorizon.test.web.pagebojects.BrightHorizonsHomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static com.brighthorizon.test.automation.framework.utils.selenium.BrowserUtils.maximizeWindow;
import static com.brighthorizon.test.automation.framework.utils.selenium.BrowserUtils.navigateTo;
import static org.junit.Assert.assertTrue;

public class SearchFunctionalitySteps {
    private WebDriver driver;
    private BrightHorizonsHomePage brightHorizonsHomePage;

    public SearchFunctionalitySteps() {
        this.driver = DriverManager.getDriver(); // Assuming WebDriver is managed in Hooks
        this.brightHorizonsHomePage = new BrightHorizonsHomePage(driver);
    }

    @Given("I navigate to the BH home page {string}")
    public void iNavigateToTheBHHomePage(String url) {
        navigateTo(driver ,url);
        brightHorizonsHomePage.acceptCookies();
        maximizeWindow(driver);
    }

    @When("I click on the search/loop icon at the top right corner")
    public void iClickOnTheSearchLoopIcon() {
        brightHorizonsHomePage.clickSearchIcon();
    }

    @Then("I should see the search field visible on the page")
    public void iShouldSeeTheSearchFieldVisibleOnThePage() {
        assertTrue("Search field is not visible", brightHorizonsHomePage.isSearchFieldVisible());
    }

    @When("I type {string} into the search field")
    public void iTypeIntoTheSearchField(String searchText) {
        brightHorizonsHomePage.enterSearchText(searchText);
    }

    @When("I click on the Search button")
    public void iClickOnTheSearchButton() {
        brightHorizonsHomePage.clickSearchButton();
    }

    @Then("the first search result should be an exact match to {string}")
    public void theFirstSearchResultShouldBeAnExactMatchTo(String expectedText) {
        brightHorizonsHomePage.verifyFirstSearchResultDynamically(expectedText,0);
    }

    @When("I leave the search field empty")
    public void iLeaveTheSearchFieldEmpty() {
        brightHorizonsHomePage.clearSearchField();
    }

    @Then("I should see a message indicating no results were found")
    public void iShouldSeeAMessageIndicatingNoResultsWereFound() {
        brightHorizonsHomePage.verifyNoResultsMessage("No results found"); // Replace with the actual message if needed
    }

    @Then("I should see a message asking the user to enter a search term")
    public void iShouldSeeAMessageAskingTheUserToEnterASearchTerm() {
        brightHorizonsHomePage.verifyEmptySearchMessage("Please enter a search term"); // Replace with the actual message if needed
    }
    @When("I click on the search-loop icon at the top right corner")
    public void iClickOnTheSearchLoopIconAtTheTopRightCorner() {
        brightHorizonsHomePage.clickSearchIcon();
    }

    @Then("I should see search results displayed")
    public void iShouldSeeSearchResultsDisplayed() {
        brightHorizonsHomePage.verifySearchResultsDisplayed();
    }


}
