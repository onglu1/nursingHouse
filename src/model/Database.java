package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

public class Database{
	public ArrayList<UserAccount> users = new ArrayList<UserAccount>();
	public ArrayList<UserAccount> admins = new ArrayList<UserAccount>();
	private Database() {
		users.add(new UserAccount("user1", "user1"));
		users.add(new UserAccount("user2", "user2"));
		admins.add(new UserAccount("admin1", "admin1"));
		admins.add(new UserAccount("admin2", "admin2"));
	}
	
	public ArrayList<UserAccount> getUsers() {
		return users;
	}
	public ArrayList<UserAccount> getAdmins() {
		return admins;
	}
	private static Database singleton;
	public static Database getInstance() {
		if(singleton == null) {
			singleton = new Database();
		}
		return singleton;
	}
}
