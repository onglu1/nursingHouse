package building;

import java.io.Serializable;
import java.util.Date;
//import java.util.regex.Patient;

import model.Patient;

public class RareRoomApplication implements Serializable {
	public static long TIMESCALE = 1000 * 60 * 60;
	private Date startTime;
	private long durationTime;
	private Patient applier;
	private Room room;
	public Date getStartTime() {
//		System.out.println(startTime);
//		System.out.println(durationTime);
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public long getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime;
	}
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}
	public Patient getApplier() {
		return applier;
	}
	public void setApplier(Patient applier) {
		this.applier = applier;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public RareRoomApplication(Date startTime, long durationTime, Patient applier, Room room) {
		this.startTime = startTime;
		this.durationTime = durationTime;
		this.applier = applier;
		this.room = room;
	}
	
	public RareRoomApplication() {
	}
	public RareRoomApplication(Date startTime, int durationTime, Patient applier, Room room) {
		this.startTime = startTime;
		this.durationTime = durationTime;
		this.applier = applier;
		this.room = room;
	}
	
}
