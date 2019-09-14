package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML private BorderPane closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        closeButton.setOnMouseEntered(event -> closeButton.setStyle("-fx-background-color:#595959"));
        closeButton.setOnMouseReleased(event -> closeButton.setStyle("-fx-background-color:#000"));
        closeButton.setOnMouseExited(event -> closeButton.setStyle("-fx-background-color:#000"));
    }
}
