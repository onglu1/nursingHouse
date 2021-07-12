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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private TableView<Patient> patientTableView;
    
    //显示菜单
    @FXML
    void sidebarShow() {
//    	System.out.println(menuVBox.getTranslateX());
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
//    	System.out.println(event.getEventType());
    	if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
    		Node source = (Node)event.getSource();
    		source.setOpacity(0.5);
//    		menuShadow.setOpacity(0.5);
//        	choiceRec.setOpacity(0.5);
    	}
    }

    @FXML
    void shadowHide(MouseEvent event) {
//    	System.out.println(event.getEventType());
    	if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
    		Node source = (Node)event.getSource();
    		source.setOpacity(0);
//        	choiceRec.setOpacity(0.5);
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
    //新增病人
    @FXML
    void addPatient() throws Exception {
//    	System.out.println("qwq");
//    	Database.getInstance().getPatients().add(new Patient("李四", 18, true, "123456789", "李四的娘", "123456"));
    	AddPatient addPatient = new AddPatient();
    	Stage stage1 = new Stage();
    	addPatient.start(stage1);
    	stage1.setOnHidden(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				patientList.setAll(Database.getInstance().getPatients());
			}
		});
    	
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		patientList.setAll(Database.getInstance().getPatients());
		patientTableView.setItems(patientList);
		TableColumn<Patient, String> nameColumn = new TableColumn<Patient, String>("姓名");
		TableColumn<Patient, Integer> ageColumn = new TableColumn<Patient, Integer>("年龄");
		TableColumn<Patient, String> sexColumn = new TableColumn<Patient, String>("性别");
		TableColumn<Patient, String> phoneNumberColumn = new TableColumn<Patient, String>("联系电话");
		TableColumn<Patient, String> emergencyContactColumn = new TableColumn<Patient, String>("紧急联系人");
		TableColumn<Patient, String> emergencyPhoneNumberColumn = new TableColumn<Patient, String>("紧急联系电话");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
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
		patientTableView.getColumns().add(nameColumn);
		patientTableView.getColumns().add(ageColumn);
		patientTableView.getColumns().add(sexColumn);
		patientTableView.getColumns().add(phoneNumberColumn);
		patientTableView.getColumns().add(emergencyContactColumn);
		patientTableView.getColumns().add(emergencyPhoneNumberColumn);
		
	}

}
