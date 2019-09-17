package sample;

import javafx.stage.Stage;

import java.time.LocalDateTime;

public class Constants {

    public static int WINDOW_WIDTH, WINDOW_HEIGHT;
    public static final String[] HEXCOLOR = { "#ffd11a", "#00cc00", "#ff80ff", "#d966ff", "#4db8ff", "#b3b3b3", "#404040"};
    public static final int LENGTH = 7;

    public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dev"};

    public static final int SPEED = 100;
    public static final String selectColor = "#d9d9d9";

    public static int randomColor;
    public static CardDetail card;
    public static Stage currStage;

    public static UserDetail user;
    public static boolean mainWindowClosed = false;

    public static FirebaseUserDetail fbDetails = null;

    public static final String FIREBASE_LINK = "https://stickynotes-efe5c.firebaseio.com/";

    public static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }
}
