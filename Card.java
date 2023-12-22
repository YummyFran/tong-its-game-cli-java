public class Card {
    private final String suit;
    private final String value;
    private int numericValue;
    private int numericSuit;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
        initNumeric();
        initSuit();
    }
    
    public void initNumeric() {
        if (value.equals("A")) numericValue = 1;
        else if (value.equals("J")) numericValue = 11;
        else if (value.equals("Q")) numericValue = 12;
        else if (value.equals("K")) numericValue = 13;
        else numericValue = Integer.parseInt(value);
    }
    
    public void initSuit() {
        if(suit.equals("♥")) numericSuit = 1;
        else if(suit.equals("♦")) numericSuit = 2;
        else if(suit.equals("♣")) numericSuit = 3;
        else if(suit.equals("♠")) numericSuit = 4;
    }

    public String getSuit() { return suit; }
    public String getValue() { return value; }
    public int getNumericValue() { return numericValue; }
    public int getNumericSuit() { return numericSuit; }
    public String toString() { return value + " " + suit; }
}
