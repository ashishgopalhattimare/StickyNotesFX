package sample;

import java.time.LocalDateTime;

public class Constants {

    public static int WINDOW_WIDTH, WINDOW_HEIGHT;
    public static final String[] hexColor = { "#ffd11a", "#00cc00", "#ff80ff", "#d966ff", "#4db8ff", "#b3b3b3", "#404040"};
    public static final int LENGTH = 7;

    public static final int SPEED = 80;
    public static final String selectColor = "#d9d9d9";

    public static int randomColor;
    public static CardDetail card;

    public static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }
}
