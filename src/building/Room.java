package building;

import java.util.ArrayList;

public class Room {
	public final static String[] RARETYPES = {"棋牌室", "健身房", "娱乐室" , "浴室"};
	public final static int GYM = "gym".hashCode();
	public final static int BATHROOM = "bathroom".hashCode();
	public final static int ENTERTAINMENTROOM = "ENTERTAINMENTROOM".hashCode();
	public final static int CHESSROOM = "chessroom".hashCode();
	private String name;
	private boolean isRareRoom;
	private int maxCapacity;
	private int resCapacity;
	private ArrayList<RareRoomApplication> applicationList;
	
	public ArrayList<RareRoomApplication> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(ArrayList<RareRoomApplication> applicationList) {
		this.applicationList = applicationList;
	}

	public Level getFather() {
		return father;
	}

	public void setFather(Level father) {
		this.father = father;
	}
	private int RareType;
	private ArrayList<Bed> beds;
	private Level father;
	
	public Room(Level father) {
		beds = new ArrayList<Bed>();
		this.father = father;
	}

	public Room(String name, boolean isRareRoom, Level father) {
		beds = new ArrayList<Bed>();
		this.name = name;
		this.isRareRoom = isRareRoom;
		this.father = father;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public int getResCapacity() {
		return resCapacity;
	}

	public void setResCapacity(int resCapacity) {
		this.resCapacity = resCapacity;
	}

	public Room(String name, boolean isRareRoom, int rareType, Level father, int maxCapacity, int resCapacity) {
		beds = new ArrayList<Bed>();
		this.name = name;
		this.isRareRoom = isRareRoom;
		RareType = rareType;
		this.father = father;
		this.maxCapacity = maxCapacity;
		this.resCapacity = resCapacity;
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
		if(s.equals("浴室")) return Room.BATHROOM;
		if(s.equals("健身房")) return Room.GYM;
		if(s.equals("娱乐室")) return Room.ENTERTAINMENTROOM;
		if(s.equals("棋牌室")) return Room.CHESSROOM;
		return -1;
	}
	public static String getType(int hashNum) {
		if(hashNum == Room.GYM) return "健身房";
		if(hashNum == Room.BATHROOM) return "浴室";
		if(hashNum == Room.ENTERTAINMENTROOM) return "娱乐室";
		if(hashNum == Room.CHESSROOM) return "棋牌室";
		return "无";
	}
}
