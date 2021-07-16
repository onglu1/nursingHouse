package main;

import controller.AdminController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Admin extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader fx = new FXMLLoader();
//		System.out.println("123weqwewq");
		fx.setLocation(fx.getClassLoader().getResource("view/AdminInterface.fxml"));
		AnchorPane ap = (AnchorPane)fx.load();
		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("超级管理员管理界面");
		stage.getIcons().add(new Image("/icon/admin-fill.png"));
		stage.show();
		AdminController ctr = fx.getController();
		ctr.init();
	}
	
}
