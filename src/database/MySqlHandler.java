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

import store.Item;

/**
 * @author Samuel Häggström **/
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
                ResultSet rs = stmt.executeQuery(sql);
                ) {
            
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
        System.out.println(sql);
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ResultSet rs = stmt.executeQuery();
                ) {
        
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
    public boolean addToCart(int cartId, String artNr, int count) throws SQLException, ClassNotFoundException {
        String getItemSql = "CALL 'get_cart_item' ("+cartId+","+artNr+")";
        String subtractSql = "UPDATE items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "CALL 'insert_into_cart' (?,?,?,?,?,?,?,?,?)";
        boolean result = false;
        Item item = getItem(artNr);
        
        try (
                Connection cn = getConnection();
                CallableStatement getItemStmt = cn.prepareCall(getItemSql);
                PreparedStatement subtractStmt = cn.prepareStatement(subtractSql);
                CallableStatement addStmt = cn.prepareCall(addSql);
                ResultSet rs = getItemStmt.executeQuery();
                ) {
            
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
        
            if (rs.next()) {
                int subtractResult = subtractStmt.executeUpdate();
                int addResult = addStmt.executeUpdate();
        
                result = (subtractResult >= 1 && addResult >= 1);
            }
        }
        return result;
    }
    
    //Increase the stock_balance value for one Item and returns true if it was added successfully
    public boolean addToInventory(String artNr, int count) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        int result;
        
        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);
                ) {
            
            stmt.setInt(1, count);
            stmt.setString(2, artNr);
        
            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Returns one item from the cart_items table and returns it to the items table. 
    //Returns true if it was returned successfully
    public boolean returnFromCart(int cartId, String artNr, int count) throws SQLException, ClassNotFoundException {
        String getItemSql = "CALL 'get_cart_item' ("+cartId+","+artNr+")";
        String subtractSql = "UPDATE cart_items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        boolean result = false;
        
        try (
                Connection cn = getConnection();
                CallableStatement getItemStmt = cn.prepareCall(getItemSql);
                PreparedStatement subtractStmt = cn.prepareStatement(subtractSql);
                PreparedStatement addStmt = cn.prepareStatement(addSql);
                ResultSet rs = getItemStmt.executeQuery();
                ) {
            
            subtractStmt.setInt(1, count);
            subtractStmt.setString(2, artNr);
        
            addStmt.setInt(1, count);
            addStmt.setString(2, artNr);
        
            if (rs.next()) {
                int subtractResult = subtractStmt.executeUpdate();
                int addResult = addStmt.executeUpdate();
        
                result = (subtractResult >= 1 && addResult >= 1);
            }
        }
        return result;
    }
    
    //Decrease the stock_balance value of one item and returns if it was decreased successfully
    public boolean subtractFromInventory(String artNr, int count) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        int result;
        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);
                ) {
            
            stmt.setInt(1, count);
            stmt.setString(2, artNr);
        
            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Deletes one item from the items table and returns true if it was deleted successfully
    public boolean deleteFromInventory(String artNr) throws SQLException, ClassNotFoundException {
        String sql = "CALL 'remove_from_inventory'(?)";
        int result;
        
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ) {
        
        stmt.setString(1, artNr);
        
        result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Adds one item to the items table and returns true if it was added successfully
    public boolean insertItemToInventory(String category, String itemName, String artNr, float price,
            String description, InputStream image, int stockBalance, String storageFormat
            ) throws SQLException, ClassNotFoundException {
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
        int result;
        
        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);
                ) {
        
            stmt.setString(1, artNr);
            stmt.setString(2, itemName);
            stmt.setFloat(3, price);
            stmt.setString(4, description);
            stmt.setBlob(5, image);
            stmt.setInt(6, stockBalance);
            stmt.setString(7, storageFormat);
            stmt.setString(8, category);
        
            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Returns a list of categories from the database
    public List<String> showCategories() throws SQLException, ClassNotFoundException {
        List<String> result = new ArrayList<>();
        
        String sql = "SELECT * FROM categories";
        
        try (
                Connection cn = getConnection();
                PreparedStatement stmt = cn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                ) {
            
            while (rs.next()) {
                String category = rs.getString("category_name");
                category += " | "+rs.getString("contents");
                
                result.add(category);
            }
        }
        return result;
    }
    
    //Deletes the cart and returns a list of the orders made
    public List<String> checkOutCart(int cartId) throws SQLException, ClassNotFoundException {
        List<String> order = new ArrayList<>();
        
        String sql = "CALL 'remove_shopping_cart'";
        String infoSql = "SELECT * FROM cart_items WHERE cart_id = " + cartId;
        
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                PreparedStatement infoStmt = cn.prepareStatement(infoSql);
                ResultSet rs = infoStmt.executeQuery();
                ) {
        
            stmt.setInt(1, cartId);
            
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
        }
        return order;
    }
    
    //Returns all items from the cart to the items table
    public boolean dumpCart(int cartId) throws SQLException, ClassNotFoundException {
        String fromCartSql = "SELECT * FROM cart_items WHERE cart_id = " + cartId;
        String toItemsSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        String deleteCartSql = "CALL 'remove_shopping_cart'(?)";
        int result;
        
        try (
                Connection cn = getConnection();
                PreparedStatement fromCartStmt = cn.prepareStatement(fromCartSql);
                PreparedStatement toItemsStmt = cn.prepareStatement(toItemsSql);
                CallableStatement deleteCartStmt = cn.prepareCall(deleteCartSql);
                ResultSet rs = fromCartStmt.executeQuery();
                ) {
            
            deleteCartStmt.setInt(1, cartId);
        
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
        
            result = deleteCartStmt.executeUpdate();
            }
        return result >= 1;
    }
    
    //Add a new category to the categories table
    public boolean addToCategory(String categoryName, String content) throws SQLException, ClassNotFoundException {
        String sql = "CALL 'create_category'(?, ?)";
        int result;
        
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ) {
        
            stmt.setString(1, categoryName);
            stmt.setString(2, content);
        
            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
    //Returns a list of items from the cart_items table
    public List<Item> getCartItems(int cartId) throws SQLException, ClassNotFoundException {
        List<Item> cartItems = new ArrayList<>();
        
        String sql = "CALL get_cart_items("+ cartId + ")";
        
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ResultSet rs = stmt.executeQuery();
                ) {
        
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
    
    //Removes a category from the categories table
    public boolean removeCategory(String category) throws SQLException, ClassNotFoundException {
        String sql = "CALL 'remove_category'(?)";
        int result;
        
        try (
                Connection cn = getConnection();
                CallableStatement stmt = cn.prepareCall(sql);
                ) {
        
            stmt.setString(1, category);
        
            result = stmt.executeUpdate();
        }
        return result >= 1;
    }
    
}
