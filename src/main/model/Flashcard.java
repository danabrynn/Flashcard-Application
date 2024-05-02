package model;

import org.json.JSONObject;
import persistence.Writable;

/*
Represents a flashcard that can be flipped to display the front and back sides. When the
flashcard is flipped, it is recorded that the card has been viewed. A card can be marked as
correct if a user guessed the card's value before flipping the card to see the back.
 */
public class Flashcard implements Writable {
    private static int totalFlashcards = 0;
    private final String id;
    private String front;
    private String back;
    private boolean flipped;
    private boolean viewed;
    private boolean correct;

    // Requires: front and back are non-empty strings
    // Effects: creates a flashcard with front and back sides. The card has not been
    // flipped or viewed and is marked as not correct. Each flashcard created has a
    // unique ID.
    public Flashcard(String front, String back) {
        this.front = front;
        this.back = back;
        totalFlashcards++;
        this.id = String.valueOf(totalFlashcards);
        this.flipped = false;
        this.viewed = false;
        this.correct = false;
    }

    // Effects: If the card has not been flipped, its front side is displayed.
    // Otherwise, the back of the card is displayed.
    public String displayCard() {
        if (flipped) {
            return back;
        }
        return front;
    }

    // Modifies: this
    // Effects: flips the card over and sets the cards viewed status to true
    public void flipCard() {
        flipped = !flipped;
        viewed = true;
    }

    // Modifies: this
    // Effects: resets the card to the state it was in when created
    // (not flipped, not viewed, and incorrect)
    public void resetCard() {
        this.flipped = false;
        this.viewed = false;
        this.correct = false;
    }

    // Effects: returns String representation of Flashcard
    public String toString() {
        return "Flashcard ID number: " + this.id + "\nFront: " + this.front
                + "\nBack: " + this.back;
    }

    // Effects: returns flashcard as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("front", this.front);
        json.put("back", this.back);
        return json;
    }

    public String getId() {
        return id;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public boolean isViewed() {
        return viewed;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }
}
