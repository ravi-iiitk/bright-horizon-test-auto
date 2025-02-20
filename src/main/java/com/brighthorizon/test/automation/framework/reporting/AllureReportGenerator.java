package com.brighthorizon.test.automation.framework.reporting;

import com.brighthorizon.test.automation.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllureReportGenerator {
    private static final Logger logger = LogManager.getLogger(AllureReportGenerator.class);
    public static void generateReport() throws IOException, InterruptedException {
        String projectName = ConfigReader.getGlobal("project.name");
        String applicationName = ConfigReader.getGlobal("application.name");
        String buildName = ConfigReader.getGlobal("build.name");
        String executionTimestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        String resultsDir = String.format("allure-results/%s-%s-%s-%s",
                projectName, applicationName, buildName, executionTimestamp);

        String reportDir = String.format("allure-reports/%s-%s-%s-%s",
                projectName, applicationName, buildName, executionTimestamp);

        // Generate Allure report command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "allure", "generate", resultsDir, "-o", reportDir, "--clean"
        );
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        process.waitFor();

        logger.info("Allure report generated at: " + reportDir);
    }
}
