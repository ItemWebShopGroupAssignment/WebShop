package database;

import java.sql.Connection;
import java.sql.DriverManager;
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
        
        String sql = "SELECT * FROM item";
        
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
                    rs.getInt("stock_balance"));
            
            itemList.add(item);
        }
        
        return itemList;
    }
    
}
