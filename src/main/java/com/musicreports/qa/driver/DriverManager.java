package com.musicreports.qa.driver;

import com.musicreports.qa.Enums.Browsers;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
public class DriverManager {

    private static final Logger LOG = LoggerFactory.getLogger("DriverManager.class");

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final String HUB_URL = "http://localhost:4444/wd/hub";
    public static RemoteWebDriver driver;

    private static final String NO_SANDBOX = "--no-sandbox";
    private static final String DISABLE_DEV_SHM = "--disable-dev-shm-usage";
    private static final String CUSTOM_WINDOW_SIZE = "--window-size=1050,600";
    private static final String HEADLESS = "--headless";
    private static final String START_MAXIMIZED = "--start-maximized";

    public static WebDriver getDriver() {
        return DriverManager.DRIVER.get();
    }

    @Before
    public static void createDriver(final Browsers browser) {
        switch (browser) {
            case FIREFOX -> setupFirefoxDriver();
            case EDGE -> setupEdgeDriver();
            case REMOTE_CHROME -> setupRemoteChrome();
            case REMOTE_FIREFOX -> setupRemoteFirefox();
            case REMOTE_EDGE -> setupRemoteEdge();
            default -> setupChromeDriver();
        }
        setupBrowserTimeouts();
    }

    private static void setupBrowserTimeouts() {
        getDriver().manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(5));
        getDriver().manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(5));
        getDriver().manage()
                .timeouts()
                .scriptTimeout(Duration.ofSeconds(5));
    }

    private static void setRemoteDriver(String browser, String version) {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setBrowserName(browser);
            caps.setVersion(version);
            driver = new RemoteWebDriver(new URI("http://localhost:444/wd/hub").toURL(), caps);
        } catch (MalformedURLException | URISyntaxException ex) {
            log.info(ex.getMessage());
        }
    }

    private static void setupEdgeDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("edge");
        caps.setPlatform(Platform.WIN10);
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments(START_MAXIMIZED);
        edgeOptions.merge(caps);

        setDriver(WebDriverManager.edgedriver()
                .capabilities(caps)
                .create());
    }

    private static void setupFirefoxDriver() {
        final var options = new FirefoxOptions();
        options.addArguments(NO_SANDBOX);
        options.addArguments(DISABLE_DEV_SHM);
        options.addArguments(CUSTOM_WINDOW_SIZE);
        options.addArguments(HEADLESS);
        setDriver(WebDriverManager.firefoxdriver()
                .capabilities(options)
                .create());
    }

    private static void setupChromeDriver() {
        DesiredCapabilities capabilityCH = new DesiredCapabilities();
        capabilityCH.setBrowserName("chrome");
        capabilityCH.setPlatform(Platform.LINUX);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.merge(capabilityCH);

        final var isHeadless = Boolean.parseBoolean(
                Objects.requireNonNullElse(System.getProperty("headless"), "true"));
        final var chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("safebrowsing.enabled", "true");
        chromePrefs.put("download.prompt_for_download", "false");
        chromePrefs.put("download.default_directory",
                String.valueOf(Paths.get(System.getProperty("user.home"), "Downloads")));

        final var options = new ChromeOptions();
        options.addArguments(NO_SANDBOX);
        options.addArguments(START_MAXIMIZED);
        options.addArguments(DISABLE_DEV_SHM);
        options.addArguments(CUSTOM_WINDOW_SIZE);
        if (isHeadless) {
            options.addArguments(HEADLESS);
        }
        options.addArguments("--safebrowsing-disable-download-protection");
        options.setExperimentalOption("prefs", chromePrefs);

        setDriver(WebDriverManager.chromiumdriver()
                .capabilities(chromeOptions).create());
    }


    private static void setupRemoteChrome() {
        try {
            LOG.info("Setting up Remote Chrome Driver....");
            final var options = new ChromeOptions();
            options.addArguments(NO_SANDBOX);
            options.addArguments(DISABLE_DEV_SHM);
            setDriver(new RemoteWebDriver(new URL(HUB_URL), options));
            LOG.info("Remote Chrome Driver created successfully!");
        } catch (final MalformedURLException e) {
            LOG.error("Error setting remote_chrome", e);
        }
    }

    private static void setupRemoteEdge() {
        try {
            LOG.info("Setting up Remote Edge Driver....");
            final var edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(NO_SANDBOX);
            edgeOptions.addArguments(DISABLE_DEV_SHM);
            setDriver(new RemoteWebDriver(new URL(HUB_URL), edgeOptions));
            LOG.info("Remote Edge Driver created successfully!");
        } catch (final MalformedURLException e) {
            LOG.error("Error setting remote_edge", e);
        }
    }

    private static void setupRemoteFirefox() {
        try {

            LOG.info("Setting up Remote Firefox Driver....");
            final var firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(NO_SANDBOX);
            firefoxOptions.addArguments(DISABLE_DEV_SHM);

            setDriver(new RemoteWebDriver(new URL(HUB_URL), firefoxOptions));
            LOG.info("Remote Firefox Driver created successfully!");
        } catch (final MalformedURLException e) {
            LOG.error("Error setting remote_firefox", e);
        }
    }


    private static void setDriver(final WebDriver driver) {
        DriverManager.DRIVER.set(driver);
    }

    public static void quitDriver() {
        if (DRIVER.get() != null) {
            getDriver().quit();
            DRIVER.remove();
        }
    }

}
