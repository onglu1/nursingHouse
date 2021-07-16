package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import building.Bed;
import building.Building;
import building.Level;
import building.Room;

public class Database implements Serializable{
	private ArrayList<UserAccount> users = new ArrayList<UserAccount>();
	private ArrayList<UserAccount> admins = new ArrayList<UserAccount>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	private ArrayList<CheckInInfo> checkInInfos = new ArrayList<CheckInInfo>();
	private ArrayList<Problem> problems = new ArrayList<Problem>();
	private ArrayList<Template> templates = new ArrayList<Template>();
	private Patient tmppatient = null;
	public boolean idChangable = true;
	public String idNumber = null;
	private long problemId = 1;
	private long templateId = 1;
	private Template tmptemplate = null;
	private static Database singleton;
	
	static {
		singleton = new Database();
		singleton.setProblemId(1);
		
//		System.out.println(problemId);
		singleton.getUsers().add(new UserAccount("user1", "user1"));
		singleton.getUsers().get(0).setTitile("医生");
		singleton.getUsers().get(0).setExpertise("摸鱼");
		singleton.getUsers().get(0).setName("优秀的医生");
		singleton.getUsers().add(new UserAccount("user2", "user2"));
		singleton.getUsers().get(1).setTitile("医生");
		singleton.getUsers().get(1).setExpertise("睡觉");
		singleton.getUsers().get(1).setName("不好的医生");
		singleton.getUsers().add(new UserAccount("user3", "user3"));
		singleton.getUsers().get(2).setTitile("护士");
		singleton.getUsers().get(2).setExpertise("睡觉");
		singleton.getUsers().get(2).setName("不好的护士");
		singleton.getUsers().add(new UserAccount("user4", "user4"));
		singleton.getUsers().get(3).setTitile("护工");
		singleton.getUsers().get(3).setExpertise("睡觉");
		singleton.getUsers().get(3).setName("不好的护工");
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
		singleton.getTemplates().add(new Template("qwer"));
		singleton.getTemplates().get(0).getProblems().add(new Problem());
		singleton.getTemplates().get(0).getProblems().add(new Problem());
		singleton.getTemplates().get(0).getProblems().add(new Problem());
		singleton.getTemplates().add(new Template("qwe"));
		singleton.getTemplates().get(1).getProblems().add(new Problem());
		singleton.getTemplates().add(new Template("awe"));
		singleton.getTemplates().get(1).getProblems().add(new Problem());
		singleton.setTmptemplate(singleton.getTemplates().get(0));
//		saveToFile();
	}

	private Database() {
		
	}
	
	public Template getTmptemplate() {
		return tmptemplate;
	}

	public void setTmptemplate(Template tmptemplate) {
		this.tmptemplate = tmptemplate;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public ArrayList<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(ArrayList<Template> templates) {
		this.templates = templates;
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
			readFromFile();
			singleton = new Database();
		}
		return singleton;
	}
	public static void saveToFile() {
		String path = ".\\data\\";
        File file = new File(path);
        
		if(!file.exists()){
            file.mkdir();
        }
		String jsonString = JSON.toJSONString(singleton);
		StringtoFile(jsonString, path + "singleton.json");
	}
	public static void readFromFile() {
		String path = ".\\data\\";
		File file = new File(path + "singleton.json");
		String jsonString = readJson(file);
//		JSONObject jo = JSON.parseObject(jsonString);
		singleton = JSON.parseObject(jsonString, Database.class);
	}
	public static String readJson(File file) {
		if(!file.exists()){
            return "";
        }
		BufferedReader bufr = null;
		String line = null;
		String tmp = null;
		try {
			bufr = new BufferedReader(new FileReader(file));
			while((tmp = bufr.readLine()) != null) {
				if(line == null) line = tmp;
				else line += tmp;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if(bufr != null) 
					bufr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return line;
	}
	public static void StringtoFile(String s, String path){
        File file = new File(path);
		if(!file.exists()){
            try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        OutputStream outPutStream = null;
        try{
            outPutStream = new FileOutputStream(file);
            byte[]  bytes = s.getBytes("UTF-8");
            outPutStream.write(bytes);
            outPutStream.close();
        }catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(outPutStream != null) 
					outPutStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
