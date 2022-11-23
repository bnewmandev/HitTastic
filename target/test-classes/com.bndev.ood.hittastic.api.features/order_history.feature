Feature: Order history

  Scenario: view order history
    Given user data is loaded
    When get order history is called
    Then order history size is 3