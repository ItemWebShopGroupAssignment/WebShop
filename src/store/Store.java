package store;

import java.sql.SQLException;
import java.util.List;
import database.MySqlHandler;

/**
 * @author Robert F�ngstr�m
 **/
public class Store {

	private MySqlHandler storeHandler;

	public List<Item> showInventory() throws SQLException, ClassNotFoundException {
		return storeHandler.getItems();

	}

	public Item getItem(String artNr) throws SQLException, ClassNotFoundException {
		return storeHandler.getItem(artNr);
	}

	public boolean addToCart(String artNr, int cartId, int count) throws SQLException, ClassNotFoundException {
		return storeHandler.addToCart(cartId, artNr, count);
	}

	public boolean addToInventory(String artNr, int count) throws SQLException, ClassNotFoundException{
		return storeHandler.addToInventory(artNr, count);
	}

	public boolean insertToInventory(Item item) throws SQLException, ClassNotFoundException {
		return storeHandler.insertItemToInventory(item);
	}

	public List<String> showCategories() throws SQLException, ClassNotFoundException {
		return storeHandler.showCategories();
	}

	public boolean returnFromCart(String artNr, int cartId, int count) throws SQLException, ClassNotFoundException {
		return storeHandler.returnFromCart(cartId, artNr, count);
	}

	public List<String> checkOutCart(int cartId) throws SQLException, ClassNotFoundException {
		return storeHandler.checkOutCart(cartId);
	}

	public boolean deleteFromInventory(String artNr, int cartId) throws SQLException, ClassNotFoundException {
		return storeHandler.deleteFromInventory(artNr);
	}

	public boolean dumpCart(int cartId) throws SQLException, ClassNotFoundException {

		return storeHandler.dumpCart(cartId);

	}

	public boolean subtractFromInventory(String artNr, int count) throws SQLException, ClassNotFoundException {
		return storeHandler.subtractFromInventory(artNr, count);

	}
	
	public List<Item> getCartItems(int cartId) throws SQLException, ClassNotFoundException {
		return storeHandler.getCartItems(cartId);
	}

	public Store() {
		storeHandler = new MySqlHandler();

	}

}
