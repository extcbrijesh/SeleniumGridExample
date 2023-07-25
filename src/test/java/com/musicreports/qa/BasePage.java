package com.musicreports.qa;

import com.musicreports.qa.driver.DriverManager;
import com.musicreports.qa.driver.Wait;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BasePage {

    protected RemoteWebDriver driver;
    protected Wait wait;

    public BasePage() {
        this.driver = DriverManager.driver;
        this.wait = new Wait(this.driver);
    }

}
