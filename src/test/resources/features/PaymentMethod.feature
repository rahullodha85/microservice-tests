Feature: Account Service all end-point tests

  @Payment
  Scenario Outline: Create And Delete Payment-Method Test
    Given User <email> signs in with valid <pwd>
    Given User has no payment methods saved
    When user <email> sent valid create payment-method request
    Then user should get that payment-method in get payment request
    When user sent delete payment-method request
    Then payment method for that user should be removed
    Examples:
      | email          | pwd     |
      | zzz@email.test | test123 |

  @Payment
  Scenario Outline: Create Payment with Invalid Data
    Given User <email> signs in with valid <pwd>
    When user <email> sent invalid create payment-method request
    Then user should get status <Status Code> and error <Error Data> in response
    Examples:
      | Status Code | email          | pwd     | Error Data                               |
      | 400         | zzz@email.test | test123 | Please enter a valid credit card number. |

  @Payment
  Scenario Outline: Create Payment - Duplicate Payment Method
    Given User <email> signs in with valid <pwd>
    And user <email> sent valid create payment-method request
    When user <email> send create payment-method request with same credit card
    Then user should get status <Status Code> and error <Error Data> in response
    Examples:
      | Status Code | email          | pwd     | Error Data                                                                       |
      | 400         | zzz@email.test | test123 | You have already entered this card number. Please enter a different card number. |

  Scenario Outline: Create Payment - Invalid JSessionID
    Given User <email> signs in with valid <pwd>
    And user has invalid JSessionID
    When user <email> sent valid create payment-method request
    Then user should get status <Status Code> and error <Error Data> in response
    Examples:
      | Status Code | email          | pwd     | Error Data            |
      | 401         | zzz@email.test | test123 | User is not logged in |
