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
    When I retrieve account "ACC-DOES-NOT-EXIST-XYZ"
    Then the response status should be 404

  @atm @withdrawal @smoke
  Scenario: Successful ATM withdrawal reduces balance correctly
    Given I have an account payload with balance 1000.00
    When I create the account
    And I withdraw 200.00 from the created account at ATM "ATM-CBD-01"
    Then the response status should be 200
    And the transaction type should be "WITHDRAWAL"
    And the balance after should be 800.0

  @atm @withdrawal @negative
  Scenario: Withdrawal with insufficient funds returns 422
    Given I have an account payload with balance 50.00
    When I create the account
    And I withdraw 200.00 from the created account at ATM "ATM-001"
    Then the response status should be 422
    And the error message should contain "Insufficient"

  @atm @deposit @regression
  Scenario: Deposit increases account balance
    Given I have an account payload with balance 500.00
    When I create the account
    And I deposit 300.00 into the created account
    Then the response status should be 200
    And the transaction type should be "DEPOSIT"

  @transfer @regression
  Scenario: Transfer funds between two accounts
    Given I have an account payload with balance 1000.00
    When I create the account
    And I create a second account with balance 500.00
    And I transfer 100.00 from the first account to the second account
    Then the response status should be 200
    And the transaction type should be "TRANSFER"

  @account @freeze @regression
  Scenario: Freeze a newly created account
    Given I have a valid account payload
    When I create the account
    And I freeze the created account
    Then the response status should be 200
    And the account status should be "FROZEN"

  @account @freeze @negative
  Scenario: Withdrawal from frozen account returns 422
    Given I have a valid account payload
    When I create the account
    And I freeze the created account
    And I withdraw 100.00 from the created account at ATM "ATM-001"
    Then the response status should be 422
    And the error message should contain "not active"
