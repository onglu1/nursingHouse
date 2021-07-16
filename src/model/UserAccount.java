package model;

import java.io.Serializable;
import java.time.LocalDate;

import javafx.util.converter.LocalDateStringConverter;

public class UserAccount implements Serializable, Comparable<UserAccount>{
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
		if(chinese.equals("登录名")) return userName;
		if(chinese.equals("姓名")) return name;
		if(chinese.equals("专长")) return expertise;
		if(chinese.equals("身份证")) return idNumber;
		return "";
	}
}
