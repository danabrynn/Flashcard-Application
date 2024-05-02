package persistence;

import model.Deck;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of a deck to file
// JsonWriter was heavily inspired by the JsonWriter class in the JsonSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // Effects: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // Modifies: this
    // Effects: opens writer. Throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // Modifies: this
    // Effects: writes JSON representation of deck to file
    public void write(Deck deck) {
        JSONObject json = deck.toJson();
        saveToFile(json.toString(TAB));
    }

    // Modifies: this
    // Effects: closes writer
    public void close() {
        writer.close();
    }

    // Modifies: this
    // Effects: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}
