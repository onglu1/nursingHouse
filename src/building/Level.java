package building;

import java.util.ArrayList;

public class Level {
	private String name;
	private ArrayList<Room> rooms;
	private Building father;

	public Level(Building father) {
		rooms = new ArrayList<Room>();
		this.father = father;
	}

	public Level(String name, Building father) {
		this.name = name;
		rooms = new ArrayList<Room>();
		this.father = father;
	}

	public Level(String name, ArrayList<Room> rooms, Building father) {
		this.name = name;
		this.rooms = rooms;
		this.father = father;
	}

	public Building getFather() {
		return father;
	}

	public void setFather(Building father) {
		this.father = father;
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
