package pages;

import java.time.LocalDateTime;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import managers.DriverManager;
import utils.ConfigReader;
import utils.ExcelReader;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		
//		WebDriver driver = DriverManager.getDriver();
//		driver.get(ConfigReader.getProperty("base_url"));
//		MenuPage test = new MenuPage();
//		test.addItemToCart("espresso");
//		test.addItemToCart("Cafe Breve");
//		test.addItemToCart("espresso");
//		test.addItemToCart("Cappuccino");
//		test.addItemToCart("Cafe Breve");
//		test.clickCartButton();
		
		Object[][] data = ExcelReader.getTestData("TestData");
		System.out.println(data.toString());
		Map<String, String> coffee = ExcelReader.getAllCoffeeItemsWithPrices("CoffeeItems");
		System.out.println(coffee.toString());
		
	}

}
