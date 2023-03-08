package io.secugrow.demo.webdriversession.webdriverfactory;

public class AppiumDriverFactory extends WebDriverFactory{

    protected String getMobileDeviceId() {
        return System.getProperty("device.id","device.id is not set, set it with -Ddevice.id in your runConfig");
    }

    protected String getSeleniumGrid() {
        return System.getProperty("selenium.grid", "selenium.grid is not set set it with -Dselenium.grid in your runConfig!");
    }
}
