package io.secugrow.demo.stepdefinitions;

import io.secugrow.demo.pageobjects.WikipediaContentPage;
import io.secugrow.demo.pageobjects.WikipediaStartPage;
import io.secugrow.demo.utils.TestDataContainer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WikipediaSteps extends AbstractStepDefs {

    TestDataContainer testDataContainer;

    public WikipediaSteps(TestDataContainer testDc) {
        super(testDc);
        testDataContainer = testDc;

        Given("the start page is loaded", () -> {
            getCurrentWebDriver().navigate().to(testDataContainer.getBaseUrl());
            new WikipediaStartPage(getCurrentWebDriverSession());
        });

        Then("the searchbar is visible", () -> {
            WebElement searchbar = getPage(WikipediaStartPage.class).getSearchbar();
            assertTrue(searchbar.isEnabled());
        });

        When("the Selenium page is opened", () -> {
            getPage(WikipediaStartPage.class).searchFor("Selenium");
        });

        Then("the header should be {string}", (String expected_header) -> assertEquals(expected_header, getPage(WikipediaContentPage.class).getHeader()));

    }
}
