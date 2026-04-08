package io.secugrow.demo.stepdefinitions;

import io.secugrow.demo.pageobjects.WikipediaContentPage;
import io.secugrow.demo.pageobjects.WikipediaStartPage;
import io.secugrow.demo.utils.TestDataContainer;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

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
            assertThat(searchbar.isEnabled()).isTrue();
        });

        When("the Selenium page is opened", () -> {
            getPage(WikipediaStartPage.class).searchFor("Selenium");
        });

        Then("the header should be {string}", (String expected_header) -> assertThat(expected_header).isEqualTo(getPage(WikipediaContentPage.class).getHeader()));

    }
}
