Feature: Account Service - Get Account Address-Book Page

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no <address-type> addresses saved
    When User with <email> creates the same <address-type> address twice
    Then user should get status <status-code> and error <error-message> in response
    Examples:
      | email          | pwd     | address-type | status-code | error-message                                                                          |
      | zzz@email.test | test123 | shipping     | 400         | You already have this address in your address book.  Please enter a different address. |
      | zzz@email.test | test123 | billing      | 400         | You already have this address in your address book.  Please enter a different address. |

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    When User with <email> creates a <address-type> address with an invalid canadian postal code
    Then user should get status <status-code> and error <error-message> in response
    Examples:
      | email          | pwd     | address-type | status-code | error-message                          |
      | zzz@email.test | test123 | shipping     | 400         | Province and postal code do not match. |
      | zzz@email.test | test123 | billing      | 400         | Province and postal code do not match. |

  @Address
  Scenario Outline:
    Given User <email> signs in with valid <pwd>
    Given User has no <address-type> addresses saved
    When User with <email> creates a <address-type> address with province <province> and a valid canadian postal code <postal-code>
    Then User will successfully save that <address-type> address and see the province <province> and postal code <postal-code>
    Examples:
      | email          | pwd     | address-type | province | postal-code |
      | zzz@email.test | test123 | shipping     | BC       | V2H 0K8     |
      | zzz@email.test | test123 | billing      | BC       | V2H 0K8     |

    @Address
    Scenario Outline:
      Given User <email> signs in with valid <pwd>
      Given User has no <address-type> addresses saved
      When User with <email> successfully creates an international <address-type> address with a province longer than 3 characters
      Then User should be able to successfully retrieve that <address-type> address
      Examples:
        | email          | pwd     | address-type |
        | zzz@email.test | test123 | shipping     |
        | zzz@email.test | test123 | billing      |