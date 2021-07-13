package building;

import java.util.ArrayList;

public class Room {
	public final static int GYM = "gym".hashCode();
	public final static int BATHROOM = "bathroom".hashCode();
	private String name;
	private boolean isRareRoom;
	private int RareType;
	private ArrayList<Bed> beds;
	
	public Room() {
		beds = new ArrayList<Bed>();
	}

	public Room(String name, boolean isRareRoom) {
		beds = new ArrayList<Bed>();
		this.name = name;
		this.isRareRoom = isRareRoom;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Room(String name, boolean isRareRoom, int rareType) {
		beds = new ArrayList<Bed>();
		this.name = name;
		this.isRareRoom = isRareRoom;
		RareType = rareType;
	}

	public boolean isRareRoom() {
		return isRareRoom;
	}

	public void setRareRoom(boolean isRareRoom) {
		this.isRareRoom = isRareRoom;
	}

	public int getRareType() {
		return RareType;
	}

	public void setRareType(int rareType) {
		RareType = rareType;
	}

	public ArrayList<Bed> getBeds() {
		return beds;
	}

	public void setBeds(ArrayList<Bed> beds) {
		this.beds = beds;
	}
	public String toString() {
		return this.name;
	}
	public static int getTypeByChinese(String s) {
		if(s.equals("淋浴室")) return Room.BATHROOM;
		if(s.equals("健身房")) return Room.GYM;
		return -1;
	}
	public static String getType(int hashNum) {
		if(hashNum == Room.GYM) return "健身房";
		if(hashNum == Room.BATHROOM) return "淋浴室";
		return "无";
	}
}
