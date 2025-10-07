package managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import org.testng.ITestContext;

public class DriverManager {
	
	private static WebDriver driver;
	
	public static WebDriver getDriver() {
		String browser = "chrome"; // default browser
		if(driver == null) {
			try {
				ITestContext context = org.testng.Reporter.getCurrentTestResult().getTestContext();
				browser = context.getCurrentXmlTest().getParameter("browser");
				if (browser == null || browser.isEmpty()) {
					browser = ConfigReader.getProperty("browser");
				}
			} catch (Exception e) {
			}
			driver = initializeDriver(browser);
		}
		return driver;
	}
	
	public static WebDriver initializeDriver(String browser) {
		
		 	switch(browser) {
			
			case "chrome" 	: 	//WebDriverManager.chromedriver().setup();			//The Webdriver is not downloading chromedriver automatically hence the below line
								WebDriverManager.chromedriver().cachePath("webdrivers").setup();
								ChromeOptions chromeOptions = new ChromeOptions();
								chromeOptions.addArguments("--disable-notifications");
								chromeOptions.addArguments("--start-maximized");
								driver = new ChromeDriver(chromeOptions);
								break;
								
			case "firefox" 	: 	WebDriverManager.firefoxdriver().setup();
								FirefoxOptions firefoxOptions = new FirefoxOptions();
								firefoxOptions.addArguments("--disable-notifications");
								firefoxOptions.addArguments("--start-maximized");
								driver = new FirefoxDriver(firefoxOptions);
								break;
								
			case "edge" 	: 	WebDriverManager.edgedriver().setup();
								EdgeOptions edgeOptions = new EdgeOptions();
								edgeOptions.addArguments("--disable-notifications");
								edgeOptions.addArguments("--start-maximized");
								driver = new EdgeDriver(edgeOptions);
								break;
								
			default			:	throw new IllegalArgumentException("Unsupported Browser : " + browser);
			}
		return driver;
	}
	
	public static void quitDriver() {
		if(driver!=null)
			driver.quit();
		driver = null;
	}

}
