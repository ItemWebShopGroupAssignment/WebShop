package store;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Fängström
 **/
public class Store {
	private List<Item> showInventory;
	private String getItem;
	private ArrayList Cart;
	private String addToCart;
	private String addToInventory;
	private String returnFromCart;
	private String subtractFromInventory;
	private String deleteFromInventory;
	private String insertToInventory;
	private String showCategories;
	private String checkoutCart;
	private String dumpCart;
	
	 /*
	  returnFromCart(artNr, Cart);
	  subtractFromCart(artNr, Cart);
	  deleteFromInventory(artNr, Cart);
	  showCategories();
	  checkoutCart();
	  dumpCart(); */

	public List<Item> showInventory() {
		return showInventory;

	}

	public String getItem(String artNr) {
		return getItem;
	}

	public String addToCart(String artNr, ArrayList Cart) {
		return addToCart(artNr, Cart);
	}

	public String addToInventory(String artNr, ArrayList Cart) {
		return addToInventory(artNr, Cart);
	}

	public String insertToInventory(Item item) {
		return insertToInventory(item);
	}

}
