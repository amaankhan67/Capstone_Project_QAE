package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import managers.DriverManager;
import pages.CartPage;
import pages.Checkout;
import pages.MenuPage;
import utils.ConfigReader;
import utils.ExcelReader;
import utils.LoggerUtils;

public class CartTests {
	
	private WebDriver driver;
	private MenuPage menuPage;
	private CartPage cartPage;
	LoggerUtils logger;
	
	@BeforeMethod
	public void setup() {
		driver = DriverManager.getDriver();
		menuPage = new MenuPage();
		cartPage = new CartPage();
		logger = new LoggerUtils(CartTests.class);
		
		String url = ConfigReader.getProperty("base_url");
		driver.get(url);
	}
	
	@Test(description = "Verify cart is empty on initial load")
	public void verifyInitialEmptyCart() {
		menuPage.clickCartButton();
		Assert.assertTrue(cartPage.isCartEmpty(),"The Cart is not empty on initial load");
		logger.info("Test Passed: The cart is empty as expected");
	}
	
	@Test(description = "Verify that item is present in cart after adding it",
			dataProvider = "dataForPresenceCheck")
	public void verifyItemPresenceInCart(String itemName,String itemPrice) {
		menuPage.addItemToCart(itemName);
		menuPage.clickCartButton();
		
		Assert.assertTrue(cartPage.isItemPresent(itemName),"The item " + itemName + " should be present in the cart.");
        logger.info("Test Passed: " + itemName + " is correctly present in the cart");
	}
	
	@DataProvider(name = "dataForPresenceCheck")
	public Object[][] getDataForPresenceTest(){
		return ExcelReader.getTestData("dataForPresenceCheck");
	}
	
	@Test(description = "Verify total amount for a single item in cart")
	public void verifySingleItemTotalPrice() {
		String itemName = "Espresso Con Panna";
		String expectedPrice = "$14.00";			
		
		menuPage.addItemToCart(itemName);
		menuPage.clickCartButton();
		String actualPrice = cartPage.getItemTotalPrice(itemName);
		
		Assert.assertEquals(actualPrice, expectedPrice, "The total price for " + itemName + " is incorrect");
        logger.info("Test Passed: The total item amount is correct");
	}
	
	@Test(description = "Verify that increasing quantity updates the tool price")
	public void verifyIncreasingItemQuantity() {
		String itemName = "Americano";
		String expectedPriceAfterIncrease = "$14.00";			//$7.00 * 2
		
		menuPage.addItemToCart(itemName);
		menuPage.clickCartButton();
		
		cartPage.increaseItemQuantity(itemName);
		String actualPrice = cartPage.getItemTotalPrice(itemName);
		
		Assert.assertEquals(actualPrice, expectedPriceAfterIncrease, "Increasing the quantity did not update the item's total price correctly.");
        logger.info("Test Passed: Increasing item quantity updated the price as expected.");
    
	}
	
    @Test(description = "Verify that decreasing item quantity updates the total price")
	public void verifyDecreasingItemQuantity() {
        String itemName = "Americano";
        String expectedPriceAfterDecrease = "$7.00"; // Start at 2 and then Decrease to 1
        
        menuPage.addItemToCart(itemName);
        menuPage.addItemToCart(itemName);
        menuPage.clickCartButton();
        
        cartPage.decreaseItemQuantity(itemName);
        String actualPrice = cartPage.getItemTotalPrice(itemName);
        
        Assert.assertEquals(actualPrice, expectedPriceAfterDecrease, "Decreasing the quantity did not update the item's total price correctly");
        logger.info("Test Passed: Decreasing item quantity updated the price as expected");
    }
    
    @Test(description = "Verify that an item is removed from the cart when quantity reaches zero")
    public void verifyItemIsRemovedWhenQuantityIsZero() {
        String itemName = "Espresso";
        
        menuPage.addItemToCart(itemName);
        menuPage.clickCartButton();
        
        cartPage.decreaseItemQuantity(itemName);
      
        Assert.assertFalse(cartPage.isItemPresent(itemName), "The item " + itemName + " should be removed from the cart");
        logger.info("Test Passed: Item was successfully removed from the cart");
    }
    
    @Test(description = "Verify that multiple different items are present in the cart")
    public void verifyMultipleItemsArePresent() {
        String item1 = "Espresso";
        String item2 = "Cappuccino";

        menuPage.addItemToCart(item1);
        menuPage.addItemToCart(item2);
        menuPage.clickCartButton();
        
        Assert.assertTrue(cartPage.isItemPresent(item1), "Item " + item1 + " is not present in the cart.");
        Assert.assertTrue(cartPage.isItemPresent(item2), "Item " + item2 + " is not present in the cart.");
        logger.info("Test Passed: Both items are correctly present in the cart");
    }
    
    @Test(description = "Verify that the total cart price is the sum of all individual item prices")
    public void verifyTotalCartPriceWithMultipleItems() {
        
        menuPage.addItemToCart("Espresso Macchiato");
        menuPage.addItemToCart("Mocha");
        String expectedTotal = "Total: $20.00";
        menuPage.clickCartButton();
        
        String actualTotal = cartPage.getTotalCartPrice();
        Assert.assertEquals(expectedTotal,actualTotal, "The total cart price is incorrect");
        logger.info("Test Passed: The total cart price is correct");
    }
    
    @Test(description = "Verify that clicking the checkout button navigates to the checkout page.")
    public void verifyCheckoutButtonNavigatesToCheckoutPage() {
        menuPage.addItemToCart("Cafe Latte");
        menuPage.clickCartButton();
        
        cartPage.clickCheckout();
        
        Checkout checkout = new Checkout();
        Assert.assertTrue(checkout.isCheckoutPageDisplayed(), "The checkout button did not navigate to the checkout page");
        logger.info("Test Passed: Checkout button successfully navigated to the checkout page");
    }
	
	@AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        logger.info("WebDriver has been closed.");
    }

}
