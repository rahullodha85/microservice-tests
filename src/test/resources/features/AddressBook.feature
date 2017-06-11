Feature: Account Service - Get Account Address-Book Page

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    When User checks <address-type> address-book
    Then User should get an account address-book response
    Examples:
      | email          | pwd     | address-type |
      | zzz@email.test | test123 | shipping     |
      | zzz@email.test | test123 | billing      |

