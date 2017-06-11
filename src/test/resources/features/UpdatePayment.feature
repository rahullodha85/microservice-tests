Feature: Account Service - Account Payment Method Page

  @Payment
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no payment methods saved
    When User <email> creates a chinese union pay card
    Then When user <email> updates the name of the card, it is still a chinese union pay card
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |
