/**
 * 
 */
package store;

import java.sql.Blob;

/**
 * @author Samuel Häggström **/
public class Item {
    
    private String category;
    private String itemName;
    private String artNr;
    private float price;
    private String description;
    private Blob image;
    private int stockBalance;
    
    public Item(String category, String itemName, String artNr, float price,
            String description, Blob image, int stockBalance) {
        this.category = category;
        this.itemName = itemName;
        this.artNr = artNr;
        this.price = price;
        this.description = description;
        this.image = image;
        this.stockBalance = stockBalance;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getArtNr() {
        return artNr;
    }
    
    public void setArtNr(String artNr) {
        this.artNr = artNr;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Blob getImage() {
        return image;
    }
    
    public void setImage(Blob image) {
        this.image = image;
    }
    
    public int getStockBalance() {
        return stockBalance;
    }
    
    public void setStockBalance(int stockBalance) {
        this.stockBalance = stockBalance;
    }

}
