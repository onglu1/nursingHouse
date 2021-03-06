package model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Patient implements Serializable{
	private String name;
	private int age;
	private String id;
	private boolean sex;
	private String phoneNumber;
	private String emergencyContact;
	private String emergencyPhoneNumber;
	private double score = 0.0;
	
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
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
		if(s.equals("??????")) return this.name;
		else if(s.equals("????????????")) return this.phoneNumber;
		else if(s.equals("???????????????")) return this.id; 
		else if(s.equals("???????????????")) return this.emergencyContact;
		else if(s.equals("??????????????????")) return this.emergencyPhoneNumber;
		return "";
	}
	public Patient() {
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
