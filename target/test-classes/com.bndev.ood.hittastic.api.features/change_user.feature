Feature: Change user

  Scenario Outline: change user details
    Given there are no users loaded
    Given an admin is logged in
    Given the following users are loaded
      | username | password |
      | ben      | 123      |
      | test     | 123      |
    When change user is called on <user> with details <newuser> <password> <admin>
    Then <newuser> is not null
    Then the password of <newuser> is <password>
    Then the admin state of <newuser> is <admin>

    Examples:
      | user   | newuser | password | admin   |
      | "ben"  | "ben"   | "1234"   | "true"  |
      | "test" | "test2" | "000"    | "false" |
