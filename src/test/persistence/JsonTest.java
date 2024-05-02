package persistence;

import model.Flashcard;

import static org.junit.jupiter.api.Assertions.assertEquals;

// JsonTest was heavily inspired by the JsonTest class in the JsonSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonTest {
    protected void checkCard(String front, String back, Flashcard card) {
        assertEquals(front, card.getFront());
        assertEquals(back, card.getBack());
    }
}
