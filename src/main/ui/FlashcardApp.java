package ui;

import model.Deck;
import model.Event;
import model.EventLog;
import model.Flashcard;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
Represents a flashcard JFrame window that allows users to build their own flashcard deck. Cards
can be added and removed from deck, the deck can be viewed, and a flashcard quiz can be generated.

Parts of the code for the FlashcardApp GUI were inspired by the Space Invaders Base, Simple Drawing Player,
and Alarm System class examples:
https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase/ ,
https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Starter,
https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
Additionally, the following YouTube series was helpful in learning how to use Java Swing:
https://www.youtube.com/watch?v=1vVJPzVzaK8&list=PL3bGLnkkGnuV699lP_f9DvxyK5lMFpq6U
 */
public class FlashcardApp extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;
    private static final Font OPENING_FONT = new Font("Serif", Font.PLAIN, 25);
    private JScrollPane deckView;
    private DeckPanel deckPanel;
    private JPanel buttonPanel;
    private JPanel title;
    private Deck deck;
    private String savePath;
    private JCheckBox showCards;
    private JLabel titleLabel;

    // Effects: runs the flashcard application
    public FlashcardApp() {
        super("Flashcard Application");
        startWindow();
        initialize();
        generateTitle();
        add(this.title, BorderLayout.NORTH);
        generateButtonPanel();
        add(this.buttonPanel, BorderLayout.WEST);
        showCards = new JCheckBox("Show cards");
        showCards.setFont(new Font("Serif", Font.PLAIN, 20));
        showCards.addActionListener(new ShowCardsTool());
        add(showCards, BorderLayout.SOUTH);
        addWindowListener(new WindowCloseListerner());
        setVisible(true);
    }

    // Modifies: this
    // Effects: generates a start window that allows user to either load a deck from file
    //          or create a new deck
    private void startWindow() {
        String[] options = {"Create new deck", "Load deck from file"};
        JLabel welcomeLabel = new JLabel("Welcome! Please select from following: ");
        welcomeLabel.setFont(OPENING_FONT);
        int selection = JOptionPane.showOptionDialog(null,
                welcomeLabel, "Flashcard Application", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                new ImageIcon("data/icons/CardLogo.png"), options, null);
        if (selection == 0) {
            createNewDeck();
        } else if (selection == 1) {
            loadDeck(true);
        } else {
            System.exit(0);
        }
    }

    // Modifies: this
    // Effects: generates title JPanel
    private void generateTitle() {
        this.title = new JPanel();
        this.title.setBackground(Color.DARK_GRAY);
        titleLabel = new JLabel(deck.getName());
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(Color.white);
        titleLabel.setIcon(new ImageIcon("data/icons/CardLogoSmall.png"));
        titleLabel.setIconTextGap(20);
        this.title.add(titleLabel);
    }

    // Modifies: this
    // Effects: creates a new deck with input from user. If user clicks cancel,
    // returns to start window
    private void createNewDeck() {
        JLabel message = new JLabel("Please enter deck name: ");
        message.setFont(OPENING_FONT);
        String name = (String) JOptionPane.showInputDialog(null, message, "Flashcard Application",
                JOptionPane.PLAIN_MESSAGE, new ImageIcon("data/icons/CardLogo.png"), null, null);
        if (name == null) {
            startWindow();
            return;
        }
        this.deck = new Deck(name);
    }

    // Modifies: this
    // Effects: loads deck from file based on user input and stores file path. If user clicks cancel,
    //          the window will return to the start window if ReturnToMainMenu is true.
    //          If unable to load file, prompts user to reenter file path.
    private void loadDeck(boolean returnToMainMenu) {
        JLabel message = new JLabel("Please enter file path: ");
        message.setFont(OPENING_FONT);
        String path = (String) JOptionPane.showInputDialog(null, message, "Flashcard Application",
                JOptionPane.PLAIN_MESSAGE, new ImageIcon("data/icons/CardLogo.png"), null,
                "./data/sampleDeck.json");
        if (path == null) {
            if (returnToMainMenu) {
                startWindow();
            }
            return;
        }
        JsonReader jsonReader = new JsonReader(path);
        try {
            deck = jsonReader.read();
            savePath = path;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to load file from '" + path
                    + "'\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
            loadDeck(returnToMainMenu);
        }
    }

    // Modifies: this
    // Effects: creates new JScrollPane that displays all the cards currently in deck
    //          if the showCards checkbox is selected. Otherwise, does nothing.
    private void showCards() {
        if (showCards.isSelected()) {
            deckPanel = new DeckPanel(deck.getFlashcards(false));
            deckView = new JScrollPane(deckPanel);
            deckView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            add(deckView);
        }
    }

    // Modifies: this
    // Effects: refreshes the JScrollPane to show cards currently in deck
    private void updateCards() {
        if (deckView != null) {
            remove(deckView);
        }
        showCards();
        revalidate();
        repaint();
    }

    // Modifies: this
    // Effects: initializes the JFrame, setting default size, layout, and close operation
    private void initialize() {
        setSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setLayout(new BorderLayout(15, 10));
        setLocationRelativeTo(null);
    }

    // Modifies: this
    // Effects: generates the button panel
    private void generateButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton addButton = new JButton("Add Card");
        addButton.addActionListener(new AddCardTool());
        JButton deleteButton = new JButton("Delete Card");
        deleteButton.addActionListener(new DeleteCardTool());
        JButton saveAsButton = new JButton("Save As");
        ActionListener saveDeckTool = new SaveDeckTool();
        saveAsButton.addActionListener(saveDeckTool);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(saveDeckTool);
        JButton quizMode = new JButton(("Quiz Mode"));
        quizMode.addActionListener(new QuizTool());
        JButton loadButton = new JButton("Load Deck");
        loadButton.addActionListener(new LoadTool());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(quizMode);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveAsButton);
        buttonPanel.add(saveButton);
    }


    // Responds to the add card button
    private class AddCardTool implements ActionListener {

        // Modifies: this
        // Effects: gets String to be displayed on front and back of card from user.
        //          If String is not null, creates new flashcard with specified front
        //          and back and adds it to deck.
        @Override
        public void actionPerformed(ActionEvent e) {
            String front = JOptionPane.showInputDialog("Front of card: ");
            if (front != null) {
                String back = JOptionPane.showInputDialog("Back of card: ");
                if (back != null) {
                    deck.addCard(new Flashcard(front, back));
                    updateCards();
                }
            }
        }
    }

    // Responds to the delete card button
    private class DeleteCardTool implements ActionListener {

        // Modifies: this
        // Effects: deletes flashcards associated with any checkboxes the user has selected
        //          if user confirms deletion. Otherwise, does nothing.
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!showCards.isSelected()) {
                showCards.doClick();
            }
            List<Flashcard> selectedFlashCards = new ArrayList<>();
            for (JCheckBox checkBox : deckPanel.getCheckBoxes()) {
                if (checkBox.isSelected()) {
                    selectedFlashCards.add(deckPanel.getCardFromCheckbox(checkBox));
                }
            }
            if (confirmDelete(selectedFlashCards)) {
                for (Flashcard flashcard : selectedFlashCards) {
                    deck.removeCard(flashcard);
                }
                updateCards();
            }
        }

        // Effects: Creates JOptionPane to confirm that user want to delete any selected cards.
        //          Returns true if user confirms, false otherwise.
        private boolean confirmDelete(List<Flashcard> flashcards) {
            if (flashcards.size() > 0) {
                String flashcardCanBePlural = "flashcard:";
                if (flashcards.size() > 1) {
                    flashcardCanBePlural = "flashcards:";
                }
                StringBuilder message = new StringBuilder("Please confirm you want to delete the following "
                        + flashcardCanBePlural);
                for (Flashcard flashcard : flashcards) {
                    String cardFront = flashcard.getFront();
                    if (cardFront.length() > 35) {
                        cardFront = cardFront.substring(0,35) + "...";
                    }
                    message.append("\n\tCard: ").append(cardFront);
                }
                int input = JOptionPane.showConfirmDialog(null, message.toString(),
                        "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                return input == 0;
            }
            JOptionPane.showMessageDialog(null, "Please select card to delete",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    // Responds to the Save and Save As deck buttons
    private class SaveDeckTool implements ActionListener {

        // Modifies: this
        // Effects: If user clicked "Save" and filepath is recorded (from loading file or previous save)
        //          saves the deck to filepath. If user clicked "Save As" or filepath not found, asks user
        //          to specify file name and then saves file
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Save") && savePath != null) {
                saveFile(savePath);
            } else {
                saveAs();
            }
        }

        // Modifies: this
        // Effects: saves deck with a file name supplied by user and records the file path
        private void saveAs() {
            String fileName = JOptionPane.showInputDialog("Enter file name",
                    deck.getName().replaceAll("\\s", ""));
            if (fileName != null) {
                String path = "./data/" + fileName + ".json";
                saveFile(path);
                savePath = path;
            }
        }

        // Effects: saves the deck to file
        private void saveFile(String path) {
            JsonWriter jsonWriter = new JsonWriter(path);
            try {
                jsonWriter.open();
                jsonWriter.write(deck);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null, "Saved file to: '" + path
                        + "'", "File saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException error) {
                JOptionPane.showMessageDialog(null, "Unable to save file to '" + path
                        + "'", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    // Responds to the Quiz Mode button
    private class QuizTool implements ActionListener {

        // Modifies: this
        // Effects: Creates new FlashcardGame JDialogue window if deck size is greater than 0.
        //          Otherwise, shows error message
        @Override
        public void actionPerformed(ActionEvent e) {
            if (deck.getDeckSize() > 0) {
                new FlashcardGame(deck);
            } else {
                JOptionPane.showMessageDialog(null, "No cards in deck!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    // Responds to the show cards checkbox
    private class ShowCardsTool implements ActionListener {

        // Modifies: this
        // Effects: updates cards when checkbox value is changed
        @Override
        public void actionPerformed(ActionEvent e) {
            updateCards();
        }

    }

    // Responds to the load card button
    private class LoadTool implements ActionListener {

        // Modifies: this
        // Effects: loads deck from file
        @Override
        public void actionPerformed(ActionEvent e) {
            int input = JOptionPane.showConfirmDialog(null, "Close this deck and open another one?",
                    "Confirm Open", JOptionPane.YES_NO_OPTION);
            if (input == 0) {
                loadDeck(false);
                titleLabel.setText(deck.getName());
                updateCards();
                revalidate();
                repaint();
            }
        }

    }

    // Responds to window close event
    // https://docs.oracle.com/javase/8/docs/api/java/awt/event/WindowAdapter.html
    private class WindowCloseListerner extends WindowAdapter {

        // Effects: prints log of events to console when window closed
        @Override
        public void windowClosing(WindowEvent e) {
            for (Event next : EventLog.getInstance()) {
                System.out.println("\n" + next.toString());
            }
            System.exit(0);
        }

    }

}
