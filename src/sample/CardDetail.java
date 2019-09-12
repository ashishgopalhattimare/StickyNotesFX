package sample;

public class CardDetail
{
    private String text, date;
    private int color;

    private Card card;

    public CardDetail(String text, String date, int color) {
        this.text = text; this.date = date;
        this.color = color;
    }

    public String getText() { return text; }
    public String getDate() { return date; }
    public int getColor()   { return color;}

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() { return card; }
}
