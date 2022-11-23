Feature: List users

  Scenario: List users
    Given an admin is logged in
    Given the following users are loaded
      | username | password |
      | "ben"    | "123"    |
      | "test"   | "test"   |
      | "admin"  | "pass"   |
    When list users is called
    Then size of user list is 4