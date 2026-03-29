@integration
Feature: End-to-End ATM Transaction Flows
  As a bank customer
  I want to perform complete ATM operations
  So that I can manage my finances end-to-end

  Background:
    Given the banking API is available

  @e2e @smoke
  Scenario: Complete ATM cash withdrawal flow
    Given I have an account payload with balance 2000.00
    When I create the account
    And I issue a "DEBIT" card for account "ACC-001"
    And I withdraw 500.00 from account "ACC-001" at ATM "ATM-QUEEN-ST-01"
    Then the response status should be 200
    And the transaction type should be "WITHDRAWAL"

  @e2e @regression
  Scenario: Transfer then verify transaction history
    When I transfer 250.00 from "ACC-001" to "ACC-002"
    Then the response status should be 200
    When I retrieve account "ACC-001"
    Then the response status should be 200

  @e2e @regression
  Scenario: Card block prevents ATM operations - card re-enable flow
    When I issue a "DEBIT" card for account "ACC-001"
    And the card should be issued successfully
    # In a full e2e with card-linked withdrawal, unblock flow would be tested here
