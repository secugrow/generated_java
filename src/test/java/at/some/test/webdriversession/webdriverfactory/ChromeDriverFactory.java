package at.some.test.webdriversession.webdriverfactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory extends WebDriverFactory {

    WebDriver webDriver;


    public WebDriver createDriver() {

        WebDriverManager.chromedriver().driverVersion(super.getWebDriverVersion()).setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false);
        options.merge(caps);
        this.webDriver = new ChromeDriver(options);


        return webDriver;

    }
}
