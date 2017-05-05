package database;

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

    private static Connection cn;

    public static final void connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        cn = DriverManager.getConnection(DB_URL + "/" + DB_DATABASE, DB_USERNAME, DB_PASSWORD);
    }

    public static final Connection getConnection() throws SQLException, ClassNotFoundException {
        if (cn == null) {
            connect();
        }

        return cn;
    }

    public static final boolean close() throws SQLException {
        if (cn != null) {
            cn.close();
            return true;
        } else {
            return false;
        }
    }
    
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
    
    public Item getItem(String artNr) throws SQLException {
        Item item = null;
        
        String sql = "SELECT * FROM items WHERE art_number = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
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
    
    public boolean addToCart(int cartId, String artNr, int count) throws SQLException {
        String getItemSql = "CALL 'get_cart_item' (?,?)";
        String subtractSql = "UPDATE items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "CALL 'insert_into_cart' (?,?,?,?,?,?,?,?,?)";
        boolean result = false;
        Item item = getItem(artNr);
        
        PreparedStatement getItemStmt = cn.prepareStatement(getItemSql);
        PreparedStatement subtractStmt = cn.prepareStatement(subtractSql);
        PreparedStatement addStmt = cn.prepareStatement(addSql);
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
    
    public boolean addToInventory(String artNr, int count) throws SQLException {
        String sql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        stmt.setInt(1, count);
        stmt.setString(2, artNr);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    public boolean returnFromCart(int cartId, String artNr, int count) throws SQLException {
        String getItemSql = "CALL 'get_cart_item' (?,?)";
        String subtractSql = "UPDATE cart_items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        String addSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        boolean result = false;
        
        PreparedStatement getItemStmt = cn.prepareStatement(getItemSql);
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
    
    public boolean subtractFromInventory(String artNr, int count) throws SQLException {
        String sql = "UPDATE items SET stock_balance = (stock_balance - ?) WHERE art_number = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        stmt.setInt(1, count);
        stmt.setString(2, artNr);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
    public boolean deleteFromInventory(String artNr) throws SQLException {
        String sql = "DELETE FROM items WHERE art_number = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
        stmt.closeOnCompletion();
        
        stmt.setString(1, artNr);
        
        int result = stmt.executeUpdate();
        
        return result >= 1;
    }
    
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
    
    public List<String> checkOutCart(int cartId) throws SQLException {
        List<String> order = new ArrayList<>();
        
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        String infoSql = "SELECT * FROM cart_items WHERE cart_id = ?";
        
        PreparedStatement stmt = cn.prepareStatement(sql);
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
                orderedItem += "Format: " + rs.getString("storage_formats") + "\n";
                orderedItem += "------------- \n";
                order.add(orderedItem);
            }
        }
        
        return order;
    }
    
    public boolean dumpCart(int cartId) throws SQLException {
        String fromCartSql = "SELECT * FROM cart_items WHERE cart_id = ?";
        String toItemsSql = "UPDATE items SET stock_balance = (stock_balance + ?) WHERE art_number = ?";
        String deleteCartSql = "DELETE FROM cart_items WHERE cart_id = ?";
        
        PreparedStatement fromCartStmt = cn.prepareStatement(fromCartSql);
        PreparedStatement toItemsStmt = cn.prepareStatement(toItemsSql);
        PreparedStatement deleteCartStmt = cn.prepareStatement(deleteCartSql);
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
    
}
