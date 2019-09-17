package sample;

import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.kairos.layouts.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StickyController implements Initializable
{
    private BorderPane openBorder, deleteBorder;
    private ImageView openImage, deleteImage;
    private Label openLabel, deleteLabel;
    private HBox openBox, deleteBox;
    private PopOver popoverMenu;
    private Adapter adapter;

    private static boolean PopUpOpen;

    @FXML private BorderPane minimizeButton, settingButton, syncButton, stickyPane, pinButton;
    @FXML private BorderPane clearButton, searchButton, addButton, closeButton;
    @FXML private ImageView clearImage, syncImage, pinImage;
    @FXML private RecyclerView<CardDetail> recyclerView;
    @FXML private JFXTextField searchField;

    private boolean clearImageVisible;
    private int selectedTile;

    public static ArrayList<CardDetail> cardList = new ArrayList<>();
    public static RecyclerView<CardDetail> s_recyclerView = null;
    public static long prevTime, currTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(s_recyclerView == null) s_recyclerView = recyclerView;

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        PopUpOpen = false;

        createPopUpMenu();

        searchButton.setOnMouseReleased(event -> searchButton.setStyle("-fx-background-color:#737373"));
        searchButton.setOnMousePressed(event -> searchButton.setStyle("-fx-background-color:#595959"));
        searchButton.setOnMouseEntered(event -> searchButton.setStyle("-fx-background-color:#595959"));
        searchButton.setOnMouseExited(event -> searchButton.setStyle("-fx-background-color:#737373"));
        searchButton.setOnMouseClicked(event -> {

            searchButton.setStyle("-fx-background-color:#595959");
        });

        syncButton.setOnMouseEntered(event -> syncButton.setStyle("-fx-background-color:#595959"));
        syncButton.setOnMouseReleased(event -> syncButton.setStyle("-fx-background-color:#000"));
        syncButton.setOnMouseExited(event -> syncButton.setStyle("-fx-background-color:#000"));
        syncButton.setOnMouseClicked(this::syncHandler);

        pinButton.setOnMouseEntered(event -> pinButton.setStyle("-fx-background-color:#595959"));
        pinButton.setOnMouseReleased(event -> pinButton.setStyle("-fx-background-color:#000"));
        pinButton.setOnMouseExited(event -> pinButton.setStyle("-fx-background-color:#000"));
        pinButton.setOnMouseClicked(event -> {
            Constants.stickyStage.setAlwaysOnTop(!Constants.stickyStage.isAlwaysOnTop());

            if(Constants.stickyStage.isAlwaysOnTop()) pinImage.setImage(new Image("/images/ispinned.png"));
            else pinImage.setImage(new Image("/images/notpinned.png"));
        });

        addButton.setOnMouseEntered(event -> addButton.setStyle("-fx-background-color:#595959"));
        addButton.setOnMouseReleased(event -> addButton.setStyle("-fx-background-color:#000"));
        addButton.setOnMouseExited(event -> addButton.setStyle("-fx-background-color:#000"));
        addButton.setOnMouseClicked(event -> {
            try { addButton.setStyle("-fx-background-color:#595959");
                initNewNote("", -1, false);
            }
            catch (Exception e) { e.printStackTrace(); }
        });

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

        minimizeButton.setOnMouseEntered(event -> minimizeButton.setStyle("-fx-background-color:#595959"));
        minimizeButton.setOnMouseReleased(event -> minimizeButton.setStyle("-fx-background-color:#000"));
        minimizeButton.setOnMouseExited(event -> minimizeButton.setStyle("-fx-background-color:#000"));
        minimizeButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });

        settingButton.setOnMouseEntered(event -> settingButton.setStyle("-fx-background-color:#595959"));
        settingButton.setOnMouseReleased(event -> settingButton.setStyle("-fx-background-color:#000"));
        settingButton.setOnMouseExited(event -> settingButton.setStyle("-fx-background-color:#000"));
        settingButton.setOnMouseClicked(event -> {
            Constants.stickyStage.hide();
            Constants.settingStage.show();
        });

        clearButton.setOnMouseReleased(event -> {
            if(clearImageVisible) { clearButton.setStyle("-fx-background-color:#737373"); }
        });
        clearButton.setOnMouseEntered(event -> {
            if(clearImageVisible) { clearButton.setStyle("-fx-background-color:#595959"); }
        });
        clearButton.setOnMousePressed(event -> {
            if(clearImageVisible) { clearButton.setStyle("-fx-background-color:#595959"); }
        });
        clearButton.setOnMouseExited(event -> {
            if(clearImageVisible) { clearButton.setStyle("-fx-background-color:#737373"); }
        });
        clearButton.setOnMouseClicked(event -> {
            if(clearImageVisible) {
                searchField.setText(null);
                clearImageVisible = false;
                clearImage.setOpacity(0);
            }
        });

        clearImageVisible = false;
        clearImage.setOpacity(0);

        prevTime = 0;
    }

    private void syncHandler(MouseEvent event)
    {
        FirebaseConfig.syncUserData();

        syncButton.setStyle("-fx-background-color:#000");

        new Thread(() -> {
            RotateTransition rotate = new RotateTransition();
            rotate.setByAngle(360); rotate.setCycleCount(3);
            rotate.setDuration(Duration.millis(1000));
            rotate.setAxis(Rotate.Z_AXIS);

            rotate.setNode(syncImage);
            rotate.play();

            new Thread(() -> {
                try { for(int i = 1; i <= 100; i++) { Thread.sleep(1000); }
                }
                catch (Exception e) {}
            }).start();
        }).start();
    }

    @FXML void keyReleased(KeyEvent event)
    {
        clearImageVisible = (searchField.getText().length() > 0);
        if(clearImageVisible) clearImage.setOpacity(1);
        else clearImage.setOpacity(0);
    }

    private void createPopUpMenu() {

        deleteLabel = new Label("Delete note");
        openLabel = new Label("Open note");

        deleteImage = new ImageView(new Image("images/garbage.png"));
        openImage = new ImageView(new Image("images/open.png"));

        deleteImage.setPreserveRatio(true); openImage.setPreserveRatio(true);
        deleteImage.setFitHeight(20); openImage.setFitHeight(20);

        openBorder = new BorderPane(); deleteBorder = new BorderPane();

        openBorder.setCenter(openImage); deleteBorder.setCenter(deleteImage);

        deleteBorder.setPrefSize(25, 35); openBorder.setPrefSize(25, 35);
        deleteLabel.setPrefSize(80, 35); openLabel.setPrefSize(80, 35);

        deleteLabel.setAlignment(Pos.CENTER); openLabel.setAlignment(Pos.CENTER);

        deleteBox = new HBox(deleteBorder, deleteLabel); deleteBox.setStyle("-fx-background-color: #737373");
        openBox = new HBox(openBorder, openLabel); openBox.setStyle("-fx-background-color: #737373");

        VBox box = new VBox(openBox, deleteBox);

        popoverMenu = new PopOver();
        popoverMenu.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popoverMenu.setDetachable(false); popoverMenu.setArrowSize(0);
        popoverMenu.setCornerRadius(0);
        popoverMenu.setContentNode(box);

        deleteBox.setOnMouseEntered(event -> deleteBox.setStyle("-fx-background-color: #bfbfbf"));
        openBox.setOnMouseEntered(event -> openBox.setStyle("-fx-background-color: #bfbfbf"));

        deleteBox.setOnMouseExited(event -> deleteBox.setStyle("-fx-background-color: #737373"));
        openBox.setOnMouseExited(event -> openBox.setStyle("-fx-background-color: #737373"));

        deleteBox.setOnMouseClicked(event -> {

            selectedTile = recyclerView.getSelectionModel().getSelectedIndex();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/confirmdelete.fxml"));
                Parent root = loader.load();

                ConfirmController.selectedIndex = selectedTile;
                stickyPane.setDisable(true);

                Stage stage = new Stage(); stage.initStyle(StageStyle.UNDECORATED);
                stage.setAlwaysOnTop(true); stage.setScene(new Scene(root));
                stage.setX(Constants.WINDOW_WIDTH-315); stage.setY(200);

                stage.showAndWait();
                popoverMenu.hide();
            }
            catch (Exception e) {}
            finally { stickyPane.setDisable(false); }
        });
        openBox.setOnMouseClicked(event -> {

            selectedTile = recyclerView.getSelectionModel().getSelectedIndex();
            popoverMenu.hide();

            try {
                CardDetail card = cardList.get(selectedTile);
                initNewNote(card.getText(), selectedTile, card.isFavourite());
            }
            catch (Exception ignored) { }
        });
    }

    private void initNewNote(String text, int index, boolean favourite) throws Exception {

        if(index == -1) { Constants.randomColor = (int) (Math.random() * Constants.LENGTH); }
        else {
            Constants.randomColor = cardList.get(index).getColor();
            Constants.card = cardList.get(index);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/note.fxml"));
        Parent root = loader.load();

        NoteController controller = loader.getController();
        controller.setNoteArea(text);

        controller.setFavouriteImage(favourite);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setTitle("Sticky Note");

        Constants.currStage = stage;
        Constants.openedNotes++;

        stage.show();

        stage.setOnCloseRequest(e -> {

            FirebaseConfig.syncUserData();
            Constants.openedNotes--;
            if(Constants.mainWindowClosed && Constants.openedNotes == 0) {
                System.exit(0);
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<Card>
    {
        @Override
        public Card onCreateViewHolder(FXMLLoader fxmlLoader)
        {
            fxmlLoader.setLocation(getClass().getResource("views/cards.fxml"));
            Card card = new Card(fxmlLoader);
            return card;
        }

        @Override
        public void onBindViewHolder(Card card, Object obj) {

            CardDetail cd = (CardDetail)obj;
            cd.setCard(card);

            card.textArea.setText(cd.getText());
            card.date.setText(cd.getDate());

            card.colorPane.setStyle("-fx-background-color: " + Constants.HEXCOLOR[cd.getColor()]);
            card.date.setStyle("-fx-text-fill: " + Constants.HEXCOLOR[cd.getColor()]);

            card.cardBorderPane.setOnMouseExited(event -> {
                if(!PopUpOpen) {
                    card.date.setStyle("-fx-text-fill: " +Constants.HEXCOLOR[cd.getColor()]);
                    card.date.setText(cd.getDate());
                    card.noteBorder.setStyle("-fx-background-color:#737373");
                }
            });
            card.cardBorderPane.setOnMouseEntered(event -> {
                card.date.setStyle("-fx-text-fill: #bfbfbf");
                card.date.setText("•••");

                card.noteBorder.setStyle("-fx-background-color:#4d4d4d");
            });
            card.cardBorderPane.setOnMouseClicked(event -> {
                PopUpOpen = false;
                card.noteBorder.setStyle("-fx-background-color:#4d4d4d");

                currTime = System.currentTimeMillis();
                if(Math.abs(currTime-prevTime) <= 500) { // open this tile

                    selectedTile = recyclerView.getSelectionModel().getSelectedIndex();
                    popoverMenu.hide();

                    try { initNewNote(cd.getText(), selectedTile, cardList.get(selectedTile).isFavourite()); }
                    catch (Exception ignored) { }
                }
                prevTime = currTime;
            });

            card.date.setOnMouseExited(event -> { card.date.setStyle("-fx-text-fill: #bfbfbf"); });
            card.date.setOnMouseEntered(event -> { card.date.setStyle("-fx-text-fill: #fff"); });

            card.date.setOnMouseClicked(event -> {
                popoverMenu.show(card.date);

                popoverMenu.setAnimated(true);
                PopUpOpen = true;
            });

        }
    }
}

class Card extends RecyclerView.ViewHolder
{
    @FXML public Label textArea, date;
    @FXML public Pane colorPane;
    @FXML public BorderPane cardBorderPane, cardBorder, noteBorder;

    public Card(FXMLLoader loader) {
        super(loader);
    }
}