Feature: Account Service - Create account

  @AccountService @debug
  Scenario: Create account
    Given User creates an account
    Then user should be able to login to make sure account was properly "created"
    And user can change password
    Then user should be able to login to make sure account was properly "updated"
    When user updates their email address
    Then user should be able to login to make sure account was properly "updated"