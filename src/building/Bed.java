package building;

import java.io.Serializable;

import model.Patient;

public class Bed implements Serializable{
	private String name;
	private Patient owner;
	private Room father;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Patient getOwner() {
		return owner;
	}
	public void setOwner(Patient owner) {
		this.owner = owner;
	}
	public Bed(Room father) {
		this.father = father;
	}
	public Bed(String name, Room father) {
		this.name = name;
		this.father = father;
	}
	public Bed(String name, Patient owner, Room father) {
		this.name = name;
		this.owner = owner;
		this.father = father;
	}
	public Room getFather() {
		return father;
	}
	public void setFather(Room father) {
		this.father = father;
	}
	@Override
	public String toString() {
		return this.name;
	}
	public Bed() {
	}
	
}
