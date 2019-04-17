package telran.accounting.api;

public class RoleData {
public String username;
public String role;
public RoleData() {
}
public RoleData(String username, String role) {
	super();
	this.username = username;
	this.role = role;
}
public String getUsername() {
	return username;
}
public String getRole() {
	return role;
}

}
