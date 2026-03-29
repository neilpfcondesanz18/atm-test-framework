@api @cards
Feature: Card Management API
  As a banking system
  I want to manage debit and credit cards
  So that customers can use ATMs and make payments

  Background:
    Given the banking API is available

  @smoke @card-issue
  Scenario: Issue a debit card for an active account
    Given I have a valid account payload
    When I create the account
    And I issue a "DEBIT" card for the created account
    Then the response status should be 201
    And the card should be issued successfully
    And the issued card number should not be null

  @card-issue @regression
  Scenario: Issue a credit card for an active account
    Given I have a valid account payload
    When I create the account
    And I issue a "CREDIT" card for the created account
    Then the response status should be 201
    And the card should be issued successfully

  @card-block @regression
  Scenario: Block a newly issued card
    Given I have a valid account payload
    When I create the account
    And I issue a "DEBIT" card for the created account
    Then the response status should be 201
    When I block the last issued card
    Then the response status should be 200
    And the card status should be "BLOCKED"

  @card-block @regression
  Scenario: Unblock a blocked card
    Given I have a valid account payload
    When I create the account
    And I issue a "DEBIT" card for the created account
    Then the response status should be 201
    When I block the last issued card
    Then the response status should be 200
    When I unblock the last issued card
    Then the response status should be 200
    And the card status should be "ACTIVE"

  @pin @regression
  Scenario: Validate a correct PIN returns true
    Given I have a valid account payload
    When I create the account
    And I issue a "DEBIT" card for the created account
    Then the response status should be 201
    When I validate PIN "1234" for the last issued card
    Then the response status should be 200
    And the PIN validation result should be "true"

  @pin @negative
  Scenario: Validate an incorrect PIN format returns false
    Given I have a valid account payload
    When I create the account
    And I issue a "DEBIT" card for the created account
    Then the response status should be 201
    When I validate PIN "ab" for the last issued card
    Then the response status should be 200
    And the PIN validation result should be "false"

  @card-issue @negative
  Scenario: Issue card for non-existent account returns 404
    When I issue a "DEBIT" card for account "ACC-NONEXISTENT-XYZ"
    Then the response status should be 404
