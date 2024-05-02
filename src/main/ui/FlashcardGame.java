package ui;

import model.Deck;
import model.Flashcard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/*
Represents a flashcard quiz window that allows users to flip through the cards in deck,
marking if they guessed the card's value correctly before flipping. Completion and correctness
stats are displayed when user terminates quiz.
 */
public class FlashcardGame extends JDialog {
    private static final int WIDTH = 910;
    private static final int HEIGHT = 650;
    private static final String PREV = "< Prev";
    private static final String NEXT = "Next >";
    private Deck deck;
    private List<Flashcard> cardList;
    private JButton currentCard;
    private JButton prev;
    private JButton next;
    private int currentIndex;
    private JPanel correctPanel;
    private JCheckBox correct;
    private JButton endQuiz;

    // Effects: Creates the flashcard game window
    public FlashcardGame(Deck deck) {
        this.deck = deck;
        initialize();
        currentIndex = 0;
        addTopPanel();
        addButtons();
        displayCurrentCard();
        setVisible(true);
    }

    // Modifies: this
    // Effects: initializes the JDialogue, setting default size and layout
    private void initialize() {
        setTitle("Flashcard Quiz");
        setMinimumSize((new Dimension(WIDTH, HEIGHT)));
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(null);
        getDeckOrder();
    }

    // Modifies: this
    // Effects: gets the list of flashcards in the deck in either the order added or in
    //          a random order, based on user input
    private void getDeckOrder() {
        int randomOrder = JOptionPane.showConfirmDialog(this, "Display cards in random order?",
                "Flashcard Quiz", JOptionPane.YES_NO_OPTION);
        if (randomOrder == 0) {
            cardList = deck.getFlashcards(true);
        } else {
            cardList = deck.getFlashcards(false);
        }
    }

    // Modifies: this
    // Effects: creates title panel
    private void addTopPanel() {
        JPanel title = new JPanel();
        title.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel(deck.getName() + " - Quiz Mode");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        title.add(titleLabel);
        add(title, BorderLayout.NORTH);
    }

    // Modifies: this
    // Effects: creates button panel
    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,5));
        ActionListener changeCardTool = new ChangeCardTool();
        prev = new JButton(PREV);
        prev.addActionListener(changeCardTool);
        next = new JButton(NEXT);
        next.addActionListener(changeCardTool);
        JPanel prevAndNext = new JPanel();
        prevAndNext.add(prev);
        prevAndNext.add(next);
        endQuiz = new JButton("End quiz & display results");
        endQuiz.addActionListener(new EndQuizTool());
        buttonPanel.add(endQuiz);
        buttonPanel.add(new JPanel());
        buttonPanel.add(prevAndNext);
        buttonPanel.add(new JPanel());
        correctPanel = new JPanel();
        buttonPanel.add(correctPanel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Modifies: this
    // Effects: creates new JButton to represent text on side of card facing user
    private void displayCurrentCard() {
        String formattedCardText = "<html>" + cardList.get(currentIndex).displayCard() + "</html>";
        currentCard = new JButton(formattedCardText);
        currentCard.setFont(new Font("Serif", Font.PLAIN, 35));
        currentCard.addActionListener(new FlipCardTool());
        add(currentCard);
        handleBoundary();
    }

    // Modifies: this
    // Effects: updates the card displayed in window. If back of card is being displayed, also
    //          displays correct button for user to indicate if card was guessed successfully.
    private void updateCard() {
        if (correct != null) {
            correctPanel.remove(correct);
        }
        remove(currentCard);
        displayCurrentCard();
        if (cardList.get(currentIndex).isFlipped()) {
            correct = new JCheckBox("Correct ?");
            correct.setFont(new Font("Serif", Font.PLAIN, 20));
            if (cardList.get(currentIndex).isCorrect()) {
                correct.setSelected(true);
            }
            correct.addActionListener(new CorrectAnswerTool());
            correctPanel.add(correct);
        }
        revalidate();
        repaint();
    }

    // Modifies: this
    // Effects: disables prev button if first element in card list is being displayed;
    //          disables next button if last element is being displayed
    private void handleBoundary() {
        this.prev.setEnabled(true);
        this.next.setEnabled(true);
        if (currentIndex <= 0) {
            this.prev.setEnabled(false);
        }
        if (currentIndex >= cardList.size() - 1) {
            this.next.setEnabled(false);
        }
    }


    // Responds to the previous and next card buttons
    private class ChangeCardTool implements ActionListener {

        // Modifies: this
        // Effects: displays the front of the previous card in list
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(PREV)) {
                currentIndex--;
            } else if (e.getActionCommand().equals(NEXT)) {
                currentIndex++;
            }
            Flashcard card = cardList.get(currentIndex);
            if (card.isFlipped()) {
                card.flipCard();
            }
            updateCard();
        }
    }

    // Responds to the flip card button
    private class FlipCardTool implements ActionListener {

        // Modifies: this
        // Effects: flips the card to display the opposite side
        @Override
        public void actionPerformed(ActionEvent e) {
            cardList.get(currentIndex).flipCard();
            updateCard();
        }
    }

    // Responds to the correct answer checkbox click
    private class CorrectAnswerTool implements ActionListener {

        // Modifies: this
        // Effects: if correct checkbox is selected, sets card to correct;
        //          if checkbox unselected, sets card to not correct
        @Override
        public void actionPerformed(ActionEvent e) {
            Flashcard card = cardList.get(currentIndex);
            card.setCorrect(correct.isSelected());
        }
    }

    // Responds to the end quiz button
    private class EndQuizTool implements ActionListener {

        // Modifies: this
        // Effects: ends the quiz and displays quiz stats
        @Override
        public void actionPerformed(ActionEvent e) {
            remove(currentCard);
            prev.setEnabled(false);
            next.setEnabled(false);
            endQuiz.setEnabled(false);
            if (correct != null) {
                correct.setVisible(false);
            }
            JPanel terminationStats = new JPanel();
            terminationStats.add(getTerminationStats());
            add(terminationStats);
            revalidate();
            repaint();
            deck.resetDeck();
        }
    }

    // Effects: creates a JPanel depicting deck completion and correctness stats
    private JPanel getTerminationStats() {
        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        Font font = new Font("Serif", Font.PLAIN, 50);
        JLabel congrats = new JLabel("Congratulations!");
        congrats.setFont(new Font("Serif", Font.ITALIC, 60));
        stats.add(congrats);
        stats.add(Box.createRigidArea(new Dimension(0, 45)));
        JLabel flipped = new JLabel("You flipped " + deck.getNumberViewed()
                + " of " + cardList.size() + " cards");
        flipped.setFont(font);
        stats.add(flipped);
        stats.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel flippedGraphic = new PercentageBar(deck.getPercentViewed());
        stats.add(flippedGraphic);
        stats.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel correct = new JLabel("You got " + deck.getNumberCorrect()
                + " of " + deck.getNumberViewed() + " cards correct");
        correct.setFont(font);
        stats.add(correct);
        stats.add(Box.createRigidArea(new Dimension(0, 10)));
        stats.add(new PercentageBar(deck.getPercentCorrect()));
        return stats;
    }

}
