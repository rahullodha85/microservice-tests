Feature: Account Service - SignOut

  @Logout
  Scenario Outline: SignOut Request
    Given User <email> signs in with valid <pwd>
    When User sends Signs out
    When User checks account settings
    Then user should get status <status-code> and error <error-message> in response
    Examples:
      | email          | pwd     | status-code | error-message         |
      | zzz@email.test | test123 | 401         | User is not logged in |

  @Logout
  Scenario Outline: SignOut Request
    Given User <email> signs in with valid <pwd>
    When User sends Signs out without any cookies
    When User checks account settings
    Then User should get an account settings response
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |
