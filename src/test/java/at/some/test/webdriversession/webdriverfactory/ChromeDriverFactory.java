package at.some.test.webdriversession.webdriverfactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory extends WebDriverFactory {

    public WebDriver createDriver() {
        WebDriverManager.chromedriver().driverVersion(super.getWebDriverVersion()).setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false);
        return new ChromeDriver(options.merge(caps));
    }
}
