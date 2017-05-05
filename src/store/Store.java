package store;

import java.sql.SQLException;
import java.util.List;
import database.MySqlHandler;

/**
 * @author Robert Fängström
 **/
public class Store {

	private MySqlHandler storeHandler;

	public List<Item> showInventory() throws SQLException {
		return storeHandler.getItems();

	}

	public Item getItem(String artNr) throws SQLException {
		return storeHandler.getItem(artNr);
	}

	public boolean addToCart(String artNr, int cartId, int count) throws SQLException {
		return storeHandler.addToCart(cartId, artNr, count);
	}

	public boolean addToInventory(String artNr, int count) throws SQLException {
		return storeHandler.addToInventory(artNr, count);
	}

	public boolean insertToInventory(Item item) throws SQLException {
		return storeHandler.insertItemToInventory(item);
	}

	public List<String> showCategories() throws SQLException {
		return storeHandler.showCategories();
	}

	public boolean returnFromCart(String artNr, int cartId, int count) throws SQLException {
		return storeHandler.returnFromCart(cartId, artNr, count);
	}

	public List<String> checkOutCart(int cartId) throws SQLException {
		return storeHandler.checkOutCart(cartId);
	}

	public boolean deleteFromInventory(String artNr, int cartId) throws SQLException {
		return storeHandler.deleteFromInventory(artNr);
	}

	public boolean dumpCart(int cartId) throws SQLException {

		return storeHandler.dumpCart(cartId);

	}

	public boolean subtractFromInventory(String artNr, int count) throws SQLException {
		return storeHandler.subtractFromInventory(artNr, count);

	}

	public Store() {
		storeHandler = new MySqlHandler();

	}

}
