Feature: Login UI
  As a user I want to login in the app


  Scenario Outline: Login Success: <caseName>
    Given I open the login page
    When I login with user "<user>" and password "<password>"
    Then I should see a welcome message containing "<user>"

    Examples:
      | caseName                                    | user      | password    |
      | successful login                            | johndoe19 | supersecret |

  Scenario Outline: Login Failure: <caseName>
    Given I open the login page
    When I login with user "<user>" and password "<password>"
    Then I should see an error message

    Examples:
      | caseName                                    | user      | password    |
      | fail login with incorrect user and password | wronguser | wrongpass   |
      | fail login with empty user and password     |           |             |
