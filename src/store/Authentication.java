/**
 * 
 */
package store;

/**
 * @author Gustaf Peter Hultgren **/
public class Authentication {
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Authentication(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
