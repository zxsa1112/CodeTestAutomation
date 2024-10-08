Feature: Stock Order

  Scenario: Order a stock
    Given I have a valid account
    When I place an order for 10 shares of stock at $100
    Then the order should be confirmed
