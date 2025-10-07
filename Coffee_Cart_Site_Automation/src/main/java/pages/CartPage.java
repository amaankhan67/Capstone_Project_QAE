package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utils.WaitUtils;

public class CartPage extends BasePage {
	
	@FindBy(className="pay")
	private WebElement checkoutButton;
	
	@FindBy(xpath="//li[@class=\"list-header\"]/following-sibling::li/div[1]")
	private List<WebElement> itemList;
	
	@FindBy(xpath="//li[@class=\"list-header\"]/following-sibling::li/div[3]")
	private List<WebElement> itemTotalPrices;
	
	@FindBy(xpath="//div[@class=\"modal\"]/following::button[text()=\"+\"]")
	private List<WebElement> plusButtons;
	
	@FindBy(xpath="//div[@class=\"modal\"]/following::button[text()=\"-\"]")
	private List<WebElement> minusButtons;
	
	public CartPage() {
		super();
		PageFactory.initElements(driver, this);
		logger.info("CartPage Object created successfully.");	
	}
	
	public boolean isCartEmpty() {
		try {
			WaitUtils.forPresenceOfElementLocated(By.className("pay"));
			return false;
		} catch(Exception e) {
			//If an Exception occurs, we assume cart is empty
			return true;
		}
	}
	
	public int returnIndexIfItemPresent(String itemName) {
		if(isCartEmpty())
			return -1;

		logger.info("Verifying if item '" + itemName + "' is present in the cart.");
		for(int i=0; i<itemList.size(); i++) {
			if(itemList.get(i).getText().replaceAll("[^A-Za-z ]", "").equalsIgnoreCase(itemName)) {
				return i;
			}
		}

		logger.warn("Item '" + itemName + "' not found in cart.");
		return -1;
	}
	
	public boolean isItemPresent(String itemName) {
		logger.info("Verifying if item '" + itemName + "' is present in the cart.");
		if(itemName == null || itemName.isEmpty() || isCartEmpty())
			return false;

		WaitUtils.forVisibilityOfAllElements(itemList);
		for(int i=0; i<itemList.size(); i++) {
			if(itemList.get(i).getText().replaceAll("[^A-Za-z ]", "").equalsIgnoreCase(itemName)) {
				return true;
			}
		}

		logger.warn("Item '" + itemName + "' not found in cart.");
		return false;
	}
	
	public String getItemTotalPrice(String itemName) {
		logger.info("Attempting to get total price for " + itemName);
		int index = returnIndexIfItemPresent(itemName);
		if(index<0) {
			logger.warn("Could not fetch total item price for " + itemName);
			return null;
		}

		return itemTotalPrices.get(index).getText().trim();
	}
	
	public String getTotalCartPrice() {
		logger.info("Attempting to get cart total");
		
		if(!isCartEmpty()) {
		String price = getText(checkoutButton,"Checkout Button");
		logger.info("The total cart price is: " + price);
		return price;
		}
		
		logger.warn("Could not fetch cart total");
		return "$0.00";
	}
	
	public void increaseItemQuantity(String itemName) {
		logger.info("Attempting to increase quantity for " + itemName);
		int index = returnIndexIfItemPresent(itemName);
		if(index<0) {
			logger.warn("Could not increase quantity for " + itemName);
			return;
		}
		click(plusButtons.get(index),itemName);
		logger.info("Successfully increased quantity for '" + itemName);
	}
	
	public void decreaseItemQuantity(String itemName) {
		logger.info("Attempting to decrease quantity for " + itemName);
		int index = returnIndexIfItemPresent(itemName);
		if(index<0) {
			logger.warn("Could not decrease quantity for " + itemName);
			return;
		}
		click(minusButtons.get(index),itemName);
		logger.info("Successfully decrease quantity for '" + itemName);
	}
	
	public void clickCheckout() {
		click(checkoutButton, "Checkout Button");
	}

}
