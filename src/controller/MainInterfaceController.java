package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
		nameColumn.setPrefWidth(150.0);
		nameColumn.setResizable(false);
		idColumn.setPrefWidth(198.0);
		idColumn.setResizable(false);
		ageColumn.setPrefWidth(50);
		ageColumn.setResizable(false);
		sexColumn.setPrefWidth(50);
		sexColumn.setResizable(false);
		phoneNumberColumn.setPrefWidth(150);
		phoneNumberColumn.setResizable(false);
		emergencyContactColumn.setPrefWidth(150);
		emergencyContactColumn.setResizable(false);
		emergencyPhoneNumberColumn.setPrefWidth(150);
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

}
