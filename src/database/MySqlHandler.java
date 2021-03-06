package database;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import store.Authentication;
import store.Category;
import store.Item;

/**
 * @author Samuel Häggström
 **/
public class MySqlHandler {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/item_web_shop?useSSL=false";

    private static final String DB_USERNAME = "root";

    private static final String DB_PASSWORD = "root";

    //Attempts to connect to the database
    public static final Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    //Returns a list of items from the items table
    public List<Item> getItems() throws SQLException, ClassNotFoundException {

        List<Item> itemList = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (
                Connection cn = getConnection();
                Statement stmt = cn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getString("category"),
                        rs.getString("item_name"),
                        rs.getString("art_number"),
                        rs.getFloat("price"),
                        rs.getString("description"),
                        rs.getBlob("image"),
                        rs.getInt("stock_balance"),
                        rs.getString("storage_formats"));

                itemList.add(item);
            }
        }
        return itemList;
    }

    //Returns a Item object
    public Item getItem(String artNr) throws SQLException, ClassNotFoundException {
        Item item = null;

        String sql = "CALL get_inventory_item('" + artNr + "')";
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ResultSet rs = stmt.executeQuery();) {

            if (rs.next()) {
                item = new Item(
                        rs.getString("category"),
                        rs.getString("item_name"),
                        rs.getString("art_number"),
                        rs.getFloat("price"),
                        rs.getString("description"),
                        rs.getBlob("image"),
                        rs.getInt("stock_balance"),
                        rs.getString("storage_formats"));
            }
        }
        return item;
    }

    //Returns a Item object
    private Item getItem(String artNr, Connection con) throws SQLException, ClassNotFoundException {
        Item item = null;

        String sql = "CALL get_inventory_item('" + artNr + "')";
        try (
                CallableStatement stmt = con.prepareCall(sql);
                ResultSet rs = stmt.executeQuery();) {

            if (rs.next()) {
                item = new Item(
                        rs.getString("category"),
                        rs.getString("item_name"),
                        rs.getString("art_number"),
                        rs.getFloat("price"),
                        rs.getString("description"),
                        rs.getBlob("image"),
                        rs.getInt("stock_balance"),
                        rs.getString("storage_formats"));
            }
        }
        return item;
    }

    //Adds a item to the shopping cart and returns true if it was added successfully
    public boolean addToCart(long cartId, String artNr, int count) throws SQLException, ClassNotFoundException {

        boolean result = false;
        String sql = "SELECT * FROM shopping_carts WHERE cart_id = " + cartId;

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        if (count <= 0)
            return false;

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            // Get item from inventory.
            Item item = getItem(artNr, con);

            if (item == null || item.getStockBalance() < count)
                return false;

            while (rs.next()) {
                // Update the inventory item.
                result = subtractFromInventory(artNr, count, con);

                if (!result)
                    return false;

                result = updateCartItem(cartId, artNr, count, con);
                if (!result) {
                    result = insertIntoCart(cartId, artNr, count, con);
                    if (!result)
                        return false;
                }
            }

        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();

        }
        return result;
    }
    
    //Adds a item to the cart and returns true if it succeeded
    private boolean insertIntoCart(long cartId, String artNr, int count, Connection con)
            throws ClassNotFoundException, SQLException {
        boolean result = false;
        Item item = getItem(artNr);
        String addSql = "CALL insert_into_cart ('" + artNr + "','" + item.getItemName() + "'," + item.getPrice()
                + ",'" + item.getDescription() + "'," + null + "," + count + ",'" + item.getStorageFormat()
                + "','" + item.getCategory() + "'," + cartId + ")";

        try (
                PreparedStatement stmt = con.prepareStatement(addSql);) {
            int addResult = stmt.executeUpdate();
            result = (addResult >= 1);
        }

        return result;
    }
    
    //Updates the stockBalance value of the given item
    private boolean updateCartItem(long cartId, String artNr, int count, Connection con)
            throws ClassNotFoundException, SQLException {
        boolean result = false;
        String updateSql = "UPDATE cart_items SET stock_balance = (stock_balance + " + count + ")"
                + " WHERE art_number = '" + artNr + "' AND cart_id = " + cartId;

        try (
                PreparedStatement stmt = con.prepareStatement(updateSql);) {
            int updateResult = stmt.executeUpdate();
            result = (updateResult >= 1);
        }

        return result;
    }

    //Increase the stock_balance value for one Item and returns true if it was added successfully
    public boolean addToInventory(String artNr, int count) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        int result;

        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);) {

            stmt.setInt(1, count);
            stmt.setString(2, artNr);

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }

    //Returns one item from the cart_items table and returns it to the items table. 
    //Returns true if it was returned successfully
    public boolean returnFromCart(long cartId, String artNr, int count) throws SQLException, ClassNotFoundException {
        String getItemSql = "CALL get_cart_item (" + cartId + ",'" + artNr + "')";
        String subtractSql = "UPDATE cart_items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        String deleteSql = "DELETE FROM cart_items WHERE art_number LIKE ? AND cart_id LIKE ?";
        boolean result = false;

        try (
                Connection cn = getConnection();
                CallableStatement getItemStmt = cn.prepareCall(getItemSql);
                PreparedStatement subtractStmt = cn.prepareStatement(subtractSql);
                PreparedStatement addStmt = cn.prepareStatement(addSql);
                PreparedStatement deleteStmt = cn.prepareStatement(deleteSql);
                ResultSet rs = getItemStmt.executeQuery();) {

            subtractStmt.setInt(1, count);
            subtractStmt.setString(2, artNr);

            addStmt.setInt(1, count);
            addStmt.setString(2, artNr);

            deleteStmt.setString(1, artNr);
            deleteStmt.setLong(2, cartId);

            if (rs.next()) {
                int subtractResult = subtractStmt.executeUpdate();
                int addResult = addStmt.executeUpdate();

                if (rs.getInt("stock_balance") == 0 || rs.getInt("stock_balance") <= count) {
                    int deleteResult = deleteStmt.executeUpdate();

                    return result = (deleteResult >= 1);
                }

                result = (subtractResult >= 1 && addResult >= 1);
            }

        }
        return result;
    }

    //Decrease the stock_balance value of one item and returns if it was decreased successfully
    public boolean subtractFromInventory(String artNr, int count) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE items SET stock_balance = (stock_balance - " + count + ") WHERE art_number = '" + artNr
                + "'";
        int result;

        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);) {

            //            stmt.setInt(1, count);
            //            stmt.setString(2, artNr);

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }

    //Decrease the stock_balance value of one item and returns if it was decreased successfully
    private boolean subtractFromInventory(String artNr, int count, Connection con)
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE items SET stock_balance = (stock_balance - " + count + ") WHERE art_number = '" + artNr
                + "'";
        int result;

        try (PreparedStatement stmt = con.prepareStatement(sql);) {

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }

    //Deletes one item from the items table and returns true if it was deleted successfully
    public boolean deleteFromInventory(String artNr) throws SQLException, ClassNotFoundException {
        String sql = "CALL remove_from_inventory('" + artNr + "')";
        int result;
        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);) {

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }

    //Adds one item to the items table and returns true if it was added successfully
    public boolean insertItemToInventory(String category, String itemName, String artNr, float price,
            String description, InputStream image, int stockBalance, String storageFormat)
            throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO items ("
                + "art_number,"
                + " item_name,"
                + " price,"
                + " description,"
                + " image,"
                + " stock_balance,"
                + " storage_formats,"
                + " category)"
                + "VALUES ('" + artNr + "','" + itemName + "'," + price + ",'" + description + "',?," + stockBalance +
                ",'" + storageFormat + "','" + category + "')";
        int result;

        System.out.println(sql);

        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);) {

            //            stmt.setString(1, artNr);
            //            stmt.setString(2, itemName);
            //            stmt.setFloat(3, price);
            //            stmt.setString(4, description);
            stmt.setBlob(1, image);
            //            stmt.setInt(6, stockBalance);
            //            stmt.setString(7, storageFormat);
            //            stmt.setString(8, category);

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Edits the given item and returns true if it succeeded
    public boolean editItem(String currentArtNr, String category, String itemName, String newArtNr, float price,
            String description, InputStream image, int stockBalance, String storageFormat)
            throws ClassNotFoundException, SQLException {
        String sql = "UPDATE items SET art_number = '" + newArtNr + "', item_name = '" + itemName + "', price = "
                + price + ","
                + " description = '" + description + "', image = ?, stock_balance = " + stockBalance +
                ", storage_formats = '" + storageFormat + "'," + " category = '" + category +
                "' WHERE art_number = '" + currentArtNr + "'";
        int result;

        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);) {

            stmt.setBlob(1, image);

            result = stmt.executeUpdate();

        }
        return result >= 1;
    }

    //Returns a list of categories from the database
    public List<Category> showCategories() throws SQLException, ClassNotFoundException {
        List<Category> result = new ArrayList<>();

        String sql = "SELECT * FROM categories";

        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getString("category_name"),
                        rs.getString("contents"));
                result.add(category);
            }
        }
        return result;
    }

    //Deletes the cart and adds the content of the cart to the orders table
    public boolean checkOutCart(long cartId, float cost, String adress, String receiver)
            throws SQLException, ClassNotFoundException {
        int result = 0;

        String sql = "CALL remove_shopping_cart(" + cartId + ");";
        String infoSql = "SELECT * FROM cart_items WHERE cart_id = " + cartId + ";";
        String addOrderSql = "INSERT INTO orders VALUES(" + null + ", " + 29 + ", " + cost + ", " + 0 + ", '" + adress
                + "', '" + receiver + "')";
        String getOrderId = "SELECT MAX(order_id) as order_id FROM orders";

        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                PreparedStatement infoStmt = cn.prepareStatement(infoSql);
                ResultSet rs = infoStmt.executeQuery();
                PreparedStatement addOrderStmt = cn.prepareStatement(addOrderSql);
                PreparedStatement getOrderStmt = cn.prepareStatement(getOrderId);) {

            int removeResult = stmt.executeUpdate();
            int addResult = addOrderStmt.executeUpdate();

            if (removeResult >= 1 && addResult >= 1) {
                ResultSet getOrderRs = getOrderStmt.executeQuery();
                while (getOrderRs.next()) {
                    while (rs.next()) {
                        String addOrderedItemsSql = "INSERT INTO ordered_items VALUES('" +
                                rs.getString("art_number") + "', '" +
                                rs.getString("item_name") + "', " +
                                rs.getFloat("price") + ", " +
                                rs.getInt("stock_balance") + ", '" +
                                rs.getString("storage_format") + "', " +
                                getOrderRs.getInt("order_id")+")";
                        System.out.println(addOrderedItemsSql);
                        try (
                                PreparedStatement addOrderedItemsStmt = cn.prepareStatement(addOrderedItemsSql);) {
                            result = addOrderedItemsStmt.executeUpdate();
                        }
                    }
                }
            }
        }
        return result >= 1;
    }

    //Returns all items from the cart to the items table
    public boolean dumpCart(long cartId) throws SQLException, ClassNotFoundException {
        String fromCartSql = "SELECT * FROM cart_items WHERE cart_id = " + cartId;
        String toItemsSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        String deleteCartSql = "CALL remove_shopping_cart(?)";
        int result;

        try (
                Connection cn = getConnection();
                PreparedStatement fromCartStmt = cn.prepareStatement(fromCartSql);
                PreparedStatement toItemsStmt = cn.prepareStatement(toItemsSql);
                CallableStatement deleteCartStmt = cn.prepareCall(deleteCartSql);
                ResultSet rs = fromCartStmt.executeQuery();) {

            deleteCartStmt.setLong(1, cartId);

            while (rs.next()) {
                String artNr = rs.getString("art_number");
                int stockBalance = rs.getInt("stock_balance");

                toItemsStmt.setInt(1, stockBalance);
                toItemsStmt.setString(2, artNr);

                int insertResult = toItemsStmt.executeUpdate();

                if (insertResult == 0) {
                    return false;
                }
            }

            result = deleteCartStmt.executeUpdate();
        }
        return result >= 1;
    }

    //Add a new category to the categories table
    public boolean addToCategory(String categoryName, String content) throws SQLException, ClassNotFoundException {
        String sql = "CALL create_category('" + categoryName + "', '" + content + "')";
        int result;
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);) {

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Returns a Item from the cart table
    public Item getCartItem(long cartId, String artNr) throws ClassNotFoundException, SQLException {
        Item item = null;

        String sql = "CALL get_cart_item (" + cartId + ",'" + artNr + "')";

        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ResultSet rs = stmt.executeQuery();) {

            if (rs.next()) {
                item = new Item(
                        rs.getString("category"),
                        rs.getString("item_name"),
                        rs.getString("art_number"),
                        rs.getFloat("price"),
                        rs.getString("description"),
                        rs.getBlob("image"),
                        rs.getInt("stock_balance"),
                        rs.getString("storage_formats"));
            }
        }
        return item;
    }

    //Returns a list of items from the cart_items table
    public List<Item> getCartItems(long cartId) throws SQLException, ClassNotFoundException {
        List<Item> cartItems = new ArrayList<>();

        String sql = "CALL get_cart_items(" + cartId + ")";

        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ResultSet rs = stmt.executeQuery();) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getString("category"),
                        rs.getString("item_name"),
                        rs.getString("art_number"),
                        rs.getFloat("price"),
                        rs.getString("description"),
                        rs.getBlob("image"),
                        rs.getInt("stock_balance"),
                        rs.getString("storage_format"));

                cartItems.add(item);
            }
        }
        return cartItems;
    }
    
    //Edits the given category and returns true if it succeeded
    public boolean editCategory(String category, String contents, String oldCategory)
            throws ClassNotFoundException, SQLException {
        String sql = "UPDATE categories SET category_name = '" + category + "', contents = '" + contents +
                "' WHERE category_name = '" + oldCategory + "'";
        int result;

        try (
                Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);) {
            result = stmt.executeUpdate();
        }
        return result >= 1;
    }

    //Removes a category from the categories table
    public boolean removeCategory(String category) throws SQLException, ClassNotFoundException {
        String sql = "CALL 'remove_category'(?)";
        int result;

        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);) {

            stmt.setString(1, category);

            result = stmt.executeUpdate();
        }
        return result >= 1;
    }

    //Returns the new cart id, if the sql statments fails it will return 0.
    public long getCart() throws SQLException, ClassNotFoundException {
        String addSql = "INSERT INTO shopping_carts VALUES(" + null + ")";
        String getCartSql = "SELECT MAX(cart_id) AS new_cart_id FROM shopping_carts";
        ResultSet rs = null;

        int result = 0;

        try (
                Connection con = getConnection();
                PreparedStatement addStmt = con.prepareStatement(addSql);
                PreparedStatement getCartStmt = con.prepareStatement(getCartSql);) {

            int temp = addStmt.executeUpdate();
            boolean addResult = (temp >= 1);
            rs = getCartStmt.executeQuery();

            while (rs.next()) {
                if (addResult == true) {
                    result = rs.getInt("new_cart_id");
                }
            }
        } finally {
            if (rs != null)
                rs.close();
        }

        return result;
    }

    /**
     * Get information about a user.
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     **/
    public Authentication getUser(String username) throws ClassNotFoundException, SQLException {
        Authentication user = null;

        String sql = "SELECT * FROM users "
                + "WHERE username LIKE ? "
                + "COLLATE utf8_swedish_ci;";

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            con = getConnection();
            statement = con.prepareStatement(sql);
            statement.setString(1, username);

            results = statement.executeQuery();

            // Did we get a user?
            if (results.next()) {
                String name = results.getString("username");
                String pwd = results.getString("pwd");

                user = new Authentication(name, pwd);
            }

        } finally {
            if (results != null)
                results.close();
            if (statement != null)
                statement.close();
            if (con != null)
                con.close();
        }

        return user;
    }

}
