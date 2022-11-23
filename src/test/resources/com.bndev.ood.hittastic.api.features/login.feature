Feature: Login

  Scenario: Successful user login
    Given No user is logged in
    When Login is attempted with username: ben and password 123
    Then session user has username ben
    But session user is an admin

  Scenario: Failed user login - bad password
    Given No user is logged in
    When Login is attempted with username: ben and password 1234
    Then session is null

  Scenario: Failed user login - bad username
    Given No user is logged in
    When Login is attempted with username: benn and password 123
    Then session is null

  Scenario: Successful admin login
    Given No user is logged in
    When Login is attempted with username: admin and password password
    Then session user has username admin
    And session user is an admin
