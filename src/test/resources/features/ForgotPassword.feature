Feature: Account Service - Forgot Password

  @AccountService
  Scenario: Forgot Password
    When I forgot my password and enter an email that does not exist in the database
    Then User should receive a 200 success response
