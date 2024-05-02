package ui.console;


import model.Deck;
import model.Flashcard;

import java.util.List;

/*
Represents a flashcard game that can be used to test flashcard knowledge. Cards
from a deck can be displayed in random order and users can go through the deck,
flipping cards and marking if cards were completed successfully. Test statistics
are displayed upon closing the game.
*/
public class FlashcardConsoleGame extends ConsoleApp {
    private final Deck deck;
    private boolean random;
    private List<Flashcard> cardList;
    private int displayIndex;
    private Flashcard card;

    // Effects: creates a flashcard game from the deck and runs the game application.
    // The deck is reset to its initial state after completion.
    public FlashcardConsoleGame(Deck deck) {
        this.deck = deck;
        displayIndex = 0;
        runApplication();
        printTerminationStats();
        deck.resetDeck();
    }

    @Override
    // Modifies: this
    // Effects: initializes the flashcard game.
    // Gets the list of flashcards to be displayed and displays first card
    protected void init() {
        super.init();
        System.out.println("\nEntering test mode....");
        getCardOrder();
        cardList = deck.getFlashcards(random);
        showCard();
    }

    // Modifies: this
    // Effects: determines if user wants cards displayed in order they were created or
    // in random order
    private void getCardOrder() {
        System.out.println("\nDisplay cards in random order? \n\ty -> yes \n\tn -> no");
        boolean getInput = true;
        while (getInput) {
            String command = scanner.next();
            if (command.equalsIgnoreCase("y") || command.equalsIgnoreCase("yes")) {
                random = true;
                getInput = false;
            } else if (command.equalsIgnoreCase("n") || command.equalsIgnoreCase("no")) {
                random = false;
                getInput = false;
            } else {
                System.out.println("Invalid input. Please try again");
            }
        }
    }

    @Override
    // Effects: displays a menu of command options to user
    protected void displayMenu() {
        System.out.println("\nSelect from:");
        if (!card.isFlipped()) {
            System.out.println("\tf -> flip card");
        }
        if (!lastCard()) {
            System.out.println("\tn -> next card");
        }
        System.out.println("\tq -> quit");
    }

    @Override
    // Modifies: this
    // Effects: processes user input
    protected void processCommand(String command) {
        if (command.equals("f") && !card.isFlipped()) {
            flipCard();
        } else if (command.equals("n") && !lastCard()) {
            nextCard();
        } else {
            System.out.println("Selection not valid. Please try again. ");
        }
    }

    // Modifies: this
    // Effects: prints the card at stored index
    private void showCard() {
        card = cardList.get(displayIndex);
        System.out.println("***********************************");
        System.out.println(card.displayCard());
        System.out.println("***********************************");
    }

    // Modifies: this
    // Effects: flips the current card to display the back and asks user if they
    // guessed the card correctly. If card flipped is not the last card in deck,
    // the next card is then displayed.
    private void flipCard() {
        card.flipCard();
        showCard();
        boolean getInput = true;
        while (getInput) {
            System.out.println("Did you get guess card correctly? \n\ty -> yes \n\tn -> no");
            String command = scanner.next();
            if (command.equalsIgnoreCase("y") || command.equalsIgnoreCase("yes")) {
                card.setCorrect(true);
                getInput = false;
                System.out.println("Good job! \n");
            } else if (command.equalsIgnoreCase("n") || command.equalsIgnoreCase("no")) {
                card.setCorrect(false);
                getInput = false;
                System.out.println("Awww too bad... you tried... \n");
            } else {
                System.out.println("Invalid input. Please try again");
            }
        }
        if (!lastCard()) {
            nextCard();
        }
    }

    // Requires: there must be a next element in list
    // (displayIndex < cardList size - 1)
    // Modifies: this
    // Effects: increments display index, showing the card at the next index
    private void nextCard() {
        displayIndex++;
        showCard();
    }

    // Effects: prints deck completion and correctness stats after termination of game
    private void printTerminationStats() {
        double percentViewed = ((displayIndex + 1.0) / deck.getDeckSize()) * 100;
        String txt = "You viewed " + (displayIndex + 1) + " of " + deck.getDeckSize() + " cards ";
        String viewed = String.format("(%.1f", percentViewed);
        String flipped = String.format("Percent of total cards flipped: %.1f", deck.getPercentViewed());
        String correct = String.format("Percent correct: %.1f", deck.getPercentCorrect());
        System.out.println("Congratulations!");
        System.out.println(txt + viewed + " %)");
        System.out.println(flipped + " %");
        System.out.println(correct + " %");
    }

    // Effects: returns true if displayIndex is indexing the last element in list.
    // Otherwise, returns false.
    private boolean lastCard() {
        return displayIndex == cardList.size() - 1;
    }
}
