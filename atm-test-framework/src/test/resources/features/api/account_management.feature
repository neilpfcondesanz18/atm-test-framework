@api
Feature: Account Management API
  As a banking system
  I want to manage accounts via API
  So that customers can perform banking operations

  Background:
    Given the banking API is available

  @smoke @account
  Scenario: Successfully create a new account
    Given I have a valid account payload
    When I create the account
    Then the account should be created successfully

  @account @regression
  Scenario: Create account with high balance
    Given I have an account payload with balance 100000.00
    When I create the account
    Then the account should be created successfully

  @account @negative
  Scenario: Retrieve non-existent account returns 404
    When I retrieve account "ACC-DOES-NOT-EXIST"
    Then the response status should be 404

  @atm @withdrawal @regression
  Scenario: Successful ATM withdrawal reduces balance correctly
    Given I have an account payload with balance 1000.00
    When I create the account
    And I withdraw 200.00 from account "ACC-001" at ATM "ATM-CBD-01"
    Then the response status should be 200
    Then the transaction type should be "WITHDRAWAL"

  @atm @withdrawal @negative
  Scenario: Withdrawal with insufficient funds returns 422
    When I withdraw 99999.00 from account "ACC-001" at ATM "ATM-001"
    Then the response status should be 422
    And the error message should contain "Insufficient"

  @atm @deposit @regression
  Scenario: Deposit increases account balance
    When I deposit 500.00 into account "ACC-001"
    Then the response status should be 200
    And the transaction type should be "DEPOSIT"

  @transfer @regression
  Scenario: Transfer funds between accounts
    When I transfer 100.00 from "ACC-001" to "ACC-002"
    Then the response status should be 200
    And the transaction type should be "TRANSFER"

  @account @freeze @regression
  Scenario: Freeze an account
    When I freeze account "ACC-003"
    Then the response status should be 200
    And the account status should be "FROZEN"

  @account @freeze @negative
  Scenario: Withdrawal from frozen account returns 422
    Given I freeze account "ACC-FROZEN"
    When I withdraw 100.00 from account "ACC-FROZEN" at ATM "ATM-001"
    Then the response status should be 422
    And the error message should contain "not active"
