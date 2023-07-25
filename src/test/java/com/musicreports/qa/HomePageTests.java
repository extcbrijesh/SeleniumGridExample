package com.musicreports.qa;

import com.musicreports.qa.tests.base.BaseSuiteSetup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.musicreports.qa.driver.DriverManager.getDriver;


public class HomePageTests extends BaseSuiteSetup {


    @Test
    public void assertTitleTests() throws InterruptedException {
        String expectedTitle = "Welcome to Music Reports!";
        System.out.println("Now we would be waiting for 15seconds");
        getDriver().get("https://www.musicreports.com");
        System.out.println(getDriver().getTitle());
        Assert.assertTrue(getDriver().getTitle().contains(expectedTitle));
    }


    @Test
    public void assertHomePageElementsTests(){
        System.out.println(getDriver().getCurrentUrl());
        Assert.assertTrue(getDriver().getCurrentUrl().contentEquals("https://www.musicreports.com"));
    }

}
