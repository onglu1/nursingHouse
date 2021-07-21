package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Problem implements Serializable{
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
	public static Problem newProblem() {
		Problem problem = new Problem();
		problem.id = Database.getInstance().getProblemId();
		Database.getInstance().setProblemId(problem.id + 1);
		return problem;
	}
	public Problem() {
		
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
