Feature: Checkout UI
  As a user, I want to complete a checkout in the app

  Scenario Outline: Checkout Success: <caseName>
    Given the user is logged in as "<user>" with password "<password>"
    And the user opens the checkout page
    When the user completes the checkout form with valid data
    And the user ensures the Shipping address is the same as billing
    And the user submits the checkout form
    Then the user is redirected to the order page and sees a non-empty confirmation number

    Examples:
      | caseName            | user      | password    |
      | successful checkout | johndoe19 | supersecret |

  Scenario Outline: Checkout Failure (Alert): <caseName>
    Given the user is logged in as "<user>" with password "<password>"
    And the user opens the checkout page
    When the user completes the checkout form with valid data
    And the user ensures the Shipping address is NOT the same as billing
    And the user tries to submit and stub
    Then the user verifies no alert is currently open

    Examples:
      | caseName                              | user      | password    |
      | alert when shipping differs from bill | johndoe19 | supersecret |

  Scenario: Cart Total
    Given the user opens the checkout page
    Then the user sees the cart total equals the sum of the item prices
