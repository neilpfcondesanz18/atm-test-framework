@api @cards
Feature: Card Management API
  As a banking system
  I want to manage debit and credit cards
  So that customers can use ATMs and make payments

  Background:
    Given the banking API is available

  @smoke @card-issue
  Scenario: Issue a debit card for an active account
    When I issue a "DEBIT" card for account "ACC-001"
    Then the card should be issued successfully
    And the issued card number should not be null

  @card-issue @regression
  Scenario: Issue a credit card for an active account
    When I issue a "CREDIT" card for account "ACC-002"
    Then the card should be issued successfully

  @card-block @regression
  Scenario: Block an issued card
    When I issue a "DEBIT" card for account "ACC-001"
    And I block card "1234567890123456"
    Then the response status should be 200
    And the card status should be "BLOCKED"

  @pin @regression
  Scenario: Validate a correct PIN
    When I validate PIN "1234" for card "1234567890123456"
    Then the response status should be 200
    And the PIN validation result should be "true"

  @pin @negative
  Scenario: Validate an incorrect PIN format
    When I validate PIN "ab" for card "1234567890123456"
    Then the response status should be 200
    And the PIN validation result should be "false"

  @card-issue @negative
  Scenario: Issue card for non-existent account returns 404
    When I issue a "DEBIT" card for account "ACC-NONEXISTENT"
    Then the response status should be 404
