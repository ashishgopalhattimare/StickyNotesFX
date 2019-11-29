package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Firebase.FirebaseConfig;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private JFXTextField signup_username, signup_password;
    @FXML private JFXTextField login_username, login_password;
    @FXML private JFXTextField signup_email, signup_confirm;
    @FXML private AnchorPane loginPage, signupPage;
    @FXML private ImageView closeButton;
    @FXML private Label signup;
    @FXML BorderPane backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        signup.setOnMouseEntered(event -> signup.setStyle("-fx-text-fill: #000"));
        signup.setOnMouseExited(event -> signup.setStyle("-fx-text-fill: #08f"));
        signup.setOnMouseClicked(event -> {
            signup.setStyle("-fx-text-fill: #08f");
            resetFields(false, 0);
        });

        closeButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) loginPage.getScene().getWindow();
            stage.close();
        });

        backButton.setOnMouseClicked(event -> {
            resetFields(true, 1);
        });
    }

    private void resetFields(boolean disable, int opacity)
    {
        signup_username.setStyle("-fx-border-color: #ddd"); signup_password.setStyle("-fx-border-color: #ddd");
        login_username.setStyle("-fx-border-color: #ddd"); login_password.setStyle("-fx-border-color: #ddd");
        signup_confirm.setStyle("-fx-border-color: #ddd"); signup_email.setStyle("-fx-border-color: #ddd");

        signupPage.setDisable(disable); signupPage.setOpacity(opacity^1);
        backButton.setDisable(disable); backButton.setOpacity(opacity^1);
        loginPage.setDisable(!disable); loginPage.setOpacity(opacity);
    }

    @FXML void loginAction(ActionEvent event) {

        String user = login_username.getText(), pass = login_password.getText();

        if(user.isEmpty()) login_username.setStyle("-fx-border-color: #f00");
        if(pass.isEmpty()) login_password.setStyle("-fx-border-color: #f00");

        if(!user.isEmpty() && !pass.isEmpty()) { // check user details
            if(FirebaseConfig.userExistLogin(user, pass)) {
                Stage stage = (Stage) loginPage.getScene().getWindow();
                stage.close();

                FileRW.writeFile(user + " " + pass);

                FirebaseConfig.AddUser();
            }
            else {
                login_password.setStyle("-fx-border-color: #f00");
            }
        }
    }

    @FXML
    void signupAction(ActionEvent event) {

        if(FirebaseConfig.freeUsername(signup_username.getText())) {
            Stage stage = (Stage) loginPage.getScene().getWindow();
            stage.close();

            FileRW.writeFile(signup_username.getText() + " " + signup_password.getText());

            FirebaseConfig.addUserDetails(new UserDetail(signup_username.getText(),
                    signup_email.getText(), UserDetail.passwordHash(signup_password.getText())));
            FirebaseConfig.AddUser();
        }
        else {
            signup_username.setStyle("-fx-border-color: #f00");
        }
    }
}
