package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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
	private ArrayList<Problem> problems = new ArrayList<Problem>();
	private Patient tmppatient = null;
	public boolean idChangable = true;
	public String idNumber = null;
	private long problemId = 1;
	private static Database singleton;
	static {
		singleton = new Database();
		singleton.setProblemId(1);
		
//		System.out.println(problemId);
		singleton.getUsers().add(new UserAccount("user1", "user1"));
		singleton.getUsers().add(new UserAccount("user2", "user2"));
		singleton.getAdmins().add(new UserAccount("admin1", "admin1"));
		singleton.getAdmins().add(new UserAccount("admin2", "admin2"));
		singleton.getPatients().add(new Patient("张三", 18, "331081201111217299", true, "张三的爹", "15305865401"));
		singleton.getPatients().add(new Patient("李四", 18, "33108120111121729X", true, "15305865401", "李四的娘", "15305865401"));
		singleton.getBuildings().add(new Building("一号楼"));
		singleton.getBuildings().add(new Building("二号楼"));
		singleton.getBuildings().get(0).getLevels().add(new Level("一楼", singleton.getBuildings().get(0)));
		singleton.getBuildings().get(0).getLevels().get(0).getRooms().add(new Room("304", true, Room.BATHROOM, singleton.getBuildings().get(0).getLevels().get(0), 3, 3));
		singleton.getBuildings().get(0).getLevels().add(new Level("二楼", singleton.getBuildings().get(0)));
		singleton.getBuildings().get(0).getLevels().get(1).getRooms().add(new Room("306", true, Room.CHESSROOM, singleton.getBuildings().get(0).getLevels().get(1), 2, 2));
		singleton.getBuildings().get(0).getLevels().get(0).getRooms().get(0).getBeds().add(new Bed("一号床", singleton.getBuildings().get(0).getLevels().get(0).getRooms().get(0)));
		singleton.getBuildings().get(0).getLevels().get(0).getRooms().get(0).getBeds().add(new Bed("二号床", singleton.getBuildings().get(0).getLevels().get(0).getRooms().get(0)));
		singleton.getProblems().add(new Problem("你是谁", 0, new ArrayList<String>(Arrays.asList("你", "我", "他")), Problem.TYPES[0]));
	}
	private Database() {
		
	}
	
	public long getProblemId() {
		return this.problemId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public ArrayList<Problem> getProblems() {
		return problems;
	}

	public void setProblems(ArrayList<Problem> problems) {
		this.problems = problems;
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
	public static Database getInstance() {
		if(singleton == null) {
			System.out.println(singleton);
			singleton = new Database();
			System.out.println(singleton);
		}
		return singleton;
	}
}
