package store;

import java.io.InputStream;
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

	public boolean addToCart(String artNr, long cartId, int count) throws SQLException, ClassNotFoundException {
		return storeHandler.addToCart(cartId, artNr, count);
	}

	public boolean addToInventory(String artNr, int count) throws SQLException, ClassNotFoundException{
		return storeHandler.addToInventory(artNr, count);
	}

	public boolean insertToInventory(String category, String itemName, String artNr, float price,
            String description, InputStream image, int stockBalance, String storageFormat
            ) throws SQLException, ClassNotFoundException {
		return storeHandler.insertItemToInventory(category,
                itemName,
                artNr,
                price,
                description,
                image,
                stockBalance,
                storageFormat);
	}

	public List<String> showCategories() throws SQLException, ClassNotFoundException {
		return storeHandler.showCategories();
	}

	public boolean returnFromCart(String artNr, long cartId, int count) throws SQLException, ClassNotFoundException {
		return storeHandler.returnFromCart(cartId, artNr, count);
	}

	public List<String> checkOutCart(long cartId) throws SQLException, ClassNotFoundException {
		return storeHandler.checkOutCart(cartId);
	}

	public boolean deleteFromInventory(String artNr, long cartId) throws SQLException, ClassNotFoundException {
		return storeHandler.deleteFromInventory(artNr);
	}

	public boolean dumpCart(long cartId) throws SQLException, ClassNotFoundException {

		return storeHandler.dumpCart(cartId);

	}

	public boolean subtractFromInventory(String artNr, int count) throws SQLException, ClassNotFoundException {
		return storeHandler.subtractFromInventory(artNr, count);

	}
	
	public List<Item> getCartItems(long cartId) throws SQLException, ClassNotFoundException {
		return storeHandler.getCartItems(cartId);
	}
	
	public long getCart() throws ClassNotFoundException, SQLException {
		return storeHandler.getCart();
	}

	public Store() {
		storeHandler = new MySqlHandler();

	}

}
