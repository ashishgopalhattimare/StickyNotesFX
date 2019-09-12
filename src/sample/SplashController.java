package sample;

import com.jfoenix.controls.JFXProgressBar;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    private static final int PAUSE = 2;
    @FXML private JFXProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Pane pane = new Pane();
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2.7), pane);
        fadeOut.setFromValue(1); fadeOut.setToValue(0);

        fadeOut.play();
        fadeOut.setOnFinished(event -> {
            try {
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("stickyframe.fxml"));

                primaryStage.getIcons().add(new Image("/images/logo.png"));
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();

                primaryStage.setX(Constants.WINDOW_WIDTH-350);
                primaryStage.setY(0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                Stage stage = (Stage) progressBar.getScene().getWindow();
                stage.close();
            }
        });
    }
}