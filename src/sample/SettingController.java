package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Firebase.FirebaseConfig;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML private BorderPane settingPane;
    @FXML private Label username, email, signoutLabel;
    @FXML private JFXButton syncButton;
    @FXML private Circle profilePane;
    @FXML private BorderPane closeButton, backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        closeButton.setOnMouseEntered(event -> closeButton.setStyle("-fx-background-color:#595959"));
        closeButton.setOnMouseReleased(event -> closeButton.setStyle("-fx-background-color:#000"));
        closeButton.setOnMouseExited(event -> closeButton.setStyle("-fx-background-color:#000"));
        closeButton.setOnMouseClicked(event -> {
            Constants.mainWindowClosed = true;
            FirebaseConfig.syncUserData();

            if(Constants.mainWindowClosed && Constants.openedNotes == 0) {
                System.exit(0);
            }

            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

        backButton.setOnMouseEntered(event -> backButton.setStyle("-fx-background-color:#595959"));
        backButton.setOnMouseReleased(event -> backButton.setStyle("-fx-background-color:#000"));
        backButton.setOnMouseExited(event -> backButton.setStyle("-fx-background-color:#000"));
        backButton.setOnMouseClicked(event -> {
            Constants.settingStage.hide();
            Constants.stickyStage.show();
        });

        signoutLabel.setOnMouseClicked(event -> {
            try {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();

                FileRW.writeFile("");

                StickyController.s_recyclerView = null; // refresh the session

                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.getIcons().add(new Image("/images/logo.png"));
                primaryStage.setScene(new Scene(root));
                primaryStage.setAlwaysOnTop(true);
                primaryStage.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        syncButton.setOnAction(event -> FirebaseConfig.syncUserData());

        profilePane.setFill(new ImagePattern(new Image("/images/myself.jpg")));

        username.setText(Constants.userDetail.getUsername());
        email.setText(Constants.userDetail.getEmail());

    }

    public void setSettingStage() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("views/setting.fxml"));

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.getIcons().add(new Image("/images/logo.png"));
            primaryStage.setScene(new Scene(root));

            primaryStage.setX(Constants.WINDOW_WIDTH-350);
            primaryStage.setY(0);

            primaryStage.setOnCloseRequest(e -> {
                System.out.println("exs");
            });
            Constants.settingStage = primaryStage;
        }
        catch (Exception e) {}
    }
}
