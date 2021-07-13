package building;

import model.Patient;

public class Bed {
	private String name;
	private Patient owner;
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
	public Bed() {
		super();
	}
	public Bed(String name) {
		this.name = name;
	}
	public Bed(String name, Patient owner) {
		this.name = name;
		this.owner = owner;
	}
	@Override
	public String toString() {
		return this.name;
	}
	
}
