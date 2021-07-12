package utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Animations {
	public static Node getFlowerView(Image image, int number, int w, int h, int z) {
		ArrayList<ImageView> imgList = new ArrayList<ImageView>();
		Random random = new Random();
		int locationX, locationY, locationZ;
		for(int i = 0; i < number; i++) {
			ImageView iv = new ImageView(image);
			iv.setFitHeight(40);
			iv.setFitWidth(40);
			iv.setPreserveRatio(true);
			if(random.nextBoolean() == true) {
				locationX = random.nextInt(w) + random.nextInt(30) + 30;
			} else {
				locationX = random.nextInt(w) - random.nextInt(30) - 30;
			}
			locationY = random.nextInt(20);
			locationZ = random.nextInt(z);
			iv.setTranslateX(locationX);
			iv.setTranslateY(locationY);
			iv.setTranslateZ(locationZ);
			imgList.add(iv);
		}
		AnchorPane ap = new AnchorPane();
		ap.setStyle("-fx-background-color:#FFB6C100");
		ap.getChildren().addAll(imgList);
		SubScene subscene = new SubScene(ap, w, h, true, SceneAntialiasing.BALANCED);
		PerspectiveCamera camera = new PerspectiveCamera();
		subscene.setCamera(camera);
		imgList.forEach(new Consumer<ImageView>() {

			@Override
			public void accept(ImageView t) {
				double time = random.nextDouble() * 5 + 3.5;
				TranslateTransition tt = new TranslateTransition(Duration.seconds(time));
				tt.setFromX(t.getTranslateX());
				tt.setFromY(t.getTranslateY());
				tt.setByX(100);
				tt.setByY(400);
				FadeTransition ft1 = new FadeTransition(Duration.seconds(time / 2));
				ft1.setFromValue(0);
				ft1.setToValue(1);
				FadeTransition ft2 = new FadeTransition(Duration.seconds(time / 2)); 
				ft1.setFromValue(1);
				ft1.setToValue(0);
				SequentialTransition st = new SequentialTransition();
				st.getChildren().addAll(ft1, ft2);
				ParallelTransition pt = new ParallelTransition();
				pt.setNode(t);
				pt.getChildren().addAll(tt, st);
				pt.setCycleCount(Animation.INDEFINITE);
				pt.play();
			}
		});
		return ap;
	}
}
