package com.brighthorizon.test.automation.framework.core;

import com.brighthorizon.test.automation.framework.runner.BrowserStackRunner;
import com.brighthorizon.test.automation.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> executionPlatform = ThreadLocal.withInitial(() -> ConfigReader.getGlobal("execution-platform").toLowerCase());

    /**
     * Initializes the WebDriver instance for the current thread.
     *
     * @param browser the browser type (e.g., chrome, firefox, edge)
     * @return WebDriver instance for the current thread
     */
    public static WebDriver initializeDriver(String browser) {
        WebDriver driver = null;

        try {
            String platform = executionPlatform.get();
            logger.info("Execution Platform: {}", platform);

            switch (platform) {
                case "local-driver":
                    driver = initializeLocalDriver(browser);
                    break;
                case "local-driver-manager":
                    driver = initializeLocalDriverManager(browser);
                    break;
                case "cloud":
                    driver = initializeCloudDriver(browser);
                    break;
                case "docker":
                    driver = initializeDockerDriver(browser);
                    break;
                case "grid":
                    driver = initializeGridDriver(browser);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported execution platform: " + platform);
            }

            if (driver != null) {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                driverThreadLocal.set(driver);
                logger.info("{} browser initialized successfully for thread: {}", browser, Thread.currentThread().getId());
            }
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: {}. Error: {}", browser, e.getMessage(), e);
            throw new RuntimeException("WebDriver initialization failed.", e);
        }

        return driver;
    }

    private static WebDriver initializeLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-geolocation");
                chromeOptions.addArguments("--use-fake-ui-for-media-stream"); // Disables popup
                System.setProperty("webdriver.chrome.driver", ConfigReader.getGlobal("chrome-driver-path"));
                logger.info("Initializing Chrome browser with disabled geolocation.");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("geo.enabled", false); // Disables geolocation
                firefoxOptions.addPreference("permissions.default.geo", 2); // Deny location automatically
                System.setProperty("webdriver.gecko.driver", ConfigReader.getGlobal("firefox-driver-path"));
                logger.info("Initializing Firefox browser with disabled geolocation.");
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-geolocation");
                System.setProperty("webdriver.edge.driver", ConfigReader.getGlobal("edge-driver-path"));
                logger.info("Initializing Edge browser with disabled geolocation.");
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }


    private static WebDriver initializeLocalDriverManager(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-geolocation");
                chromeOptions.addArguments("--use-fake-ui-for-media-stream"); // Blocks location pop-ups
                logger.info("Initializing Chrome browser using WebDriverManager with geolocation disabled.");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("geo.enabled", false);
                firefoxOptions.addPreference("permissions.default.geo", 2);
                logger.info("Initializing Firefox browser using WebDriverManager with geolocation disabled.");
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-geolocation");
                logger.info("Initializing Edge browser using WebDriverManager with geolocation disabled.");
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }


    private static WebDriver initializeCloudDriver(String browser) {
        String cloudProvider = ConfigReader.getGlobal("cloud-provider").toLowerCase();
        if ("browserstack".equals(cloudProvider)) {
            return initializeBrowserStackDriver();
        } else {
            throw new IllegalArgumentException("Unsupported cloud provider: " + cloudProvider);
        }
    }

    private static WebDriver initializeDockerDriver(String browser) throws MalformedURLException {
        String dockerHubURL = ConfigReader.getGlobal("docker-hub-url");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless", "--disable-gpu");
                capabilities.merge(chromeOptions);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless", "--disable-gpu");
                capabilities.merge(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless", "--disable-gpu");
                capabilities.merge(edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        return new RemoteWebDriver(new URL(dockerHubURL), capabilities);
    }

    private static WebDriver initializeGridDriver(String browser) throws MalformedURLException {
        String seleniumGridUrl = ConfigReader.getGlobal("grid-url");

        switch (browser.toLowerCase()) {
            case "chrome":
                return new RemoteWebDriver(new URL(seleniumGridUrl), new ChromeOptions());
            case "firefox":
                return new RemoteWebDriver(new URL(seleniumGridUrl), new FirefoxOptions());
            case "edge":
                return new RemoteWebDriver(new URL(seleniumGridUrl), new EdgeOptions());
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver initializeBrowserStackDriver() {
        MutableCapabilities capabilities = new MutableCapabilities();
        HashMap<String, Object> bstackOptions = new HashMap<>();

        String os = BrowserStackRunner.getOS();
        String browser = BrowserStackRunner.getBrowser();
        String osVersion = BrowserStackRunner.getOSVersion();
        String deviceName = BrowserStackRunner.getDeviceName();
        String browserVersion = BrowserStackRunner.getBrowserVersion();
        String username = ConfigReader.getBrowserStack("browserstack.username");
        String accessKey = ConfigReader.getBrowserStack("browserstack.accessKey");

        String hubUrl = String.format("https://%s:%s@hub.browserstack.com/wd/hub", username, accessKey);

        if (deviceName != null && !deviceName.isEmpty()) {
            capabilities.setCapability("browserName", browser);
            bstackOptions.put("deviceName", deviceName);
            bstackOptions.put("osVersion", osVersion);
        } else {
            capabilities.setCapability("browserName", browser);
            bstackOptions.put("os", os);
            bstackOptions.put("osVersion", osVersion);
            bstackOptions.put("browserVersion", browserVersion);
        }

        bstackOptions.put("userName", username);
        bstackOptions.put("accessKey", accessKey);

        capabilities.setCapability("bstack:options", bstackOptions);

        try {
            return new RemoteWebDriver(new URL(hubUrl), capabilities);
        } catch (MalformedURLException e) {
            logger.error("Invalid BrowserStack Hub URL.", e);
            throw new RuntimeException("Failed to initialize BrowserStack driver.", e);
        }
    }

    /**
     * Retrieves the WebDriver instance for the current thread.
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Cleans up the WebDriver instance for the current thread.
     */
    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
