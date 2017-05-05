package store;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Fängström
 **/
public class Store {
	private List<Item> showInventory;
	private String getItem;
	private ArrayList<Item> Cart;
	private Boolean addToCart;
	private Boolean addToInventory;
	private String returnFromCart;
	private String subtractFromInventory;
	private Boolean deleteFromInventory;
	private Boolean insertToInventory;
	private List<Category> showCategories;
	private String checkoutCart;
	private Boolean dumpCart;

	public List<Item> showInventory() {
		return showInventory;

	}

	public String getItem(String artNr) {
		return getItem;
	}

	public Boolean addToCart(String artNr, ArrayList<Item> Cart) {
		return addToCart;
	}

	public Boolean addToInventory(String artNr, ArrayList<Item> Cart) {
		return addToInventory;
	}

	public Boolean insertToInventory(Item item) {
		return insertToInventory;
	}

	public List<Category> showCategories() {
		return showCategories;
	}

	public String returnFromCart(String artNr, ArrayList<Item> Cart) {
		return returnFromCart;
	}

	public String checkoutCart() {
		return checkoutCart;
	}

	public Boolean deleteFromInventory(String artNr, ArrayList<Item> Cart) {
		return deleteFromInventory;
	}

	public Boolean dumpCart() {
	
		return dumpCart;
	

	}

	public String subtractFromInventory(String artNr, ArrayList<Item> Cart) {
		return subtractFromInventory;

	}

}
