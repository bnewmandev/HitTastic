Feature: Add a song

  Scenario: add a song
    Given an admin is logged in
    Given there are no songs loaded
    When a new song is added
    Then the length of songs is 1

  Scenario: user attempt to add song
    Given a user is logged in
    Given there are no songs loaded
    When a new song is added
    Then the length of songs is 0
    And an authentication exception is thrown