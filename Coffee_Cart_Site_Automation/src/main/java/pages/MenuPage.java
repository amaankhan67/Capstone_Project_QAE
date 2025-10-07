package pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MenuPage extends BasePage{
	
	@FindBy(className="cup-body")
	private List<WebElement> coffeeItems;

	@FindBy(xpath="//h4")
	private List<WebElement> coffeeItemsWithPrice;
	
	@FindBy(xpath="//small[contains(text(),'$')]")
	private List<WebElement> coffeePrices;
	
	@FindBy(xpath="//a[@aria-label=\"Cart page\"]")
	private WebElement cartIcon;
	
	public MenuPage() {
		super();
		PageFactory.initElements(driver, this);
		logger.info("MenuPage Object created Successfully!");
	}
	
	public void addItemToCart(String itemName) {

		logger.info("Attempting to add " + itemName + " to cart.");
		for(WebElement item : coffeeItems) {
			if(item.getAttribute("aria-label").equalsIgnoreCase(itemName)) {
				hoverAndClick(item,itemName);
				return;
			}
		}
		logger.error("Item not Found :" + itemName);
		throw new IllegalArgumentException("Item not found: " + itemName);
	}
	
	public String getCoffeePrice(String itemName) {
		
		logger.info("Attempting to get the price of " + itemName + ".");
		for(int i=0; i<coffeeItemsWithPrice.size(); i++) {
			WebElement item = coffeeItemsWithPrice.get(i);
			String coffeeName = item.getText().replaceAll("[^A-Za-z ]", "").toLowerCase();
			if(coffeeName.equals(itemName.toLowerCase())) {
				return item.getText().replaceAll("[A-Za-z\n ]", "");
			}
		}
		logger.error("Item not Found :" + itemName);
			throw new IllegalArgumentException("Item not found: " + itemName);
	}
	
	public void clickCartButton() {
		click(cartIcon, "Cart Icon");
	}
	

}
