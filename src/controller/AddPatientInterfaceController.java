package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Database;
import model.Patient;

public class AddPatientInterfaceController implements Initializable{
	//[start]
	private final int widowWid = 380;
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
    private TextField idField;

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
    private Label idAlert;
    //[end]
    @FXML
    void enterPressed(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER) {
    		confirm();
    	}
    }
    @FXML
    void confirm() {
    	boolean isStorable = true;
    	int maxlength = 380;
    	String name = null;
    	long age = 0;
    	String idNumber = null;
    	boolean sex = false;
    	String emergencyContact = null;
    	String emergencyPhoneNumber = null;
    	String phoneNumber = null;
		String agePattern = "^(?:[1-9][0-9]?|1[01][0-9]|120)$";
		String phonePattern = "^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\\d{8}$";
		String idPattern = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

		//??????
    	if(nameField.getText().equals("")) {
    		nameAlert.setText("*??????????????????");
    		isStorable = false;
        	maxlength = Math.max(maxlength, widowWid + 130);
    	} else {
    		name = nameField.getText();
    		nameAlert.setText("");
    	}
    	//??????
    	if(ageField.getText().equals("")) {
    		ageAlert.setText("*??????????????????");
    		isStorable = false;
        	maxlength = Math.max(maxlength, widowWid + 130);
    	} else {
    		if(Pattern.matches(agePattern, ageField.getText()) == false) {
    			ageAlert.setText("*???????????????????????????(0~120)");
        		isStorable = false;
    			maxlength = Math.max(maxlength, widowWid + 200);
    		}else {
    			age = Integer.parseInt(ageField.getText());
    			ageAlert.setText("");
    		}
    	}
    	//????????????
//    	Pattern.matches(idPattern, idField.getText());
    	if(idField.getText().equals("")) {
    		idAlert.setText("*???????????????????????????");
    		isStorable = false;
        	maxlength = Math.max(maxlength, widowWid + 150);
    	} else {
    		if(Pattern.matches(idPattern, idField.getText()) == false) {
    			idAlert.setText("*?????????????????????????????????");
    			isStorable = false;
    			maxlength = Math.max(maxlength, widowWid + 200);
    		} else {
    			idNumber = idField.getText();
        		idAlert.setText("");
    		}
    		
    	}
    	//??????
    	if(!(maleButton.isSelected() || femalButton.isSelected())) {
    		sexAlert.setText("*???????????????");
    		isStorable = false;
        	maxlength = Math.max(maxlength, widowWid + 150);
    	} else {
    		sex = maleButton.isSelected();
    		sexAlert.setText("");
    	}
    	//?????????
    	phoneNumber = phoneNumberField.getText();
    	if(!phoneNumber.equals("") && Pattern.matches(phonePattern, phoneNumber) == false) {
    		phoneNumberAlert.setText("*?????????????????????????????????11??????");
    		isStorable = false;
			maxlength = Math.max(maxlength, widowWid + 200);
    	} else {
    		phoneNumberAlert.setText("");
    	}
    	//???????????????
    	if(emergencyContactField.getText().equals("")) {
    		emergencyContactAlert.setText("*???????????????????????????");
    		isStorable = false;
        	maxlength = Math.max(maxlength, widowWid + 150);
    	} else {
    		emergencyContact = emergencyContactField.getText();
    		emergencyContactAlert.setText("");
    	}
    	//??????????????????
    	if(emergencyPhoneNumberField.getText().equals("")) {
    		emergencyPhoneNumberAlert.setText("*??????????????????????????????");
    		isStorable = false;
        	maxlength = Math.max(maxlength, widowWid + 150);
    	} else {
    		if(Pattern.matches(phonePattern, emergencyPhoneNumberField.getText()) == false) {
    			emergencyPhoneNumberAlert.setText("*?????????????????????????????????11??????");
    			isStorable = false;
    			maxlength = Math.max(maxlength, widowWid + 200);
    		} else {
    			emergencyPhoneNumber = emergencyPhoneNumberField.getText();
        		emergencyPhoneNumberAlert.setText("");
    		}
    		
    	}
    	confirmButton.getScene().getWindow().setWidth(maxlength);
    	if(isStorable) {
    		Database.getInstance().setTmppatient(new Patient(name, (int)age, idNumber,sex, phoneNumber, emergencyContact, emergencyPhoneNumber));
        	confirmButton.getScene().getWindow().hide();
    	}
    }

    @FXML
    void cancel() {
    	//????????????
    	confirmButton.getScene().getWindow().hide();
    }

    @FXML
    void reset() {
    	//????????????
		
		maleButton.setSelected(false);
		femalButton.setSelected(false);
		sexAlert.setText("");
		nameField.setText("");
		nameAlert.setText("");
		ageField.setText("");
		ageAlert.setText("");
		idField.setText("");
		idAlert.setText("");
		phoneNumberField.setText("");
		phoneNumberAlert.setText("");
		emergencyContactField.setText("");
		emergencyContactAlert.setText("");
		emergencyPhoneNumberField.setText("");
		emergencyPhoneNumberAlert.setText("");
		
    	confirmButton.getScene().getWindow().setWidth(widowWid);
    	
    }
    void setLengthListener(TextField text, int length) {
    	text.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue.length() > length) {
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
		if(Database.getInstance().idChangable == false) {
    		idField.setText(Database.getInstance().idNumber);
			idField.setEditable(false);
			phoneNumberField.setText(Database.getInstance().getTmppatient().getPhoneNumber());
			nameField.setText(Database.getInstance().getTmppatient().getName());
			emergencyContactField.setText(Database.getInstance().getTmppatient().getEmergencyContact());
			emergencyPhoneNumberField.setText(Database.getInstance().getTmppatient().getEmergencyPhoneNumber());
			ageField.setText(String.valueOf(Database.getInstance().getTmppatient().getAge()));
			if(Database.getInstance().getTmppatient().getSex()) {
				maleButton.setSelected(true);
				femalButton.setSelected(false);
			} else {
				femalButton.setSelected(true);
				maleButton.setSelected(false);
			}
			
    	} else {
    		idField.setText("");
    		idField.setEditable(true);
    	}
		ToggleGroup sexChoiceGroup = new ToggleGroup();
		sexChoiceGroup.getToggles().add(femalButton);
		sexChoiceGroup.getToggles().add(maleButton);
		//????????????
		setLengthListener(nameField, 15);
		setLengthListener(ageField, 3);
		setLengthListener(emergencyContactField, 15);
		setLengthListener(emergencyPhoneNumberField, 15);
		setLengthListener(phoneNumberField, 15);
		setLengthListener(idField, 18);
		//???????????????
		setNumberOnlyListener(phoneNumberField);
		setNumberOnlyListener(emergencyPhoneNumberField);
		setNumberOnlyListener(ageField);
	}

}
