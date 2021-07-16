package building;

import java.io.Serializable;
import java.util.Date;
//import java.util.regex.Patient;

import model.Patient;

public class RareRoomApplication implements Serializable {
	private long timescale = 1000 * 60 * 60;
	private Date startTime;
	private long durationTime;
	private Patient applier;
	private Room room;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public long getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime * timescale;
	}
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime * timescale;
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
		this.durationTime = durationTime * timescale;
		this.applier = applier;
		this.room = room;
	}
	public RareRoomApplication(Date startTime, int durationTime, Patient applier, Room room) {
		this.startTime = startTime;
		this.durationTime = durationTime * timescale;
		this.applier = applier;
		this.room = room;
	}
	
}
