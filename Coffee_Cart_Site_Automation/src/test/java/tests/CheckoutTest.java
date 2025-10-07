package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import managers.DriverManager;
import pages.CartPage;
import pages.Checkout;
import pages.MenuPage;
import utils.ConfigReader;
import utils.LoggerUtils;

public class CheckoutTest {
	
	private MenuPage menuPage;
    private CartPage cartPage;
    private Checkout checkout;
    private LoggerUtils logger;
    private WebDriver driver;

    // This is the Test Data to reach Checkout Page
    private static final String TEST_ITEM = "Flat White";
    private static final String VALID_NAME = "Amaan Khan";
    private static final String VALID_EMAIL = "amaankhan@example.com";
    
    @BeforeMethod
    public void setup() {
        this.logger = new LoggerUtils(CheckoutTest.class);
        driver = DriverManager.getDriver();
        String url = ConfigReader.getProperty("base_url");
        driver.get(url);

        menuPage = new MenuPage();
        cartPage = new CartPage();
        checkout = new Checkout(); 

        menuPage.addItemToCart(TEST_ITEM);
        menuPage.clickCartButton();
        cartPage.clickCheckout();
        logger.info("Test Environment Setup Complete");
    }
    
    @Test(description = "Verify that the checkout form is displayed and ready")
    public void verifyFormIsDisplayed() {
    	Assert.assertTrue(checkout.isCheckoutPageDisplayed(), 
            "The checkout form was not displayed after clicking the checkout button");
        logger.info("Test Passed: Checkout form is displayed as expected");
    }
    
    @Test(description = "Verify successful payment submission with valid Name and Email")
    public void verifySuccessfulPayment() {
        Assert.assertTrue(checkout.isCheckoutPageDisplayed(), "Checkout form not displayed.");
        
        checkout.fillCheckoutForm(VALID_NAME, VALID_EMAIL);
        checkout.clickSubmitButton();
        
        Assert.assertTrue(checkout.isSuccessMessageDisplayed(), 
            "Success message was not displayed after submitting a valid form");
        logger.info("Test Passed: Payment submitted successfully");
    }
    
    @Test(description = "Verify payment failure when Name field is empty")
    public void verifyPaymentFailsWithEmptyName() {
        checkout.fillCheckoutForm("", VALID_EMAIL);
        checkout.clickSubmitButton();
        
        Assert.assertFalse(checkout.isSuccessMessageDisplayed(), 
            "Success message was displayed despite the Name field being empty");
        logger.info("Test Passed: Payment correctly prevented submission with empty name");
    }
    
    @Test(description = "Verify payment failure with invalid Email format")
    public void verifyPaymentFailsWithInvalidEmail() {
        String invalidEmail = "invalid-email-no-at-sign.com";
        
        checkout.fillCheckoutForm(VALID_NAME, invalidEmail);
        checkout.clickSubmitButton();
        
        Assert.assertFalse(checkout.isSuccessMessageDisplayed(), 
            "Success message was displayed despite the Email field being invalid");
        logger.info("Test Passed: Payment correctly prevented submission with invalid email");
    }
    
    @Test(description = "Verify payment failure when Email field is empty")
    public void verifyPaymentFailsWithEmptyEmail() {
        checkout.fillCheckoutForm(VALID_NAME, "");
        checkout.clickSubmitButton();
        
        Assert.assertFalse(checkout.isSuccessMessageDisplayed(), 
            "Success message was displayed despite the Email field being empty");
        logger.info("Test Passed: Payment correctly prevented submission with empty email");
    }
    
    @Test(description = "Verify payment failure when both Name and Email fields are empty")
    public void verifyPaymentFailsWithBothEmpty() {
        checkout.fillCheckoutForm("", "");
        checkout.clickSubmitButton();
        
        Assert.assertFalse(checkout.isSuccessMessageDisplayed(), 
            "Success message was displayed despite both fields being empty");
        logger.info("Test passed: Payment correctly prevented submission with both fields empty");
    }
    
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        logger.info("WebDriver has been closed.");
    }
}
