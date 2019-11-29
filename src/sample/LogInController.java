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

public class LogInController implements Initializable
{
    @FXML private JFXTextField signup_username, signup_password;
    @FXML private JFXTextField login_username, login_password;
    @FXML private JFXTextField signup_email, signup_confirm;
    @FXML private AnchorPane loginPage, signupPage;
    @FXML private ImageView closeButton;
    @FXML private Label signup;
    @FXML BorderPane backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // When the mouse hovers over to the signup label
        // Open the signup page when user clicks on this label text
        signup.setOnMouseEntered(event -> signup.setStyle("-fx-text-fill: #000"));
        signup.setOnMouseExited(event -> signup.setStyle("-fx-text-fill: #08f"));
        signup.setOnMouseClicked(event -> {
            signup.setStyle("-fx-text-fill: #08f");
            resetFields(false, 0);
        });

        // Close the login page when the user clicks on this button on the stage
        closeButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) loginPage.getScene().getWindow();
            stage.close();
        });

        // Allows the user to go back to the login page from the signup page
        backButton.setOnMouseClicked(event -> {
            resetFields(true, 1);
        });
    }// end initialise(URL, ResourceBundle)

    /**
     * Method Name : resetFields
     * Purpose : Reset the style of each fields on this stage
     *
     * @param disable - toggle between the stages (login and signup pane)
     * @param opacity - visibility level of the pane
     */
    private void resetFields(boolean disable, int opacity)
    {
        signup_username.setStyle("-fx-border-color: #ddd"); signup_password.setStyle("-fx-border-color: #ddd");
        login_username.setStyle("-fx-border-color: #ddd"); login_password.setStyle("-fx-border-color: #ddd");
        signup_confirm.setStyle("-fx-border-color: #ddd"); signup_email.setStyle("-fx-border-color: #ddd");

        signupPage.setDisable(disable); signupPage.setOpacity(opacity^1);
        backButton.setDisable(disable); backButton.setOpacity(opacity^1);
        loginPage.setDisable(!disable); loginPage.setOpacity(opacity);

    }// end resetFields(boolean, int)

    /**
     * Method Name : loginAction
     * Purpose : Perform action when the user clicks on the login button on the login page
     *
     * @param event
     */
    @FXML void loginAction(ActionEvent event)
    {
        // Get the username and password from the textfields
        String user = login_username.getText(), pass = login_password.getText();

        // If the username or password is empty, display it in RED color
        if(user.isEmpty()) login_username.setStyle("-fx-border-color: #f00");
        if(pass.isEmpty()) login_password.setStyle("-fx-border-color: #f00");

        // If username and password are not empty
        if(!user.isEmpty() && !pass.isEmpty())
        {
            // Check if the user with the username and password exists or not for valid login
            if(FirebaseConfig.userExistLogin(user, pass)) // successful login
            {
                // Close the login stage
                Stage stage = (Stage) loginPage.getScene().getWindow();
                stage.close();

                // Write the current user's username and password in the file for the auto-login next time
                FileRW.writeFile(user + " " + pass);

                // Open the StickyNotes stage with users notes from the firebase
                FirebaseConfig.AddUser();
            }
            // If the login fails
            else {
                login_password.setStyle("-fx-border-color: #f00");
            }// end if
        }// end if

    }// end loginAction(ActionEvent)

    /**
     * Method Name : signupAction
     * Purpose : Perform action when the user clicks on the signup  button on the signup page
     *
     * @param event
     */
    @FXML
    void signupAction(ActionEvent event) {

        // Check if the username is valid primary username or not and has not been used by the previous users
        if(FirebaseConfig.freeUsername(signup_username.getText())) // valid username for new user
        {
            // Close the signup stage
            Stage stage = (Stage) loginPage.getScene().getWindow();
            stage.close();

            // Write the current user's username and password in the file for the auto-login next time
            FileRW.writeFile(signup_username.getText() + " " + signup_password.getText());

            // Add this user to the firebase and details
            FirebaseConfig.addUserDetails(new UserDetail(signup_username.getText(),
                    signup_email.getText(), UserDetail.passwordHash(signup_password.getText())));

            // Open the StickyNotes stage with users notes from the firebase
            FirebaseConfig.AddUser();
        }
        // If the username is not a primary key
        else {
            signup_username.setStyle("-fx-border-color: #f00");
        }// end if

    }// end signupAction(ActionEvent)

}// end LogInController class
