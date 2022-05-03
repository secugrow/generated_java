package at.some.test.webdriversession.webdriverfactory;

import org.openqa.selenium.remote.DesiredCapabilities;

public abstract class WebDriverFactory {
    protected DesiredCapabilities caps = new DesiredCapabilities();
    String getWebDriverVersion() {
        return System.getProperty("driver.version");
    }
    protected String getBrowserVersion() {
        return System.getProperty("browser.version");
    }
}
