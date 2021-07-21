package controller;

import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Database;
import model.Template;

public class PatientEvaluateController {
	private ArrayList<Integer> anses = new ArrayList<Integer>();
	private int nowId;
	private Template template;
    @FXML
    private Label titleIdLabel;
    @FXML
    private RadioButton choice0;

    @FXML
    private Label descriptionLabel;

    @FXML
    private RadioButton choice1;

    @FXML
    private RadioButton choice2;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private Button previousButton;
    
    @FXML
    void previousProblem() {
    	if(nowId == 0) return ;
    	nowId--;
    	fitProblem();
    }

    @FXML
    void submit() {
    	if(anses.contains(-1)) {
    		Alert alert = new Alert(AlertType.ERROR, "还有问题没有作答");
    		alert.showAndWait();
    		return ;
    	}
    	Alert alert = new Alert(AlertType.CONFIRMATION, "确定提交？");
    	alert.setHeaderText("提示");
    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.get() == ButtonType.OK) {
    		double totalscore = 0;
    		for(int i = 0; i < anses.size(); i++) {
    			if(anses.get(i) == template.getProblems().get(i).getAns()) {
    				totalscore += (100.0 / anses.size());
    			}
    		}
    		totalscore = (Math.round(totalscore * 100)) / 100.0;
    		Database.getInstance().getTmppatient().setScore(totalscore);
    		Alert alert2 = new Alert(AlertType.INFORMATION, "得分：" + String.valueOf(totalscore));
    		if(totalscore > 60) {
        		alert2.setHeaderText("恭喜");
        		alert2.setGraphic(new ImageView("icon/wind-smile.png"));
    		} else {
        		alert2.setHeaderText("再接再厉");
        		alert2.setGraphic(new ImageView("icon/wind-cry.png"));
    		}
    		alert2.showAndWait();
    		choice0.getScene().getWindow().hide();
    		Database.saveToFile();
    		return ;
    	}
    }

    @FXML
    void nextProblem() {
    	if(nowId == template.getProblems().size() - 1) return ;
    	nowId++;
    	fitProblem();
    }
    void fitProblem() {
    	previousButton.setDisable(true);
    	nextButton.setDisable(true);
    	if(nowId > 0) previousButton.setDisable(false);
    	if(template.getProblems().size() > nowId + 1) nextButton.setDisable(false);
    	titleIdLabel.setText("第" + (nowId + 1) + "题");
    	descriptionLabel.setText(template.getProblems().get(nowId).getDescription());
    	choice0.setText(template.getProblems().get(nowId).getChoice().get(0));
    	choice1.setText(template.getProblems().get(nowId).getChoice().get(1));
    	choice2.setText(template.getProblems().get(nowId).getChoice().get(2));
    	if(anses.get(nowId) == -1) {
    		choice0.setSelected(false);
    		choice1.setSelected(false);
    		choice2.setSelected(false);
    	} else if(anses.get(nowId) == 0) {
    		choice0.setSelected(true);
    		choice1.setSelected(false);
    		choice2.setSelected(false);
    	} else if(anses.get(nowId) == 1) {
    		choice0.setSelected(false);
    		choice1.setSelected(false);
    		choice2.setSelected(true);
    	} else if(anses.get(nowId) == 2) {
    		choice0.setSelected(false);
    		choice1.setSelected(true);
    		choice2.setSelected(false);
    	}
    }
    public void initialize_d() {	
    	ToggleGroup tg = new ToggleGroup();
    	tg.getToggles().addAll(choice0, choice1, choice2);
    	tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				if(newValue == null) return ;
				if(newValue.equals(choice0)) {
					anses.set(nowId, 0);
				} else if(newValue.equals(choice1)) {
					anses.set(nowId, 1);
				} else if(newValue.equals(choice2)) {
					anses.set(nowId, 2);
				}
			}
		});
    	nowId = 0;
    	template = Database.getInstance().getTmptemplate();
    	if(template == null) {
    		Alert alert = new Alert(AlertType.ERROR, "该模板不包含问题");
    		alert.showAndWait();
    		choice1.getScene().getWindow().hide();
    		return ;
    	}
    	for(int i = 0; i < template.getProblems().size(); i++) 
    		anses.add(-1);
    	fitProblem();
    	
    }

}
