package ui.console;

import model.Deck;
import model.Flashcard;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/*
Represents a flashcard console application that allows users to build their own
flashcard deck. Cards can be added and removed from deck, the deck can be viewed,
and users can test themselves on the deck through a flashcard game.
 */
public class FlashcardConsoleApp extends ConsoleApp {
    private static final String JSON_STORE = "./data/deck.json";
    private Deck deck;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private boolean applicationChanged;

    // Effects: runs the flashcard application
    public FlashcardConsoleApp() {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonReader(JSON_STORE);
        this.deck = new Deck("User deck");
        this.applicationChanged = false;
        runApplication();
        checkApplicationSaved();
        printTerminationMessage();
    }

    // Modifies: this
    // Effects: processes user inout
    @Override
    protected void processCommand(String command) {
        if (command.equals("a")) {
            addCard();
        } else if (command.equals("d") && deck.getDeckSize() > 0) {
            deleteCard();
        } else if (command.equals("g") && deck.getDeckSize() == 0) {
            generateSampleDeck();
        } else if (command.equals("v") && deck.getDeckSize() > 0) {
            viewDeck();
        } else if (command.equals("t") && deck.getDeckSize() > 0) {
            new FlashcardConsoleGame(deck);
        } else if (command.equals("s")) {
            saveDeck();
        } else if (command.equals("l")) {
            loadDeck(this.jsonReader);
        } else {
            System.out.println("Selection not valid. Please try again. ");
        }
    }

    // Effects: displays a menu of command options to user
    @Override
    protected void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add card to deck");
        if (deck.getDeckSize() == 0) {
            System.out.println("\tg -> generate sample deck");
        }
        if (deck.getDeckSize() > 0) {
            System.out.println("\td -> delete card from deck");
            System.out.println("\tv -> view all cards in deck");
            System.out.println("\tt -> test yourself on deck");
        }
        System.out.println("\n\ts -> save deck to file");
        System.out.println("\tl -> load deck from file");
        System.out.println("\n\tq -> quit");
    }

    // Modifies: this
    // Effects: adds a user generated card to the end of the deck
    private void addCard() {
        System.out.println("Please enter what you want displayed on the front of the card:");
        String cardFace = scanner.next();
        System.out.println("Please enter what you want displayed on the back of the card:");
        String cardBack = scanner.next();
        Flashcard card = new Flashcard(cardFace, cardBack);
        deck.addCard(card);
        this.applicationChanged = true;
    }

    // Modifies: this
    // Effects: deletes a card from the deck based on user input
    private void deleteCard() {
        viewDeck();
        System.out.println("Cards in deck are displayed above. To return to main menu enter 'back'");
        System.out.println("Otherwise, please enter the ID number of the card you want to delete:");
        String input = scanner.next();
        if (input.equalsIgnoreCase("back") || input.equalsIgnoreCase("b")) {
            System.out.println("Returning to main menu.");
        } else if (deck.removeCard(input)) {
            System.out.println("Removed card with id " + input);
        } else {
            System.out.println("Unable to find card with id '" + input + "'. Returning to main menu");
        }
        this.applicationChanged = true;
    }

    // Modifies: this
    // Effects: creates a sample card deck
    private void generateSampleDeck() {
        JsonReader jsonSampleReader = new JsonReader("./data/sampleDeck.json");
        loadDeck(jsonSampleReader);
    }

    // Effects: prints out all the cards in deck
    private void viewDeck() {
        List<Flashcard> cards = deck.getFlashcards(false);
        for (Flashcard flashcard : cards) {
            System.out.println(flashcard);
            System.out.println("================================================");
        }
    }

    // Effects: saves the deck to file
    private void saveDeck() {
        try {
            jsonWriter.open();
            jsonWriter.write(this.deck);
            jsonWriter.close();
            System.out.println("Saved " + deck.getName() + " to " + JSON_STORE);
            this.applicationChanged = false;
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Modifies: this
    // Effects: loads flashcard deck from file
    private void loadDeck(JsonReader reader) {
        try {
            deck = reader.read();
            System.out.println("Loaded " + deck.getName() + " from " + reader.getSource());
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + reader.getSource());
        }
    }

    // Effects: checks if application has been saved, and if not outputs message asking
    //          if user wants to save file
    private void checkApplicationSaved() {
        if (applicationChanged) {
            boolean getInput = true;
            while (getInput) {
                System.out.println("Unsaved changes. Would you like to save before quitting?");
                System.out.println("\t y -> yes \n\t n -> no");
                String command = scanner.next();
                if (command.equalsIgnoreCase("y") || command.equalsIgnoreCase("yes")) {
                    saveDeck();
                    getInput = false;
                } else if (command.equalsIgnoreCase("n") || command.equalsIgnoreCase("no")) {
                    getInput = false;
                } else {
                    System.out.println("Invalid input. Please try again");
                }
            }
        }
    }

    // Effects: prints termination message to console
    private void printTerminationMessage() {
        System.out.println("Closing application..... Goodbye :)");
    }
}
