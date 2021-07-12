package model;

public class Patient {
	private String name;
	private int age;
	private boolean sex;
	private String phoneNumber;
	private String emergencyContact;
	private String emergencyPhoneNumber;
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
	public boolean isSex() {
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
	public Patient(String name, int age, boolean sex, String phoneNumber, String emergencyContact,
			String emergencyPhoneNumber) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
		this.emergencyContact = emergencyContact;
		this.emergencyPhoneNumber = emergencyPhoneNumber;
	}
	public Patient(String name, int age, boolean sex, String emergencyContact, String emergencyPhoneNumber) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.emergencyContact = emergencyContact;
		this.emergencyPhoneNumber = emergencyPhoneNumber;
	}
	
}
