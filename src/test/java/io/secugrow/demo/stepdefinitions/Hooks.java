package io.secugrow.demo.stepdefinitions;

import io.secugrow.demo.utils.TestDataContainer;
import io.secugrow.demo.utils.TestDataContainer.Keys;
import io.secugrow.demo.webdriversession.WebDriverSession;
import io.secugrow.demo.webdriversession.WebDriverSessionStore;
import io.secugrow.demo.webdriversession.webdriverfactory.DriverType;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.assertj.core.api.SoftAssertions;


import java.io.File;
import java.util.ArrayList;

public class Hooks {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TestDataContainer testDataContainer;
    
    private final boolean skipA11Y = System.getProperty("skipA11y", "true").equalsIgnoreCase("true");
    

    public Hooks(TestDataContainer testDataContainer) {
        this.testDataContainer = testDataContainer;
    }

    @BeforeStep
    public void beforeStep() {
        testDataContainer.incrementStepIndex();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        int debuglength = 80;
        String fillchar = "#";

        testDataContainer.setTestDataString(Keys.BASEURL, System.getProperty(Keys.BASEURL.getKeyValue(), "baseUrl is not set, please add to your commandline '-DbaseUrl=yourvalue or add to your runConfiguration"));
        testDataContainer.setScenario(scenario);
        testDataContainer.setTestDataBrowserType(DriverType.valueOf(System.getProperty("browser", "no browser set").toUpperCase()));
        testDataContainer.setTestDataString("browser.version", System.getProperty("browser.version", "no version set"));
        testDataContainer.setTestDataBoolean(Keys.INITIALIZED, false);
        testDataContainer.setTestDataInt(Keys.STEP_INDEX, 0);
        
        testDataContainer.setTestDataBoolean(Keys.SKIP_A11Y, skipA11Y);
        testDataContainer.setTestDataSoftAssertion("softAssertion.object", new SoftAssertions());
        testDataContainer.setTestDataBoolean(Keys.SOFTASSERTIONS_ACTIVE, !skipA11Y);

        if (!skipA11Y) {
            testDataContainer.setTestDataList("a11y.description", new ArrayList<>());
        }
        

        // to check if it runs on Jenkins or local
        String jobname = System.getenv("JOB_NAME");

        //Do Database resets here

        if (jobname != null) {
            testDataContainer.setTestDataBoolean(Keys.LOCALRUN, false);
            logger.info(
                    StringUtils.leftPad("JENKINS INFOS: ", debuglength, fillchar) + "\n" +
                            String.format("BUILD_NUMBER: %s", System.getProperty("BUILD_NUMBER")) + "\n" +
                            StringUtils.leftPad("# BUILD_NUMBER:" + System.getenv("BUILD_NUMBER"), debuglength, fillchar) + "\n" +
                            StringUtils.leftPad("# JOB_NAME: " + System.getenv("JOB_NAME"), debuglength, fillchar) + "\n" +
                            StringUtils.leftPad("# JENKINS_URL: " + System.getenv("JENKINS_URL"), debuglength, fillchar) + "\n" +
                            StringUtils.leftPad("# WORKSPACE: " + System.getenv("WORKSPACE"), debuglength, fillchar) + "\n" +
                            StringUtils.leftPad("# NODE_NAME: " + System.getenv("NODE_NAME"), debuglength, fillchar) + "\n" +
                            StringUtils.leftPad(fillchar, debuglength, fillchar)
            );
        } else {
            testDataContainer.setTestDataBoolean(Keys.LOCALRUN, true);
        }

        logger.debug(StringUtils.leftPad(fillchar, debuglength, fillchar));
        logger.info("# executing Scenario: " + scenario.getName());
        logger.debug(StringUtils.leftPad(fillchar, debuglength, fillchar));
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        
        testDataContainer.getAndClearA11YDescriptions().forEach(issue ->
                scenario.attach(issue.getBytes(), "text/plain", "A11Y Issue in step #" + testDataContainer.getStepIndex())
        );
        
    }

    @After(order = 1000)
    public void afterScenario(Scenario scenario) {
        logger.info("AfterScenario");

        String testId = testDataContainer.getTestId();
        WebDriverSession webDriverSession = WebDriverSessionStore.getIfExists(testDataContainer.getCurrentSessionId());

        if (!scenario.isFailed()) {
            WebDriverSessionStore.closeAll();
            return;
        }

        if (webDriverSession != null && webDriverSession.getCurrentPage() != null) {
            try {
                TakesScreenshot takesScreenshot = (TakesScreenshot) webDriverSession.getWebDriver();
                scenario.attach(takesScreenshot.getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot");
                scenario.attach(webDriverSession.getWebDriver().getPageSource().getBytes(), "text/html", "Page Sourcecode");

                testDataContainer.getScreenshots().forEach(screenshot ->
                        scenario.attach(screenshot.getLeft(), "image/png", "Forced Screenshot " + screenshot.getRight())
                );

                WebDriver webDriver = webDriverSession.getWebDriver();
                if (testDataContainer.isLocalRun()) {
                    File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(screenshot, new File(System.getProperty("user.dir") + "/target/error_selenium_" + testId + "_" + testDataContainer.getCurrentSessionId() + ".png"));
                } else {
                    byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Screenshot");
                }
            } catch (Exception e) {
                logger.error("Error during afterScenario screenshot handling", e);
            } finally {
                WebDriverSessionStore.closeAll();
            }
        }
    }

    
    @After(order = 1100)
    public void softAssertAll() {
        if (testDataContainer.hasSoftAssertions() || !skipA11Y) {
            testDataContainer.getSoftAssertionObject().assertAll();
        }
    }
    
}
