package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Problem {
	public final static String[] TYPES = {"A", "B"};
	private String description = "";
	private int ans = 0;
	private ArrayList<String> choice = new ArrayList<String>(Arrays.asList("", "", ""));
	private long id = 0;
	private String type = Problem.TYPES[0];
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAns() {
		return ans;
	}
	public void setAns(int ans) {
		this.ans = ans;
	}

	public ArrayList<String> getChoice() {
		return choice;
	}
	public void setChoice(ArrayList<String> choice) {
		this.choice = choice;
	}
	
	public Problem() {
		this.id = Database.getInstance().getProblemId();
		Database.getInstance().setProblemId(this.id + 1);
	}
	public Problem(String description, int ans, ArrayList<String> choice, String type) {
		this.description = description;
		this.ans = ans;
		while(choice.size() < 3)
			choice.add("");
		while(choice.size() > 3) choice.remove(3);
		this.choice = choice;
		this.type = type;
		this.id = Database.getInstance().getProblemId();
		Database.getInstance().setProblemId(id + 1);
	}

}
