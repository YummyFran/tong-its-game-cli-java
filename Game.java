import java.util.*;

class Game {
    static Scanner sc = new Scanner(System.in);
    static Stack<Card> dumps = new Stack<>();
    static LinkedList<LinkedList<Card>> drops = new LinkedList<>();
    static Deck deck = new Deck();
    static Card lastCard = null;
    static Player[] players = new Player[3];
    static Player dealer = null;
    static Player currentPlayer = null;
    static int currentPlayerIndex = 0;
    static String stateMessage = "";
    static String errorMessage = "";
    static String meldsText = "";
    static String dropsText = "";
    static String[] options = {
        "Dump Card\t(labay)",
        "Draw a Card\t(bunot)",
        "Add a Card\t(dagdag)",
        "Sort Cards\t(bahig)",
        "Move Card",
        "Group Melds",
        "Drop Meld\t(down)",
        "Take Card\t(kaon)",
        "View Dumps"
    };
    
    public static void main(String[] args) {
        intro();
    }
    
    public static void intro() {
        String title = ".------..------..------..------.     .------..------..------.\n" +
                         "|T.--. ||O.--. ||N.--. ||G.--. |.-.  |I.--. ||T.--. ||S.--. |\n" +
                         "| :/\\: || :/\\: || :(): || :/\\: ((5)) | (\\/) || :/\\: || :/\\: |\n" +
                         "| (__) || :\\/ :|| ()() || :\\/ :|'-.-.| :\\/ :|| (__) || :\\/ :|\n" +
                         "| '--'T|| '--'O|| '--'N|| '--'G| ((1)) '--'I|| '--'T|| '--'S|\n" +
                         "`------'`------'`------'`------'  '-'`------'`------'`------'";
                         
        System.out.print("\033[H\033[2J");
        System.out.println(title);
        System.out.println("1. Play");
        System.out.println("2. Exit");
        System.out.print("> ");
        
        players[0] = null;
        players[1] = null;
        players[2] = null;
        
        int choice = sc.nextInt();
        
        if(choice == 1) startGame();
        else if(choice == 2) return;
        else intro();
    }
    
    public static void startGame() {
        initPlayers();
        hatag(deck);
        playerTurn();
    }
    
    public static void playerTurn() {
        if(deck.size() <= 0 || currentPlayer.getCards().size() <= 0) {
            gameOver();
            return;
        }
        printCurrentGameData();
        System.out.println("Options:");
        for(int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("> ");
        int choice = sc.nextInt();
        decide(choice);
    }
    
    public static void printCurrentGameData() {
        System.out.print("\033[H\033[2J");
        System.out.println(currentPlayer.name + "'s Turn");
        if(errorMessage != "") System.out.println(errorMessage);
        
        System.out.println("====================================================");
        if(stateMessage != "") System.out.println(stateMessage);
        System.out.println("Cards Left: " + deck.size());
        if(dropsText != "") System.out.println(dropsText);
        System.out.println("====================================================");
        
        currentPlayer.printCards();
        if(meldsText != "") System.out.println(meldsText);
        errorMessage = "";
        meldsText = "";
    }
    
    public static void decide(int choice) {
        printCurrentGameData();
        switch(choice) {
            case 1:
                dumpCard();
                break;
            case 2:
                drawCard();
                break;
            case 3:
                addCard();
                break;
            case 4:
                sortCards();
                break;
            case 5:
                moveCard();
                break;
            case 6:
                groupMelds();
                break;
            case 7:
                dropMelds();
                break;
            case 8:
                takeCard();
                break;
            case 9:
                viewDumps();
                break;
            default:
                errorMessage = "Invalid Option";
                break;
        }
        
        playerTurn();
    }
    
    public static void nextPlayer() {
        if(currentPlayerIndex > 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
        
        currentPlayer = players[currentPlayerIndex];
    }
    
    public static void dumpCard() {
        if(!currentPlayer.drew()) {
            errorMessage = "You have to draw a card first!";
            return;
        }
        
        System.out.print("Which card do you want to dump? (index)\n> ");
        int choice = sc.nextInt();
        
        Card dumped = currentPlayer.labay(choice);
        
        if(dumped == null) {
            errorMessage = "Card doesn't exist";
            return;
        }
        
        dumps.push(dumped);
        lastCard = dumped;
        currentPlayer.setDrew(false);
        
        stateMessage = currentPlayer.name + " dumped " + dumped.toString();
        nextPlayer();
    }
    
    public static void drawCard() {
        if(currentPlayer.drew()) {
            errorMessage = "You already drew a card";
            return;
        }
        currentPlayer.bunot(deck.deal());
        currentPlayer.setDrew(true);
    }
    
    public static void sortCards() {
        currentPlayer.sortCards();
    }
    
    public static void moveCard() {
        System.out.print("Which card do you want to move? (index)\n> ");
        int start = sc.nextInt();
        System.out.print("At what index do you wish this card to move to?\n> ");
        int end = sc.nextInt();
        
        currentPlayer.moveCard(start, end);
    }
    
    public static void groupMelds() {
        currentPlayer.identifySets();
        
        meldsText = "Melds: " + currentPlayer.getSets();
    }
    
    public static void dropMelds() {
        if(currentPlayer.getSets().isEmpty()) {
            System.out.println("There are no sets formed");
            return;
        }
        
        LinkedList<Card> dropped;
        if(currentPlayer.getSets().size() == 1) {
            dropped = currentPlayer.dropSet(0);
        } else {
            System.out.print("Which meld do you want to drop? (index)\n> ");
            int set = sc.nextInt();
            dropped = currentPlayer.dropSet(set);
        }
        
        drops.add(dropped);
        dropsText = "Drops: " + drops;
        currentPlayer.setDrew(true);
    }
    
    public static void takeCard() {
        if(lastCard == null) {
            errorMessage = "There's nothing to take";
            return;
        }
        
        currentPlayer.bunot(lastCard);
        currentPlayer.identifySets();
        LinkedList<Card> containedSet = currentPlayer.checkIfSetContains(lastCard);
        LinkedList<Card> dropped = currentPlayer.dropSet(currentPlayer.getSets().indexOf(containedSet));
        
        if(dropped == null) {
            errorMessage = "Invalid set, can't take card";
            return;
        }
        
        drops.add(dropped);
        dropsText = "Drops: " + drops;
        currentPlayer.setDrew(true);
        lastCard = null;
    }
    
    public static void addCard() {
        System.out.print("Select the index of a card you want to add on the sets\n> ");
        int cardIndex = sc.nextInt();
        System.out.print("Which index of the dropped sets you want this card to add to?\n> ");
        int dropIndex = sc.nextInt();
        
        Card targetCard = currentPlayer.getCard(cardIndex);
        LinkedList<Card> selectedSet = drops.get(dropIndex);

        if(isValid(targetCard, selectedSet)) {
            selectedSet.add(targetCard);
            selectedSet.add(targetCard);
            currentPlayer.labay(cardIndex);
            dropsText = "Drops: " + drops;
        } else {
            errorMessage = "Invalid addition to the set";
        } 
    }
    
    public static boolean isValid(Card targetCard, LinkedList<Card> set) {
        int targetCardValue = targetCard.getNumericValue();
        boolean isSequence = set.get(0).getNumericValue() < set.get(1).getNumericValue();
        
        if(isSequence) {
            if(targetCardValue + 1 == set.get(0).getNumericValue() ||
            targetCardValue - 1 == set.get(set.size() - 1).getNumericValue()) {
                return true;
            }
        } else {
            if(targetCardValue == set.get(0).getNumericValue() &&
            targetCardValue == set.get(set.size() - 1).getNumericValue()) {
                return true;
            }
        }
        
        return false;
    }
    
    public static void viewDumps() {
        System.out.print("Dumps: ");
        for(Card dump : dumps) {
            System.out.print("[" + dump + "] ");
        }
        System.out.println("\nPress 1 to continue");
        sc.nextInt();
    }
    
    public static void initPlayers() {
        for(int i = 0; i < 3; i++) {
            System.out.print("Enter Player " + (i + 1) + "'s name: ");
            String name = sc.next();
            players[i] = new Player(name);
            System.out.print("\033[H\033[2J");
        }
        
        dealer = players[currentPlayerIndex];
        currentPlayer = players[currentPlayerIndex];
    }

    public static void hatag(Deck deck) {
        int cardsToDeal = 12;

        for(int i = 0; i < cardsToDeal; i++) {
            players[0].bunot(deck.deal());
            players[1].bunot(deck.deal());
            players[2].bunot(deck.deal());
        }
        
        dealer.bunot(deck.deal());
        dealer.setDrew(true);
    }
    
    public static void gameOver() {
        if(deck.size() <= 0) {
            System.out.println("Game Over!\n\nScores:");
            Player winner = null;
            
            for(Player player : players) {
                System.out.println(player.name + ": " + player.countScores());
                winner = winner == null || player.countScores() < winner.countScores() ? player : winner;
            }
            
            System.out.println(winner.name + " Wins!!");
            dealer = winner;
            
            return;
        }
        
        System.out.println(currentPlayer.name + "Tong Its!!");
        dealer = currentPlayer;
     
        return;
    }
}