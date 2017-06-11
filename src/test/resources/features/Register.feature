Feature: Account Service - Get Register Account Page

  @AccountService
  Scenario:
    When User sends Get Register Account Page request
    Then User should receive a valid register account response
