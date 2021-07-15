package model;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Patient {
	private String name;
	private int age;
	private String id;
	private boolean sex;
	private String phoneNumber;
	private String emergencyContact;
	private String emergencyPhoneNumber;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean getSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getEmergencyPhoneNumber() {
		return emergencyPhoneNumber;
	}
	public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
		this.emergencyPhoneNumber = emergencyPhoneNumber;
	}

	public Patient(String name, int age, String id, boolean sex, String emergencyContact, String emergencyPhoneNumber) {
		this.name = name;
		this.age = age;
		this.id = id;
		this.phoneNumber = "";
		this.sex = sex;
		this.emergencyContact = emergencyContact;
		this.emergencyPhoneNumber = emergencyPhoneNumber;
	}
	public Patient(String name, int age, String id, boolean sex, String phoneNumber, String emergencyContact,
			String emergencyPhoneNumber) {
		this.name = name;
		this.age = age;
		this.id = id;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
		this.emergencyContact = emergencyContact;
		this.emergencyPhoneNumber = emergencyPhoneNumber;
	}
	public String getByName(String s) {
		if(s.equals("姓名")) return this.name;
		else if(s.equals("联系电话")) return this.phoneNumber;
		else if(s.equals("身份证号码")) return this.id; 
		else if(s.equals("紧急联系人")) return this.emergencyContact;
		else if(s.equals("紧急联系电话")) return this.emergencyPhoneNumber;
		return "";
	}
	@Override
	public String toString() {
		return name;
	}
	
	public void copy(Patient newValue) {
		name = newValue.getName();
		age = newValue.getAge();
		id = newValue.getId();
		sex = newValue.getSex();
		phoneNumber = newValue.getPhoneNumber();
		emergencyContact = newValue.getEmergencyContact();
		emergencyPhoneNumber = newValue.getEmergencyPhoneNumber();
	}
}
