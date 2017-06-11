Feature: Account Service - Update Account Address-Book Page

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no <address-type> addresses saved
    Given User <email> creates address in address-book with address-type <address-type>
    When User with <email> changes <address-type> address to have street address <address1>
    Then User with <email> should be able to change <address-type> address back to have street address <address2>
    Examples:
      | email          | pwd     | address-type | address1     | address2      |
      | zzz@email.test | test123 | shipping     | 250 vesey st | 12 barclay st |
      | zzz@email.test | test123 | billing      | 123 main st  | 3 broadway    |
