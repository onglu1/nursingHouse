package model;


import java.io.Serializable;
import java.time.LocalDate;

import building.Bed;

public class CheckInInfo implements Serializable{
	private Bed bed;
	private Patient patient;
	private LocalDate checkInTime;
	private LocalDate checkOutTime;
	private boolean isInBed;
	public Bed getBed() {
		return bed;
	}
	public void setBed(Bed bed) {
		this.bed = bed;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public LocalDate getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(LocalDate checkInTime) {
		this.checkInTime = checkInTime;
	}
	public LocalDate getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(LocalDate checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public boolean isInBed() {
		return isInBed;
	}
	public void setInBed(boolean isInBed) {
		this.isInBed = isInBed;
	}
	public CheckInInfo(Bed bed, Patient patient, LocalDate checkInTime, LocalDate checkOutTime, boolean isInBed) {
		this.bed = bed;
		this.patient = patient;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.isInBed = isInBed;
	}
	
}
