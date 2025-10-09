package tests;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import managers.DriverManager;
import pages.CartPage;
import pages.MenuPage;
import utils.ConfigReader;
import utils.ExcelReader;
import utils.LoggerUtils;

public class MenuTests {
	
	private MenuPage menuPage;
	private LoggerUtils logger;
	private WebDriver driver;
	
	@BeforeMethod
	public void setup() {
		driver = DriverManager.getDriver();
		menuPage = new MenuPage();
		logger = new LoggerUtils(MenuTests.class);
		
		String url = ConfigReader.getProperty("base_url");
		driver.get(url);
	}
	
	@Test(description = "Verify that coffee item can be added to cart")
	public void verifyItemCanBeAddedToCart() {
		String itemName = "Espresso";
		menuPage.addItemToCart(itemName);
		menuPage.clickCartButton();
		
		CartPage cartPage = new CartPage();
		boolean isItemPresent = cartPage.isItemPresent(itemName);
		Assert.assertTrue(isItemPresent,"Failed to verify that " + itemName + " was added to the cart");
        logger.info("Test Passed: " + itemName + " was successfully added and verified in the cart");
	}
	
	@Test(description = "Verify adding same item twice increases item quantity")
	public void verifyAddingSameItemsIncreaseQuantity() {
		String itemName="Espresso Con Panna";
		menuPage.addItemToCart(itemName);
		menuPage.addItemToCart(itemName);
		menuPage.clickCartButton();
		
		CartPage cartPage=new CartPage();
		cartPage.increaseItemQuantity(itemName);
		String cartTotal=cartPage.getTotalCartPrice();
		
		Assert.assertEquals(cartTotal,"Total: $42.00","The total price for " + itemName + " is incorrect after adding it thrice.");
        logger.info("Test Passed: Adding " + itemName + " thrice correctly updated the quantity and total price.");
		
	}
	
	@Test(description = "Verify that correct price is displayed for coffee item",
			dataProvider = "dataForCorrectPrice")
	public void verifyCorrectPriceDisplayed(String itemName, String ExpectedPrice) {
		String actualPrice = menuPage.getCoffeePrice(itemName);
		
		Assert.assertEquals(actualPrice, ExpectedPrice, "The Price of " + itemName + " is incorrectly displayed");
        logger.info("Test Passed: The price of " + itemName + " is correct.");
	}
	
	@DataProvider(name = "dataForCorrectPrice")
	public Object[][] getDataForCorrectPriceTest(){
		return ExcelReader.getTestData("dataForCorrectPrice");
	}
	
	@Test(description = "Verify correct prices of all coffee items")
	public void verifyAllItemsPrices() {
		
		//Fetching All Coffee Data from Excel File
		Map<String,String> allCoffeeDetail = ExcelReader.getAllCoffeeItemsWithPrices("CoffeeItems");
		for(String coffee : allCoffeeDetail.keySet()) {
			String coffeeName = coffee;
			String coffeePrice = allCoffeeDetail.get(coffee);
			String actualPrice=menuPage.getCoffeePrice(coffeeName);
			Assert.assertEquals(coffeePrice, actualPrice, "The price for " + coffeeName + " is incorrect.");
			logger.info("Verified price for " + coffeeName + " is " + actualPrice);
		}
		logger.info("Test Passed: All coffee item prices are correct");
	}
	
	
	@Test(description = "Verify that invalid item cannot be added to cart", 
			expectedExceptions = IllegalArgumentException.class)
	public void verifyInvalidItemsCannotBeAdded() {
		String invalidItemName = "Invalid Coffee";
		menuPage.addItemToCart(invalidItemName);
	}
	
	@Test(description = "Verify that cart icon navigates to cart page")
	public void verifyCartIconNavigation() {
		menuPage.clickCartButton();
		CartPage cartPage = new CartPage();
		Assert.assertTrue(cartPage.isCartEmpty(), "The Cart Page did not load successfully after clicking the cart icon");
        logger.info("Test Passed: The cart icon successfully navigates to the cart page.");
	}
	
	@AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        logger.info("WebDriver has been closed.");
    }

}
