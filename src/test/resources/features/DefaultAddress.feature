Feature: Account Service - Account Address-Book Page

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no <address-type> addresses saved
    When User with <email> creates <address-type> address and marks it as default <default>
    Then User will see <address-type> address as default anyway because it is the only address
    Examples:
      | email          | pwd     | address-type | default |
      | zzz@email.test | test123 | shipping     | false   |
      | zzz@email.test | test123 | shipping     | true    |
      | zzz@email.test | test123 | billing      | false   |
      | zzz@email.test | test123 | billing      | true    |

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no <address-type> addresses saved
    When User with <email> creates two <address-type> addresses
    Then After deleting the default <address-type> address the other address will be marked as default
    Examples:
      | email          | pwd     | address-type |
      | zzz@email.test | test123 | shipping     |
      | zzz@email.test | test123 | billing      |
