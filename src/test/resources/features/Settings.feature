Feature: Account Service - Get Account Settings Page

  @AccountService
  Scenario Outline: Get Account Service Request
    Given User <email> signs in with valid <pwd>
    When User checks account settings
    Then User should get an account settings response
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |


  @AccountService
  Scenario Outline: Remove all email preferences
    Given User <email> signs in with valid <pwd>
    When User <email> removes all email preferences
    Then User should see no email preferences
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |

  @AccountService
  Scenario Outline: Add all email preferences
    Given User <email> signs in with valid <pwd>
    When User <email> selects all email preferences
    Then User should see all email preferences
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |


  @AccountService
  Scenario Outline: Add all email preferences
    When User <email> with bad JSessionId updates email preferences
    Then user should get status 401 and error User is not logged in in response even though the call does not hit Website Backend
    Examples:
      | email          |
      | zzz@email.test |
    