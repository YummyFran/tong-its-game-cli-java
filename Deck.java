import java.util.*;

public class Deck {
    private final String[] suits = {"♥", "♦", "♣", "♠"};
    private final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private final Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        generateDeck();
        shuffle();
    }

    private void generateDeck() {
        for (String suit : suits) {
            for (String value : values) {
                Card card = new Card(suit, value);
                cards.push(card);
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.empty() ? null : cards.pop();
    }

    public int size() {
        return cards.size();
    }
}
