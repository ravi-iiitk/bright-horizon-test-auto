Feature: Verify Find a Center functionality

  @smoke
  Scenario: Verify navigation to Find a Center page
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on Find a Center option in the top header
    Then I should see that the newly opened page contains "/child-care-locator" in the URL

  @smoke
  Scenario: Search for a center by location
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on Find a Center option in the top header
    Then I should see that the newly opened page contains "/child-care-locator" in the URL
    When I type "New York" into the search box and press Enter
    Then I verify if the number of found centers is the same as the number of centers displayed in the list

  @smoke
  Scenario: Verify center name and address in details popup
    Given I navigate to the BH home page "https://www.brighthorizons.com/"
    When I click on Find a Center option in the top header
    Then I should see that the newly opened page contains "/child-care-locator" in the URL
    When I type "New York" into the search box and press Enter
    Then I verify if the number of found centers is the same as the number of centers displayed in the list
    When I click on the first center in the list
    Then I verify that the center name and address match between the list and the popup
