package com.atm.hooks;

import com.atm.utils.DriverManager;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class CucumberHooks {

    private static final Logger log = LoggerFactory.getLogger(CucumberHooks.class);

    @Before("@ui")
    public void beforeUiScenario(Scenario scenario) {
        log.info("▶ Starting UI scenario: {}", scenario.getName());
        DriverManager.getDriver(); // initialise driver
    }

    @After("@ui")
    public void afterUiScenario(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (scenario.isFailed() && driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
            scenario.attach(screenshot, "image/png", "Failure screenshot");
            log.warn("📸 Screenshot captured for failed scenario: {}", scenario.getName());
        }
        DriverManager.quitDriver();
        log.info("✅ Finished scenario: {} — Status: {}", scenario.getName(), scenario.getStatus());
    }

    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        log.info("▶ Starting API scenario: {}", scenario.getName());
    }

    @After("@api")
    public void afterApiScenario(Scenario scenario) {
        log.info("✅ Finished API scenario: {} — Status: {}", scenario.getName(), scenario.getStatus());
    }

    @BeforeAll
    public static void globalSetup() {
        log.info("🚀 ATM Test Framework starting...");
    }

    @AfterAll
    public static void globalTeardown() {
        log.info("🏁 ATM Test Framework finished.");
    }
}
