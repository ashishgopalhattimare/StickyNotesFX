package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Firebase.FirebaseConfig;

import java.net.URL;
import java.util.ResourceBundle;

public class NoteController implements Initializable {

    @FXML private JFXButton color1, color2, color3, color4, color5, color6, color7;
    @FXML private BorderPane addButton, ellipseButton, deleteButton, favouriteButton, notePane;
    @FXML private JFXTextArea noteArea;
    @FXML private VBox scrollBox;
    @FXML private JFXListView<Pane> imageView;
    @FXML private ImageView favouriteImage;

    @FXML private FlowPane leftPane, rightPane;
    @FXML private Pane midPane, separator;

    private FadeTransition[] fadeArray = new FadeTransition[Constants.LENGTH];
    private JFXButton[] arrColor;

    private boolean colorDisplay, fadeInProcess;
    private double note_x, note_y;
    private CardDetail cardDetail;
    private int cardColor;
    private boolean cardFavourite = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        arrColor = new JFXButton[] {color1, color2, color3, color4, color5, color6, color7};
        for(int i = 0; i < Constants.LENGTH; i++) {
            fadeArray[i] = new FadeTransition(Duration.millis(Constants.SPEED), arrColor[i]);
            arrColor[i].setOpacity(0);
            
            arrColor[i].setOnAction(this::colorHandler);
        }

        colorDisplay = false; fadeInProcess = false;
        separator.setOpacity(0);

        /**
         * Open new Note in a new separate stage for writing new content
         */
        addButton.setOnMouseEntered(event -> addButton.setStyle("-fx-background-color:" + Constants.selectColor));
        addButton.setOnMouseReleased(event -> addButton.setStyle(""));
        addButton.setOnMouseExited(event -> addButton.setStyle(""));
        addButton.setOnMouseClicked(event -> {
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("views/note.fxml"))));
                stage.getIcons().add(new Image("/images/logo.png"));
                stage.setTitle("Sticky Note");

                addButton.setStyle("-fx-background-color:" + Constants.selectColor);

                // Choose a random color for this new note
                Constants.randomColor = (int) (Math.random() * Constants.LENGTH);
                Constants.currStage = stage;

                Constants.openedNotes++; // Increase the number of opened stages on the platform by 1

                stage.show(); // display the note stage

                // Once the stage is closed, performt the following action
                stage.setOnCloseRequest(syncData -> {

                    FirebaseConfig.syncUserData(); // Sync the note of the user in the Google Firebase
                    Constants.openedNotes--; // decreaes the number of the opened stages on the platform by 1

                    // If the count reaches 0, force the application to close
                    if(Constants.mainWindowClosed && Constants.openedNotes == 0) {
                        System.exit(0);
                    }// end if
                });
            }
            catch (Exception ignored) {} // end try/catch
        });

        /**
         * Delete the current Note from the user's account as well
         */
        deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color:" + Constants.selectColor));
        deleteButton.setOnMouseReleased(event -> deleteButton.setStyle(""));
        deleteButton.setOnMouseExited(event -> deleteButton.setStyle(""));
        deleteButton.setOnMouseClicked(this::deleteNote);

        /**
         * Mark this note as favourite
         */
        favouriteButton.setOnMouseEntered(event -> favouriteButton.setStyle("-fx-background-color:" + Constants.selectColor));
        favouriteButton.setOnMouseReleased(event -> favouriteButton.setStyle(""));
        favouriteButton.setOnMouseExited(event -> favouriteButton.setStyle(""));
        favouriteButton.setOnMouseClicked(event -> {
            if(cardDetail != null){
                cardDetail.setFavourite(!cardDetail.isFavourite());
                cardFavourite = cardDetail.isFavourite();
            }
            else {
                cardFavourite = !(cardFavourite);
            }// end if

            // Update the image of the favourite
            if(cardFavourite) favouriteImage.setImage(new Image("/images/isfavourite.png"));
            else favouriteImage.setImage(new Image("/images/notfavourite.png"));
        });

        /**
         * If the user wants to change the color of the user's note
         */
        ellipseButton.setOnMouseEntered(event -> ellipseButton.setStyle("-fx-background-color:" + Constants.selectColor));
        ellipseButton.setOnMouseReleased(event -> ellipseButton.setStyle(""));
        ellipseButton.setOnMouseExited(event -> ellipseButton.setStyle(""));
        ellipseButton.setOnMouseClicked(event -> {

            // If the animation of the color display is not in progress
            if(!fadeInProcess)
            {
                ellipseButton.setStyle("-fx-background-color:" + Constants.selectColor);
                fadeInProcess = true;

                // Display the colors on order
                if(!colorDisplay) { displayColors(fadeArray, Constants.LENGTH-1); }
                else {
                    separator.setOpacity(0); disappearColors(fadeArray, 0);
                }

                // Toggle when to open and close the color bar
                colorDisplay = !(colorDisplay);
            }// end if
        });

        // Perform action of every KeyEvent on the note
        noteArea.setOnKeyReleased(event -> {
            // Card does not exist, if the note is empty
            if(cardDetail != null) { // note object is not currently created on the system

                // Update the text of the card object on the array
                cardDetail.getCard().textArea.setText(noteArea.getText());
                // Directly update the card text
                cardDetail.setText(noteArea.getText());

                // If the note is not empty
                if(noteArea.getText().length() > 0) {

                    cardDetail.setDateTime(); // update the last updated time of the note

                    // The current note is not the top note of the list as they are they by most recently
                    // used to least recently used note
                    if(StickyController.s_recyclerView.getItems().get(0) != cardDetail) {

                        // Remove the note from the lists
                        StickyController.s_recyclerView.getItems().remove(cardDetail);
                        StickyController.cardList.remove(cardDetail);

                        // Append the note at the top of the list
                        StickyController.s_recyclerView.getItems().add(0, cardDetail);
                        StickyController.cardList.add(0, cardDetail);
                    }
                    // Directly update the first note
                    else {
                        cardDetail.getCard().date.setText(cardDetail.getDate());
                    }// end if
                }
                // if the note is empty, remove it from the Sticky List
                else {
                    // Remove the note from the list
                    StickyController.s_recyclerView.getItems().remove(cardDetail);
                    StickyController.cardList.remove(cardDetail);
                    cardDetail = null;
                }// end if
            }
            else {
                // note does not exist, and is not empty -> place it at the top of the Sticky List
                if(noteArea.getText().length() > 0)
                {
                    cardDetail = new CardDetail(noteArea.getText(), cardColor, cardFavourite);
                    StickyController.s_recyclerView.getItems().add(0, cardDetail);
                    StickyController.cardList.add(0, cardDetail);
                }// end if
            }// end if
        });

        cardColor = Constants.randomColor; // Get the color of the card
        fillTitleBarColor(Constants.HEXCOLOR[cardColor]); // fill the color

        cardDetail = Constants.card; // get the card details
        Constants.card = null; // next card

    }// end initialize(URL, ResourceBundle)



    /**
     * Method Name : fillTitleBarColor
     * Purpose : Change the color of the note bar
     *
     * @param color - color to which the note bar has to be updated
     */
    private void fillTitleBarColor(String color) {
        rightPane.setStyle("-fx-background-color:" + color);
        leftPane.setStyle("-fx-background-color:" + color);
        midPane.setStyle("-fx-background-color:" + color);

    }// end fillTitleBarColor(String)

    /**
     * Method Name : colorHandler
     * Purpose : From the range of colors, decide the color and update the note bar to that color
     *
     * @param event
     */
    public void colorHandler(ActionEvent event) {

        if(fadeInProcess && cardDetail == null) return;

        // Iterate through all the colors panes
        for(int i = 0; i < Constants.LENGTH; i++) {
            if(event.getSource() == arrColor[i]) { // if the pane index found
                cardColor = i; // get the color at that index

                // Update the note bar color
                fillTitleBarColor(Constants.HEXCOLOR[cardColor = i]);

                // If the cardDetail exists, then store the details as well
                try {
                    cardDetail.getCard().colorPane.setStyle("-fx-background-color: " + Constants.HEXCOLOR[cardColor]);
                    cardDetail.getCard().date.setStyle("-fx-text-fill: " + Constants.HEXCOLOR[cardColor]);
                    cardDetail.setColor(cardColor);
                }
                catch (Exception e) {} // end try/catch

                break;
            }// end if
        }// end for

    }// end colorHandler(ActionEvent)

    // Update the notepad text
    public void setNoteArea(String text) {
        noteArea.setText(text);
    }

    /**
     * Method Name : displayColors
     * Purpose : display the colors on the note bar
     * @param fadeArray - the color which should be displayed now
     * @param index - index of the color displaying away
     */
    private void displayColors(FadeTransition[] fadeArray, int index) {
        if(index == -1) { fadeInProcess = false; separator.setOpacity(1); return;}

        fadeArray[index].setFromValue(0);
        fadeArray[index].setToValue(1);
        fadeArray[index].play();

        fadeArray[index].setOnFinished(event -> displayColors(fadeArray, index-1));

    }// end displayColors(FaderTransition[], int)

    /**
     * Method Name : disappearColors
     * Purpose : disappear the colors from the note, if not in use
     * @param fadeArray - the color which should be faded now
     * @param index - index of the color fading away
     */
    private void disappearColors(FadeTransition[] fadeArray, int index) {
        if(index == Constants.LENGTH) { fadeInProcess = false; return; }

        fadeArray[index].setFromValue(1);
        fadeArray[index].setToValue(0);
        fadeArray[index].play();
        fadeArray[index].setOnFinished(event -> disappearColors(fadeArray, index + 1));

    }// end disappearColors(FadeTransition[], int)

    // Update the favourite Image on the note
    public void setFavouriteImage(boolean favourite) {
        if(favourite) favouriteImage.setImage(new Image("/images/isfavourite.png"));
        else favouriteImage.setImage(new Image("/images/notfavourite.png"));

    }// end setFavouriteImage(boolean)

    /**
     * Method Name : deleteNote
     * Purpose : delete the current note from the user's account
     *
     * @param event
     */
    private void deleteNote(MouseEvent event)
    {
        // If the cardDetail exists, remove the card
        if(cardDetail != null)
        {
            StickyController.s_recyclerView.getItems().remove(cardDetail);
            StickyController.cardList.remove(cardDetail);
        }// end if

        FirebaseConfig.syncUserData(); // Sync the note of the user in the Google Firebase
        Constants.openedNotes--; // decreaes the number of the opened stages on the platform by 1

        // If the count reaches 0, force the application to close
        if(Constants.mainWindowClosed && Constants.openedNotes == 0) {
            System.exit(0);
        }// end if

        // Close this stage immediately
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    // ALLOW THE USER TO DRAG THE STAGE ON THE SCREEN
    @FXML public void mouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setY(event.getScreenY() - note_y);
        stage.setX(event.getScreenX() - note_x);

    }// end mouseDragged(MouseEvent)

    @FXML public void mousePressed(MouseEvent event) {
        note_x = event.getSceneX();
        note_y = event.getSceneY();

    }// end mousePressed(MouseEvent)

}// end NoteController class
