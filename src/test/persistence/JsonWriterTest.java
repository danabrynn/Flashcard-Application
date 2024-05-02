package persistence;

import model.Deck;
import model.Flashcard;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// JsonWriterTest was heavily inspired by the JsonWriterTest class in the JsonSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyDeck() {
        try {
            Deck deck = new Deck("My deck");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDeck.json");
            writer.open();
            writer.write(deck);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDeck.json");
            deck = reader.read();
            assertEquals("My deck", deck.getName());
            assertEquals(0, deck.getDeckSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDeck() {
        try {
            Deck deck = new Deck("My deck");
            deck.addCard(new Flashcard("Hello", "Goodbye"));
            deck.addCard(new Flashcard("Moon", "Sun"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDeck.json");
            writer.open();
            writer.write(deck);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDeck.json");
            deck = reader.read();
            assertEquals("My deck", deck.getName());
            List<Flashcard> cards = deck.getFlashcards(false);
            assertEquals(2, cards.size());
            checkCard("Hello", "Goodbye", cards.get(0));
            checkCard("Moon", "Sun", cards.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
