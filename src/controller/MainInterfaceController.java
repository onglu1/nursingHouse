package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import building.Bed;
import building.Building;
import building.Level;
import building.RareRoomApplication;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.AddPatient;
import main.Evaluate;
import main.Login;
import model.CheckInInfo;
import model.Database;
import model.Patient;
import model.Problem;
import model.Template;
import utils.DragWindowHandler;

public class MainInterfaceController{
//------------------------------------------------------界面管理--------------------------------------------------------------------
//[start]
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
    //登出
    @FXML
    void logoutButtonFired() {
    	Login main = new Login();
    	Stage stage = new Stage();
    	try {
			main.start(stage);
			stage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	thisStage.hide();
    }
//[end]
//------------------------------------------------------病患管理--------------------------------------------------------------------
//[start]
	//[start]
	private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private Rectangle choiceRec;
    @FXML
    private ChoiceBox<Template> patientManTemplateChoiceBox;
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
    //评估病患
    @FXML
    private void patientManRemoveButtonFired() {
    	if(patientTableView.getSelectionModel().getSelectedItems().size() == 0) {
    		Alert alert = new Alert(AlertType.ERROR, "请在表中选择要删除的病患。");
    		alert.show();
    		return ;
    	}
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("警告");
		alert.getButtonTypes().add(ButtonType.CANCEL);
		alert.setContentText("该操作不可逆，请确认是否删除。");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() != ButtonType.OK) return ;
		for(Patient patient2 : patientTableView.getSelectionModel().getSelectedItems()) {
			Database.getInstance().getPatients().remove(patient2);
		}
    	
    	Database.saveToFile();
    	patientManagementInit();
    }
    @FXML
    private void patientEvaluateButtonFIred() throws Exception {
    	Patient patient = patientTableView.getSelectionModel().getSelectedItem();
    	Template template = patientManTemplateChoiceBox.getSelectionModel().getSelectedItem();
    	if(template == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请选择模板。");
    		alert.show();
    		return ;
    	}
    	if(template.getProblems().size() == 0) {
    		Alert alert = new Alert(AlertType.ERROR, "当前模板不包含问题，请先在评估管理界面中。");
    		alert.show();
    		return ;
    	}
    	if(patient == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请在表格中选择病人。");
    		alert.show();
    		return ;
    	}
    	Evaluate main = new Evaluate();
    	AnchorPane ap = new AnchorPane();
    	Scene sc = new Scene(ap);
    	Stage stage = new Stage();
    	stage.setScene(sc);
    	Database.getInstance().setTmppatient(patient);
    	Database.getInstance().setTmptemplate(template);
    	stage.initModality(Modality.WINDOW_MODAL.APPLICATION_MODAL);
    	main.start(stage);
    	stage.setOnHidden(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				patientManagementInit();
				Database.saveToFile();
			}
		});
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
				Database.saveToFile();
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
    	Database.getInstance().setTmppatient(oldPatient);
    	addPatient.start(stage1);
    	
    	stage1.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				if(Database.getInstance().getTmppatient() != null) {
					oldPatient.copy(Database.getInstance().getTmppatient());
				}
				Database.getInstance().idNumber = null;
				Database.getInstance().setTmppatient(null);
				patientList.setAll(Database.getInstance().getPatients());
				Database.saveToFile();
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
    @FXML
    void patientManagementInit() {
    	patientTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	patientList.clear();
    	patientTableView.getColumns().clear();
    	patientList.setAll(Database.getInstance().getPatients());
    	ObservableList<Template> tmplist = FXCollections.observableArrayList();
    	tmplist.setAll(Database.getInstance().getTemplates());
    	patientManTemplateChoiceBox.setItems(tmplist);
    	patientManTemplateChoiceBox.getSelectionModel().clearSelection();
    	patientManTemplateChoiceBox.setConverter(new StringConverter<Template>() {
			
			@Override
			public String toString(Template arg0) {
				// TODO Auto-generated method stub
				return arg0.getName();
			}
			
			@Override
			public Template fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		patientTableView.setItems(patientList);
		TableColumn<Patient, String> nameColumn = new TableColumn<Patient, String>("姓名");
		TableColumn<Patient, Integer> ageColumn = new TableColumn<Patient, Integer>("年龄");
		TableColumn<Patient, String> idColumn = new TableColumn<Patient, String>("身份证号码");
		TableColumn<Patient, String> sexColumn = new TableColumn<Patient, String>("性别");
		TableColumn<Patient, String> phoneNumberColumn = new TableColumn<Patient, String>("联系电话");
		TableColumn<Patient, String> emergencyContactColumn = new TableColumn<Patient, String>("紧急联系人");
		TableColumn<Patient, String> emergencyPhoneNumberColumn = new TableColumn<Patient, String>("紧急联系电话");
		TableColumn<Patient, Double> scoreColumn = new TableColumn<Patient, Double>("评估分数");
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
		scoreColumn.setCellValueFactory(new PropertyValueFactory<Patient, Double>("score"));
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("phoneNumber"));
		emergencyContactColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("emergencyContact"));
		emergencyPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("emergencyPhoneNumber"));
		double p = patientTableView.getWidth();
		nameColumn.setPrefWidth(patientTableView.getWidth() * 0.1); p -= nameColumn.getPrefWidth();
		idColumn.setPrefWidth(patientTableView.getWidth() * 0.21); p -= idColumn.getPrefWidth();
		ageColumn.setPrefWidth(patientTableView.getWidth() * 0.07); p -= ageColumn.getPrefWidth();
		sexColumn.setPrefWidth(patientTableView.getWidth() * 0.07); p -= sexColumn.getPrefWidth();
		phoneNumberColumn.setPrefWidth(patientTableView.getWidth() * 0.16); p -= phoneNumberColumn.getPrefWidth();
		emergencyContactColumn.setPrefWidth(patientTableView.getWidth() * 0.1); p -= emergencyContactColumn.getPrefWidth();
		emergencyPhoneNumberColumn.setPrefWidth(patientTableView.getWidth() * 0.16); p -= emergencyPhoneNumberColumn.getPrefWidth();
		scoreColumn.setPrefWidth(p - 3);
		
		patientTableView.getColumns().add(nameColumn);
		patientTableView.getColumns().add(ageColumn);
		patientTableView.getColumns().add(idColumn);
		patientTableView.getColumns().add(sexColumn);
		patientTableView.getColumns().add(phoneNumberColumn);
		patientTableView.getColumns().add(emergencyContactColumn);
		patientTableView.getColumns().add(emergencyPhoneNumberColumn);
		patientTableView.getColumns().add(scoreColumn);
		
		searchChoice.getItems().clear();
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
//------------------------------------------------------床位管理--------------------------------------------------------------------
//[start]
    //[start]
	private ObservableList<CheckInInfo> checkInInfoList = FXCollections.observableArrayList();
    @FXML
    private TableView<CheckInInfo> bedTableView;
    @FXML
    private ChoiceBox<Patient> checkInPatientChoiceBox;
    @FXML
    private ChoiceBox<Building> bedManBuildingChoice;
    @FXML
    private ChoiceBox<Level> bedManLevelChoice;
    @FXML
    private ChoiceBox<Room> bedManRoomChoice;
    @FXML
    private ChoiceBox<Bed> bedManBedChoice;
    
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;
    @FXML
    private Label bedManInfoLabel;
    //[end]
    //删除记录
    @FXML
    private void bedManRemoveButtonFired() {
    	CheckInInfo checkInInfo = bedTableView.getSelectionModel().getSelectedItem();
    	if(checkInInfo == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请先在表中选择数据。");
    		alert.show();
    		return ;
    	}
    	if(checkInInfo.isInBed() == true) {
    		Alert alert = new Alert(AlertType.ERROR, "所选病人尚未出院，无法删除");
    		alert.show();
    		return ;
    	}
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("警告");
		alert.getButtonTypes().add(ButtonType.CANCEL);
		alert.setContentText("该操作不可逆，请确认是否删除。");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() != ButtonType.OK) return ;
		
    	for(CheckInInfo info : bedTableView.getSelectionModel().getSelectedItems()) {
    		Database.getInstance().getCheckInInfos().remove(info);
    	}
    	
    	Database.saveToFile();
    	bedManagementInit();
    }
    //入住按钮触发事件
    @FXML
    private void checkInButtonFired() {
    	boolean isOk = true;
    	isOk &= bedManBuildingChoice.getSelectionModel().getSelectedItem() != null;
    	isOk &= bedManLevelChoice.getSelectionModel().getSelectedItem() != null;
    	isOk &= bedManRoomChoice.getSelectionModel().getSelectedItem() != null;
    	isOk &= bedManBedChoice.getSelectionModel().getSelectedItem() != null;
    	if(!isOk) {
    		Alert alert = new Alert(AlertType.ERROR, "未选择床号");
    		alert.show();
    		return ;
    	}
    	isOk &= checkInPatientChoiceBox.getSelectionModel().getSelectedItem() != null;
    	if(!isOk) {
    		Alert alert = new Alert(AlertType.ERROR, "未选择入住人");
    		alert.show();
    		return ;
    	}
    	isOk &= checkInDatePicker.getValue() != null;
    	isOk &= checkOutDatePicker.getValue() != null;
    	if(!isOk) {
    		Alert alert = new Alert(AlertType.ERROR, "未选择入住时间");
    		alert.show();
    		return ;
    	}
    	if(checkInDatePicker.getValue().isAfter(checkOutDatePicker.getValue())) {
    		Alert alert = new Alert(AlertType.ERROR, "时间选择有误（离开时间晚于入住时间）");
    		alert.show();
    		return ;
    	}
    	if(bedManBedChoice.getSelectionModel().getSelectedItem().getOwner() != null) {
    		Alert alert = new Alert(AlertType.ERROR, "所选床位已经被占用");
    		alert.show();
    		return ;
    	}
    	for(Building building : Database.getInstance().getBuildings()) {
    		for(Level level : building.getLevels()) {
    			for(Room room : level.getRooms()) {
    				for(Bed bed : room.getBeds()) if(bed.getOwner() != null){
    					if(bed.getOwner().equals(checkInPatientChoiceBox.getSelectionModel().getSelectedItem())) {
    						Alert alert = new Alert(AlertType.ERROR, "该病人已入院，请重新选择。");
    			    		alert.show();
    			    		return ;
    					}
    				}
    			}
    		}
    	}
    	bedManBedChoice.getSelectionModel().getSelectedItem().setOwner(checkInPatientChoiceBox.getSelectionModel().getSelectedItem());
    	Database.getInstance().getCheckInInfos().add(new CheckInInfo(bedManBedChoice.getSelectionModel().getSelectedItem()
    			, checkInPatientChoiceBox.getSelectionModel().getSelectedItem()
    			, checkInDatePicker.getValue()
    			, checkOutDatePicker.getValue()
    			, true));
    	bedManInfoLabel.setText("");
    	bedManagementInit();
		Database.saveToFile();
    	
    }
    //找到空闲床位
    @FXML
    private void findAvailableBedButtonFired() {
    	for(Building building : Database.getInstance().getBuildings()) {
    		for(Level level : building.getLevels()) {
    			for(Room room : level.getRooms()) {
    				for(Bed bed : room.getBeds()) {
    					if(bed.getOwner() == null) {
    						setSelectedBed(bed);
    						return ;
    					}
    				}
    			}
    		}
    	}
    	Alert alert = new Alert(AlertType.ERROR, "暂无可用床位");
    	alert.show();
    	return ;
    }
    //办理退房
    @FXML
    private void checkOutButtonFired() {
    	if(bedTableView.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未在表格中选择记录");
    		alert.show();
    		return ;
    	}
    	CheckInInfo checkInInfo = bedTableView.getSelectionModel().getSelectedItem();
    	checkInInfo.setInBed(false);
    	checkInInfo.getBed().setOwner(null);
    	bedManagementInit();
		Database.saveToFile();
    	return ;
    }
    //调换床位
    @FXML
    private void swapBedButtonFired() {
    	if(bedTableView.getSelectionModel().getSelectedItems().size() != 2) {
    		Alert alert = new Alert(AlertType.ERROR, "选择的床位不正确（需要恰好两个选择）");
    		alert.show();
    		return ;
    	}
    	if(bedTableView.getSelectionModel().getSelectedItems().get(0).isInBed() == false) {
    		Alert alert = new Alert(AlertType.ERROR, "第一个记录已经失效（已出院记录）");
    		alert.show();
    		return ;
    	}
    	if(bedTableView.getSelectionModel().getSelectedItems().get(1).isInBed() == false) {
    		Alert alert = new Alert(AlertType.ERROR, "第二个记录已经失效（已出院记录）");
    		alert.show();
    		return ;
    	}
    	Bed bed0 = bedTableView.getSelectionModel().getSelectedItems().get(0).getBed();
    	Bed bed1 = bedTableView.getSelectionModel().getSelectedItems().get(1).getBed();
    	Patient patient0 = bedTableView.getSelectionModel().getSelectedItems().get(0).getPatient();
    	Patient patient1 = bedTableView.getSelectionModel().getSelectedItems().get(1).getPatient();
    	bed0.setOwner(patient1);
    	bed1.setOwner(patient0);
    	bedTableView.getSelectionModel().getSelectedItems().get(0).setBed(bed1);
    	bedTableView.getSelectionModel().getSelectedItems().get(1).setBed(bed0);
    	bedManagementInit();
		Database.saveToFile();
    	return ;
    }
    //初始化床位管理系统
    @FXML
    private void bedManagementInit() {
    	//------------------------------------------------------还原所有栏目-----------------------------------------------------------
    	bedManInfoLabel.setText("");
    	checkInInfoList.clear();
    	bedTableView.getColumns().clear();
    	bedTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	refreshcheckInPatientChoiceBox();
		//------------------------------------------------------表格展示-----------------------------------------------------------
    	//[start]
    	checkInInfoList.setAll(Database.getInstance().getCheckInInfos());
		bedTableView.setItems(checkInInfoList);
		TableColumn<CheckInInfo, String> positionColumn = new TableColumn<CheckInInfo, String>("位置");
		TableColumn<CheckInInfo, String> checkInTimeColumn = new TableColumn<CheckInInfo, String>("入住开始时间");
		TableColumn<CheckInInfo, String> checkOutTimeColumn = new TableColumn<CheckInInfo, String>("入住结束时间");
		TableColumn<CheckInInfo, String> stateColumn = new TableColumn<CheckInInfo, String>("状态");
		TableColumn<CheckInInfo, String> nameColumn = new TableColumn<CheckInInfo, String>("姓名");
		TableColumn<CheckInInfo, String> sexColumn = new TableColumn<CheckInInfo, String>("性别");
		TableColumn<CheckInInfo, String> ageColumn = new TableColumn<CheckInInfo, String>("年龄");
		positionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				Bed bed = param.getValue().getBed();
				Room room = bed.getFather();
				Level level = room.getFather();
				Building building = level.getFather();
				str.setValue(building.getName() + "->"
						+ level.getName() + "->"
						+ room.getName() + "->"
						+ bed.getName());
				return str;
			}
		});
		checkInTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				LocalDate date = param.getValue().getCheckInTime();
				str.setValue(date.toString());
				return str;
			}
		});
		checkOutTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				LocalDate date = param.getValue().getCheckOutTime();
				str.setValue(date.toString());
				return str;
			}
		});
		stateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				return new SimpleStringProperty(param.getValue().isInBed() ? "入住中" : "已出院");
			}
		});
		nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				return new SimpleStringProperty(param.getValue().getPatient().getName());
			}
		});
		sexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				return new SimpleStringProperty(param.getValue().getPatient().getSex() ? "男" : "女");
			}
		});
		ageColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckInInfo,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<CheckInInfo, String> param) {
				// TODO Auto-generated method stub
				return new SimpleStringProperty(String.valueOf(param.getValue().getPatient().getAge()));
			}
		});
		
		positionColumn.setPrefWidth(250);
		checkInTimeColumn.setPrefWidth(130);
		checkOutTimeColumn.setPrefWidth(130);
		positionColumn.setResizable(false);
		checkInTimeColumn.setResizable(false);
		checkOutTimeColumn.setResizable(false);
		stateColumn.setResizable(false);
		nameColumn.setResizable(false);
		sexColumn.setResizable(false);
		ageColumn.setResizable(false);
		bedTableView.getColumns().add(positionColumn);
		bedTableView.getColumns().add(checkInTimeColumn);
		bedTableView.getColumns().add(checkOutTimeColumn);
		bedTableView.getColumns().add(stateColumn);
		bedTableView.getColumns().add(nameColumn);
		bedTableView.getColumns().add(sexColumn);
		bedTableView.getColumns().add(ageColumn);
		//[end]
		//---------------------------------------------------楼层选择框监听器------------------------------------------------------
		//[start]
		bedManBuildingChoice.getItems().setAll(Database.getInstance().getBuildings());
		bedManLevelChoice.setDisable(true);
		bedManRoomChoice.setDisable(true);
		bedManBedChoice.setDisable(true);
		bedManBuildingChoice.getSelectionModel().clearSelection();
		bedManLevelChoice.getSelectionModel().clearSelection();
		bedManRoomChoice.getSelectionModel().clearSelection();
		bedManBedChoice.getSelectionModel().clearSelection();
		bedManBuildingChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Building>() {

			@Override
			public void changed(ObservableValue<? extends Building> observable, Building oldValue, Building newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == oldValue) return ;
				bedManLevelChoice.getSelectionModel().clearSelection();
				bedManRoomChoice.getSelectionModel().clearSelection();
				bedManBedChoice.getSelectionModel().clearSelection();
				bedManLevelChoice.setDisable(false);
				bedManRoomChoice.setDisable(true);
				bedManBedChoice.setDisable(true);
				bedManInfoLabel.setText("");
				bedManLevelChoice.getItems().setAll(newValue.getLevels());
				
			}
		});
		bedManLevelChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Level>() {

			@Override
			public void changed(ObservableValue<? extends Level> observable, Level oldValue, Level newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == oldValue) return ;
				bedManRoomChoice.getSelectionModel().clearSelection();
				bedManBedChoice.getSelectionModel().clearSelection();
				bedManRoomChoice.setDisable(false);
				bedManBedChoice.setDisable(true);
				bedManInfoLabel.setText("");
				bedManRoomChoice.getItems().setAll(newValue.getRooms());
				
			}
		});
		bedManRoomChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Room>() {

			@Override
			public void changed(ObservableValue<? extends Room> observable, Room oldValue, Room newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue == oldValue) return ;
				bedManBedChoice.getSelectionModel().clearSelection();
				bedManBedChoice.setDisable(false);
				bedManInfoLabel.setText("");
				bedManBedChoice.getItems().setAll(newValue.getBeds());
				
				
				
			}
		});
		bedManBedChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bed>() {

			@Override
			public void changed(ObservableValue<? extends Bed> observable, Bed oldValue, Bed newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
//				if(newValue == oldValue) return ;

				bedManInfoLabel.setText(newValue.getName() + (newValue.getOwner() == null ? "：空闲" : (" 占有者：" + newValue.getOwner().getName())));
				
			}


		});
		//[end]
		//入住人刷新
		refreshcheckInPatientChoiceBox();
    }
    //设置选择框里的床位
    private void setSelectedBed(Bed bed) {
    	Room room = bed.getFather();
    	Level level = room.getFather();
    	Building building = level.getFather();
    	bedManBuildingChoice.getSelectionModel().select(building);
    	bedManLevelChoice.getSelectionModel().select(level);
    	bedManRoomChoice.getSelectionModel().select(room);
    	bedManBedChoice.getSelectionModel().select(bed);
    	return ;
    }

    //刷新入住人choicebox
    void refreshcheckInPatientChoiceBox() {
    	ArrayList<Patient> tmp = new ArrayList<Patient>();
    	for(Patient patient : Database.getInstance().getPatients()) {
    		boolean f = false;
    		for(Building building : Database.getInstance().getBuildings()) {
        		for(Level level : building.getLevels())  {
        			for(Room room : level.getRooms())  {
        				for(Bed bed : room.getBeds())  {
        					if(bed.getOwner() != null)
        						f |= (bed.getOwner().equals(patient));
        				}
        			}
        		}
        	}
    		if(!f) tmp.add(patient);
    	}
    	checkInPatientChoiceBox.getItems().setAll(tmp);
    	//设置展示内容
    	checkInPatientChoiceBox.setConverter(new StringConverter<Patient>() {
			
			@Override
			public String toString(Patient object) {
				// TODO Auto-generated method stub
				return object.getName() + "-" + object.getId();
			}
			
			@Override
			public Patient fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
    	checkInPatientChoiceBox.getSelectionModel().clearSelection();
    	checkInDatePicker.setValue(null);
    	checkOutDatePicker.setValue(null);
    	bedManBuildingChoice.getSelectionModel().clearSelection();
		bedManLevelChoice.getSelectionModel().clearSelection();
		bedManRoomChoice.getSelectionModel().clearSelection();
		bedManBedChoice.getSelectionModel().clearSelection();
		bedManLevelChoice.setDisable(true);
		bedManRoomChoice.setDisable(true);
		bedManBedChoice.setDisable(true);
    	
    }
//[end]
//----------------------------------------------------稀有设备管理------------------------------------------------------------------
//[start]

//	private ObservableList<CheckInInfo> checkInInfoList = FXCollections.observableArrayList();
//    private TableView<CheckInInfo> bedTableView;
    private ObservableList<Room> rareRoomList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<Patient> rareAplicationPatientChoice;
    @FXML
    private TextField rareApplicationTimeField;
    @FXML
    private TextField rareSearchField;
    @FXML
    private TableView<Room> rareTableView;
    
    @FXML
    private void rareSearchFieldKeyPressed(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER) {
    		rareRoomSearchButtonFired();
    	}
    }
    @FXML
    private void rareRoomSearchButtonFired() {
    	if(rareSearchField.getText().equals("")) {
    		Patient patient = rareAplicationPatientChoice.getSelectionModel().getSelectedItem();
    		String s = rareApplicationTimeField.getText();
    		rareManagementInit();
    		rareAplicationPatientChoice.getSelectionModel().select(patient);
    		rareApplicationTimeField.setText(s);
    		return ;
    	}
    	ArrayList<Room> rooms = new ArrayList<Room>();
    	for(Building buidling : Database.getInstance().getBuildings()) {
    		for(Level level : buidling.getLevels()) {
    			for(Room room : level.getRooms()) {
    				room.refreshApplicationTime();
    				if( room.isRareRoom() && room.getRareType() == room.getTypeByChinese(rareSearchField.getText()) ) {
    					rooms.add(room);
    				}
    			}
    		}
    	}
    	rareRoomList.setAll(rooms);
    }
    @FXML
    private void rareRoomRefresh() {
    	rareManagementInit();
    }
    @FXML
    private void rareRoomApplicationButtonFired() {
    	if(rareTableView.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请选择申请的房间。");
    		alert.show();
    		return ;
    	}
    	if(rareAplicationPatientChoice.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.ERROR, "请选择申请人 。");
    		alert.show();
    		return ;
    	}
    	if(rareApplicationTimeField.getText().equals("")) {
    		Alert alert = new Alert(AlertType.ERROR, "请输入申请时间（小时）。");
    		alert.show();
    		return ;
    	}
    	Room room = rareTableView.getSelectionModel().getSelectedItem();
    	Patient patient = rareAplicationPatientChoice.getSelectionModel().getSelectedItem();
    	long time = Long.parseLong(rareApplicationTimeField.getText()) * RareRoomApplication.TIMESCALE;
//    	room.getApplicationList().add(e);
//    	if(room)
    	room.refreshApplicationTime();
    	if(room.getApplicationList().size() >= room.getMaxCapacity()) {
    		long mintime = Long.MAX_VALUE;
    		long nowtime = new Date().getTime();
    		for(RareRoomApplication application : room.getApplicationList()) 
    			mintime = Math.min(mintime, application.getStartTime().getTime() + application.getDurationTime());
    		String s = "";
    		if(mintime - nowtime < 60 * 1000) s = String.valueOf((mintime - nowtime) / 1000) + "秒";
    		else if(mintime - nowtime < 60 * 60 * 1000) s = String.valueOf((mintime - nowtime) / 60 / 1000) + "分" 
    													+  String.valueOf((mintime - nowtime) / 1000 % 60) + "秒";

    		else if(mintime - nowtime < 60 * 60 * 60 * 1000) s = String.valueOf((mintime - nowtime) / 60 / 60 / 1000) + "小时" 
    													+ String.valueOf((mintime - nowtime) % (60 * 60 * 1000) / 60 / 1000) + "分" 
    													+  String.valueOf((mintime - nowtime) % (60 * 1000) / 1000 % 60) + "秒";
    		Alert alert = new Alert(AlertType.ERROR, "当前房间申请数已满，还需" + s);
    		alert.show();
    		return ;
    	}
    	room.getApplicationList().add(new RareRoomApplication(new Date(), time, patient, room));
    	room.refreshApplicationTime();
    	rareManagementInit();
		Database.saveToFile();
    }
    //稀有设备管理界面初始化
    @FXML
    private void rareManagementInit() {
    	//初始化表格
    	//[start]
    	rareSearchField.setText("");
    	rareApplicationTimeField.setText("");
    	rareRoomList.clear();
    	for(Building buidling : Database.getInstance().getBuildings()) {
    		for(Level level : buidling.getLevels()) {
    			for(Room room : level.getRooms()) {
    				room.refreshApplicationTime();
    				if(room.isRareRoom()) {
    					rareRoomList.add(room);
    				}
    			}
    		}
    	}
    	rareTableView.setItems(rareRoomList);
    	rareTableView.getColumns().clear();
    	TableColumn<Room, String> roomTypeColumn = new TableColumn<Room, String>("房间种类");
    	TableColumn<Room, String> roomPositionColumn = new TableColumn<Room, String>("位置");
    	TableColumn<Room, String> maxCapacityColumn = new TableColumn<Room, String>("最大容纳人数");
    	TableColumn<Room, String> resCapacityColumn = new TableColumn<Room, String>("剩余容纳人数");
    	roomTypeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Room, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getType(param.getValue().getRareType()));
				return str;
			}
		});
    	roomPositionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Room, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				Room room = param.getValue();
				Level level = room.getFather();
				Building building = level.getFather();
				str.setValue(building.getName() + "->"
						+ level.getName() + "->"
						+ room.getName());
				return str;
			}
		});
    	maxCapacityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Room, String> param) {
				// TODO Auto-generated method stub
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getMaxCapacity()));
			}
		});
    	resCapacityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Room, String> param) {
				// TODO Auto-generated method stub
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getResCapacity()));
			}
		});
    	rareTableView.getColumns().add(roomTypeColumn);
    	rareTableView.getColumns().add(roomPositionColumn);
    	rareTableView.getColumns().add(maxCapacityColumn);
    	rareTableView.getColumns().add(resCapacityColumn);
    	rareTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	//[end]
 
    	//初始化choicebox
    	rareAplicationPatientChoice.getItems().setAll(Database.getInstance().getPatients());
    	//输入框监听器
//    	rareAplicationPatientChoice.set
    	rareApplicationTimeField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(!Pattern.matches("[0-9]{0,6}", newValue)) {
					rareApplicationTimeField.setText(oldValue);
				}
			}
		});	
    }
//[end]
//------------------------------------------------------模板管理--------------------------------------------------------------------
//[start]
    //[start]
    ObservableList<Template> templateManTemplateList = FXCollections.observableArrayList();
    ObservableList<Problem> templateManProblemList = FXCollections.observableArrayList();
    
    @FXML
    private TextField templateManTemplateSearchField;
    @FXML
    private TableView<Template> templateManTemplateTableView;
    @FXML
    private TableView<Problem> templateManProblemTableView;
    @FXML
    private ChoiceBox<Problem> templateManProblemChoiceBox;
    @FXML
    private Label templateManProblemLabel;
    @FXML
    private Label templateManAnsLabel;
    @FXML
    private Label templateManTypeLabel;
    @FXML
    private Label templateManChoice0Lable;
    @FXML
    private Label templateManChoice1Label;
    @FXML
    private Label templateManChoice2Label;
    //[end]
    //删除问题
    @FXML
    private void templateManRemoveProblemButtonFired() {
    	Template template = templateManTemplateTableView.getSelectionModel().getSelectedItem();
    	if(template == null) return ;
    	Problem problem = templateManProblemTableView.getSelectionModel().getSelectedItem();
    	if(problem == null) return ;
    	template.getProblems().remove(problem);
    	templateManagementInit();
    	templateManTemplateTableView.getSelectionModel().select(template);
		Database.saveToFile();
    }
    //添加问题
    @FXML
    private void templateManAddProblemButtonFired() {
    	Template template = templateManTemplateTableView.getSelectionModel().getSelectedItem();
    	if(template == null) return ;
    	Problem problem = templateManProblemChoiceBox.getSelectionModel().getSelectedItem();
    	if(problem == null) return ;
    	template.getProblems().add(problem);
    	templateManagementInit();
    	templateManTemplateTableView.getSelectionModel().select(template);
		Database.saveToFile();
    }
    //搜索模板
    @FXML
    private void templateManSearchTemplateButtonFired() {
    	String name = templateManTemplateSearchField.getText();
    	if(name.equals("")) {
    		templateManagementInit();
    		return ;
    	}
    	templateManTemplateList.clear();
    	for(Template template : Database.getInstance().getTemplates()) {
    		if(template.getName().contains(name)) {
    			templateManTemplateList.add(template);
    		}
    	}
    }
    //删除模板
    @FXML
    private void templateManRemoveTemplateButtonFired() {
    	Template template = templateManTemplateTableView.getSelectionModel().getSelectedItem();
    	if(template == null) return ;
    	Database.getInstance().getTemplates().remove(template);
    	templateManagementInit();
		Database.saveToFile();
    }
    //添加模板
    @FXML
    private void templateManAddTemplateButtonFired() {
    	Database.getInstance().getTemplates().add(Template.newTemplate());
    	templateManagementInit();
		Database.saveToFile();
    }
    @FXML
    void templateManagementInit() {
    	templateManTemplateList.clear();
    	templateManProblemList.clear();
    	templateManTemplateTableView.getColumns().clear();
    	templateManProblemTableView.getColumns().clear();
    	templateManTemplateSearchField.setText("");
		templateManProblemLabel.setText("");
		templateManAnsLabel.setText("");
		templateManTypeLabel.setText("");
		templateManChoice0Lable.setText("");
		templateManChoice1Label.setText("");
		templateManChoice2Label.setText("");
    	templateManProblemChoiceBox.getSelectionModel().clearSelection();
    	
    	templateManTemplateTableView.setEditable(true);
    	for(Template template : Database.getInstance().getTemplates()) {
    		for(int i = 0; i < template.getProblems().size(); i++) {
    			Problem problem = template.getProblems().get(i);
    			if(!Database.getInstance().getProblems().contains(problem)) {
    				template.getProblems().remove(problem);
    				i--;
    			}
    		}
    	}
    	//设置模板表格的显示
    	templateManTemplateList.setAll(Database.getInstance().getTemplates());
    	templateManTemplateTableView.setItems(templateManTemplateList);
    	TableColumn<Template, Integer> templateManTemplateIdColumn = new TableColumn<Template, Integer>("ID");
    	TableColumn<Template, String> templateManTemplateNameColumn = new TableColumn<Template, String>("名字");
    	TableColumn<Template, String> templateManTemplateTypeColumn = new TableColumn<Template, String>("种类");
    	templateManTemplateTableView.getColumns().add(templateManTemplateIdColumn);
    	templateManTemplateTableView.getColumns().add(templateManTemplateNameColumn);
    	templateManTemplateTableView.getColumns().add(templateManTemplateTypeColumn);
    	templateManTemplateIdColumn.setCellValueFactory(new PropertyValueFactory<Template, Integer>("id"));
    	templateManTemplateNameColumn.setCellValueFactory(new PropertyValueFactory<Template, String>("name"));
    	templateManTemplateTypeColumn.setCellValueFactory(new PropertyValueFactory<Template, String>("type"));
    	templateManTemplateNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    	templateManTemplateIdColumn.setPrefWidth(templateManTemplateTableView.getWidth() * 0.1);
    	templateManTemplateNameColumn.setPrefWidth(templateManTemplateTableView.getWidth() * 0.795);
    	templateManTemplateTypeColumn.setPrefWidth(templateManTemplateTableView.getWidth() * 0.1);
    	templateManTemplateNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Template,String>>() {
			
			@Override
			public void handle(CellEditEvent<Template, String> event) {
				// TODO Auto-generated method stub
				Template template = event.getRowValue();
				template.setName(event.getNewValue());
			}
		});
    	
    	//设置问题表格的显示
    	templateManProblemTableView.setItems(templateManProblemList);
    	TableColumn<Problem, Long> problemManIdColumn = new TableColumn<Problem, Long>("ID");
    	TableColumn<Problem, String> problemManDescriptionColumn = new TableColumn<Problem, String>("题目描述");
    	TableColumn<Problem, String> problemManTypeColumn = new TableColumn<Problem, String>("种类");
    	TableColumn<Problem, String> problemManChoice0Column = new TableColumn<Problem, String>("选项1");
    	TableColumn<Problem, String> problemManChoice1Column = new TableColumn<Problem, String>("选项2");
    	TableColumn<Problem, String> problemManChoice2Column = new TableColumn<Problem, String>("选项3");
    	problemManIdColumn.setCellValueFactory(new PropertyValueFactory<Problem, Long>("id"));
    	problemManDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Problem, String>("description"));
    	problemManTypeColumn.setCellValueFactory(new PropertyValueFactory<Problem, String>("type"));
    	problemManChoice0Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Problem,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Problem, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getChoice().get(0));
				return str;
			}
		});
    	problemManChoice1Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Problem,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Problem, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getChoice().get(1));
				return str;
			}
		});
    	problemManChoice2Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Problem,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Problem, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getChoice().get(2));
				return str;
			}
		});
    	
    	templateManProblemTableView.getColumns().add(problemManIdColumn);
    	templateManProblemTableView.getColumns().add(problemManDescriptionColumn);
    	templateManProblemTableView.getColumns().add(problemManChoice0Column);
    	templateManProblemTableView.getColumns().add(problemManChoice1Column);
    	templateManProblemTableView.getColumns().add(problemManChoice2Column);
    	templateManProblemTableView.getColumns().add(problemManTypeColumn);
    	for(TableColumn<Problem, ?> c : templateManProblemTableView.getColumns()) {
    		c.setResizable(false);
    	}
    	problemManIdColumn.setPrefWidth(templateManProblemTableView.getWidth() * 0.1);
    	problemManDescriptionColumn.setPrefWidth(templateManProblemTableView.getWidth() * 0.2);
    	problemManChoice0Column.setPrefWidth(templateManProblemTableView.getWidth() * 0.195);
    	problemManChoice1Column.setPrefWidth(templateManProblemTableView.getWidth() * 0.2);
    	problemManChoice2Column.setPrefWidth(templateManProblemTableView.getWidth() * 0.2);
    	problemManTypeColumn.setPrefWidth(templateManProblemTableView.getWidth() * 0.1);
    	templateManTemplateTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Template>() {

			@Override
			public void changed(ObservableValue<? extends Template> observable, Template oldValue, Template newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) {
					templateManProblemList.clear();
					return ;
				} else {
					templateManProblemList.setAll(newValue.getProblems());
				}
				
			}
		});
    	//初始化选择框
    	ObservableList<Problem> tempList = FXCollections.observableArrayList();
    	tempList.setAll(Database.getInstance().getProblems());
    	templateManProblemChoiceBox.setItems(tempList);
    	templateManProblemChoiceBox.setConverter(new StringConverter<Problem>() {
			
			@Override
			public String toString(Problem object) {
				// TODO Auto-generated method stub
				return "问题id-" + String.valueOf(object.getId()) + "-问题描述-" + (object.getDescription().length() > 10 ? object.getDescription().substring(0, 10) : object.getDescription());
			}
			
			@Override
			public Problem fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
    	templateManProblemChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Problem>() {

			@Override
			public void changed(ObservableValue<? extends Problem> observable, Problem oldValue, Problem newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) {
					templateManProblemLabel.setText("");
					templateManAnsLabel.setText("");
					templateManTypeLabel.setText("");
					templateManChoice0Lable.setText("");
					templateManChoice1Label.setText("");
					templateManChoice2Label.setText("");
				} else {
					templateManProblemLabel.setText(newValue.getDescription());
					templateManAnsLabel.setText(String.valueOf(newValue.getAns() + 1));
					templateManTypeLabel.setText(newValue.getType());
					templateManChoice0Lable.setText(newValue.getChoice().get(0));
					templateManChoice1Label.setText(newValue.getChoice().get(1));
					templateManChoice2Label.setText(newValue.getChoice().get(2));
					
				}
			}
		});
    	
    	
    }
//[end]
//------------------------------------------------------问题管理--------------------------------------------------------------------
//[start]
    private ObservableList<Problem> problemManProblemList = FXCollections.observableArrayList();
    @FXML
    private TableView<Problem> problemTableView;
    @FXML
    private TextField problemManSearchField;
    @FXML
    private TextField problemManDescriptionField;
    @FXML
    private TextField problemManChoice0;
    @FXML
    private TextField problemManChoice1;
    @FXML
    private TextField problemManChoice2;
    @FXML
    private ChoiceBox<Integer> problemManAnsChoiceBox;
    @FXML
    private ChoiceBox<String> problemManTypeChoiceBox;
    //删除键按下
    @FXML
    private void problemManRemoveButtonFired() {
    	if(problemTableView.getSelectionModel().getSelectedItem() == null) return ;
    	Problem problem = problemTableView.getSelectionModel().getSelectedItem();
    	Database.getInstance().getProblems().remove(problem);
    	problemManagementInit();
		Database.saveToFile();
    }
    //搜索键按下
    @FXML
    private void problemManSearchButtonFired() {
    	String text = problemManSearchField.getText();
    	if(Pattern.matches(" *", text)) {
    		problemManagementInit();
    		return ;
    	}
    	problemManProblemList.clear();
    	for(Problem problem : Database.getInstance().getProblems()) {
    		if(problem.getDescription().contains(text)) {
    			problemManProblemList.add(problem);
    		}
    	}
    }
    //新增键按下
    @FXML
    private void problemManAddButtonFired() {
    	Database.getInstance().getProblems().add(Problem.newProblem());
    	problemManagementInit();
		Database.saveToFile();
    }
    //确认键按下
    @FXML
    private void problemManConfirmButtonFired() {
    	Problem problem = problemTableView.getSelectionModel().getSelectedItem();
    	if(problem == null) return ;
    	problem.setAns(problemManAnsChoiceBox.getSelectionModel().getSelectedItem());
    	problem.setType(problemManTypeChoiceBox.getSelectionModel().getSelectedItem());
    	problem.setDescription(problemManDescriptionField.getText());
    	problem.getChoice().set(0, problemManChoice0.getText());
    	problem.getChoice().set(1, problemManChoice1.getText());
    	problem.getChoice().set(2, problemManChoice2.getText());
    	problemManagementInit();
		Database.saveToFile();
    	
    }
    
    //初始化问题管理界面
    @FXML
    private void problemManagementInit() {
    	//清空重置
    	problemTableView.getColumns().clear();
    	problemManProblemList.clear();

		problemManAnsChoiceBox.getItems().clear();
		problemManTypeChoiceBox.getItems().clear();
    	problemManDescriptionField.setText("");
    	problemManChoice0.setText("");
    	problemManChoice1.setText("");
    	problemManChoice2.setText("");
    	problemManSearchField.setText("");

    	problemTableView.setItems(problemManProblemList);
    	problemManProblemList.setAll(Database.getInstance().getProblems());
    	TableColumn<Problem, Long> problemManIdColumn = new TableColumn<Problem, Long>("ID");
    	TableColumn<Problem, String> problemManDescriptionColumn = new TableColumn<Problem, String>("题目描述");
    	TableColumn<Problem, String> problemManTypeColumn = new TableColumn<Problem, String>("种类");
    	TableColumn<Problem, String> problemManChoice0Column = new TableColumn<Problem, String>("选项1");
    	TableColumn<Problem, String> problemManChoice1Column = new TableColumn<Problem, String>("选项2");
    	TableColumn<Problem, String> problemManChoice2Column = new TableColumn<Problem, String>("选项3");
    	problemManIdColumn.setCellValueFactory(new PropertyValueFactory<Problem, Long>("id"));
    	problemManDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Problem, String>("description"));
    	problemManTypeColumn.setCellValueFactory(new PropertyValueFactory<Problem, String>("type"));
    	
    	problemManChoice0Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Problem,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Problem, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getChoice().get(0));
				return str;
			}
		});
    	problemManChoice1Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Problem,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Problem, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getChoice().get(1));
				return str;
			}
		});
    	problemManChoice2Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Problem,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Problem, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty str = new SimpleStringProperty();
				str.setValue(param.getValue().getChoice().get(2));
				return str;
			}
		});
    	
    	problemTableView.getColumns().add(problemManIdColumn);
    	problemTableView.getColumns().add(problemManDescriptionColumn);
    	problemTableView.getColumns().add(problemManChoice0Column);
    	problemTableView.getColumns().add(problemManChoice1Column);
    	problemTableView.getColumns().add(problemManChoice2Column);
    	problemTableView.getColumns().add(problemManTypeColumn);
    	for(TableColumn<Problem, ?> c : problemTableView.getColumns()) {
    		c.setResizable(false);
    	}
    	problemManIdColumn.setPrefWidth(problemTableView.getWidth() * 0.1);
    	problemManDescriptionColumn.setPrefWidth(problemTableView.getWidth() * 0.2);
    	problemManChoice0Column.setPrefWidth(problemTableView.getWidth() * 0.2);
    	problemManChoice1Column.setPrefWidth(problemTableView.getWidth() * 0.2);
    	problemManChoice2Column.setPrefWidth(problemTableView.getWidth() * 0.2);
    	problemManTypeColumn.setPrefWidth(problemTableView.getWidth() * 0.1);
    	
    	

    	problemManAnsChoiceBox.converterProperty().set(new StringConverter<Integer>() {

			@Override
			public Integer fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Integer object) {
				// TODO Auto-generated method stub
				return String.valueOf(object + 1);
			}
    		
		});
    	problemTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Problem>() {

			@Override
			public void changed(ObservableValue<? extends Problem> observable, Problem oldValue, Problem newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) {
					problemManAnsChoiceBox.getItems().clear();
					problemManTypeChoiceBox.getItems().clear();
			    	problemManDescriptionField.setText("");
			    	problemManChoice0.setText("");
			    	problemManChoice1.setText("");
			    	problemManChoice2.setText("");
				} else {
					problemManAnsChoiceBox.getItems().setAll(0, 1, 2);
					problemManAnsChoiceBox.getSelectionModel().select(newValue.getAns());
					problemManTypeChoiceBox.getItems().setAll(Problem.TYPES);
					problemManTypeChoiceBox.getSelectionModel().select(newValue.getType());
			    	problemManDescriptionField.setText(newValue.getDescription());
			    	problemManChoice0.setText(newValue.getChoice().get(0));
			    	problemManChoice1.setText(newValue.getChoice().get(1));
			    	problemManChoice2.setText(newValue.getChoice().get(2));
			    	
				}
				
			}
		});
    }
    
//[end]
//------------------------------------------------------楼宇管理--------------------------------------------------------------------
//[start]
    //[start]

    @FXML
    private TextField maxCapacityField;
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
    private VBox bedModifier;
    @FXML
    private TextField bedField;
    
    @FXML
    private Text roomInfoField;
    
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
		Database.saveToFile();
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
    		if(result.get() != ButtonType.OK) return ;
			Database.getInstance().getBuildings().remove(buildingList.getSelectionModel().getSelectedItem());
    		buildingList.getItems().setAll(Database.getInstance().getBuildings());
        	levelList.getItems().clear();
    		roomList.getItems().clear();
    		bedList.getItems().clear();
    	}
		Database.saveToFile();
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
    		father.getLevels().add(new Level(name, father));
    		levelList.getItems().setAll(father.getLevels());
    		levelField.setText("");
    	}
		Database.saveToFile();
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
		Database.saveToFile();
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
    	} else if(isRareChoice.getSelectionModel().getSelectedItem().equals("是") && maxCapacityField.getText().equals("")) {
    		Alert alert = new Alert(AlertType.ERROR, "请输入最大容量限制");
    		alert.show();
    	}else {
    		String name = roomField.getText();
    		for(Room room : father.getRooms()) {
    			if(room.getName().equals(name)) {
    				Alert alert = new Alert(AlertType.ERROR, "已存在同名房间");
    	    		alert.show();
    	    		return ;
    			}
    		}
    		if(isRareChoice.getSelectionModel().getSelectedItem().equals("是")) {
        		int capacity = Integer.parseInt(maxCapacityField.getText());
    			father.getRooms().add(new Room(roomField.getText(), true
    					, Room.getTypeByChinese(rareTypeChoice.getSelectionModel().getSelectedItem()), father
    					, capacity, capacity));
    		} else {
    			father.getRooms().add(new Room(roomField.getText(), false, father));
    		}
    		roomList.getItems().setAll(father.getRooms());
    		roomField.setText("");
    		maxCapacityField.setText("");
    		isRareChoice.getSelectionModel().clearSelection();
    		rareTypeChoice.getSelectionModel().clearSelection();
			Database.saveToFile();
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
				Database.saveToFile();
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
    	} else if(father.isRareRoom()) {
    		Alert alert = new Alert(AlertType.ERROR, "稀有房间不能创建床位");
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
    		father.getBeds().add(new Bed(name, father));
    		bedList.getItems().setAll(father.getBeds());
    		bedField.setText("");
			Database.saveToFile();
    		
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
				Database.saveToFile();
    		}
    	}
    	
    }
    //楼宇管理初始化
    @FXML
    void buildingManagementInit() {
    	//清理数据
    	buildingList.getItems().clear();
    	levelList.getItems().clear();
    	roomList.getItems().clear();
    	bedList.getItems().clear();
    	buildingField.setText("");
    	levelField.setText("");
    	roomField.setText("");
    	isRareChoice.getItems().clear();
    	rareTypeChoice.getItems().clear();
    	maxCapacityField.setText("");
    	bedField.setText("");
    	roomInfoField.setText("");
    	buildingModifier.setVisible(false);
    	levelModifier.setVisible(false);
    	roomModifier.setVisible(false);
    	bedModifier.setVisible(false);
//    	buildingList.setItems(n);
    	//设置房间信息展示栏
    	roomInfoField.setText("");
    	//添加房间选项栏
    	isRareChoice.getItems().addAll("是", "否");
    	for(String string : Room.RARETYPES) {
    		rareTypeChoice.getItems().addAll(string);
    	}
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
					roomInfoField.setText("是否为稀有房间：" + (newValue.isRareRoom() ? "是" :"否"));
					if(newValue.isRareRoom()) {
						roomInfoField.setText(roomInfoField.getText() 
								+ "\n" + "房间种类：" + Room.getType(newValue.getRareType())
								+ "\n" + "最大容量：" + newValue.getMaxCapacity()
								+ "\n" + "剩余容量：" + newValue.getResCapacity());
						bedList.setDisable(true);
						bedList.getItems().clear();
					} else {
						bedList.getItems().setAll(newValue.getBeds());
						bedList.setDisable(false);
					}
				} else {
					roomInfoField.setText("");
				}
				
			}
		});
    	bedList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bed>() {

			@Override
			public void changed(ObservableValue<? extends Bed> observable, Bed oldValue, Bed newValue) {
				// TODO Auto-generated method stub
				if(newValue != null) {
					if(newValue.getOwner() != null)
						roomInfoField.setText(newValue.getName() + "，占有者：" + newValue.getOwner().toString());
					else roomInfoField.setText(newValue.getName() + "：当前空闲");
				} else {
					roomInfoField.setText("");
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
					maxCapacityField.setDisable(true);
				} else if(newValue.equals("是")){
					rareTypeChoice.setDisable(false);
					maxCapacityField.setDisable(false);
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
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused()) {
						buildingModifier.setVisible(false); 
						buildingField.setText("");
					}
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
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused()) {
						levelModifier.setVisible(false); 
			    		levelField.setText("");
					}
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
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused()) {
						roomModifier.setVisible(false); 

			    		roomField.setText("");
			    		maxCapacityField.setText("");
			    		isRareChoice.getSelectionModel().clearSelection();
			    		rareTypeChoice.getSelectionModel().clearSelection();
					}
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
					if(buildingList.isFocused() || levelList.isFocused() || roomList.isFocused() || bedList.isFocused()) {
						bedModifier.setVisible(false); 
						bedField.setText("");
					}
				}
				
			}
		});
    	maxCapacityField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(!Pattern.matches("[0-9]{0,3}", newValue))
						maxCapacityField.setText(oldValue);
				return ;
			}
		});
    }
    
//[end]
//------------------------------------------------------基本设置--------------------------------------------------------------------
//[start]
    
    @FXML
    private TabPane evaluateTabPane;
    @FXML
    private Tab templateTab;
    @FXML
    private void evaluateTabChoiced() {
    	templateManagementInit();
    	evaluateTabPane.getSelectionModel().select(templateTab);
    }
    @FXML
    private TabPane userTabPane;
    @FXML
    private Tab patientTab;
    @FXML
    private void userTabChoiced() {
    	patientManagementInit();
    	userTabPane.getSelectionModel().select(patientTab);
    }
    @FXML
    private Text loginLabel;
	Stage thisStage = null;
    //关闭窗口
    @FXML
    private void closeWindow() {
    	thisStage.hide();
    }
    //最小化窗口
    @FXML 
    private void minimizeWindow() { 
    	thisStage.setIconified(true);
    }
//[end]

    
    void setLengthLimit(TextField text, int length) {
    	text.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue.length() > length) text.setText(oldValue);
			}
		});
    }
    public void init() {
    	loginLabel.setText("");
    	if(Database.getInstance().getLoginUser() != null) {
    		loginLabel.setText("您好，" + Database.getInstance().getLoginUser().getName()
    				 + Database.getInstance().getLoginUser().getTitile());
    	}
    	thisStage = (Stage)menuShadow.getScene().getWindow();
    	EventHandler handler = new DragWindowHandler(thisStage);
    	
    	userTabPane.setOnMouseDragged(handler);
    	userTabPane.setOnMousePressed(handler);
    	
    	evaluateTabPane.setOnMouseDragged(handler);
    	evaluateTabPane.setOnMousePressed(handler);
    	
    	choinePane.setOnMouseDragged(handler);
    	choinePane.setOnMousePressed(handler);
		patientManagementInit();
		bedManagementInit();
		rareManagementInit();
		templateManagementInit();
		problemManagementInit();
		buildingManagementInit();
		//对所有文本框设置长度限制
		setLengthLimit(searchField, 20);
		setLengthLimit(rareApplicationTimeField, 20);
		setLengthLimit(rareSearchField, 20);
		setLengthLimit(templateManTemplateSearchField, 20);
		setLengthLimit(problemManSearchField, 20);
		setLengthLimit(problemManDescriptionField, 20);
		setLengthLimit(problemManChoice0, 20);
		setLengthLimit(problemManChoice1, 20);
		setLengthLimit(problemManChoice2, 20);
		setLengthLimit(buildingField, 20);
		setLengthLimit(levelField, 20);
		setLengthLimit(roomField, 20);
		setLengthLimit(maxCapacityField, 20);
		setLengthLimit(bedField, 20);

	}
}
