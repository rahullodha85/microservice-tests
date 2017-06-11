Feature: Account Service - Get Account Order Status Page


  Scenario Outline: Check Order Status
    When User checks order status with zip <zip>
    Then User should get order status
    Examples:
      | zip   |
      | 10080 |


  Scenario Outline: Check Order Status action with bad session cookie
    When Check order status with zip <zip> with session <sessionid>
    Then User should get order status
    Examples:
      | zip   | sessionid |
      | 10080 | test      |