package utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import managers.DriverManager;

public class WaitUtils {
	
    private static final int EXPLICIT_WAIT_SECONDS = Integer.parseInt(ConfigReader.getProperty("explicitwait"));
	private static WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(),Duration.ofSeconds(EXPLICIT_WAIT_SECONDS));
	
	public static WebElement forVisibilityOf (WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
	
	public static WebElement forElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
	
	public static boolean forTextToBePresentInElement(WebElement element, String expectedText) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, expectedText));
    }
	
	public static WebElement forPresenceOfElementLocated(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	public static WebElement forVisibilityOfElementLocated(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public static List<WebElement> forVisibilityOfAllElements(List<WebElement> elements) {
		return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}
}
