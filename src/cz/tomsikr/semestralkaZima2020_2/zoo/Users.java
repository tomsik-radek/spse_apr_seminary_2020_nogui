package cz.tomsikr.semestralkaZima2020_2.zoo;

public class Users {
	int id;
	String name;
	String password;
	String permission;
	String phoneNumber;
	String age;
	public Users(int id, String name, String password, String permission, String age, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.permission = permission;
		this.phoneNumber = phoneNumber;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getID() {
		return id;
	}
}
