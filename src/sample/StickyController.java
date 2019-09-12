package sample;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;
import org.kairos.layouts.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StickyController implements Initializable {

    private static boolean PopUpOpen;

    private PopOver popoverMenu;
    private HBox openBox, deleteBox;
    private Label openLabel, deleteLabel;
    private ImageView openImage, deleteImage;

    private Adapter adapter;

    public static RecyclerView<CardDetail> s_recyclerView = null;

    @FXML private RecyclerView<CardDetail> recyclerView;
    @FXML private BorderPane clearButton, searchButton, addButton, closeButton;
    @FXML private BorderPane minimizeButton, settingButton;
    @FXML private JFXTextField searchField;
    @FXML private ImageView clearImage;

    private boolean clearImageVisible;
    private int selectedTile;

    public static ArrayList<CardDetail> cardList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        if(recyclerView == null) s_recyclerView = recyclerView;

        cardList.add(new CardDetail("Surbhi" , "10 Sep", (int) (Math.random() * Constants.LENGTH)));
        cardList.add(new CardDetail("Ashish" , "11 Sep", (int) (Math.random() * Constants.LENGTH)));
        cardList.add(new CardDetail("Yogesh" , "12 Sep", (int) (Math.random() * Constants.LENGTH)));
        cardList.add(new CardDetail("Vinnet" , "13 Sep", (int) (Math.random() * Constants.LENGTH)));
        cardList.add(new CardDetail("Dogras" , "14 Sep", (int) (Math.random() * Constants.LENGTH)));

        for(CardDetail x : cardList) recyclerView.getItems().add(x);
        PopUpOpen = false;

        createPopUpMenu();

        searchButton.setOnMouseReleased(event -> searchButton.setStyle("-fx-background-color:#737373"));
        searchButton.setOnMousePressed(event -> searchButton.setStyle("-fx-background-color:#595959"));
        searchButton.setOnMouseEntered(event -> searchButton.setStyle("-fx-background-color:#595959"));
        searchButton.setOnMouseExited(event -> searchButton.setStyle("-fx-background-color:#737373"));
        searchButton.setOnMouseClicked(event -> {

            searchButton.setStyle("-fx-background-color:#595959");
        });

        addButton.setOnMouseEntered(event -> addButton.setStyle("-fx-background-color:#595959"));
        addButton.setOnMouseReleased(event -> addButton.setStyle("-fx-background-color:#000"));
        addButton.setOnMouseExited(event -> addButton.setStyle("-fx-background-color:#000"));
        addButton.setOnMouseClicked(event -> {
            try { initNewNote("", -1); addButton.setStyle("-fx-background-color:#595959"); }
            catch (Exception e) {}
        });

        closeButton.setOnMouseEntered(event -> closeButton.setStyle("-fx-background-color:#595959"));
        closeButton.setOnMouseReleased(event -> closeButton.setStyle("-fx-background-color:#000"));
        closeButton.setOnMouseExited(event -> closeButton.setStyle("-fx-background-color:#000"));
        closeButton.setOnMouseClicked(event -> {
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
            System.out.println("Setting Open");

            settingButton.setStyle("-fx-background-color:#595959");
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

        deleteLabel.setPrefSize(90, 35);
        openLabel.setPrefSize(90, 35);

        deleteLabel.setAlignment(Pos.CENTER); deleteLabel.setStyle("-fx-background-color:#737373; -fx-text-fill:#fff");
        openLabel.setAlignment(Pos.CENTER); openLabel.setStyle("-fx-background-color:#737373; -fx-text-fill:#fff");

        deleteBox = new HBox(deleteLabel); openBox = new HBox(openLabel);

        VBox box = new VBox(openBox, deleteBox);

        popoverMenu = new PopOver();
        popoverMenu.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popoverMenu.setDetachable(false); popoverMenu.setArrowSize(0);
        popoverMenu.setCornerRadius(0);
        popoverMenu.setContentNode(box);

        deleteBox.setOnMouseEntered(event -> deleteLabel.setStyle("-fx-background-color: #bfbfbf"));
        openBox.setOnMouseEntered(event -> openLabel.setStyle("-fx-background-color: #bfbfbf"));

        deleteBox.setOnMouseExited(event -> deleteLabel.setStyle("-fx-background-color: #737373"));
        openBox.setOnMouseExited(event -> openLabel.setStyle("-fx-background-color: #737373"));

        deleteBox.setOnMouseClicked(event -> {

            selectedTile = recyclerView.getSelectionModel().getSelectedIndex();
            System.out.println("Delete Tile : " + selectedTile);

            recyclerView.getItems().remove(selectedTile);
            cardList.remove(selectedTile);

            popoverMenu.hide();
        });
        openBox.setOnMouseClicked(event -> {

            selectedTile = recyclerView.getSelectionModel().getSelectedIndex();
            popoverMenu.hide();

            try {
                initNewNote(
                        "public class A {\n" +
                                "    public static void main(String[] args) {\n" +
                                "        System.out.println(\"Hello World\");\n" +
                                "    }\n" +
                                "}", selectedTile);
            }
            catch (Exception ignored) { }
        });
    }

    private void initNewNote(String text, int index) throws Exception {

        if(index == -1) Constants.randomColor = (int) (Math.random() * Constants.LENGTH);
        else {
            Constants.randomColor = cardList.get(index).getColor();
        }
        Constants.card = cardList.get(index);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("note.fxml"));
        Parent root = loader.load();

        NoteController controller = loader.getController();
        controller.setNoteArea(text);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.setTitle("Sticky Note");
        stage.show();

        stage.getIcons().add(new Image("/images/logo.png"));
    }

    class Adapter extends RecyclerView.Adapter<Card>
    {
        @Override
        public Card onCreateViewHolder(FXMLLoader fxmlLoader) {

            fxmlLoader.setLocation(getClass().getResource("cards.fxml"));
            Card card = new Card(fxmlLoader);

            return card;
        }

        @Override
        public void onBindViewHolder(Card card, Object obj) {

            CardDetail cd = (CardDetail)obj;
            cd.setCard(card);

            card.textArea.setText(cd.getText());
            card.date.setText(cd.getDate());

            card.colorPane.setStyle("-fx-background-color: " + Constants.hexColor[cd.getColor()]);
            card.date.setStyle("-fx-text-fill: " + Constants.hexColor[cd.getColor()]);

            card.cardBorderPane.setOnMouseExited(event -> {
                if(!PopUpOpen) {
                    card.date.setStyle("-fx-text-fill: " +Constants.hexColor[cd.getColor()]);
                    card.date.setText(cd.getDate());
                }
            });
            card.cardBorderPane.setOnMouseEntered(event -> {
                card.date.setStyle("-fx-text-fill: #bfbfbf");
                card.date.setText("•••");
            });
            card.cardBorderPane.setOnMouseClicked(event -> {
                PopUpOpen = false;
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

class Card extends RecyclerView.ViewHolder {

    @FXML public Label textArea;
    @FXML public Label date;
    @FXML public Pane colorPane;
    @FXML public BorderPane cardBorderPane;

    public Card(FXMLLoader loader) {
        super(loader);
    }
}