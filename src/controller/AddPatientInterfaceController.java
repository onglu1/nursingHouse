package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.Database;
import model.Patient;

public class AddPatientInterfaceController implements Initializable{

    @FXML
    private RadioButton maleButton;

    @FXML
    private TextField ageField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button cancelButtom;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emergencyContactField;

    @FXML
    private RadioButton femalButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emergencyPhoneNumberField;

    @FXML
    private Button resetButton;
    
    @FXML
    private Label ageAlert;
    
    @FXML
    private Label nameAlert;

    @FXML
    private Label emergencyContactAlert;

    @FXML
    private Label emergencyPhoneNumberAlert;
    
    @FXML
    private Label sexAlert;

    @FXML
    private Label phoneNumberAlert;

    @FXML
    void confirm() {
    	boolean isStorable = true;
    	int maxlength = 339;
//    	System.out.println("qwq");
    	String name = null;
    	int age = 0;
    	boolean sex = false;
    	String emergencyContact = null;
    	String emergencyPhoneNumber = null;
    	String phoneNumber = null;
    	confirmButton.getScene().getWindow().setWidth(500);
    	//姓名
    	if(nameField.getText().equals("")) {
    		nameAlert.setText("*姓名不能为空");
    		System.out.println("qwq");
    		isStorable = false;
        	maxlength = Math.max(maxlength, 430);
    	} else {
    		name = nameField.getText();
    		nameAlert.setText("");
    	}
    	//年龄
    	if(ageField.getText().equals("")) {
    		ageAlert.setText("*年龄不能为空");
    		isStorable = false;
        	maxlength = Math.max(maxlength, 430);
    	} else {
    		age = Integer.parseInt(ageField.getText());
    		if(age < 0 || age > 200) {
    			ageAlert.setText("*年龄范围不符合要求(0~200)");
        		isStorable = false;
    			maxlength = Math.max(maxlength, 550);
    		}else ageAlert.setText("");
    	}
    	//性别
    	if(!(maleButton.isSelected() || femalButton.isSelected())) {
    		sexAlert.setText("*未选择性别");
    		isStorable = false;
        	maxlength = Math.max(maxlength, 430);
    	} else {
    		sex = maleButton.isSelected();
    		sexAlert.setText("");
    	}
    	//手机号
    	phoneNumber = phoneNumberField.getText();
    	//紧急联系人
    	if(emergencyContactField.getText().equals("")) {
    		emergencyContactAlert.setText("*紧急联系人不能为空");
    		isStorable = false;
        	maxlength = Math.max(maxlength, 500);
    	} else {
    		emergencyContact = emergencyContactField.getText();
    		emergencyContactAlert.setText("");
    	}
    	//紧急联系电话
    	if(emergencyPhoneNumberField.getText().equals("")) {
    		emergencyPhoneNumberAlert.setText("*紧急联系电话不能为空");
    		isStorable = false;
        	maxlength = Math.max(maxlength, 500);
    	} else {
    		emergencyPhoneNumber = emergencyPhoneNumberField.getText();
    		emergencyPhoneNumberAlert.setText("");
    	}
    	confirmButton.getScene().getWindow().setWidth(maxlength);
    	if(isStorable) {
    		Database.getInstance().getPatients().add(new Patient(name, age, sex, phoneNumber, emergencyContact, emergencyPhoneNumber));
        	confirmButton.getScene().getWindow().hide();
    	}
    }

    @FXML
    void cancel() {
    	//取消添加
    	confirmButton.getScene().getWindow().hide();
    }

    @FXML
    void reset() {
    	//重置输入
		emergencyPhoneNumberField.setText("");
		emergencyContactField.setText("");
		maleButton.setSelected(false);
		femalButton.setSelected(false);
		phoneNumberField.setText("");
		emergencyPhoneNumberAlert.setText("");
		emergencyContactAlert.setText("");
		sexAlert.setText("");
		ageAlert.setText("");
		ageField.setText("");
		nameAlert.setText("");
		nameField.setText("");
    	confirmButton.getScene().getWindow().setWidth(339);
    	
    }
    void setTextListener(TextField text) {
    	text.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue.length() >= 15) {
					text.setText(oldValue);
				}
			}
		});
    }
    void setNumberOnlyListener(TextField text) {
    	text.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				for(int i = 0; i < newValue.length(); i++) {
					if(newValue.charAt(i) > '9' || newValue.charAt(i) < '0') {
						text.setText(oldValue);
						return ;
					}
				}
			}
		});
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ToggleGroup sexChoiceGroup = new ToggleGroup();
		sexChoiceGroup.getToggles().add(femalButton);
		sexChoiceGroup.getToggles().add(maleButton);
		//限制长度
		setTextListener(nameField);
		setTextListener(ageField);
		setTextListener(emergencyContactField);
		setTextListener(emergencyPhoneNumberField);
		setTextListener(phoneNumberField);
		//仅允许数字
		setNumberOnlyListener(phoneNumberField);
		setNumberOnlyListener(emergencyPhoneNumberField);
		setNumberOnlyListener(ageField);
	}

}
