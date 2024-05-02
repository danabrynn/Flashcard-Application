package persistence;

import model.Deck;
import model.Flashcard;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads deck from JSON data stored in file
// JsonReader was heavily inspired by the JsonReader class in the JsonSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonReader {
    private String source;

    // Effects: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // Effects: reads the deck from file and returns it
    // throws IOException if an error occurs reading data from file
    public Deck read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDeck(jsonObject);
    }

    // Effects: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // Effects: parses deck from JSON object and returns it
    private Deck parseDeck(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Deck deck = new Deck(name);
        addCards(deck, jsonObject);
        return deck;
    }

    // Modifies: deck
    // Effects: parses flashcards from JSON object and adds them to deck
    private void addCards(Deck deck, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("flashcards");
        for (Object json : jsonArray) {
            JSONObject nextCard = (JSONObject) json;
            String front = nextCard.getString("front");
            String back = nextCard.getString("back");
            Flashcard card = new Flashcard(front, back);
            deck.addCard(card);
        }
    }

    public String getSource() {
        return source;
    }

}
