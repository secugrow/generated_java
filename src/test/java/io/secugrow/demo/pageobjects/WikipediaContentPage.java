package io.secugrow.demo.pageobjects;

import io.secugrow.demo.webdriversession.WebDriverSession;
import org.openqa.selenium.By;

public class WikipediaContentPage extends MainPage{

    public WikipediaContentPage(WebDriverSession session) {
        super(session);
    }

    public String getHeader() {
        return getWebDriver().findElement(By.id("firstHeading")).getText();
    }
}
