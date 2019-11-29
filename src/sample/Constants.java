package sample;

import javafx.stage.Stage;
import sample.Firebase.FirebaseUser;

// System.setProperty("http.proxyHost", "webcache.example.com");
// System.setProperty("http.proxyPort", "8080");

// System.setProperty("java.net.useSystemProxies", "true");

import java.time.LocalDateTime;

public class Constants {

    public static int WINDOW_WIDTH, WINDOW_HEIGHT;
    public static final String[] HEXCOLOR = { "#99cc00", "#4775d1", "#ff80ff", "#d966ff", "#4db8ff", "#b3b3b3", "#404040"};
    public static final int LENGTH = 7;

    public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dev"};

    public static final int SPEED = 100;
    public static final String selectColor = "#d9d9d9";

    public static int randomColor;
    public static CardDetail card;
    public static Stage currStage;

    public static UserDetail userDetail;

    public static boolean mainWindowClosed = false;
    public static int openedNotes = 0;

    public static Stage stickyStage, settingStage;

    public static FirebaseUser fbDetails = null;

    public static final String FIREBASE_LINK = "https://stickynotes-efe5c.firebaseio.com/";

    public static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }
}
