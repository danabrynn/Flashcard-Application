package persistence;

import model.Deck;
import model.Flashcard;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// JsonReaderTest was heavily inspired by the JsonReaderTest class in the JsonSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Deck deck = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyDeck() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyDeck.json");
        try {
            Deck deck = reader.read();
            assertEquals("User deck", deck.getName());
            assertEquals(0, deck.getDeckSize());
            assertEquals("./data/testReaderEmptyDeck.json", reader.getSource());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralDeck() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralDeck.json");
        try {
            Deck deck = reader.read();
            assertEquals("General deck", deck.getName());
            List<Flashcard> cards = deck.getFlashcards(false);
            assertEquals(2, cards.size());
            checkCard("Front card 1", "Back card 1", cards.get(0));
            checkCard("Front card 2", "Back card 2", cards.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
