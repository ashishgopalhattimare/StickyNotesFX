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

        fadeOut.play(); // Start the splash loading

        // After the splash event ents
        fadeOut.setOnFinished(event -> {
            try {
                // Get the current username and password, if exists
                String [] userInfo = FileRW.readFile();

                // If the userInfo is invalid or empty, open login page
                if(userInfo == null || userInfo.length != 2)
                {
                    Stage primaryStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    primaryStage.getIcons().add(new Image("/images/logo.png"));
                    primaryStage.setScene(new Scene(root));
                    primaryStage.setAlwaysOnTop(true);
                    primaryStage.show();
                }
                // username and password are present, assume they are not modified "externally"
                else {
                    // Try to Login
                    if(FirebaseConfig.userExistLogin(userInfo[0], userInfo[1])) {
                        FirebaseConfig.AddUser();
                    }// end if
                }// end if
            }
            catch (Exception ignored) {}
            finally {
                Stage stage = (Stage) progressBar.getScene().getWindow();
                stage.close();
            }// end try/catch/finally
        });

    }// end initialise(URL, ResourceBundle)

    // Open the Sticky Frame for the user
    public void generateStickyFrame() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("views/stickyframe.fxml"));

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.getIcons().add(new Image("/images/logo.png"));
            primaryStage.setScene(new Scene(root));

            // Fix its position on Top-Right of the window
            primaryStage.setX(Constants.WINDOW_WIDTH - 350);
            primaryStage.setY(0);

            primaryStage.show();

            Constants.stickyStage = primaryStage;
            new SettingController().setSettingStage();
        }
        catch (Exception ignored) {} // end try/catch

    }// end generateStickyFrame()

}// end SplashController  class