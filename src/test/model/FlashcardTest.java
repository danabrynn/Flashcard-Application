package model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardTest {
    Flashcard flashcard;

    @BeforeEach
    void runBefore() {
        flashcard = new Flashcard("test question", "test answer");
    }

    @Test
    void testConstructor() {
        assertEquals("test question", flashcard.displayCard());
        assertFalse(flashcard.isFlipped());
        assertFalse(flashcard.isViewed());
        assertFalse(flashcard.isCorrect());
    }

    @Test
    void testDisplayCardFlipped() {
        assertEquals("test question", flashcard.displayCard());
        flashcard.flipCard();
        assertEquals("test answer", flashcard.displayCard());
    }

    @Test
    void testFlipCard() {
        flashcard.flipCard();
        assertTrue(flashcard.isFlipped());
        assertTrue(flashcard.isViewed());
    }

    @Test
    void testFlipCardMultiple() {
        flashcard.flipCard();
        assertTrue(flashcard.isFlipped());
        assertTrue(flashcard.isViewed());
        flashcard.flipCard();
        assertFalse(flashcard.isFlipped());
        assertTrue(flashcard.isViewed());
    }

    @Test
    void testResetCard() {
        flashcard.setCorrect(true);
        flashcard.flipCard();
        flashcard.resetCard();
        assertFalse(flashcard.isFlipped());
        assertFalse(flashcard.isViewed());
        assertFalse(flashcard.isCorrect());
    }

    @Test
    void testToString() {
        String toString = flashcard.toString();
        assertEquals(toString, "Flashcard ID number: " + flashcard.getId()
                + "\nFront: test question"
                + "\nBack: test answer");
    }
}