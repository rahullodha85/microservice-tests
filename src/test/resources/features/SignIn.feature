Feature: Account Service - Get SignIn Page

  @AccountService
  Scenario: Get Sign-In Request
    When User sends Get SignIn page request
    Then User should receive a valid SignIn page response
