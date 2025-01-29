Feature: Verify search functionality

  @smoke
  Scenario: Positive Scenario - Verify search functionality with valid input
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on the search-loop icon at the top right corner
    Then I should see the search field visible on the page
    When I type "Employee Education in 2018: Strategies to Watch" into the search field
    And I click on the Search button
    Then the first search result should be an exact match to "Employee Education in 2018: Strategies to Watch"

  @smoke
  Scenario: Negative Scenario - Verify search functionality with invalid input
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on the search-loop icon at the top right corner
    Then I should see the search field visible on the page
    When I type "InvalidSearchQuery12345!@#" into the search field
    And I click on the Search button
    Then I should see a message indicating no results were found

  @smoke
  Scenario: Negative Scenario - Verify search functionality with empty input
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on the search-loop icon at the top right corner
    Then I should see the search field visible on the page
    When I leave the search field empty
    And I click on the Search button
    Then I should see search results displayed

  @smoke
  Scenario: Negative Scenario - Verify search functionality with special characters
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on the search-loop icon at the top right corner
    Then I should see the search field visible on the page
    When I type "***^&$@#@#$@@@###$$$" into the search field
    And I click on the Search button
    Then I should see a message indicating no results were found
