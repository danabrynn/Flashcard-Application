package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Represents a deck of flashcards. Decks contain a name and a list of the flashcards
they store. The flashcards in the deck can be returned in the order they were added
or in a random order. Statistics on deck correctness and completion can be calculated.
 */
public class Deck implements Writable {
    private String name;
    private List<Flashcard> flashcards;

    // Requires: name must be a non-empty string
    // Effects: Creates a flashcard deck with the specified name and an empty
    // list of flashcards.
    public Deck(String name) {
        this.name = name;
        flashcards = new ArrayList<>();
    }

    // Requires: flashcard must not be null
    // Modifies: this
    // Effects: Adds flashcard to deck and returns true if the flashcard has not already
    // been added. Duplicate cards are not allowed. If the flashcard has already been added,
    // returns false.
    public boolean addCard(Flashcard card) {
        if (!flashcards.contains(card)) {
            flashcards.add(card);
            EventLog.getInstance().logEvent(new Event("Added Flashcard to " + this.name + "\n" + card));
            return true;
        }
        return false;
    }

    // Requires: flashcard must not be null
    // Modifies: this
    // Effects: Removes flashcard from deck and returns true. If flashcard is not in deck,
    // returns false.
    public boolean removeCard(Flashcard card) {
        EventLog.getInstance().logEvent(new Event("Deleted Flashcard from " + this.name + "\n" + card));
        return this.flashcards.remove(card);
    }

    // Modifies: this
    // Effects: Removes flashcard from deck based on flashcard id. If flashcard with
    // corresponding id is found, flashcard is removed from deck and true is returned.
    // Otherwise, returns false.
    public boolean removeCard(String cardID) {
        for (Flashcard flashcard: flashcards) {
            if (cardID.equals(flashcard.getId())) {
                return removeCard(flashcard);
            }
        }
        return false;
    }

    // Effects: Returns the number of flashcards in the deck
    public int getDeckSize() {
        return flashcards.size();
    }

    // Effects: Returns the number of cards in the deck that were marked as correct
    public int getNumberCorrect() {
        int correct = 0;
        for (Flashcard flashcard: flashcards) {
            if (flashcard.isCorrect()) {
                correct++;
            }
        }
        return correct;
    }

    // Effects: Returns the percentage (0-100) of viewed cards in the deck that
    // were marked as correct. If no cards have been viewed, 0 is returned. A card
    // is considered viewed if it has been flipped at least once.
    public double getPercentCorrect() {
        if (getNumberViewed() == 0) {
            return 0;
        } else {
            return ((double) getNumberCorrect() / getNumberViewed()) * 100;
        }
    }

    // Effects: Returns the number of cards in the deck that were viewed.
    // A card is considered viewed if it has been flipped at least once. If there are no
    // cards in the deck, 0 is returned.
    public int getNumberViewed() {
        int viewed = 0;
        for (Flashcard flashcard: flashcards) {
            if (flashcard.isViewed()) {
                viewed++;
            }
        }
        return viewed;
    }

    // Effects: Returns the percentage (0-100) of cards in the deck that were viewed.
    // A card is considered viewed if it has been flipped at least once. If there are no
    // cards in the deck, 0 is returned.
    public double getPercentViewed() {
        if (flashcards.size() == 0) {
            return 0;
        }
        int viewed = getNumberViewed();
        EventLog.getInstance().logEvent(new Event("Flipped " + viewed
                + " of " + flashcards.size() + " cards in " + name));
        return ((double) viewed / flashcards.size()) * 100;
    }

    // Effects: Returns the list of flashcards. Flashcards are returned in the same order
    // they were added to the deck if random = false. Otherwise, they are returned in a
    // random order. Returns an empty list if no flashcards have been added to deck.
    public List<Flashcard> getFlashcards(boolean random) {
        List<Flashcard> flashcardsCopy = new ArrayList<>(flashcards);
        if (random) {
            Collections.shuffle(flashcardsCopy);
        }
        return flashcardsCopy;
    }

    // Modifies: this
    // Effects: resets all the cards in the deck to their initial state
    // (not flipped, not viewed, not correct)
    public void resetDeck() {
        for (Flashcard flashcard: this.flashcards) {
            flashcard.resetCard();
        }
    }

    public String getName() {
        return name;
    }

    // Effects: returns the deck as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("flashcards", flashcardsToJson());
        return json;
    }

    // Effects: returns flashcards in this deck as a JSON array
    private JSONArray flashcardsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Flashcard card : flashcards) {
            jsonArray.put(card.toJson());
        }
        return jsonArray;
    }
}
