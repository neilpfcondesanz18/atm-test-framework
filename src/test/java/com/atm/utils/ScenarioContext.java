package com.atm.utils;

import io.restassured.response.Response;

/**
 * Shared state container injected by Cucumber's PicoContainer into step def classes.
 * Allows multiple step def classes to share the last API response.
 */
public class ScenarioContext {
    public Response lastResponse;
    public String currentAccountNumber;
    public String secondAccountNumber;
    public String lastIssuedCardNumber;
}
