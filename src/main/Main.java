package main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import controller.MainInterfaceController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.DragWindowHandler;

public class Main extends Application{
	public static void main(String[] args) {
		launch(args); 
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader fx = new FXMLLoader();
//		System.out.println("123weqwewq");
		fx.setLocation(fx.getClassLoader().getResource("view/MainInterface.fxml"));
		AnchorPane ap = (AnchorPane)fx.load();
		EventHandler handler = new DragWindowHandler(stage);
		ap.setOnMouseDragged(handler);
		ap.setOnMousePressed(handler);
//		URL cssurl = this.getClass().getClassLoader().getResource("css/Main.css");
		Scene scene = new Scene(ap);
//		scene.getStylesheets().add(cssurl.toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("管理界面");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.getIcons().add(new Image("/icon/nursingHouse.png"));
		stage.show();
		MainInterfaceController ctr = fx.getController();
		ctr.init();
		
	}
	
}
