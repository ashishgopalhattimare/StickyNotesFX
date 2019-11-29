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
import sample.Firebase.FirebaseConfig;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML private JFXProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Pane pane = new Pane();
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2.3), pane);
        fadeOut.setFromValue(1); fadeOut.setToValue(0);

        fadeOut.play();
        fadeOut.setOnFinished(event -> {
            try {
                String [] userInfo = FileRW.readFile();

                if(userInfo == null || userInfo.length != 2) {
                    Stage primaryStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    primaryStage.getIcons().add(new Image("/images/logo.png"));
                    primaryStage.setScene(new Scene(root));
                    primaryStage.setAlwaysOnTop(true);
                    primaryStage.show();
                }
                else {
                    if(FirebaseConfig.userExistLogin(userInfo[0], userInfo[1])) {
                        FirebaseConfig.AddUser();
                    }
                }
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

    public void generateStickyFrame() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("views/stickyframe.fxml"));

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.getIcons().add(new Image("/images/logo.png"));
            primaryStage.setScene(new Scene(root));

            primaryStage.setX(Constants.WINDOW_WIDTH-350);
            primaryStage.setY(0);

            primaryStage.show();

            Constants.stickyStage = primaryStage;
            new SettingController().setSettingStage();
        }
        catch (Exception e) {}
    }
}