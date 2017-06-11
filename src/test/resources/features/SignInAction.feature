Feature: Account Service - SignIn with bad session id

  Scenario Outline: Sign-In with bad session id
    When User signs in with <email> and <pwd> with <sessionid>
    Then User should be successfully signed in
    Examples:
      | email          | pwd     | sessionid |
      | zzz@email.test | test123 | test      |