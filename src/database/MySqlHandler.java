package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import store.Item;

/**
 * @author Samuel Häggström **/
public class MySqlHandler {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306";

    private static final String DB_DATABASE = "item_web_shop";

    private static final String DB_USERNAME = "root";

    private static final String DB_PASSWORD = "root";
    
    //Database connection
    private static Connection cn;
    
    //Attempts to connect to the database
    public static final void connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        cn = DriverManager.getConnection(DB_URL + "/" + DB_DATABASE, DB_USERNAME, DB_PASSWORD);
    }
    
    //Gets the connection interface associated with the database
    public static final Connection getConnection() throws SQLException, ClassNotFoundException {
        if (cn == null) {
            connect();
        }

        return cn;
    }
    
    //Closes the connection to the database
    public static final boolean close() throws SQLException {
        if (cn != null) {
            cn.close();
            return true;
        } else {
            return false;
        }
    }
    
    //Returns a list of items from the items table
    public List<Item> getItems() throws SQLException {
        List<Item> itemList = new ArrayList<>();
        
        String sql = "SELECT * FROM items";
        
        Statement stmt = cn.createStatement();
        stmt.closeOnCompletion();
        
        ResultSet rs = stmt.executeQuery(sql);
        
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
        
        return itemList;
    }
    
    //Returns a Item object
    public Item getItem(String artNr) throws SQLException {
        Item item = null;
        
        String sql = "CALL 'get_inventory_item'(?)";
        
        CallableStatement stmt = cn.prepareCall(sql);
        stmt.closeOnCompletion();
        
        stmt.setString(1, artNr);
        
        ResultSet rs = stmt.executeQuery();
        
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
        
        return item;
    }
    
    //Adds a item to the shopping cart and returns true if it was added successfully
    public boolean addToCart(int cartId, String artNr, int count) throws SQLException {
        String getItemSql = "CALL 'get_cart_item' (?,?)";
        String subtractSql = "UPDATE items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "CALL 'insert_into_cart' (?,?,?,?,?,?,?,?,?)";
        boolean result = false;
        Item item = getItem(artNr);
        
        CallableStatement getItemStmt = cn.prepareCall(getItemSql);
        PreparedStatement subtractStmt = cn.prepareStatement(subtractSql);
        CallableStatement addStmt = cn.prepareCall(addSql);
        getItemStmt.closeOnCompletion();
        subtractStmt.closeOnCompletion();
        addStmt.closeOnCompletion();
        
        getItemStmt.setInt(1, cartId);
        getItemStmt.setString(2, artNr);
        
        subtractStmt.setInt(1, count);
        subtractStmt.setString(2, artNr);
        
        addStmt.setString(1, artNr);
        addStmt.setString(2, item.getItemName());
        addStmt.setFloat(3, item.getPrice());
        addStmt.setString(4, item.getDescription());
        addStmt.setBlob(5, item.getImage());
        addStmt.setInt(6, item.getStockBalance());
        addStmt.setString(7, item.getStorageFormat());
        addStmt.setString(8, item.getCategory());
        addStmt.setInt(9, cartId);
        
        ResultSet rs = getItemStmt.executeQuery();
        
        if (rs.next()) {
            int subtractResult = subtractStmt.executeUpdate();
            int addResult = addStmt.executeUpdate();
        
            result = (subtractResult >= 1 && addResult >= 1);
        }
        
        return result;
    }
    
    //Increase the stock_balance value for one Item and returns true if it was added successfully
    public boolean addToInventory(String artNr, int count) throws SQLException {
        String sql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        stmt.setInt(1, count);
        stmt.setString(2, artNr);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    //Returns one item from the cart_items table and returns it to the items table. 
    //Returns true if it was returned successfully
    public boolean returnFromCart(int cartId, String artNr, int count) throws SQLException {
        String getItemSql = "CALL 'get_cart_item' (?,?)";
        String subtractSql = "UPDATE cart_items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        boolean result = false;
        
        CallableStatement getItemStmt = cn.prepareCall(getItemSql);
        PreparedStatement subtractStmt = cn.prepareStatement(subtractSql);
        PreparedStatement addStmt = cn.prepareStatement(addSql);
        getItemStmt.closeOnCompletion();
        subtractStmt.closeOnCompletion();
        addStmt.closeOnCompletion();
        
        getItemStmt.setInt(1, cartId);
        getItemStmt.setString(2, artNr);
        
        subtractStmt.setInt(1, count);
        subtractStmt.setString(2, artNr);
        
        addStmt.setInt(1, count);
        addStmt.setString(2, artNr);
        
        ResultSet rs = getItemStmt.executeQuery();
        
        if (rs.next()) {
            int subtractResult = subtractStmt.executeUpdate();
            int addResult = addStmt.executeUpdate();
        
            result = (subtractResult >= 1 && addResult >= 1);
        }
        return result;
    }
    
    //Decrease the stock_balance value of one item and returns if it was decreased successfully
    public boolean subtractFromInventory(String artNr, int count) throws SQLException {
        String sql = "UPDATE items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        stmt.setInt(1, count);
        stmt.setString(2, artNr);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    //Deletes one item from the items table and returns true if it was deleted successfully
    public boolean deleteFromInventory(String artNr) throws SQLException {
        String sql = "CALL 'remove_from_inventory'(?)";
        
        CallableStatement stmt = cn.prepareCall(sql);
        stmt.closeOnCompletion();
        
        stmt.setString(1, artNr);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    //Adds one item to the items table and returns true if it was added successfully
    public boolean insertItemToInventory(Item item) throws SQLException {
        String sql = "INSERT INTO items ("
                + "art_number,"
                + " item_name,"
                + " price,"
                + " description,"
                + " image,"
                + " stock_balance,"
                + " storage_formats,"
                + " category)"
                + "VALUES (?,?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        stmt.setString(1, item.getArtNr());
        stmt.setString(2, item.getItemName());
        stmt.setFloat(3, item.getPrice());
        stmt.setString(4, item.getDescription());
        stmt.setBlob(5, item.getImage());
        stmt.setInt(6, item.getStockBalance());
        stmt.setString(7, item.getStorageFormat());
        stmt.setString(8, item.getCategory());
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    //Returns a list of categories from the database
    public List<String> showCategories() throws SQLException {
        List<String> result = new ArrayList<>();
        
        String sql = "SELECT category_name FROM categories";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String category = rs.getString("category_name");
            
            result.add(category);
        }
        
        return result;
    }
    
    //Deletes the cart and returns a list of the orders made
    public List<String> checkOutCart(int cartId) throws SQLException {
        List<String> order = new ArrayList<>();
        
        String sql = "CALL 'remove_shopping_cart'";
        String infoSql = "SELECT * FROM cart_items WHERE cart_id = ?";
        
        CallableStatement stmt = cn.prepareCall(sql);
        PreparedStatement infoStmt = cn.prepareStatement(infoSql);
        stmt.closeOnCompletion();
        infoStmt.closeOnCompletion();
        
        stmt.setInt(1, cartId);
        infoStmt.setInt(1, cartId);
        
        ResultSet rs = infoStmt.executeQuery();
        
        int result = stmt.executeUpdate();
        
        if (result >= 1) {
            while (rs.next()) {
                String orderedItem = "";
                orderedItem += "Article number: " + rs.getString("art_number") + "\n";
                orderedItem += "Name: " + rs.getString("item_name") + "\n";
                orderedItem += "Price: " + rs.getFloat("price") + "\n";
                orderedItem += "Format: " + rs.getString("storage_format") + "\n";
                orderedItem += "------------- \n";
                order.add(orderedItem);
            }
        }
        
        return order;
    }
    
    //Returns all items from the cart to the items table
    public boolean dumpCart(int cartId) throws SQLException {
        String fromCartSql = "SELECT * FROM cart_items WHERE cart_id = ?";
        String toItemsSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        String deleteCartSql = "CALL 'remove_shopping_cart'(?)";
        
        PreparedStatement fromCartStmt = cn.prepareStatement(fromCartSql);
        PreparedStatement toItemsStmt = cn.prepareStatement(toItemsSql);
        CallableStatement deleteCartStmt = cn.prepareCall(deleteCartSql);
        fromCartStmt.closeOnCompletion();
        toItemsStmt.closeOnCompletion();
        deleteCartStmt.closeOnCompletion();
        
        fromCartStmt.setInt(1, cartId);
        deleteCartStmt.setInt(1, cartId);
        
        ResultSet rs = fromCartStmt.executeQuery();
        
        while(rs.next()) {
            String artNr = rs.getString("art_number");
            int stockBalance = rs.getInt("stock_balance");
            
            toItemsStmt.setInt(1, stockBalance);
            toItemsStmt.setString(2, artNr);
            
            int insertResult = toItemsStmt.executeUpdate();
            
            if (insertResult == 0) {
                return false;
            }
        }
        
        int result = deleteCartStmt.executeUpdate();
        
        return result >= 1;
    }
    
    //Add a new category to the categories table
    public boolean addToCategory(String categoryName, String content) throws SQLException {
        String sql = "CALL 'create_category'(?, ?)";
        
        CallableStatement stmt = cn.prepareCall(sql);
        stmt.closeOnCompletion();
        
        stmt.setString(1, categoryName);
        stmt.setString(2, content);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    //Returns a list of items from the cart_items table
    public List<Item> getCartItems(int cartId) throws SQLException {
        List<Item> cartItems = new ArrayList<>();
        
        String sql = "CALL get_cart_items(?)";
        
        CallableStatement stmt = cn.prepareCall(sql);
        stmt.closeOnCompletion();
        
        stmt.setInt(1, cartId);
        
        ResultSet rs = stmt.executeQuery();
        
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
        return cartItems;
    }
    
    //Removes a category from the categories table
    public boolean removeCategory(String category) throws SQLException {
        String sql = "CALL 'remove_category'(?)";
        
        CallableStatement stmt = cn.prepareCall(sql);
        stmt.closeOnCompletion();
        
        stmt.setString(1, category);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
}
