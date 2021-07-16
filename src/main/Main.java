package main;

import controller.MainInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("管理界面");
		stage.getIcons().add(new Image("/icon/nursingHouse.png"));
		stage.show();
		MainInterfaceController ctr = fx.getController();
		ctr.init();
	}
	
}
