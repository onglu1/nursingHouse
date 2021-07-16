package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Template implements Serializable{
	public final static String[] TYPES = {"A", "B"};
	private ArrayList<Problem> problems = new ArrayList<Problem>();
	private String name = "";
	private String type = "A";
	private long id = 0;
	public ArrayList<Problem> getProblems() {
		return problems;
	}
	public void setProblems(ArrayList<Problem> problems) {
		this.problems = problems;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public static Template newTemplate() {
		Template template = new Template();
		template.id = Database.getInstance().getTemplateId();
		Database.getInstance().setTemplateId(template.id + 1);
		return template;
	}
	public Template() {
		
	}
	public Template(String name) {
		this.id = Database.getInstance().getTemplateId();
		this.name = name;
		Database.getInstance().setTemplateId(this.id + 1);
	}
}