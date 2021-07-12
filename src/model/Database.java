package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

public class Database{
	private ArrayList<UserAccount> users = new ArrayList<UserAccount>();
	private ArrayList<UserAccount> admins = new ArrayList<UserAccount>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Database() {
		users.add(new UserAccount("user1", "user1"));
		users.add(new UserAccount("user2", "user2"));
		admins.add(new UserAccount("admin1", "admin1"));
		admins.add(new UserAccount("admin2", "admin2"));
		patients.add(new Patient("张三", 18, true, "张三的爹", "123456"));
		patients.add(new Patient("李四", 18, true, "123456789", "李四的娘", "123456"));
	}
	
	public ArrayList<Patient> getPatients() {
		return patients;
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
