package model;

import java.time.LocalDate;
import java.util.ArrayList;

import building.Bed;
import building.Building;
import building.Level;
import building.Room;
import javafx.util.converter.LocalDateStringConverter;

public class Database{
	private ArrayList<UserAccount> users = new ArrayList<UserAccount>();
	private ArrayList<UserAccount> admins = new ArrayList<UserAccount>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	private ArrayList<CheckInInfo> checkInInfos = new ArrayList<CheckInInfo>();
	private Patient tmppatient = null;
	public boolean idChangable = true;
	public String idNumber = null;
	
	private Database() {
		users.add(new UserAccount("user1", "user1"));
		users.add(new UserAccount("user2", "user2"));
		admins.add(new UserAccount("admin1", "admin1"));
		admins.add(new UserAccount("admin2", "admin2"));
		patients.add(new Patient("张三", 18, "331081201111217299", true, "张三的爹", "15305865401"));
		patients.add(new Patient("李四", 18, "33108120111121729X", true, "15305865401", "李四的娘", "15305865401"));
		buildings.add(new Building("一号楼"));
		buildings.add(new Building("二号楼"));
		buildings.get(0).getLevels().add(new Level("一楼", buildings.get(0)));
		buildings.get(0).getLevels().get(0).getRooms().add(new Room("304", true, buildings.get(0).getLevels().get(0)));
		buildings.get(0).getLevels().add(new Level("二楼", buildings.get(0)));
		buildings.get(0).getLevels().get(1).getRooms().add(new Room("306", true, buildings.get(0).getLevels().get(1)));
		buildings.get(0).getLevels().get(0).getRooms().get(0).getBeds().add(new Bed("一号床", buildings.get(0).getLevels().get(0).getRooms().get(0)));
		checkInInfos.add(new CheckInInfo(buildings.get(0).getLevels().get(0).getRooms().get(0).getBeds().get(0), patients.get(0), LocalDate.now(), LocalDate.now(), true));
	}
	
	public ArrayList<CheckInInfo> getCheckInInfos() {
		return checkInInfos;
	}

	public void setCheckInInfos(ArrayList<CheckInInfo> checkInInfos) {
		this.checkInInfos = checkInInfos;
	}

	public static ArrayList<Bed> getBedList() {
		ArrayList<Bed> list = new ArrayList<Bed>();
		for(Building building : Database.getInstance().getBuildings()) {
			for(Level level : building.getLevels()) {
				for(Room room : level.getRooms()) {
					for(Bed bed : room.getBeds()) {
						list.add(bed);
					}
				}
			}
		}
		return list;
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(ArrayList<Building> buildings) {
		this.buildings = buildings;
	}

	public Patient getTmppatient() {
		return tmppatient;
	}

	public void setTmppatient(Patient tmppatient) {
		this.tmppatient = tmppatient;
	}

	public ArrayList<Patient> getPatients() {
		return patients;
	}

	public ArrayList<UserAccount> getUsers() {
		return users;
	}
	public ArrayList<UserAccount> getAdmins() {
		return admins;
	}
	private static Database singleton;
	public static Database getInstance() {
		if(singleton == null) {
			singleton = new Database();
		}
		return singleton;
	}
}
