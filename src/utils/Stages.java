package utils;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Stages {
	public static Stage allertStage(String text) {
		Stage window = new Stage();
		window.setHeight(300);
		window.setWidth(300);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.initStyle(StageStyle.UNIFIED);
		return window;
	} 
}
