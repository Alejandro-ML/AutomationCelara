Feature: Grid UI
  As a user, I want to view items in the grid with correct info

  Scenario Outline: Grid Item: <caseName>
    Given the user opens the grid page
    Then the user sees that item at position <position> has title "<title>"
    And the user sees that item at position <position> has price "<price>"

    Examples:
      | caseName                         | position | title            | price |
      | item 7 is Super Pepperoni price | 7        | Super Pepperoni  | $10   |

  Scenario: Grid All Items
    Given the user opens the grid page
    Then each grid item has non-empty title, price, image and button
