Feature: Login UI
  As a user, I want to log in to the app

  Scenario Outline: Login Success: <caseName>
    Given the user opens the login page
    When the user logs in with user "<user>" and password "<password>"
    Then the user sees a welcome message containing "<user>"

    Examples:
      | caseName             | user      | password    |
      | successful login     | johndoe19 | supersecret |

  Scenario Outline: Login Failure: <caseName>
    Given the user opens the login page
    When the user logs in with user "<user>" and password "<password>"
    Then the user sees an error message

    Examples:
      | caseName                                    | user      | password  |
      | fail login with incorrect user and password | wronguser | wrongpass |
      | fail login with empty user and password     |           |           |
