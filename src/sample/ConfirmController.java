package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import sample.Firebase.FirebaseConfig;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmController implements Initializable
{
    @FXML private JFXButton deleteButton, keepButton;
    public static int selectedIndex;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // To confirm if the user wants to delete the current note or not
        deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-radius: 0; -fx-background-color: #1aa3ff; -fx-text-fill: #fff;"));
        deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-radius: 0; -fx-background-color: #1aa3ff; -fx-text-fill: #000;"));
        deleteButton.setOnAction(event -> {
            StickyController.s_recyclerView.getItems().remove(selectedIndex);
            StickyController.cardList.remove(selectedIndex);

            FirebaseConfig.syncUserData();

            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
        });

        // To confirm if the user wants to keep the current note or not
        keepButton.setOnMouseEntered(event -> keepButton.setStyle("-fx-background-radius: 0; -fx-background-color: #a1a1a1; -fx-text-fill: #fff;"));
        keepButton.setOnMouseExited(event -> keepButton.setStyle("-fx-background-radius: 0; -fx-background-color: #a1a1a1; -fx-text-fill: #000;"));
        keepButton.setOnAction(event -> {
            Stage stage = (Stage) keepButton.getScene().getWindow();
            stage.close();
        });

    }
}
