package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    Deck testDeck;
    Flashcard cardA;
    Flashcard cardB;
    Flashcard cardC;
    Flashcard cardD;
    List<Flashcard> cards;

    @BeforeEach
    void runBefore() {
        testDeck = new Deck("Test deck");
        cardA = new Flashcard("Front side A", "Back side A");
        cardB = new Flashcard("Front side B", "Back side B");
        cardC = new Flashcard("Front side C", "Back side C");
        cardD = new Flashcard("Front side D", "Back side D");
    }

    @Test
    void testConstructor() {
        assertEquals("Test deck", testDeck.getName());
        assertEquals(0, testDeck.getDeckSize());
    }

    @Test
    void testAddCard() {
        assertTrue(testDeck.addCard(cardA));
        assertEquals(1, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardA);
    }

    @Test
    void testAddMultipleCards() {
        assertTrue(testDeck.addCard(cardA));
        assertTrue(testDeck.addCard(cardB));
        assertEquals(2, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardA);
        assertEquals(cards.get(1), cardB);
    }

    @Test
    void testAddSameCard() {
        assertTrue(testDeck.addCard(cardA));
        assertFalse(testDeck.addCard(cardA));
        assertEquals(1, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardA);
    }

    @Test
    void testRemoveCard() {
        createDeck();
        assertTrue(testDeck.removeCard(cardB));
        assertEquals(3, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardA);
        assertEquals(cards.get(1), cardC);
        assertEquals(cards.get(2), cardD);
    }

    @Test
    void testRemoveCardNotInDeck() {
        testDeck.addCard(cardA);
        assertFalse(testDeck.removeCard(cardB));
        assertEquals(1, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardA);
    }

    @Test
    void testRemoveMultipleCards() {
        createDeck();
        assertTrue(testDeck.removeCard(cardA));
        assertTrue(testDeck.removeCard(cardB));
        assertEquals(2, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardC);
        assertEquals(cards.get(1), cardD);

    }

    @Test
    void testRemoveCardUsingId() {
        createDeck();
        assertTrue(testDeck.removeCard(cardB.getId()));
        assertEquals(3, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardA);
        assertEquals(cards.get(1), cardC);
        assertEquals(cards.get(2), cardD);
    }

    @Test
    void testDeleteMultipleCardsUsingId() {
        createDeck();
        assertTrue(testDeck.removeCard(cardA.getId()));
        assertTrue(testDeck.removeCard(cardB.getId()));
        assertFalse(testDeck.removeCard(cardB.getId()));
        assertEquals(2, testDeck.getDeckSize());
        cards = testDeck.getFlashcards(false);
        assertEquals(cards.get(0), cardC);
        assertEquals(cards.get(1), cardD);
    }

    @Test
    void testGetPercentCorrectEmptyDeck() {
        assertEquals(0, testDeck.getPercentCorrect());
    }

    @Test
    void testGetPercentCorrectNoneViewed() {
        createDeck();
        cardA.setCorrect(true);
        assertEquals(0, testDeck.getPercentCorrect());
    }

    @Test
    void testGetPercentCorrectViewedNoneCorrect() {
        createDeck();
        cardA.flipCard();
        cardC.flipCard();
        assertEquals(0, testDeck.getPercentCorrect());
    }

    @Test
    void testGetPercentCorrectViewedSomeCorrect() {
        createDeck();
        cardA.flipCard();
        cardB.flipCard();
        cardC.flipCard();
        cardA.setCorrect(true);
        cardB.setCorrect(true);
        assertEquals(100 * ((double)2/3), testDeck.getPercentCorrect());
    }

    @Test
    void testGetPercentCorrectViewedAllCorrect() {
        createDeck();
        cardA.flipCard();
        cardB.flipCard();
        cardA.setCorrect(true);
        cardB.setCorrect(true);
        assertEquals(100, testDeck.getPercentCorrect());
    }

    @Test
    void testGetNumberCorrectEmptyDeck() {
        assertEquals(0, testDeck.getNumberCorrect());
    }

    @Test
    void testGetNumberCorrectNoneCorrect() {
        createDeck();
        assertEquals(0, testDeck.getNumberCorrect());
    }

    @Test
    void testGetNumberCorrect() {
        createDeck();
        cardA.flipCard();
        cardA.setCorrect(true);
        assertEquals(1, testDeck.getNumberCorrect());
    }

    @Test
    void testGetPercentViewedEmptyDeck() {
        assertEquals(0, testDeck.getPercentViewed());
    }

    @Test
    void testGetPercentViewedNoneViewed() {
        createDeck();
        assertEquals(0, testDeck.getPercentViewed());
    }

    @Test
    void testGetPercentViewedSomeViewed() {
        createDeck();
        cardA.flipCard();
        cardB.flipCard();
        assertEquals(50, testDeck.getPercentViewed());
    }

    @Test
    void testGetPercentViewedAllViewed() {
        createDeck();
        cardA.flipCard();
        cardB.flipCard();
        cardC.flipCard();
        cardD.flipCard();
        assertEquals(100, testDeck.getPercentViewed());
    }

    @Test
    void testGetNumberViewedNoneViewed() {
        createDeck();
        assertEquals(0, testDeck.getNumberViewed());
    }

    @Test
    void testGetNumberViewed() {
        createDeck();
        cardA.flipCard();
        assertEquals(1, testDeck.getNumberViewed());
    }

    @Test
    void testGetFlashcardsEmptyDeck() {
        assertEquals(0, testDeck.getFlashcards(false).size());
    }

    @Test
    void testGetFlashcards() {
        createDeck();
        cards = testDeck.getFlashcards(false);
        assertEquals(4, cards.size());
        assertEquals(cards.get(0), cardA);
        assertEquals(cards.get(1), cardB);
        assertEquals(cards.get(2), cardC);
        assertEquals(cards.get(3), cardD);
    }

    @Test
    void testGetFlashcardsPreventAlteration() {
        createDeck();
        List<Flashcard> cardsA = testDeck.getFlashcards(false);
        List<Flashcard> cardsB = testDeck.getFlashcards(false);
        cardsA.remove(cardB);
        assertEquals(3, cardsA.size());
        assertEquals(4, cardsB.size());
    }

    @Test
    void testGetFlashcardsRandomOrder() {
        createDeck();
        cards = testDeck.getFlashcards(true);
        assertEquals(4, cards.size());
        assertTrue(cards.contains(cardA));
        assertTrue(cards.contains(cardB));
        assertTrue(cards.contains(cardC));
        assertTrue(cards.contains(cardD));
    }

    @Test
    void testResetDeck() {
        createDeck();
        cardA.setCorrect(true);
        cardB.flipCard();
        testDeck.resetDeck();
        for (Flashcard flashcard: testDeck.getFlashcards(false)) {
            assertFalse(flashcard.isViewed());
            assertFalse(flashcard.isFlipped());
            assertFalse(flashcard.isCorrect());
        }
    }

    private void createDeck(){
        testDeck.addCard(cardA);
        testDeck.addCard(cardB);
        testDeck.addCard(cardC);
        testDeck.addCard(cardD);
    }

}
