package com.atm.utils;

import com.github.javafaker.Faker;
import java.util.*;

public class TestDataFactory {

    private static final Faker faker = new Faker();

    public static Map<String, Object> validAccount() {
        return validAccount(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }

    public static Map<String, Object> validAccount(String accountNumber) {
        Map<String, Object> account = new LinkedHashMap<>();
        account.put("accountNumber", "ACC-" + accountNumber);
        account.put("accountHolderName", faker.name().fullName());
        account.put("balance", faker.number().randomDouble(2, 100, 50000));
        account.put("accountType", randomFrom("SAVINGS", "CHECKING"));
        account.put("currency", "NZD");
        return account;
    }

    public static Map<String, Object> accountWithBalance(double balance) {
        Map<String, Object> account = validAccount();
        account.put("balance", balance);
        return account;
    }

    public static Map<String, Object> frozenAccount() {
        Map<String, Object> account = validAccount();
        account.put("status", "FROZEN");
        return account;
    }

    public static Map<String, Object> invalidAccount() {
        return new HashMap<>(); // missing required fields
    }

    public static String randomAccountNumber() {
        return "ACC-" + faker.numerify("######");
    }

    public static String randomAtmId() {
        return "ATM-" + faker.letterify("???").toUpperCase() + "-" + faker.numerify("##");
    }

    public static String validPin() { return "1234"; }
    public static String invalidPin() { return "9999"; }
    public static String shortPin() { return "12"; }

    @SafeVarargs
    private static <T> T randomFrom(T... values) {
        return values[new Random().nextInt(values.length)];
    }
}
