Feature: Account Service - Get Account Summary Page

  @AccountService
  Scenario Outline: Get Account Summary Request
    Given User <email> signs in with valid <pwd>
    When User check account summary
    Then User should get an account summary response
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |
