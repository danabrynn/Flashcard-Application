package ui;

import model.Flashcard;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/*
Represents the panel in which cards in the deck are displayed.
 */
public class DeckPanel extends JPanel {
    private List<Flashcard> flashcards;
    private static final int CARD_WIDTH = 275;
    private static final int CARD_HEIGHT = 130;
    private static final Font CARD_FONT = new Font("Serif", Font.PLAIN, 35);
    private static final Font CARD_NUM_FONT = new Font("Serif", Font.BOLD, 20);
    private Map<JCheckBox, Flashcard> checkBoxMap;

    // Effects: constructs the deck panel. Sets layout and colour of panel and
    //          displays the flashcards in the deck.
    public DeckPanel(List<Flashcard> flashcards) {
        setBackground(Color.LIGHT_GRAY);
        this.flashcards = flashcards;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        checkBoxMap = new LinkedHashMap<>();
        displayFlashcards();
    }

    // Modifies: this
    // Effects: creates a JPanel for each card in deck that contains the card's
    //          ID number, front, and back. Each flashcard JPanel is added to the DeckPanel.
    private void displayFlashcards() {
        JPanel flashcard;
        int i = 1;
        for (Flashcard card : flashcards) {
            flashcard = new JPanel();
            JLabel cardLabel = new JLabel(i + " ");
            cardLabel.setFont(CARD_NUM_FONT);
            JTextArea front = displayCardText(card.getFront());
            JTextArea back = displayCardText(card.getBack());
            JCheckBox checkBox = new JCheckBox();
            checkBoxMap.put(checkBox, card);
            flashcard.add(checkBox);
            flashcard.add(cardLabel);
            flashcard.add(front);
            flashcard.add(back);
            this.add(flashcard);
            i++;
        }
    }

    // Effects: Creates and returns an uneditable JTextArea to display text from a card.
    private JTextArea displayCardText(String text) {
        JTextArea cardText = new JTextArea(text, 5, 5);
        cardText.setSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardText.setEditable(false);
        cardText.setLineWrap(true);
        cardText.setWrapStyleWord(true);
        cardText.setFont(CARD_FONT);
        return cardText;
    }

    // Effects: returns a set containing the checkboxes in map in the order added
    public Set<JCheckBox> getCheckBoxes() {
        return checkBoxMap.keySet();
    }

    // Effects: returns the flashcard a checkbox is mapped to
    public Flashcard getCardFromCheckbox(JCheckBox checkBox) {
        return checkBoxMap.get(checkBox);
    }
}
