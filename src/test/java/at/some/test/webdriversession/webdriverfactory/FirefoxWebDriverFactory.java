package at.some.test.webdriversession.webdriverfactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxWebDriverFactory extends WebDriverFactory {

    WebDriver webDriver;

        public WebDriver createDriver() {

        WebDriverManager.firefoxdriver().driverVersion(super.getWebDriverVersion()).setup();
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);

        options.merge(caps);

        this.webDriver = new FirefoxDriver(options);


        return webDriver;

    }
}
