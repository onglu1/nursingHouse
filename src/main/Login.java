package main;

import controller.LoginInterfaceController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Database;
import utils.Animations;
import utils.DragWindowHandler;

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
		//设置标题和图标

		
		EventHandler handler = new DragWindowHandler(stage);
		root.setOnMouseDragged(handler);
		root.setOnMousePressed(handler);
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);// 设定窗口无边框
//		Database.readFromFile();
		stage.setScene(scene);
		stage.setTitle("东软颐养社区中心系统");
		stage.getIcons().add(new Image("/icon/nursingHouse.png"));
		stage.show();
		Image image = new Image("/icon/flower.png");
		root.getChildren().add(0, sp);
		Node flowerview = Animations.getFlowerView(image, 15, (int)scene.getWidth(), (int)scene.getHeight(), 100);
		sp.getChildren().addAll(flowerview);
		LoginInterfaceController controller = fx.getController();
		controller.init();
		
	}
	

}
