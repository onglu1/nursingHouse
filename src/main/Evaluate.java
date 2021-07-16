package main;

import controller.PatientEvaluateController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Evaluate extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader fx = new FXMLLoader();
//		System.out.println("123weqwewq");
		fx.setLocation(fx.getClassLoader().getResource("view/PatientEvaluateInterface.fxml"));
		AnchorPane ap = (AnchorPane)fx.load();
		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("用户评估");
		stage.getIcons().add(new Image("/icon/quiz.png"));
//		stage.showAndWait();
		stage.show();
		PatientEvaluateController ctr = fx.getController();
		ctr.initialize_d();
	}
	
}
