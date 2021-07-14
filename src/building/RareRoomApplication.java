package building;

import java.util.Date;
import java.util.regex.Pattern;

public class RareRoomApplication {
	private Date startTime;
	private int durationTime;
	private Pattern applier;
	private Room room;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public int getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}
	public Pattern getApplier() {
		return applier;
	}
	public void setApplier(Pattern applier) {
		this.applier = applier;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public RareRoomApplication(Date startTime, int durationTime, Pattern applier, Room room) {
		this.startTime = startTime;
		this.durationTime = durationTime;
		this.applier = applier;
		this.room = room;
	}
	
}
