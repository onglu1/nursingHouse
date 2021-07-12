package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AdminController implements Initializable{
    private Rectangle choiceRec;
    @FXML
    private AnchorPane menuButtonAnchorPane;
    
    @FXML
    private Circle menuShadow;

    @FXML
    private VBox menuVBox;
    
    @FXML
    private Tab doctorTab;

    @FXML
    private Tab workerTab;

    @FXML
    private Tab nurseTab;
    
    @FXML
    private TabPane interfaceTabPane;
    
    @FXML
    private Rectangle doctorRec;
    
    @FXML
    private Rectangle nurseRec;
    
    @FXML
    private Rectangle workerRec;

    @FXML
    private Rectangle screenCover;
    
    @FXML
    private TabPane choinePane;
    
    @FXML
    void sidebarShow() {
//    	System.out.println(menuVBox.getTranslateX());
    	if(menuVBox.getTranslateX() >= -menuVBox.getWidth() / 2) return ;
    	TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), menuVBox);
    	tt.setToX(0);
    	screenCover.setVisible(true);
    	tt.play();
    }
    @FXML
    void sidebarHide() {
    	if(menuVBox.getTranslateX() <= -menuVBox.getWidth() / 2) return ;
    	TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), menuVBox);
    	tt.setToX(-menuVBox.getWidth());
    	tt.setInterpolator(Interpolator.EASE_OUT);
    	tt.play();
    	screenCover.setVisible(false);
    }



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
    
    @FXML
    void switchChoice(MouseEvent event) {
    	choiceRec = (Rectangle)event.getSource();
    	if(choiceRec.equals(workerRec)) {
    		choinePane.getSelectionModel().select(workerTab);
    		sidebarHide();
    	} else if(choiceRec.equals(nurseRec)) {
    		choinePane.getSelectionModel().select(nurseTab);
    		sidebarHide();
    	} else if(choiceRec.equals(doctorRec)) {
    		choinePane.getSelectionModel().select(doctorTab);
    		sidebarHide();
    	}
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
