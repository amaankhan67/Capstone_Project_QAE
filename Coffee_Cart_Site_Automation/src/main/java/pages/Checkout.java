package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utils.WaitUtils;

public class Checkout extends BasePage{
	
	@FindBy(id="name")
	private WebElement inputName;
	
	@FindBy(id="email")
	private WebElement inputEmail;
	
	@FindBy(id="submit-payment")
	private WebElement submitButton;
	
	@FindBy(xpath="//div[@class=\"snackbar success\"]")
	private WebElement successMessage;
	
	public Checkout() {
		super();
		PageFactory.initElements(driver, this);
		logger.info("Checkout Page Object created successfully.");	
	}
	
	public void fillCheckoutForm(String name, String email) {
		logger.info("Attempting to fill out the checkout form.");
		type(inputName,name,"Name");
		type(inputEmail,email,"Email");
		logger.info("Successfully filled out the form.");
	}
	
	public void clickSubmitButton() {
		click(submitButton,"Submit Button");
	}
	
	public boolean isSuccessMessageDisplayed() {
		try {
			WaitUtils.forVisibilityOf(successMessage);
			logger.info("Success Message Displayed!!");
			return true;
		} catch(Exception e) {
			logger.warn("Success Message is not Displayed");
			return false;
		}
	}
	
	public boolean isCheckoutPageDisplayed() {
		try {
			WaitUtils.forVisibilityOf(submitButton);
			logger.info("Checkout Page Displayed!!");
			return true;
		} catch(Exception e) {
			logger.warn("Checkout Page is not displayed");
			return false;
		}
	}

}
