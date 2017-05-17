/**
 * 
 */
package store;

/**
 * @author Samuel Häggström **/
public class Category {
    private String categoryName;
    private String contents;
    private String oldCategoryName;
    
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

    public String getOldCategoryName() {
        return oldCategoryName;
    }

    public void setOldCategoryName(String oldCategoryName) {
        this.oldCategoryName = oldCategoryName;
    }
}
