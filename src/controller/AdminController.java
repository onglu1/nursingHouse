package controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Login;
import model.Database;
import model.UserAccount;
import utils.DragWindowHandler;

public class AdminController {
	public final String phonePattern = "^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\\d{8}$";
	public final String idPattern = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

//------------------------------------------------------界面管理--------------------------------------------------------------------
//[start]
	//[start]
	private Rectangle choiceRec;
    @FXML
    private Tab workerTab;

    @FXML
    private Rectangle nurseRec;

    @FXML
    private Rectangle screenCover;

    @FXML
    private TabPane choinePane;

    @FXML
    private AnchorPane menuButtonAnchorPane;

    @FXML
    private Circle menuShadow;

    @FXML
    private VBox menuVBox;


    @FXML
    private Rectangle workerRec;

    @FXML
    private Tab doctorTab;

    @FXML
    private Tab nurseTab;

    @FXML
    private Rectangle doctorRec;
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
    	if(choiceRec.equals(workerRec)) {
    		choinePane.getSelectionModel().select(workerTab);
    		sidebarHide();
    	} else if(choiceRec.equals(doctorRec)) {
    		choinePane.getSelectionModel().select(doctorTab);
    		sidebarHide();
    	} else if(choiceRec.equals(nurseRec)) {
    		choinePane.getSelectionModel().select(nurseTab);
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
//------------------------------------------------------医生管理--------------------------------------------------------------------
//[start]
    //[start]
    ObservableList<UserAccount> doctorManDoctorList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> doctorSearchChoiceBox;
    @FXML
    private TextField doctorManPhoneField;

    @FXML
    private DatePicker doctorManDatePicker;
    @FXML
    private TextField doctorManNameField;
    @FXML
    private TableView<UserAccount> doctorManTableView;
    @FXML
    private TextField doctorManSearchField;
    @FXML
    private TextField doctorManExpertiseField;
    @FXML
    private TextField doctorManIdField;
    @FXML
    private ChoiceBox<String> doctorManTitleChoiceBox;
    //[end]
    //新增医生
    @FXML
    void doctorManAddButtonFired() {
    	String userName = "";
    	String password = "";
    	TextInputDialog tid = new TextInputDialog("");
    	
    	tid.setTitle("用户名");
    	tid.setGraphic(new ImageView("/icon/user.png"));
    	tid.setHeaderText("请输入用户名");
    	
		try {
			userName = tid.showAndWait().get();
		} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
		if(Pattern.matches(" *", userName)) {
    		Alert alert = new Alert(AlertType.ERROR, "用户名不能为空或全为空格");
    		alert.show();
    		return ;
    	}
		for(UserAccount user : Database.getInstance().getUsers()) {
			if(user.getUserName().equals(userName)) {
				Alert alert = new Alert(AlertType.ERROR, "数据库中存在重复用户，请重新输入用户名！");
	    		alert.show();
	    		return ;
			}
		}
    	TextInputDialog tid1 = new TextInputDialog("");
    	tid1.setTitle("密码");
    	tid1.setGraphic(new ImageView("/icon/password.png"));
    	tid1.setHeaderText("请输入密码");
    	try {
    		password = tid1.showAndWait().get();
    	} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
    	if(Pattern.matches(" *", password)) {
    		Alert alert = new Alert(AlertType.ERROR, "密码不能为空或全为空格");
    		alert.show();
    		return ;
    	}
    	UserAccount ua = new UserAccount(userName, password);
    	ua.setTitile("医生");
    	Database.getInstance().getUsers().add(ua);
    	doctorInit();
		Database.saveToFile();
    	return ;
    }
    //查找医生
    @FXML
    void doctorManSearchButtonFired() {
    	String chinese = doctorSearchChoiceBox.getSelectionModel().getSelectedItem();
    	String text = doctorManSearchField.getText();
    	doctorManDoctorList.clear();
    	if(text.equals("")) {
    	   	for(UserAccount userAccount : Database.getInstance().getUsers()) {
        		if(userAccount.getTitile().equals("医生")) {
        			doctorManDoctorList.add(userAccount);
        		}
        	}
    		return ;
    	}
    	for(UserAccount user : Database.getInstance().getUsers()) if(user.getTitile().equals("医生")) {
    		if(user.getByChinese(chinese).contains(text)) {
    			doctorManDoctorList.add(user);
    		}
    	}
    	return ;
    }
    //修改医生
    @FXML
    void doctorManModifyButtonFired() {
    	UserAccount user = doctorManTableView.getSelectionModel().getSelectedItem();
    	if(user == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未选中医生。");
    		alert.showAndWait();
    		return ;
    	}
		if(!doctorManIdField.getText().equals("") && !Pattern.matches(idPattern, doctorManIdField.getText())) {
			Alert alert1 = new Alert(AlertType.ERROR, "请输入正确的身份证号码");
    		alert1.show();
    		return ;
		}
		if(!doctorManPhoneField.getText().equals("") && !Pattern.matches(phonePattern, doctorManPhoneField.getText())) {
			Alert alert1 = new Alert(AlertType.ERROR, "请输入正确的手机号");
    		alert1.show();
    		return ;
		}
		Alert alert = new Alert(AlertType.WARNING, "此操作不可恢复，请确认是否修改");
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	Optional<ButtonType> result = alert.showAndWait();
		if(result.get() != ButtonType.OK) {
			return ;
		} 
    	user.setName(doctorManNameField.getText());
    	user.setTitile(doctorManTitleChoiceBox.getSelectionModel().getSelectedItem());
    	user.setExpertise(doctorManExpertiseField.getText());
    	user.setIdNumber(doctorManIdField.getText());
    	user.setPhoneNumber(doctorManPhoneField.getText());
    	user.setBirth(doctorManDatePicker.getValue());
    	doctorInit();
    	doctorManTableView.getSelectionModel().select(user);
		Database.saveToFile();
    }
    //删除医生
    @FXML
    void doctorManRemoveButtonFired() {
    	UserAccount user = doctorManTableView.getSelectionModel().getSelectedItem();
    	if(user == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未选中医生。");
    		alert.showAndWait();
    		return ;
    	}

		Alert alert = new Alert(AlertType.WARNING, "此操作不可恢复，请确认是否删除");
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	alert.setContentText("该操作不可逆，请确认是否删除。");
    	Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			Database.getInstance().getUsers().remove(user);
    		doctorInit();
		} 
		Database.saveToFile();
    }
    @FXML
    void doctorInit() {
    	//清空数据
    	doctorManNameField.setText("");
    	doctorManTitleChoiceBox.getSelectionModel().clearSelection();
    	doctorManDatePicker.setValue(null);
    	doctorManExpertiseField.setText("");
    	doctorManIdField.setText("");
    	doctorManPhoneField.setText("");
    	//设置数据表    	
    	doctorManDoctorList.clear();
    	for(UserAccount userAccount : Database.getInstance().getUsers()) {
    		if(userAccount.getTitile().equals("医生")) {
    			doctorManDoctorList.add(userAccount);
    		}
    	}
    	doctorManTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	doctorManTableView.getColumns().clear();
    	doctorManTableView.setItems(doctorManDoctorList);
    	TableColumn<UserAccount, String> userNameColumn = new TableColumn<UserAccount, String>("登录名");
    	TableColumn<UserAccount, String> nameColumn = new TableColumn<UserAccount, String>("姓名");
    	TableColumn<UserAccount, LocalDate> birthColumn = new TableColumn<UserAccount, LocalDate>("出生日期");
    	TableColumn<UserAccount, LocalDate> titleColumn = new TableColumn<UserAccount, LocalDate>("职称");
    	TableColumn<UserAccount, LocalDate> expertiseColumn = new TableColumn<UserAccount, LocalDate>("专长");
    	TableColumn<UserAccount, LocalDate> idColumn = new TableColumn<UserAccount, LocalDate>("身份证");
    	userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
    	titleColumn.setCellValueFactory(new PropertyValueFactory<>("titile"));
    	expertiseColumn.setCellValueFactory(new PropertyValueFactory<>("expertise"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    	doctorManTableView.getColumns().add(userNameColumn);
    	doctorManTableView.getColumns().add(nameColumn);
    	doctorManTableView.getColumns().add(birthColumn);
    	doctorManTableView.getColumns().add(titleColumn);
    	doctorManTableView.getColumns().add(expertiseColumn);
    	doctorManTableView.getColumns().add(idColumn);
    	
    	userNameColumn.setPrefWidth(doctorManTableView.getWidth() * 0.2);
    	nameColumn.setPrefWidth(doctorManTableView.getWidth() * 0.145);
    	birthColumn.setPrefWidth(doctorManTableView.getWidth() * 0.15);
    	titleColumn.setPrefWidth(doctorManTableView.getWidth() * 0.1);
    	expertiseColumn.setPrefWidth(doctorManTableView.getWidth() * 0.1);
    	idColumn.setPrefWidth(doctorManTableView.getWidth() * 0.3);
    	
    	doctorManTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserAccount>() {

			@Override
			public void changed(ObservableValue<? extends UserAccount> observable, UserAccount oldValue,
					UserAccount newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				setDoctorInfo(newValue);
			}
		});
    	//设置选择框
    	doctorManTitleChoiceBox.getItems().setAll("医生", "护士", "护工");
    	doctorSearchChoiceBox.getItems().setAll("登录名", "姓名", "专长", "身份证");
    	doctorSearchChoiceBox.getSelectionModel().select("姓名");
    	TextField[] tfList = {doctorManNameField, doctorManExpertiseField, doctorManSearchField};
    	for(TextField tf : tfList) {
    		tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					if(newValue == null) return ;
					if(newValue.length() > 12)
						tf.setText(oldValue);
				}
			});
    	}
    	doctorManIdField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				
				if(!Pattern.matches("[0-9Xx]{0,18}", newValue)) {
					doctorManIdField.setText(oldValue);
				}
			}
		});
    	doctorManPhoneField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(!Pattern.matches("[0-9]{0,11}", newValue)) {
					doctorManPhoneField.setText(oldValue);
				}
			}
		});
    }
    void setDoctorInfo(UserAccount user) {
    	doctorManNameField.setText(user.getName());
    	doctorManTitleChoiceBox.getSelectionModel().select(user.getTitile());
    	doctorManDatePicker.setValue(user.getBirth());
    	doctorManExpertiseField.setText(user.getExpertise());
    	doctorManIdField.setText(user.getIdNumber());
    	doctorManPhoneField.setText(user.getPhoneNumber());
    }
//[end]
//------------------------------------------------------护士管理--------------------------------------------------------------------
  //[start]
    //[start]
    ObservableList<UserAccount> nurseManNurseList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> nurseSearchChoiceBox;
    @FXML
    private TextField nurseManPhoneField;

    @FXML
    private DatePicker nurseManDatePicker;
    @FXML
    private TextField nurseManNameField;
    @FXML
    private TableView<UserAccount> nurseManTableView;
    @FXML
    private TextField nurseManSearchField;
    @FXML
    private TextField nurseManExpertiseField;
    @FXML
    private TextField nurseManIdField;
    @FXML
    private ChoiceBox<String> nurseManTitleChoiceBox;
    //[end]
    //新增护士
    @FXML
    void nurseManAddButtonFired() {
    	String userName = "";
    	String password = "";
    	TextInputDialog tid = new TextInputDialog("");
    	
    	tid.setTitle("用户名");
    	tid.setGraphic(new ImageView("/icon/user.png"));
    	tid.setHeaderText("请输入用户名");
    	
		try {
			userName = tid.showAndWait().get();
		} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
		if(Pattern.matches(" *", userName)) {
    		Alert alert = new Alert(AlertType.ERROR, "用户名不能为空或全为空格");
    		alert.show();
    		return ;
    	}
		for(UserAccount user : Database.getInstance().getUsers()) {
			if(user.getUserName().equals(userName)) {
				Alert alert = new Alert(AlertType.ERROR, "数据库中存在重复用户，请重新输入用户名！");
	    		alert.show();
	    		return ;
			}
		}
    	TextInputDialog tid1 = new TextInputDialog("");
    	tid1.setTitle("密码");
    	tid1.setGraphic(new ImageView("/icon/password.png"));
    	tid1.setHeaderText("请输入密码");
    	try {
    		password = tid1.showAndWait().get();
    	} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
    	if(Pattern.matches(" *", password)) {
    		Alert alert = new Alert(AlertType.ERROR, "密码不能为空或全为空格");
    		alert.show();
    		return ;
    	}
    	UserAccount ua = new UserAccount(userName, password);
    	ua.setTitile("护士");
    	Database.getInstance().getUsers().add(ua);
    	nurseInit();
		Database.saveToFile();
    	return ;
    }
    //查找护士
    @FXML
    void nurseManSearchButtonFired() {
    	String chinese = nurseSearchChoiceBox.getSelectionModel().getSelectedItem();
    	String text = nurseManSearchField.getText();
    	nurseManNurseList.clear();
    	if(text.equals("")) {
    	   	for(UserAccount userAccount : Database.getInstance().getUsers()) {
        		if(userAccount.getTitile().equals("护士")) {
        			nurseManNurseList.add(userAccount);
        		}
        	}
    		return ;
    	}
    	for(UserAccount user : Database.getInstance().getUsers()) if(user.getTitile().equals("护士")) {
    		if(user.getByChinese(chinese).contains(text)) {
    			nurseManNurseList.add(user);
    		}
    	}
    	return ;
    }
    //修改护士
    @FXML
    void nurseManModifyButtonFired() {
    	UserAccount user = nurseManTableView.getSelectionModel().getSelectedItem();
    	if(user == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未选中护士。");
    		alert.showAndWait();
    		return ;
    	}
		if(!nurseManIdField.getText().equals("") && !Pattern.matches(idPattern, nurseManIdField.getText())) {
			Alert alert1 = new Alert(AlertType.ERROR, "请输入正确的身份证号码");
    		alert1.show();
    		return ;
		}
		if(!nurseManPhoneField.getText().equals("") && !Pattern.matches(phonePattern, nurseManPhoneField.getText())) {
			Alert alert1 = new Alert(AlertType.ERROR, "请输入正确的手机号");
    		alert1.show();
    		return ;
		}
		Alert alert = new Alert(AlertType.WARNING, "此操作不可恢复，请确认是否修改");
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	Optional<ButtonType> result = alert.showAndWait();
		if(result.get() != ButtonType.OK) {
			return ;
		} 
    	user.setName(nurseManNameField.getText());
    	user.setTitile(nurseManTitleChoiceBox.getSelectionModel().getSelectedItem());
    	user.setExpertise(nurseManExpertiseField.getText());
    	user.setIdNumber(nurseManIdField.getText());
    	user.setPhoneNumber(nurseManPhoneField.getText());
    	user.setBirth(nurseManDatePicker.getValue());
    	nurseInit();
    	nurseManTableView.getSelectionModel().select(user);
		Database.saveToFile();
    }
    //删除护士
    @FXML
    void nurseManRemoveButtonFired() {
    	UserAccount user = nurseManTableView.getSelectionModel().getSelectedItem();
    	if(user == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未选中护士。");
    		alert.showAndWait();
    		return ;
    	}

		Alert alert = new Alert(AlertType.WARNING, "此操作不可恢复，请确认是否删除");
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	alert.setContentText("该操作不可逆，请确认是否删除。");
    	Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			Database.getInstance().getUsers().remove(user);
    		nurseInit();
		} 
		Database.saveToFile();
    }
    @FXML
    void nurseInit() {
    	//清空数据
    	nurseManNameField.setText("");
    	nurseManTitleChoiceBox.getSelectionModel().clearSelection();
    	nurseManDatePicker.setValue(null);
    	nurseManExpertiseField.setText("");
    	nurseManIdField.setText("");
    	nurseManPhoneField.setText("");
    	//设置数据表    	
    	nurseManNurseList.clear();
    	for(UserAccount userAccount : Database.getInstance().getUsers()) {
    		if(userAccount.getTitile().equals("护士")) {
    			nurseManNurseList.add(userAccount);
    		}
    	}
    	nurseManTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	nurseManTableView.getColumns().clear();
    	nurseManTableView.setItems(nurseManNurseList);
    	TableColumn<UserAccount, String> userNameColumn = new TableColumn<UserAccount, String>("登录名");
    	TableColumn<UserAccount, String> nameColumn = new TableColumn<UserAccount, String>("姓名");
    	TableColumn<UserAccount, LocalDate> birthColumn = new TableColumn<UserAccount, LocalDate>("出生日期");
    	TableColumn<UserAccount, LocalDate> titleColumn = new TableColumn<UserAccount, LocalDate>("职称");
    	TableColumn<UserAccount, LocalDate> expertiseColumn = new TableColumn<UserAccount, LocalDate>("专长");
    	TableColumn<UserAccount, LocalDate> idColumn = new TableColumn<UserAccount, LocalDate>("身份证");
    	userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
    	titleColumn.setCellValueFactory(new PropertyValueFactory<>("titile"));
    	expertiseColumn.setCellValueFactory(new PropertyValueFactory<>("expertise"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    	nurseManTableView.getColumns().add(userNameColumn);
    	nurseManTableView.getColumns().add(nameColumn);
    	nurseManTableView.getColumns().add(birthColumn);
    	nurseManTableView.getColumns().add(titleColumn);
    	nurseManTableView.getColumns().add(expertiseColumn);
    	nurseManTableView.getColumns().add(idColumn);
    	
    	userNameColumn.setPrefWidth(nurseManTableView.getWidth() * 0.2);
    	nameColumn.setPrefWidth(nurseManTableView.getWidth() * 0.145);
    	birthColumn.setPrefWidth(nurseManTableView.getWidth() * 0.15);
    	titleColumn.setPrefWidth(nurseManTableView.getWidth() * 0.1);
    	expertiseColumn.setPrefWidth(nurseManTableView.getWidth() * 0.1);
    	idColumn.setPrefWidth(nurseManTableView.getWidth() * 0.3);
    	
    	nurseManTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserAccount>() {

			@Override
			public void changed(ObservableValue<? extends UserAccount> observable, UserAccount oldValue,
					UserAccount newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				setNurseInfo(newValue);
			}
		});
    	//设置选择框
    	nurseManTitleChoiceBox.getItems().setAll("医生", "护士", "护工");
    	nurseSearchChoiceBox.getItems().setAll("登录名", "姓名", "专长", "身份证");
    	nurseSearchChoiceBox.getSelectionModel().select("姓名");
    	TextField[] tfList = {nurseManNameField, nurseManExpertiseField, nurseManSearchField};
    	for(TextField tf : tfList) {
    		tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					if(newValue == null) return ;
					if(newValue.length() > 12)
						tf.setText(oldValue);
				}
			});
    	}
    	nurseManIdField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				
				if(!Pattern.matches("[0-9Xx]{0,18}", newValue)) {
					nurseManIdField.setText(oldValue);
				}
			}
		});
    	nurseManPhoneField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(!Pattern.matches("[0-9]{0,11}", newValue)) {
					nurseManPhoneField.setText(oldValue);
				}
			}
		});
    }
    void setNurseInfo(UserAccount user) {
    	nurseManNameField.setText(user.getName());
    	nurseManTitleChoiceBox.getSelectionModel().select(user.getTitile());
    	nurseManDatePicker.setValue(user.getBirth());
    	nurseManExpertiseField.setText(user.getExpertise());
    	nurseManIdField.setText(user.getIdNumber());
    	nurseManPhoneField.setText(user.getPhoneNumber());
    }
//[end]

//------------------------------------------------------医工管理--------------------------------------------------------------------
  //[start]
    //[start]
    ObservableList<UserAccount> workerManWorkerList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> workerSearchChoiceBox;
    @FXML
    private TextField workerManPhoneField;

    @FXML
    private DatePicker workerManDatePicker;
    @FXML
    private TextField workerManNameField;
    @FXML
    private TableView<UserAccount> workerManTableView;
    @FXML
    private TextField workerManSearchField;
    @FXML
    private TextField workerManExpertiseField;
    @FXML
    private TextField workerManIdField;
    @FXML
    private ChoiceBox<String> workerManTitleChoiceBox;
    //[end]
    //新增护工
    @FXML
    void workerManAddButtonFired() {
    	String userName = "";
    	String password = "";
    	TextInputDialog tid = new TextInputDialog("");
    	
    	tid.setTitle("用户名");
    	tid.setGraphic(new ImageView("/icon/user.png"));
    	tid.setHeaderText("请输入用户名");
    	
		try {
			userName = tid.showAndWait().get();
		} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
		if(Pattern.matches(" *", userName)) {
    		Alert alert = new Alert(AlertType.ERROR, "用户名不能为空或全为空格");
    		alert.show();
    		return ;
    	}
		for(UserAccount user : Database.getInstance().getUsers()) {
			if(user.getUserName().equals(userName)) {
				Alert alert = new Alert(AlertType.ERROR, "数据库中存在重复用户，请重新输入用户名！");
	    		alert.show();
	    		return ;
			}
		}
    	TextInputDialog tid1 = new TextInputDialog("");
    	tid1.setTitle("密码");
    	tid1.setGraphic(new ImageView("/icon/password.png"));
    	tid1.setHeaderText("请输入密码");
    	try {
    		password = tid1.showAndWait().get();
    	} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
    	if(Pattern.matches(" *", password)) {
    		Alert alert = new Alert(AlertType.ERROR, "密码不能为空或全为空格");
    		alert.show();
    		return ;
    	}
    	UserAccount ua = new UserAccount(userName, password);
    	ua.setTitile("护工");
    	Database.getInstance().getUsers().add(ua);
    	workerInit();
		Database.saveToFile();
    	return ;
    }
    //查找护工
    @FXML
    void workerManSearchButtonFired() {
    	String chinese = workerSearchChoiceBox.getSelectionModel().getSelectedItem();
    	String text = workerManSearchField.getText();
    	workerManWorkerList.clear();
    	if(text.equals("")) {
    	   	for(UserAccount userAccount : Database.getInstance().getUsers()) {
        		if(userAccount.getTitile().equals("护工")) {
        			workerManWorkerList.add(userAccount);
        		}
        	}
    		return ;
    	}
    	for(UserAccount user : Database.getInstance().getUsers()) if(user.getTitile().equals("护工")) {
    		if(user.getByChinese(chinese).contains(text)) {
    			workerManWorkerList.add(user);
    		}
    	}
    	return ;
    }
    //修改护工
    @FXML
    void workerManModifyButtonFired() {
    	UserAccount user = workerManTableView.getSelectionModel().getSelectedItem();
    	if(user == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未选中护工。");
    		alert.showAndWait();
    		return ;
    	}
		if(!workerManIdField.getText().equals("") && !Pattern.matches(idPattern, workerManIdField.getText())) {
			Alert alert1 = new Alert(AlertType.ERROR, "请输入正确的身份证号码");
    		alert1.show();
    		return ;
		}
		if(!workerManPhoneField.getText().equals("") && !Pattern.matches(phonePattern, workerManPhoneField.getText())) {
			Alert alert1 = new Alert(AlertType.ERROR, "请输入正确的手机号");
    		alert1.show();
    		return ;
		}
		Alert alert = new Alert(AlertType.WARNING, "此操作不可恢复，请确认是否修改");
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	Optional<ButtonType> result = alert.showAndWait();
		if(result.get() != ButtonType.OK) {
			return ;
		} 
    	user.setName(workerManNameField.getText());
    	user.setTitile(workerManTitleChoiceBox.getSelectionModel().getSelectedItem());
    	user.setExpertise(workerManExpertiseField.getText());
    	user.setIdNumber(workerManIdField.getText());
    	user.setPhoneNumber(workerManPhoneField.getText());
    	user.setBirth(workerManDatePicker.getValue());
    	workerInit();
    	workerManTableView.getSelectionModel().select(user);
		Database.saveToFile();
    }
    //删除护工
    @FXML
    void workerManRemoveButtonFired() {
    	UserAccount user = workerManTableView.getSelectionModel().getSelectedItem();
    	if(user == null) {
    		Alert alert = new Alert(AlertType.ERROR, "您尚未选中护工。");
    		alert.showAndWait();
    		return ;
    	}

		Alert alert = new Alert(AlertType.WARNING, "此操作不可恢复，请确认是否删除");
    	alert.getButtonTypes().add(ButtonType.CANCEL);
    	alert.setContentText("该操作不可逆，请确认是否删除。");
    	Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			Database.getInstance().getUsers().remove(user);
    		workerInit();
		} 
		Database.saveToFile();
    }
    @FXML
    void workerInit() {
    	//清空数据
    	workerManNameField.setText("");
    	workerManTitleChoiceBox.getSelectionModel().clearSelection();
    	workerManDatePicker.setValue(null);
    	workerManExpertiseField.setText("");
    	workerManIdField.setText("");
    	workerManPhoneField.setText("");
    	//设置数据表    	
    	workerManWorkerList.clear();
    	for(UserAccount userAccount : Database.getInstance().getUsers()) {
    		if(userAccount.getTitile().equals("护工")) {
    			workerManWorkerList.add(userAccount);
    		}
    	}
    	workerManTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	workerManTableView.getColumns().clear();
    	workerManTableView.setItems(workerManWorkerList);
    	TableColumn<UserAccount, String> userNameColumn = new TableColumn<UserAccount, String>("登录名");
    	TableColumn<UserAccount, String> nameColumn = new TableColumn<UserAccount, String>("姓名");
    	TableColumn<UserAccount, LocalDate> birthColumn = new TableColumn<UserAccount, LocalDate>("出生日期");
    	TableColumn<UserAccount, LocalDate> titleColumn = new TableColumn<UserAccount, LocalDate>("职称");
    	TableColumn<UserAccount, LocalDate> expertiseColumn = new TableColumn<UserAccount, LocalDate>("专长");
    	TableColumn<UserAccount, LocalDate> idColumn = new TableColumn<UserAccount, LocalDate>("身份证");
    	userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
    	titleColumn.setCellValueFactory(new PropertyValueFactory<>("titile"));
    	expertiseColumn.setCellValueFactory(new PropertyValueFactory<>("expertise"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    	workerManTableView.getColumns().add(userNameColumn);
    	workerManTableView.getColumns().add(nameColumn);
    	workerManTableView.getColumns().add(birthColumn);
    	workerManTableView.getColumns().add(titleColumn);
    	workerManTableView.getColumns().add(expertiseColumn);
    	workerManTableView.getColumns().add(idColumn);
    	
    	userNameColumn.setPrefWidth(workerManTableView.getWidth() * 0.2);
    	nameColumn.setPrefWidth(workerManTableView.getWidth() * 0.145);
    	birthColumn.setPrefWidth(workerManTableView.getWidth() * 0.15);
    	titleColumn.setPrefWidth(workerManTableView.getWidth() * 0.1);
    	expertiseColumn.setPrefWidth(workerManTableView.getWidth() * 0.1);
    	idColumn.setPrefWidth(workerManTableView.getWidth() * 0.3);
    	
    	workerManTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserAccount>() {

			@Override
			public void changed(ObservableValue<? extends UserAccount> observable, UserAccount oldValue,
					UserAccount newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				setWorkerInfo(newValue);
			}
		});
    	//设置选择框
    	workerManTitleChoiceBox.getItems().setAll("医生", "护士", "护工");
    	workerSearchChoiceBox.getItems().setAll("登录名", "姓名", "专长", "身份证");
    	workerSearchChoiceBox.getSelectionModel().select("姓名");
    	TextField[] tfList = {workerManNameField, workerManExpertiseField, workerManSearchField};
    	for(TextField tf : tfList) {
    		tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					if(newValue == null) return ;
					if(newValue.length() > 12)
						tf.setText(oldValue);
				}
			});
    	}
    	workerManIdField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				
				if(!Pattern.matches("[0-9Xx]{0,18}", newValue)) {
					workerManIdField.setText(oldValue);
				}
			}
		});
    	workerManPhoneField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(!Pattern.matches("[0-9]{0,11}", newValue)) {
					workerManPhoneField.setText(oldValue);
				}
			}
		});
    }
    void setWorkerInfo(UserAccount user) {
    	workerManNameField.setText(user.getName());
    	workerManTitleChoiceBox.getSelectionModel().select(user.getTitile());
    	workerManDatePicker.setValue(user.getBirth());
    	workerManExpertiseField.setText(user.getExpertise());
    	workerManIdField.setText(user.getIdNumber());
    	workerManPhoneField.setText(user.getPhoneNumber());
    }
//[end]
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
    //输入框长度上限
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

    	thisStage = (Stage)menuShadow.getScene().getWindow();
		EventHandler handler = new DragWindowHandler(thisStage);
    	choinePane.setOnMouseDragged(handler);
    	choinePane.setOnMousePressed(handler);
    	doctorInit();
    	nurseInit();
    	workerInit();
    	setLengthLimit(doctorManSearchField, 20);
    	setLengthLimit(doctorManNameField, 20);
    	setLengthLimit(doctorManExpertiseField, 20);
    	setLengthLimit(doctorManIdField, 20);
    	setLengthLimit(doctorManPhoneField, 20);
    	
    	setLengthLimit(nurseManSearchField, 20);
    	setLengthLimit(nurseManNameField, 20);
    	setLengthLimit(nurseManExpertiseField, 20);
    	setLengthLimit(nurseManIdField, 20);
    	setLengthLimit(nurseManPhoneField, 20);
    	
    	setLengthLimit(workerManSearchField, 20);
    	setLengthLimit(workerManNameField, 20);
    	setLengthLimit(workerManExpertiseField, 20);
    	setLengthLimit(workerManIdField, 20);
    	setLengthLimit(workerManPhoneField, 20);
   
    }
}
