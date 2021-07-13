package building;

import java.util.ArrayList;

public class Level {
	private String name;
	private ArrayList<Room> rooms;

	public Level() {
		rooms = new ArrayList<Room>();
	}

	public Level(String name) {
		this.name = name;
		rooms = new ArrayList<Room>();
	}

	public Level(String name, ArrayList<Room> rooms) {
		this.name = name;
		this.rooms = rooms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}

	public String toString() {
		return this.name;
	}
}
