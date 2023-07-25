package com.musicreports.qa.tests.base;

import com.musicreports.qa.Enums.Browsers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import static com.musicreports.qa.driver.DriverManager.createDriver;
import static com.musicreports.qa.driver.DriverManager.quitDriver;

public class BaseSuiteSetup {
    @Parameters("browser")
    @BeforeClass(alwaysRun = true)
    public void setupTest (final String browser) {
        createDriver (Browsers.valueOf (browser.toUpperCase ()));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown () {
        quitDriver ();
    }


}
