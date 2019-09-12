package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteController implements Initializable {

    @FXML private JFXTextArea noteArea;
    @FXML private BorderPane addButton, ellipseButton, deleteButton, progressBar;
    @FXML private JFXButton color1, color2, color3, color4, color5, color6, color7;

    @FXML private FlowPane leftPane, rightPane;
    @FXML private Pane midPane, separator;

    private FadeTransition[] fadeArray = new FadeTransition[Constants.LENGTH];
    private JFXButton[] arrColor;

    private boolean colorDisplay, fadeInProcess;
    private double note_x, note_y;
    private int initialColor;
    private CardDetail cardDetail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        arrColor = new JFXButton[] {color1, color2, color3, color4, color5, color6, color7};
        for(int i = 0; i < Constants.LENGTH; i++) {
            fadeArray[i] = new FadeTransition(Duration.millis(Constants.SPEED), arrColor[i]);
            arrColor[i].setOpacity(0);
            
            arrColor[i].setOnAction(this::colorHandler);
        }

        colorDisplay = false; fadeInProcess = false;
        // progressBar.setOpacity(0);
        separator.setOpacity(0);

        addButton.setOnMouseEntered(event -> addButton.setStyle("-fx-background-color:" + Constants.selectColor));
        addButton.setOnMouseReleased(event -> addButton.setStyle(""));
        addButton.setOnMouseExited(event -> addButton.setStyle(""));
        addButton.setOnMouseClicked(event -> {
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("note.fxml"))));
                stage.getIcons().add(new Image("/images/logo.png"));
                stage.setTitle("Sticky Note");

                Constants.randomColor = (int) (Math.random() * Constants.LENGTH);
                stage.show();

                addButton.setStyle("-fx-background-color:" + Constants.selectColor);
            }
            catch (Exception e) {}
        });

        deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color:" + Constants.selectColor));
        deleteButton.setOnMouseReleased(event -> deleteButton.setStyle(""));
        deleteButton.setOnMouseExited(event -> deleteButton.setStyle(""));
        deleteButton.setOnMouseClicked(this::deleteNote);

        ellipseButton.setOnMouseEntered(event -> ellipseButton.setStyle("-fx-background-color:" + Constants.selectColor));
        ellipseButton.setOnMouseReleased(event -> ellipseButton.setStyle(""));
        ellipseButton.setOnMouseExited(event -> ellipseButton.setStyle(""));
        ellipseButton.setOnMouseClicked(event -> {
            if(fadeInProcess == false) {
                ellipseButton.setStyle("-fx-background-color:" + Constants.selectColor);
                fadeInProcess = true;

                if(colorDisplay == false) { displayColors(fadeArray, Constants.LENGTH-1); }
                else {
                    separator.setOpacity(0); disappearColors(fadeArray, 0);
                }

                colorDisplay = !(colorDisplay);
            }
        });

        noteArea.setOnKeyReleased(event -> {
            if(cardDetail != null) {
                cardDetail.getCard().textArea.setText(noteArea.getText());

                if(noteArea.getText().length() > 0) { // if the note is not empty
                    if(StickyController.s_recyclerView.getItems().get(0) != cardDetail) {

                        StickyController.s_recyclerView.getItems().remove(cardDetail);
                        StickyController.s_recyclerView.getItems().add(0, cardDetail);
                    }
                }
                else { // if the note is empty, remove it from the Sticky List
                    StickyController.s_recyclerView.getItems().remove(cardDetail);
                    cardDetail = null;
                }
            }
            else {
                // note does not exist, and is not empty -> place it at the top of the Sticky List
                if(noteArea.getText().length() > 0) {
                    cardDetail = new CardDetail(noteArea.getText() , "10 Sep", initialColor);
                    StickyController.s_recyclerView.getItems().add(0, cardDetail);
                }
            }
        });

        initialColor = Constants.randomColor;
        fillTitleBarColor(Constants.hexColor[initialColor]);

        cardDetail = Constants.card;
        Constants.card = null;
    }

    private void fillTitleBarColor(String color) {
        rightPane.setStyle("-fx-background-color:" + color);
        leftPane.setStyle("-fx-background-color:" + color);
        midPane.setStyle("-fx-background-color:" + color);
    }
    
    public void colorHandler(ActionEvent event) {
        if(fadeInProcess) return;

        for(int i = 0; i < Constants.LENGTH; i++) {
            if(event.getSource() == arrColor[i]) {
                fillTitleBarColor(Constants.hexColor[i]);
            }
        }
    }

    private void displayColors(FadeTransition[] fadeArray, int index) {
        if(index == -1) { fadeInProcess = false; separator.setOpacity(1); return;}

        fadeArray[index].setFromValue(0);
        fadeArray[index].setToValue(1);
        fadeArray[index].play();

        fadeArray[index].setOnFinished(event -> displayColors(fadeArray, index-1));
    }

    private void disappearColors(FadeTransition[] fadeArray, int index) {
        if(index == Constants.LENGTH) { fadeInProcess = false; return; }

        fadeArray[index].setFromValue(1);
        fadeArray[index].setToValue(0);
        fadeArray[index].play();
        fadeArray[index].setOnFinished(event -> disappearColors(fadeArray, index+1));
    }

    public void setNoteArea(String text) {
        noteArea.setText(text);
    }

    @FXML public void mouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setY(event.getScreenY() - note_y);
        stage.setX(event.getScreenX() - note_x);

    }

    @FXML public void mousePressed(MouseEvent event) {
        note_x = event.getSceneX();
        note_y = event.getSceneY();
    }

    private void deleteNote(MouseEvent event)
    {
        if(cardDetail != null)
        {
            StickyController.s_recyclerView.getItems().remove(cardDetail);
        }

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}