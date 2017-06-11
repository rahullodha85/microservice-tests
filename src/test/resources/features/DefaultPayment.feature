Feature: Account Service - Account Payment Method Page

  @Payment
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no payment methods saved
    When User with <email> creates two payment methods
    Then After deleting the default payment method the other payment will be marked as default
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |
