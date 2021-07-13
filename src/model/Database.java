package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

public class Database{
	private ArrayList<UserAccount> users = new ArrayList<UserAccount>();
	private ArrayList<UserAccount> admins = new ArrayList<UserAccount>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Patient tmppatient = null;
	public boolean idChangable = true;
	public String idNumber = null;
	private Database() {
		users.add(new UserAccount("user1", "user1"));
		users.add(new UserAccount("user2", "user2"));
		admins.add(new UserAccount("admin1", "admin1"));
		admins.add(new UserAccount("admin2", "admin2"));
		patients.add(new Patient("张三", 18, "331081201111217299", true, "张三的爹", "15305865401"));
		patients.add(new Patient("李四", 18, "33108120111121729X", true, "15305865401", "李四的娘", "15305865401"));
	}
	
	public Patient getTmppatient() {
		return tmppatient;
	}

	public void setTmppatient(Patient tmppatient) {
		this.tmppatient = tmppatient;
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
