package at.some.test.pageobjects;

import at.some.test.webdriversession.WebDriverSession;
import org.openqa.selenium.By;

public class WikiPediaSearchresultPage extends MainPage{
    public WikiPediaSearchresultPage(WebDriverSession session) {
        super(session);
    }

    public WikipediaContentPage openFromResults(String partLink) {
        logger.info("::: some logmessage for testing purposes :::");
        getWebDriver().findElement(By.cssSelector("li.mw-search-result a[href='" + partLink + "']")).click();
        return new WikipediaContentPage(session);
    }
}
