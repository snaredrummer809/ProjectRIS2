package Controllers;

public class ModelTable {
	String username,displayName,email,role;
	int ID;

	public ModelTable(int ID, String username, String displayName, String email, String role) {
		super();
		this.ID = ID;
		this.username = username;
		this.displayName = displayName;
		this.email = email;
		this.role = role;
	}

	public ModelTable(String username, String displayName, String email, String role, int iD) {
		super();
		this.username = username;
		this.displayName = displayName;
		this.email = email;
		this.role = role;
		this.ID = iD;
	}
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
