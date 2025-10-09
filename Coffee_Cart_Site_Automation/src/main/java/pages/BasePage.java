package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import managers.DriverManager;
import utils.LoggerUtils;
import utils.WaitUtils;

public class BasePage {
	
	protected WebDriver driver;
	protected LoggerUtils logger;
	
	public BasePage() {
		this.driver = DriverManager.getDriver();
		this.logger = new LoggerUtils(getClass());
	}
	
	protected void click(WebElement element, String elementName) {
		try {
			logger.info("Attempting to click on " + elementName);
			WaitUtils.forElementToBeClickable(element).click();
			logger.info("Successfully clicked on " + elementName);
		} catch(Exception e) {
			logger.error("Failed to click on " + elementName, e);
			throw new RuntimeException("Could not click on " + elementName, e);
		}
	}
	
	protected void hoverAndClick(WebElement element, String elementName) {
		try {
			logger.info("Attempting to hover and click on " + elementName);
			Actions action = new Actions(driver);
			action.moveToElement(WaitUtils.forVisibilityOf(element)).click().perform();
			logger.info("Successfully hovered and clicked on " + elementName);
		} catch(Exception e) {
			logger.error("Failed to hover and click on " + elementName, e);
			throw new RuntimeException("Could not hover and click on " + elementName, e);
		}
	}
	
	protected void type(WebElement element, String text, String elementName) {
		try {
			logger.info("Attempting to type " + text + " into " + elementName);
			WebElement visibleElement = WaitUtils.forVisibilityOf(element);
			visibleElement.clear();
			visibleElement.sendKeys(text);
			logger.info("Successfully typed " + text + " into " + elementName);
		} catch(Exception e) {
			logger.error("Failed to type " + text + " into " + elementName, e);
			throw new RuntimeException("Could not type into " + elementName, e);
		}
	}
	
	protected String getText(WebElement element, String elementName) {
		try {
			logger.info("Attempting to get text from " + elementName);
			String text = WaitUtils.forVisibilityOf(element).getText();
			logger.info("Successfully retrieved text from " + elementName);
			return text;
		} catch(Exception e) {
			logger.error("Failed to get text from " + elementName, e);
			throw new RuntimeException("Could not get text from " + elementName, e);
		}
	}
	
	protected boolean isElementDisplayed(WebElement element, String elementName) {
	    try {
	        logger.info("Checking if " + elementName + " is displayed");
	        return WaitUtils.forVisibilityOf(element).isDisplayed();
	    } catch (Exception e) {
	        logger.warn(elementName + " is not displayed");
	        return false;
	    }
	}
	
	protected boolean isElementsDisplayed(List<WebElement> elements, String listName) {
	    try {
	        logger.info("Checking if " + listName + " are displayed");
	        return WaitUtils.forVisibilityOfAllElements(elements).isEmpty() == false;
	    } catch (Exception e) {
	        logger.warn(listName + " are not displayed");
	        return false;
	    }
	}
	
	protected WebElement find(By locator) {
		logger.info("Finding Element by Locator : " + locator);
		return WaitUtils.forPresenceOfElementLocated(locator);
	}

}
