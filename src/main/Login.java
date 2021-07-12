package main;

import java.net.URL;

import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Database;
import utils.Animations;

public class Login extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		//加载界面
		FXMLLoader fx = new FXMLLoader();
//		URL url = 
		fx.setLocation(fx.getClassLoader().getResource("view/LoginInterface.fxml"));
		StackPane sp = new StackPane();
		AnchorPane root = (AnchorPane)fx.load();
		Scene scene = new Scene(root);
		//设置标题和图标
		stage.setScene(scene);
		stage.setTitle("东软颐养社区中心系统");
		stage.getIcons().add(new Image("/icon/nursingHouse.png"));
		stage.show();
		Image image = new Image("/icon/flower.png");
		root.getChildren().add(0, sp);
		Node flowerview = Animations.getFlowerView(image, 15, (int)scene.getWidth(), (int)scene.getHeight(), 100);
		
		sp.getChildren().addAll(flowerview);
		
		
	}
	

}
