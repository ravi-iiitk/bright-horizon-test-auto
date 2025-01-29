package hooks;

import com.brighthorizon.test.automation.framework.config.ConfigReader;
import com.brighthorizon.test.automation.framework.core.DriverManager;
import com.brighthorizon.test.automation.framework.reporting.AllureReportGenerator;
import com.brighthorizon.test.automation.framework.reporting.LogSetup;
import com.brighthorizon.test.automation.framework.runner.TestRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterSuite;

import java.io.IOException;

public class CucumberHooks {

    @BeforeAll
    public static void beforeAll()
    {
        LogSetup.setupLoggingProperties();
    }
    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);


    @Before
    public void setUp(Scenario scenario) {
        try {
            logger.info("Starting setup for scenario: {}", scenario.getName());
            String browser = ConfigReader.getBrowser();
            if (!ConfigReader.getGlobal("execution-platform").equalsIgnoreCase("cloud"))
            {
                browser = TestRunner.getBrowser();
                if (browser == null || browser.isEmpty()) {
                    throw new IllegalArgumentException("Browser not specified for the current thread.");
                }
            }

            // Initialize WebDriver
            DriverManager.initializeDriver(browser);
            logger.info("Initialized WebDriver for browser: {}", browser);
        } catch (Exception e) {
            logger.error("Setup failed for scenario: {}. Error: {}", scenario.getName(), e.getMessage(), e);
            throw new RuntimeException("Setup failed for scenario: " + scenario.getName(), e);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                logger.warn("Scenario failed: {}. Capturing screenshot...", scenario.getName());
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot for failed scenario");
            }
        } catch (Exception e) {
            logger.error("Teardown failed for scenario: {}. Error: {}", scenario.getName(), e.getMessage(), e);
        } finally {
            DriverManager.quitDriver();
            logger.info("Closed WebDriver for scenario: {}", scenario.getName());
        }
    }

    @AfterSuite
    public void generateAllureReport() {
        try {
            AllureReportGenerator.generateReport();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
