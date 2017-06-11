Feature: Account Service - Get Account Order History Page

  @AccountService
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    When User checks account order history
    Then User should get an account order history response
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |
