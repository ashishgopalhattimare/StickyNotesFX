package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private JFXTextField signup_username, signup_password;
    @FXML private JFXTextField login_username, login_password;
    @FXML private JFXTextField signup_email, signup_confirm;
    @FXML private AnchorPane loginPage, signupPage;
    @FXML private Label signup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        signup.setOnMouseEntered(event -> signup.setStyle("-fx-text-fill: #000"));
        signup.setOnMouseExited(event -> signup.setStyle("-fx-text-fill: #08f"));
        signup.setOnMouseClicked(event -> {

            loginPage.setDisable(true); loginPage.setOpacity(0);
            signupPage.setDisable(false); signupPage.setOpacity(1);

            signup.setStyle("-fx-text-fill: #08f");

            signup_username.setStyle("-fx-border-color: #ddd"); signup_password.setStyle("-fx-border-color: #ddd");
            login_username.setStyle("-fx-border-color: #ddd"); login_password.setStyle("-fx-border-color: #ddd");
            signup_confirm.setStyle("-fx-border-color: #ddd"); signup_email.setStyle("-fx-border-color: #ddd");
        });
    }

    @FXML void loginAction(ActionEvent event) {

        String user = login_username.getText();
        String pass = login_password.getText();

        if(user.isEmpty()) login_username.setStyle("-fx-border-color: #f00");
        if(pass.isEmpty()) login_password.setStyle("-fx-border-color: #f00");

        if(!user.isEmpty() && !pass.isEmpty()) { // check user details
            if(FirebaseConfig.loginValid(user, pass)) {
                Stage stage = (Stage) loginPage.getScene().getWindow();
                stage.close();

                FirebaseConfig.userExist(user, pass);
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

            FirebaseConfig.addUserDetails(new UserDetail(signup_username.getText(),
                    signup_email.getText(), UserDetail.passwordHash(signup_password.getText())));
            FirebaseConfig.AddUser();
        }
        else {
            signup_username.setStyle("-fx-border-color: #f00");
        }
    }
}
