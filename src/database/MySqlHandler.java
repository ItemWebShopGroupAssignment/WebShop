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
    
    public boolean addToCart(String artNr, int count) {
        return true;
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
    
    public boolean returnFromCart(String artNr, int count) {
        return true;
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
    
    public boolean checkOutCart() {
        return true;
    }
    
    public boolean dumpCart() {
        return true;
    }
    
}
