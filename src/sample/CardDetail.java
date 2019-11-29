package sample;

import java.time.LocalDateTime;

public class CardDetail
{
    private int color;          // Color of the note
    private String text;        // text in the note
    private String date;        // last updated time
    private Card card;          // Card features
    private boolean favourite;  // isFavourite card or not

    private LocalDateTime dateTime;

    public CardDetail(String text, int color, boolean favourite) {
        this.text = text; this.color = color;
        this.favourite = favourite;
        setDateTime();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() { return text; }
    public String getDate() { return date; }

    public void setDefaultDate(String date){
        this.date = date;
    }

    public int getColor() { return color; }
    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFavourite() { return favourite; }
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public Card getCard() { return card; }
    public void setCard(Card card) {
        this.card = card;
    }

    public void setDateTime() {
        dateTime = Constants.getDateTime();
        date = dateTime.toString();
//        date = dateTime.getDayOfMonth() + " " + Constants.MONTHS[dateTime.getMonthValue()-1];
    }
}
