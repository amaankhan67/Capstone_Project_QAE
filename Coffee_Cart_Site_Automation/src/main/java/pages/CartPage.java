package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage extends BasePage {
	
	@FindBy(className="pay")
	private WebElement checkoutButton;
	
	@FindBy(className="list")
	private WebElement cartList;	//To check if Cart is empty or not
	
	@FindBy(className="list-item")
	public List<WebElement> itemList;
	
	@FindBy(css = "[aria-label='Cart page']")
    private WebElement cartIcon;
	
	public CartPage() {
		super();
		PageFactory.initElements(driver, this);
		logger.info("CartPage Object created successfully.");	
	}
	
	public boolean isCartEmpty() {
		logger.info("Checking if cart is empty.");
		String cartText = getText(cartList, "Cart List");
		if(cartText.contains("No coffee")) {
			logger.warn("Cart is empty.");
			return true;
		}
		logger.info("Cart is not empty.");
		return false;
		
	}
	
	private WebElement getWebElementforItem(String itemName) {
		if(itemName == null || itemName.isEmpty() || isCartEmpty())
			return null;
		
		// Wait for the cart to update
		itemList.removeIf(s -> s == null || s.getText().trim().isEmpty());	//itemList cleanup

		for(int i=0; i<itemList.size(); i++) {
			String coffeeName = getCoffeeNameFromWebElement(itemList.get(i));
			if(coffeeName.contains(itemName)) {
				return itemList.get(i);
			}
		}

		logger.warn("Item '" + itemName + "' not found in cart.");
		return null;
	}
	
	public boolean isItemPresent(String itemName) {
		logger.info("Verifying if item '" + itemName + "' is present in the cart.");
		WebElement item = getWebElementforItem(itemName);
		
		if(item != null) {
			logger.info("Item '" + itemName + "' is present in the cart.");
			return true;
		}
		logger.warn("Item '" + itemName + "' not found in cart.");
		return false;
	}
	
	private String getCoffeeNameFromWebElement(WebElement item) {
		String[] splitString = item.getText().split("\n");
		return splitString[0].trim();
	}
	
	public String getItemTotalPrice(String itemName) {
		logger.info("Attempting to get total price for " + itemName);

		WebElement item = getWebElementforItem(itemName);
		if(item != null) {
			String price = getTotalPriceFromWebElement(item);
			logger.info("The total price for '" + itemName + "' is: " + price);
			return price;
		}
		
		logger.warn("Item '" + itemName + "' not found in cart.");
		return "$0.00";
	}
	
	private String getTotalPriceFromWebElement(WebElement item) {
		String[] splitString = item.getText().split("\n");
		return splitString[4].trim();
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
	
	public int getItemQuantity(String itemName) {
		logger.info("Attempting to get quantity for " + itemName);
		
		WebElement item = getWebElementforItem(itemName);
		if(item != null) {
			String str = item.getText().split("\n")[1];
			String quantityStr = str.substring(str.indexOf("x") + 1).trim();
			int quantity = Integer.parseInt(quantityStr);
			logger.info("The quantity for '" + itemName + "' is: " + quantity);
			return quantity;
		}
		
		logger.warn("Item '" + itemName + "' not found in cart.");
		return 0;
	}
	
	public void increaseItemQuantity(String itemName) {
		logger.info("Attempting to increase quantity for " + itemName);
		if(!isItemPresent(itemName)) {
			logger.error("Cannot increase quantity. Item '" + itemName + "' not found in cart.");
			return;
		}
		
		WebElement parentItem = getWebElementforItem(itemName);
		WebElement plusButton = parentItem.findElement(By.cssSelector(String.format("button[aria-label*='Add one %s']", itemName)));
		
		click(plusButton,itemName);
		logger.info("Successfully increased quantity for '" + itemName);
	}
	
	public void decreaseItemQuantity(String itemName) {
		logger.info("Attempting to decrease quantity for " + itemName);
		if(!isItemPresent(itemName)) {
			logger.error("Cannot decrease quantity. Item '" + itemName + "' not found in cart.");
			return;
		}
		
		WebElement parentItem = getWebElementforItem(itemName);
		WebElement minusButton = parentItem.findElement(By.cssSelector(String.format("button[aria-label*='Remove one %s']", itemName)));
		
		click(minusButton,itemName);
		logger.info("Successfully decrease quantity for '" + itemName);
	}
	
	public void openCart() {
        try {
            click(cartIcon,"Cart Icon");
        } catch (Exception e) {
        	logger.error("Failed to open cart", e);
        }
    }
	
	public void clickCheckout() {
		click(checkoutButton, "Checkout Button");
	}

}
