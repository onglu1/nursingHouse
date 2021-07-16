package building;

import java.io.Serializable;
import java.util.ArrayList;

public class Building implements Serializable{
	private String name;
	private ArrayList<Level> levels;

	public Building() {
		levels = new ArrayList<Level>();
	}

	public Building(String name) {
		levels = new ArrayList<Level>();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Level> getLevels() {
		return levels;
	}

	public void setLevels(ArrayList<Level> levels) {
		this.levels = levels;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
