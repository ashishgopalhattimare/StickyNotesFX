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

import sample.Firebase.FirebaseConfig;

public class Main extends Application {

    /**
     * Purpose : Start the application from here
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("views/splashscreen.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));

        primaryStage.setAlwaysOnTop(true);
        primaryStage.getIcons().add(new Image("/images/logo.png"));

        primaryStage.show();

        Rectangle2D window = Screen.getPrimary().getVisualBounds();
        Constants.WINDOW_HEIGHT = (int) window.getHeight();
        Constants.WINDOW_WIDTH = (int) window.getWidth();

        FirebaseConfig.SetUpConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
