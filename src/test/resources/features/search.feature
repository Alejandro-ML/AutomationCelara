Feature: Search UI
  As a user, I want to search and see appropriate messages

  Scenario Outline: Search Success: <caseName>
    Given the user is logged in as "<user>" with password "<password>"
    And the user opens the search page
    When the user searches for the word "<word>"
    Then the user sees the success message containing "<word>"

    Examples:
      | caseName          | user      | password    | word       |
      | search automation | johndoe19 | supersecret | automation |

  Scenario Outline: Search Empty: <caseName>
    Given the user is logged in as "<user>" with password "<password>"
    And the user opens the search page
    When the user submits an empty search
    Then the user sees the empty-search error message

    Examples:
      | caseName     | user      | password    |
      | empty search | johndoe19 | supersecret |
