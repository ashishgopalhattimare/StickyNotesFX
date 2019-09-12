package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("splashscreen.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.getIcons().add(new Image("/images/logo.png"));

        Rectangle2D window = Screen.getPrimary().getVisualBounds();
        Constants.WINDOW_HEIGHT = (int) window.getHeight();
        Constants.WINDOW_WIDTH = (int) window.getWidth();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
