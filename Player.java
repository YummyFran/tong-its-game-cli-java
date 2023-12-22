import java.util.LinkedList;

public class Player {
    public String name;
    private LinkedList<LinkedList<Card>> sets;
    private LinkedList<Card> cardsOnHand;
    private boolean drew = false;
    private boolean sortByNumber = true;

    public Player(String name) {
        this.name = name.substring(0,1).toUpperCase() + name.substring(1);
        sets = new LinkedList<>();
        cardsOnHand = new LinkedList<>();
    }

    public void bunot(Card card) {
        cardsOnHand.add(card);
    }

    public Card labay(int index) {
        return index >= 0 && index < cardsOnHand.size() ? cardsOnHand.remove(index) : null;
    }

    public LinkedList<Card> getCards() {
        return cardsOnHand;
    }

    public void printCards() {
        int cardHeight = 12;
        
        for(int i = 0; i < cardHeight; i++) {
            for(int j = 0; j < cardsOnHand.size(); j++) {
                Card card = cardsOnHand.get(j);
                if(i == 0 || i == cardHeight - 1) System.out.print(" ______");
                else if(i == 2) System.out.printf("| %s%s ", card, card.getValue() == "10" ? "" : " ");
                else if(i == cardHeight - 2) System.out.printf("| %d%s   ", j, j >= 10 ? "" : " ");
                else System.out.print("|      ");
            }
            
            if(i == 0 || i == cardHeight - 1) System.out.print("_______");
            else System.out.print("       |");
            System.out.println();
        }
    }
    
    public void sortCards() {
        int n = cardsOnHand.size();
        
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n - 1; j++) {
                Card card1 = cardsOnHand.get(j);
                Card card2 = cardsOnHand.get(j + 1);
                
                if(card1.getNumericValue() > card2.getNumericValue()) {
                    cardsOnHand.set(j, card2);
                    cardsOnHand.set(j + 1, card1);
                }
                
                if(!sortByNumber) {
                    if(card1.getNumericSuit() > card2.getNumericSuit()) {
                        cardsOnHand.set(j, card2);
                        cardsOnHand.set(j + 1, card1);
                    }
                }  
            }
        }
        sortByNumber = !sortByNumber;
    }
    
    public void identifySets() {
        sets.clear();
        sortCards();
        findSequences();
        findDegrees();
    }
    
    private void findSequences() {
        LinkedList<LinkedList<Card>> sequences = new LinkedList<>();
        
        for(int i = 0; i < cardsOnHand.size(); i++) {
            LinkedList<Card> sequence = new LinkedList<>();
            sequence.add(cardsOnHand.get(i));
            
            for(int j = i + 1; j < cardsOnHand.size(); j++) {
                if(cardsOnHand.get(j).getNumericValue() == 
                sequence.get(sequence.size() - 1).getNumericValue() + 1
                &&
                cardsOnHand.get(j).getNumericSuit() ==
                sequence.get(sequence.size() - 1).getNumericSuit()) {
                    sequence.add(cardsOnHand.get(j));
                }
            }
            
            if(sequence.size() >= 3) {
                sequences.add(sequence);
            }
        }
        
        sets.addAll(sequences);
    }
    
    public void findDegrees() {
        LinkedList<LinkedList<Card>> degrees = new LinkedList<>();
        
        for(int i = 0; i < cardsOnHand.size(); i++) {
            LinkedList<Card> degree = new LinkedList<>();
            degree.add(cardsOnHand.get(i));
            
            for(int j = i + 1; j < cardsOnHand.size(); j++) {
                if(cardsOnHand.get(j).getNumericValue() == degree.get(degree.size() - 1).getNumericValue()) {
                    degree.add(cardsOnHand.get(j));
                }
            }
            
            if(degree.size() >= 3) {
                degrees.add(degree);
            }
        }
        
        sets.addAll(degrees);
    }
    
    public LinkedList<Card> dropSet(int i) {
        LinkedList<Card> set = sets.remove(i);
        
        for(Card card : set) {
            cardsOnHand.remove(card);
        }
        
        return set;
    }
    
    
    public LinkedList<Card> checkIfSetContains(Card lastcard) {
        LinkedList<Card> containedSet = null;
        
        for(LinkedList<Card> set : sets) {
            for(Card card : set) {
                if(lastcard.equals(card)) {
                    containedSet = set;
                    break;
                }
            }
        }
        
        return containedSet;
    }
    
    public void moveCard(int start, int end) {
        Card targetCard = cardsOnHand.get(start);
        
        cardsOnHand.remove(targetCard);
        cardsOnHand.add(end, targetCard);
    }
    
    public int countScores() {
        int score = 0;
        for(Card card : cardsOnHand) {
            score += card.getNumericValue();
        }
        return score;
    }
    
    public boolean drew() {
        return drew;
    }
    
    public void setDrew(boolean b) {
        drew = b;
    }
    
    public LinkedList<LinkedList<Card>> getSets() {
        return sets;
    }
    
    public Card getCard(int index) {
        return cardsOnHand.get(index);
    }
}
