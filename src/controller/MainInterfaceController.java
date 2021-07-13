package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import building.Bed;
import building.Building;
import building.Level;
import building.Room;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import main.AddPatient;
import model.Database;
import model.Patient;

public class MainInterfaceController implements Initializable{
//------------------------------------------------------病患管理--------------------------------------------------------------------
//[start]
	//[start]
	private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private Rectangle choiceRec;
    @FXML
    private AnchorPane menuButtonAnchorPane;
    
    @FXML
    private Circle menuShadow;

    @FXML
    private VBox menuVBox;
    
    @FXML
    private Tab evaluateTab;

    @FXML
    private Tab buildingTab;

    @FXML
    private Tab userTab;
    
    @FXML
    private TabPane interfaceTabPane;
    
    @FXML
    private Rectangle evaluateRec;
    
    @FXML
    private Rectangle userRec;
    
    @FXML
    private Rectangle buildingRec;

    @FXML
    private Rectangle screenCover;
    
    @FXML
    private TabPane choinePane;
    @FXML
    private ChoiceBox<String> searchChoice;
    @FXML
    private TableView<Patient> patientTableView;
    
    @FXML
    private TextField searchField;
    //[end]
    
    //显示菜单
    @FXML
    void sidebarShow() {
    	if(menuVBox.getTranslateX() >= -menuVBox.getWidth() / 2) return ;
    	TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), menuVBox);
    	tt.setToX(0);
    	screenCover.setVisible(true);
    	tt.play();
    }
    //隐藏菜单
    @FXML
    void sidebarHide() {
    	if(menuVBox.getTranslateX() <= -menuVBox.getWidth() / 2) return ;
    	TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), menuVBox);
    	tt.setToX(-menuVBox.getWidth());
    	tt.setInterpolator(Interpolator.EASE_OUT);
    	tt.play();
    	screenCover.setVisible(false);
    }
    
    //鼠标移动显示阴影
    @FXML
    void shadowShow(MouseEvent event) {
    	if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
    		Node source = (Node)event.getSource();
    		source.setOpacity(0.5);
    	}
    }

    @FXML
    void shadowHide(MouseEvent event) {
    	if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
    		Node source = (Node)event.getSource();
    		source.setOpacity(0);
    	}
    }
    //切换菜单
    @FXML
    void switchChoice(MouseEvent event) {
    	choiceRec = (Rectangle)event.getSource();
    	if(choiceRec.equals(buildingRec)) {
    		choinePane.getSelectionModel().select(buildingTab);
    		sidebarHide();
    	} else if(choiceRec.equals(userRec)) {
    		choinePane.getSelectionModel().select(userTab);
    		sidebarHide();
    	} else if(choiceRec.equals(evaluateRec)) {
    		choinePane.getSelectionModel().select(evaluateTab);
    		sidebarHide();
    	}
    	
    }
    
    //新增病患
    @FXML
    void addPatient() throws Exception {
    	AddPatient addPatient = new AddPatient();
    	Stage stage1 = new Stage();
    	stage1.initModality(Modality.APPLICATION_MODAL);
    	addPatient.start(stage1);
    	stage1.setOnHidden(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				if(Database.getInstance().getTmppatient() != null) 
					Database.getInstance().getPatients().add(Database.getInstance().getTmppatient());
				Database.getInstance().setTmppatient(null);
				patientList.setAll(Database.getInstance().getPatients());
			}
		});
    	
    }
    
    //修改病患
    @FXML
    void modify() throws Exception {
    	AddPatient addPatient = new AddPatient();
    	Patient oldPatient = patientTableView.getSelectionModel().getSelectedItem();
    	if(oldPatient == null) {
    		Alert alert = new Alert(AlertType.ERROR, "尚未选中病患");
    		alert.show();
    		return ;
    	}
    	Stage stage1 = new Stage();
    	stage1.initModality(Modality.APPLICATION_MODAL);
    	Database.getInstance().idChangable = false;
    	Database.getInstance().idNumber = oldPatient.getId();
    	addPatient.start(stage1);
    	
    	stage1.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				if(Database.getInstance().getTmppatient() != null) {
					Database.getInstance().getPatients().remove(oldPatient);
					Database.getInstance().getPatients().add(Database.getInstance().getTmppatient());
				}
				Database.getInstance().idNumber = null;
				Database.getInstance().setTmppatient(null);
				patientList.setAll(Database.getInstance().getPatients());
			}
		});
    }
    
    //查询病患
    @FXML
    void query() throws Exception {
		//为空则为重置

		if(searchField.getText().equals("")) {
			patientList.setAll(Database.getInstance().getPatients());
			return ;
		}
		if(searchChoice.getSelectionModel().getSelectedItem() == null) {
			return ;
		}
		patientList.clear();
		//查找年龄
    	if(searchChoice.getSelectionModel().getSelectedItem().equals("年龄")) {
    		if(searchField.getText().length() >= 3) return ;
    		for(Patient patient : Database.getInstance().getPatients()) {
    			if(patient.getAge() == Integer.parseInt(searchField.getText())) {
    				patientList.add(patient);
    			}
    		}
    	} else if(searchChoice.getSelectionModel().getSelectedItem().equals("性别")){
    		boolean sex = true;
    		if(searchField.getText().equals("男")) sex = true;
    		else sex = false;
    		for(Patient patient : Database.getInstance().getPatients()) {
    			if(patient.getSex() == sex) {
    				patientList.add(patient);
    			}
    		}
    	} else {
    		for(Patient patient : Database.getInstance().getPatients()) {
    			if(patient.getByName(searchChoice.getSelectionModel().getSelectedItem()).contains(searchField.getText())) {
    				patientList.add(patient);
    			}
    		}
    	}
    }
    
    //查询快捷键
    @FXML
    void enterPressed(KeyEvent event) throws Exception {
    	if(event.getCode() == KeyCode.ENTER) {
    		if(searchField.isFocused()) 
    			query();
    	}
    }
	
    //初始化病患管理界面
    void patientManagementInit() {
    	patientList.setAll(Database.getInstance().getPatients());
		patientTableView.setItems(patientList);
		TableColumn<Patient, String> nameColumn = new TableColumn<Patient, String>("姓名");
		TableColumn<Patient, Integer> ageColumn = new TableColumn<Patient, Integer>("年龄");
		TableColumn<Patient, String> idColumn = new TableColumn<Patient, String>("身份证号码");
		TableColumn<Patient, String> sexColumn = new TableColumn<Patient, String>("性别");
		TableColumn<Patient, String> phoneNumberColumn = new TableColumn<Patient, String>("联系电话");
		TableColumn<Patient, String> emergencyContactColumn = new TableColumn<Patient, String>("紧急联系人");
		TableColumn<Patient, String> emergencyPhoneNumberColumn = new TableColumn<Patient, String>("紧急联系电话");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		idColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("id"));
		ageColumn.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
		sexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Patient, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty((param.getValue().getSex()) ? "男" : "女");
				return str;
			}
		});
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("phoneNumber"));
		emergencyContactColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("emergencyContact"));
		emergencyPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("emergencyPhoneNumber"));
		nameColumn.setPrefWidth(100.0);
		nameColumn.setResizable(false);
		idColumn.setPrefWidth(198.0);
		idColumn.setResizable(false);
		ageColumn.setPrefWidth(50);
		ageColumn.setResizable(false);
		sexColumn.setPrefWidth(50);
		sexColumn.setResizable(false);
		phoneNumberColumn.setPrefWidth(200);
		phoneNumberColumn.setResizable(false);
		emergencyContactColumn.setPrefWidth(100);
		emergencyContactColumn.setResizable(false);
		emergencyPhoneNumberColumn.setPrefWidth(200);
		emergencyPhoneNumberColumn.setResizable(false);
		
		patientTableView.getColumns().add(nameColumn);
		patientTableView.getColumns().add(ageColumn);
		patientTableView.getColumns().add(idColumn);
		patientTableView.getColumns().add(sexColumn);
		patientTableView.getColumns().add(phoneNumberColumn);
		patientTableView.getColumns().add(emergencyContactColumn);
		patientTableView.getColumns().add(emergencyPhoneNumberColumn);
		
		searchChoice.getItems().add("姓名");
		searchChoice.getItems().add("年龄");
		searchChoice.getItems().add("身份证号码");
		searchChoice.getItems().add("性别");
		searchChoice.getItems().add("联系电话");
		searchChoice.getItems().add("紧急联系人");
		searchChoice.getItems().add("紧急联系电话");
		searchChoice.getSelectionModel().select("姓名");
    }
    
//[end]
//------------------------------------------------------评估管理--------------------------------------------------------------------

//------------------------------------------------------楼宇管理--------------------------------------------------------------------
//[start]
    //[start]

    @FXML
    private ListView<Building> buildingList;
    @FXML
    private ListView<Level> levelList;
    @FXML
    private ListView<Room> roomList;
    @FXML
    private ListView<Bed> bedList;
    
    @FXML
    private VBox buildingModifier;
    @FXML
    private TextField buildingField;
    @FXML
    private VBox levelModifier;
    @FXML
    private TextField levelField;
    @FXML
    private VBox roomModifier;
    @FXML
    private TextField roomField;
    @FXML
    private ChoiceBox<String> isRareChoice;
    @FXML
    private ChoiceBox<String> rareTypeChoice;
    @FXML
    private Label roomInfo1;
    @FXML
    private Label roomInfo2;
    
    @FXML
    private VBox bedModifier;
    @FXML
    private TextField bedField;
    
    //[end]
    
    //返回上级菜单
    @FXML
    private void preMenu() {
		buildingModifier.setVisible(false);
		levelModifier.setVisible(false);
		roomModifier.setVisible(false);
		bedModifier.setVisible(false);
    	if(!bedList.getItems().isEmpty()) {
    		bedList.getItems().clear();
    		roomList.getSelectionModel().clearSelection();
    		return ;
    	}
    	if(!roomList.getItems().isEmpty()) {
    		roomList.getItems().clear();
    		levelList.getSelectionModel().clearSelection();
    		return ;
    	}
    	if(!levelList.getItems().isEmpty()) {
    		levelList.getItems().clear();
    		buildingList.getSelectionModel().clearSelection();
    		return ;
    	}
		buildingList.getSelectionModel().clearSelection();
		return ;
    	
    }
    
    //增加大楼
    @FXML
    private void addBuilding() {
    	if(Pattern.matches(" *", buildingField.getText())) {
    		Alert alert = new Alert(AlertType.ERROR, "请输入大楼名称");
    		alert.show();
    	} else {
    		String name = buildingField.getText();
    		for(Building building : Database.getInstance().getBuildings()) {
    			if(building.getName().equals(name)) {
    				Alert alert = new Alert(AlertType.ERROR, "已存在同名大楼");
    	    		alert.show();
    	    		return ;
    			}
    		}
    		Database.getInstance().getBuildings().add(new Building(name));
    		buildingList.getItems().setAll(Database.getInstance().getBuildings());
    		buildingField.setText("");
    	}
    }
    
    //删除大楼
    @FXML
    private void removeBuilding() {
    	if(buildingList.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "尚未选择大楼");
    		alert.show();
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("警告");
    		alert.getButtonTypes().add(ButtonType.CANCEL);
    		alert.setContentText("该操作不可逆，请确认是否删除。");
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			Database.getInstance().getBuildings().remove(buildingList.getSelectionModel().getSelectedItem());
        		buildingList.getItems().setAll(Database.getInstance().getBuildings());
            	levelList.getItems().clear();
        		roomList.getItems().clear();
        		bedList.getItems().clear();
    		}
    	}
    }
    
    //新建楼层
    @FXML
    private void addLevel() {
    	Building father = buildingList.getSelectionModel().getSelectedItem();
    	if(Pattern.matches(" *", levelField.getText())) {
    		Alert alert = new Alert(AlertType.ERROR, "请输入楼层名称");
    		alert.show();
    	} else {
    		String name = levelField.getText();
    		for(Level level : father.getLevels()) {
    			if(level.getName().equals(name)) {
    				Alert alert = new Alert(AlertType.ERROR, "已存在同名楼层");
    	    		alert.show();
    	    		return ;
    			}
    		}
    		father.getLevels().add(new Level(name));
    		levelList.getItems().setAll(father.getLevels());
    		levelField.setText("");
    		
    	}
    }
    
    //删除楼层
    @FXML
    private void removeLevel() {
    	Building father = buildingList.getSelectionModel().getSelectedItem();
    	if(levelList.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "尚未选择楼层");
    		alert.show();
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("警告");
    		alert.getButtonTypes().add(ButtonType.CANCEL);
    		alert.setContentText("该操作不可逆，请确认是否删除。");
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			father.getLevels().remove(levelList.getSelectionModel().getSelectedItem());
        		levelList.getItems().setAll(father.getLevels());
        		roomList.getItems().clear();
        		bedList.getItems().clear();
    		}
    	}
    }
    
    //添加房间
    @FXML
    private void addRoom() {
    	Level father = levelList.getSelectionModel().getSelectedItem();
    	if(Pattern.matches(" *", roomField.getText())) {
    		Alert alert = new Alert(AlertType.ERROR, "请输入房间名称");
    		alert.show();
    	} else if(isRareChoice.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请选择是否为稀有房间");
    		alert.show();
    	} else if(isRareChoice.getSelectionModel().getSelectedItem().equals("是") && rareTypeChoice.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请选择稀有房间类型");
    		alert.show();
    	} else {
    		String name = roomField.getText();
    		for(Room room : father.getRooms()) {
    			if(room.getName().equals(name)) {
    				Alert alert = new Alert(AlertType.ERROR, "已存在同名房间");
    	    		alert.show();
    	    		return ;
    			}
    		}
//    		System.out.println(father);
    		if(isRareChoice.getSelectionModel().getSelectedItem().equals("是")) {
    			father.getRooms().add(new Room(roomField.getText(), true, Room.getTypeByChinese(rareTypeChoice.getSelectionModel().getSelectedItem())));
    		} else {
    			System.out.println(father.getRooms() == null);
    			father.getRooms().add(new Room(roomField.getText(), false));
    		}
    		roomList.getItems().setAll(father.getRooms());
    		roomField.setText("");
    		isRareChoice.getSelectionModel().clearSelection();
    		rareTypeChoice.getSelectionModel().clearSelection();
    	}
    	
    }

    //删除房间
    @FXML
    private void removeRoom() {
    	Level father = levelList.getSelectionModel().getSelectedItem();
    	if(levelList.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "尚未选择楼层");
    		alert.show();
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("警告");
    		alert.getButtonTypes().add(ButtonType.CANCEL);
    		alert.setContentText("该操作不可逆，请确认是否删除。");
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			father.getRooms().remove(roomList.getSelectionModel().getSelectedItem());
        		roomList.getItems().setAll(father.getRooms());
        		bedList.getItems().clear();
    		}
    	}
    }
    //添加床
    @FXML
    private void addBed() {
    	Room father = roomList.getSelectionModel().getSelectedItem();
    	if(Pattern.matches(" *", bedField.getText())) {
    		Alert alert = new Alert(AlertType.ERROR, "请输入床号");
    		alert.show();
    	} else {
    		String name = bedField.getText();
    		for(Bed bed : father.getBeds()) {
    			if(bed.getName().equals(name)) {
    				Alert alert = new Alert(AlertType.ERROR, "已存在同名床位 ");
    	    		alert.show();
    	    		return ;
    			}
    		}
    		father.getBeds().add(new Bed(name));
    		bedList.getItems().setAll(father.getBeds());
    		bedField.setText("");
    		
    	}
    }
    //删除床位
    @FXML
    private void removeBed() {
    	Room father = roomList.getSelectionModel().getSelectedItem();
    	if(bedList.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "尚未选择床位");
    		alert.show();
    	} else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("警告");
    		alert.getButtonTypes().add(ButtonType.CANCEL);
    		alert.setContentText("该操作不可逆，请确认是否删除。");
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			father.getBeds().remove(bedList.getSelectionModel().getSelectedItem());
        		bedList.getItems().setAll(father.getBeds());
    		}
    	}
    	
    }
    //楼宇管理初始化
    void buildingmanagementInit() {
    	buildingModifier.setVisible(false);
    	levelModifier.setVisible(false);
    	roomModifier.setVisible(false);
    	bedModifier.setVisible(false);
    	//设置房间信息展示栏
    	roomInfo1.setText("");
    	roomInfo2.setText("");
    	//添加房间选项栏
    	isRareChoice.getItems().addAll("是", "否");
    	rareTypeChoice.getItems().addAll("健身房", "淋浴房");
    	//添加切换窗口的监听器
    	ObservableList<Building> obl = buildingList.getItems();
    	obl.setAll(Database.getInstance().getBuildings());
    	buildingList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Building>() {
			@Override
			public void changed(ObservableValue<? extends Building> observable, Building oldValue, Building newValue) {
				// TODO Auto-generated method stub
				roomList.getItems().clear();
				bedList.getItems().clear();
				if(newValue != null) levelList.getItems().setAll(newValue.getLevels());
			}
		});
    	levelList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Level>() {

			@Override
			public void changed(ObservableValue<? extends Level> observable, Level oldValue, Level newValue) {
				// TODO Auto-generated method stub
				
				bedList.getItems().clear();
				if(newValue != null) roomList.getItems().setAll(newValue.getRooms());
				
			}
		});
    	roomList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Room>() {

			@Override
			public void changed(ObservableValue<? extends Room> observable, Room oldValue, Room newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					roomInfo1.setText("是否为稀有房间：" + (newValue.isRareRoom() ? "是" :"否"));
					if(newValue.isRareRoom())
						roomInfo2.setText("房间种类：" + Room.getType(newValue.getRareType()));
					bedList.getItems().setAll(newValue.getBeds());
				} else {
					roomInfo1.setText("");
					roomInfo2.setText("");
				}
				
			}
		});
    	bedList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bed>() {

			@Override
			public void changed(ObservableValue<? extends Bed> observable, Bed oldValue, Bed newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					if(newValue.getOwner() != null)
						roomInfo1.setText(newValue.getName() + "，占有者：" + newValue.getOwner().toString());
					else roomInfo1.setText(newValue.getName() + "：当前空闲");
					roomInfo2.setText("");
				} else {
					roomInfo1.setText("");
					roomInfo2.setText("");
				}
				
			}
		});
    	isRareChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue.equals("否")) {
					if(rareTypeChoice.getSelectionModel() != null)
						rareTypeChoice.getSelectionModel().clearSelection();
					rareTypeChoice.setDisable(true);
				} else if(newValue.equals("是")){
					rareTypeChoice.setDisable(false);
				}
			}
		
    	});
    	buildingList.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == true) {
					buildingModifier.setVisible(true);
					levelModifier.setVisible(false);
					roomModifier.setVisible(false);
					bedModifier.setVisible(false);
				}
				if(newValue == false) {
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused())
						buildingModifier.setVisible(false); 
				}
				
			}
		});
    	levelList.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == true) {
					if(buildingList.getSelectionModel().getSelectedItem() != null){
						buildingModifier.setVisible(false);
						levelModifier.setVisible(true);
						roomModifier.setVisible(false);
						bedModifier.setVisible(false);
					}
				}
				if(newValue == false) {
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused())
						levelModifier.setVisible(false); 
				}
				
			}
		});
    	roomList.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == true) {
					if(levelList.getSelectionModel().getSelectedItem() != null) {
						buildingModifier.setVisible(false);
						levelModifier.setVisible(false);
						roomModifier.setVisible(true);
						bedModifier.setVisible(false);
					}
				}
				if(newValue == false) {
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused())
						roomModifier.setVisible(false); 
				}
				
			}
		});
    	bedList.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == true) {
					if(roomList.getSelectionModel().getSelectedItem() != null){
						buildingModifier.setVisible(false);
						levelModifier.setVisible(false);
						roomModifier.setVisible(false);
						bedModifier.setVisible(true);
					}
				}
				if(newValue == false) {
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused())
						bedModifier.setVisible(false); 
				}
				
			}
		});
    }
    
//[end]
    
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		patientManagementInit();
		buildingmanagementInit();
	}

}
