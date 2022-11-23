Feature: Buy Song

  Background:
    Given user is logged in
    Given song is selected
    Given the selected song has no sales
    Given the current user has no orders

  Scenario Outline: buy a song
    When Buy is run with a quantity of <qty>
    Then song sales is <qty>
    And orders size is 1
    And order is not null
    Examples:
      | qty |
      | 1   |
      | 2   |
      | 3   |

  Scenario Outline: buy a song (invalid)
    When Buy is run with a quantity of <qty>
    Then song sales is 0
    And orders size is 0
    And order is null
    Examples:
      | qty |
      | 0   |
      | -1  |