package sample;

import java.time.LocalDateTime;

public class CardDetail
{
    private String text, date;
    private int color;
    private Card card;

    private LocalDateTime dateTime;

    public CardDetail(String text, int color) {
        this.text = text; this.color = color;
        setDateTime();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() { return text; }
    public String getDate() { return date; }
    public int getColor()   { return color;}

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() { return card; }

    public void setDateTime() {
        dateTime = Constants.getDateTime();
        date = dateTime.toString();
    }
}
