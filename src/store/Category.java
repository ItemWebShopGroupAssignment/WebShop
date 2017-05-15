/**
 * 
 */
package store;

/**
 * @author Samuel Häggström **/
public class Category {
    private String categoryName;
    private String contents;
    
    public Category(String categoryName, String contents) {
        this.categoryName = categoryName;
        this.contents = contents;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getContents() {
        return contents;
    }
    
    public void setContents(String contents) {
        this.contents = contents;
    }
}
