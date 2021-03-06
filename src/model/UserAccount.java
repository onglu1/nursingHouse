package model;

import java.io.Serializable;
import java.time.LocalDate;

import javafx.util.converter.LocalDateStringConverter;

public class UserAccount implements Comparable<UserAccount>{
	private String userName = "";
	private String name = "";
	private String password = "";
	private String titile = "";
	private LocalDate birth = LocalDate.now();
	private String expertise = "";
	private String idNumber = "";
	private String phoneNumber = "";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
	public LocalDate getBirth() {
		return birth;
	}
	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}
	public String getExpertise() {
		return expertise;
	}
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	//	private 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserAccount(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	@Override
	public int compareTo(UserAccount o) {
		// TODO Auto-generated method stub
		return this.getUserName().compareTo(o.getUserName());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	
	public UserAccount() {
	}
	//	public UserAccount(String userName, String name, String password, String titile, LocalDate birth, String expertise,
//			String idNumber, String phoneNumber) {
//		this.userName = userName;
//		this.name = name;
//		this.password = password;
//		this.titile = titile;
//		this.birth = birth;
//		this.expertise = expertise;
//		this.idNumber = idNumber;
//		this.phoneNumber = phoneNumber;
//	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccount other = (UserAccount) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	public String getByChinese(String chinese) {
		if(chinese.equals("?????????")) return userName;
		if(chinese.equals("??????")) return name;
		if(chinese.equals("??????")) return expertise;
		if(chinese.equals("?????????")) return idNumber;
		return "";
	}
	@Override
	public String toString() {
		return "UserAccount [userName=" + userName + ", name=" + name + ", password=" + password + ", titile=" + titile
				+ ", birth=" + birth + ", expertise=" + expertise + ", idNumber=" + idNumber + ", phoneNumber="
				+ phoneNumber + "]";
	}
	
}
