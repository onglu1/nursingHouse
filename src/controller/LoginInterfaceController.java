package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Admin;
import main.Main;
import model.Database;
import model.UserAccount;

public class LoginInterfaceController implements Initializable{
	
	@FXML
    private TextField userNameTextField;

    @FXML
    private TextField showPasswordField;

    @FXML
    private AnchorPane background;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView showPasswordImageView;

    @FXML
    private PasswordField hidePasswordField;
    
    @FXML
    private Text promptText;
    
    @FXML
    private Hyperlink loginTypeHyperlink;
    
    private boolean loginType = false;
    @FXML
    void getFocus(MouseEvent event) {
    	if(event.getButton() == MouseButton.PRIMARY && event.getEventType() == MouseEvent.MOUSE_PRESSED) {
    		background.requestFocus();
    	}
    }
    @FXML
    void changePassworShowState(MouseEvent event) {
    	if(event.getButton() != MouseButton.PRIMARY) return ;
    	if(showPasswordField.isVisible()) {
    		showPasswordImageView.setImage(new Image("/icon/password-visible.png"));
    		showPasswordField.setVisible(false);
    		hidePasswordField.setVisible(true);
    		hidePasswordField.setText(showPasswordField.getText());
    	} else {
    		showPasswordImageView.setImage(new Image("/icon/password-invisible.png"));
    		hidePasswordField.setVisible(false);
    		showPasswordField.setVisible(true);
    		showPasswordField.setText(hidePasswordField.getText());
    	}
    }
    @FXML
    void login() throws Exception {
    	String userName = userNameTextField.getText();
    	TextField password = null;
//    	String password = "";
    	if(showPasswordField.isVisible()) password = showPasswordField;
    	else password = hidePasswordField;
    	
    	if(userName.equals("") || password.getText().equals("")) {
    		if(password.getText().equals("")) {
    			password.setPromptText("密码不能为空");
        		password.requestFocus();
    		}
    		if(userName.equals("")) {
    			userNameTextField.setPromptText("用户名不能为空");
    			userNameTextField.requestFocus();
    		}
    		
    	} else {
    		ArrayList<UserAccount> users = null;
    		if(loginType) users = Database.getInstance().getAdmins();
    		else users = Database.getInstance().getUsers();
    		System.out.println(users.contains(new UserAccount(userName, password.getText())));
			if(users.contains(new UserAccount(userName, password.getText()))) {
        		System.out.println("登陆成功");
        		Stage stage = new Stage();
        		if(loginType) {
        			Admin main = new Admin();
            		loginButton.getScene().getWindow().hide();
            		main.start(stage);
        					
        		} else {
        			Main main = new Main();
            		loginButton.getScene().getWindow().hide();
            		main.start(stage);
        		}
        		
        	} else {
        		FadeTransition fade = new FadeTransition();
        		fade.setDuration(Duration.seconds(0.1));
        		fade.setNode(background);
        		fade.setFromValue(0);
        		fade.setToValue(1);
        		fade.play();
//            		userNameTextField.setText("qwq");
        		Alert alert = new Alert(AlertType.ERROR, "登陆失败，用户名或密码有误");
        		userNameTextField.setPromptText("输入用户名");
        		password.setPromptText("输入密码");
        		alert.show();
        	}
    		
    	}
    }
    @FXML
    void enterPressed(KeyEvent event) throws Exception {
    	if(event.getCode() == KeyCode.ENTER) {
    		login();
    	}
    }
    @FXML
    void switchLoginType(MouseEvent event) throws Exception {
    	if(loginType) {
    		loginTypeHyperlink.setText("超级管理员登录");
    		loginType = false;
    	} else { 
    		loginTypeHyperlink.setText("普通管理员登录");
    		loginType = true;
    	}
    }
    void setTextListener(TextField text) {
    	text.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue.length() > 15) {
					text.setText(oldValue);
				}
			}
		});
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showPasswordImageView.setImage(new Image("/icon/password-visible.png"));
		setTextListener(showPasswordField);
		setTextListener(hidePasswordField);
		setTextListener(userNameTextField);
	}
}
