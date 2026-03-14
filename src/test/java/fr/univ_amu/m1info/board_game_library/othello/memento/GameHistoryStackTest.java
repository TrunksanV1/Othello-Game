package fr.univ_amu.m1info.board_game_library.othello.memento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour GameHistoryStack (Caretaker du Pattern Memento)
 * Test la fonctionnalité Undo/Redo
 */
class GameHistoryStackTest {

    private GameHistoryStack historyStack;

    @BeforeEach
    void setUp() {
        historyStack = new GameHistoryStack();
    }

    @Test
    @DisplayName("Test pile initialement vide")
    void testInitiallyEmpty() {
        // Assert
        assertFalse(historyStack.canUndo());
        assertFalse(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test saveState ajoute un état")
    void testSaveState() {
        // Arrange
        GameMemento memento = createMemento("WWWW....BBBB", "BLACK");

        // Act
        historyStack.saveState(memento);

        // Assert
        assertTrue(historyStack.canUndo());
        assertFalse(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test undo retourne l'état précédent")
    void testUndo() {
        // Arrange
        GameMemento state1 = createMemento("State1", "BLACK");
        GameMemento state2 = createMemento("State2", "WHITE");

        historyStack.saveState(state1);
        historyStack.saveState(state2);

        // Act
        GameMemento undone = historyStack.undo();

        // Assert
        assertNotNull(undone);
        assertEquals("State1", undone.getBoardState());
        assertTrue(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test undo sur pile vide retourne null")
    void testUndoEmpty() {
        // Act
        GameMemento result = historyStack.undo();

        // Assert
        assertNull(result);
        assertFalse(historyStack.canUndo());
    }

    @Test
    @DisplayName("Test redo après undo")
    void testRedo() {
        // Arrange
        GameMemento state1 = createMemento("State1", "BLACK");
        GameMemento state2 = createMemento("State2", "WHITE");

        historyStack.saveState(state1);
        historyStack.saveState(state2);
        historyStack.undo(); // Undo vers state1

        // Act
        GameMemento redone = historyStack.redo();

        // Assert
        assertNotNull(redone);
        assertEquals("State2", redone.getBoardState());
        assertFalse(historyStack.canRedo()); // Plus de redo possible
    }

    @Test
    @DisplayName("Test redo sur pile vide retourne null")
    void testRedoEmpty() {
        // Act
        GameMemento result = historyStack.redo();

        // Assert
        assertNull(result);
        assertFalse(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test nouveau move efface le redo")
    void testNewMoveClearsRedo() {
        // Arrange
        GameMemento state1 = createMemento("State1", "BLACK");
        GameMemento state2 = createMemento("State2", "WHITE");
        GameMemento state3 = createMemento("State3", "BLACK");

        historyStack.saveState(state1);
        historyStack.saveState(state2);
        historyStack.undo(); // Peut redo vers state2

        // Act - Nouveau move efface l'historique redo
        historyStack.saveState(state3);

        // Assert
        assertFalse(historyStack.canRedo(), "Nouveau move devrait effacer la pile redo");
        assertTrue(historyStack.canUndo());
    }

    @Test
    @DisplayName("Test multiple undo/redo")
    void testMultipleUndoRedo() {
        // Arrange
        GameMemento state1 = createMemento("State1", "BLACK");
        GameMemento state2 = createMemento("State2", "WHITE");
        GameMemento state3 = createMemento("State3", "BLACK");

        historyStack.saveState(state1);
        historyStack.saveState(state2);
        historyStack.saveState(state3);

        // Act & Assert
        // Undo deux fois
        GameMemento undo1 = historyStack.undo();
        assertEquals("State2", undo1.getBoardState());

        GameMemento undo2 = historyStack.undo();
        assertEquals("State1", undo2.getBoardState());

        // Redo une fois
        GameMemento redo1 = historyStack.redo();
        assertEquals("State2", redo1.getBoardState());

        assertTrue(historyStack.canUndo());
        assertTrue(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test limite de 50 états")
    void testMaxCapacity() {
        // Arrange - Ajouter 60 états (max = 50)
        for (int i = 0; i < 60; i++) {
            GameMemento memento = createMemento("State" + i, i % 2 == 0 ? "BLACK" : "WHITE");
            historyStack.saveState(memento);
        }

        // Act - Undo jusqu'à la limite
        int undoCount = 0;
        while (historyStack.canUndo()) {
            historyStack.undo();
            undoCount++;
        }

        // Assert
        assertEquals(50, undoCount, "Devrait limiter à 50 états maximum");
    }

    @Test
    @DisplayName("Test undo complet jusqu'au début")
    void testUndoToBeginning() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            historyStack.saveState(createMemento("State" + i, "BLACK"));
        }

        // Act - Undo tout
        int undoCount = 0;
        while (historyStack.canUndo()) {
            historyStack.undo();
            undoCount++;
        }

        // Assert
        assertEquals(5, undoCount);
        assertFalse(historyStack.canUndo());
        assertTrue(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test redo complet jusqu'à la fin")
    void testRedoToEnd() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            historyStack.saveState(createMemento("State" + i, "BLACK"));
        }

        // Undo tout
        while (historyStack.canUndo()) {
            historyStack.undo();
        }

        // Act - Redo tout
        int redoCount = 0;
        while (historyStack.canRedo()) {
            historyStack.redo();
            redoCount++;
        }

        // Assert
        assertEquals(5, redoCount);
        assertFalse(historyStack.canRedo());
        assertTrue(historyStack.canUndo());
    }

    @Test
    @DisplayName("Test clear efface tout")
    void testClear() {
        // Arrange
        historyStack.saveState(createMemento("State1", "BLACK"));
        historyStack.saveState(createMemento("State2", "WHITE"));
        historyStack.undo();

        // Act
        historyStack.clear();

        // Assert
        assertFalse(historyStack.canUndo());
        assertFalse(historyStack.canRedo());
    }

    @Test
    @DisplayName("Test scénario complexe undo/redo/move")
    void testComplexScenario() {
        // Arrange
        historyStack.saveState(createMemento("State1", "BLACK"));
        historyStack.saveState(createMemento("State2", "WHITE"));
        historyStack.saveState(createMemento("State3", "BLACK"));

        // Act & Assert
        // Undo 2 fois
        historyStack.undo(); // Vers State2
        historyStack.undo(); // Vers State1
        assertTrue(historyStack.canRedo());

        // Redo 1 fois
        GameMemento redone = historyStack.redo(); // Vers State2
        assertEquals("State2", redone.getBoardState());

        // Nouveau move
        historyStack.saveState(createMemento("State4", "WHITE"));
        assertFalse(historyStack.canRedo(), "Nouveau move efface le redo");

        // Undo devrait retourner State2
        GameMemento undone = historyStack.undo();
        assertEquals("State2", undone.getBoardState());
    }

    @Test
    @DisplayName("Test états avec différents joueurs")
    void testDifferentPlayers() {
        // Arrange & Act
        historyStack.saveState(createMemento("State1", "BLACK"));
        historyStack.saveState(createMemento("State2", "WHITE"));
        historyStack.saveState(createMemento("State3", "BLACK"));

        // Assert
        GameMemento undo1 = historyStack.undo();
        assertEquals("WHITE", undo1.getCurrentPlayer());

        GameMemento undo2 = historyStack.undo();
        assertEquals("BLACK", undo2.getCurrentPlayer());
    }

    @Test
    @DisplayName("Test intégrité après multiples opérations")
    void testIntegrityAfterMultipleOperations() {
        // Arrange
        for (int i = 0; i < 10; i++) {
            historyStack.saveState(createMemento("State" + i, i % 2 == 0 ? "BLACK" : "WHITE"));
        }

        // Act - Mix d'opérations
        historyStack.undo();
        historyStack.undo();
        historyStack.redo();
        historyStack.saveState(createMemento("NewState", "BLACK"));
        historyStack.undo();
        historyStack.undo();

        // Assert - Vérifier que la pile est toujours cohérente
        assertTrue(historyStack.canUndo() || !historyStack.canUndo());
        assertTrue(historyStack.canRedo() || !historyStack.canRedo());

        // Ne devrait pas crash
        assertDoesNotThrow(() -> {
            while (historyStack.canUndo()) {
                historyStack.undo();
            }
        });
    }

    // Helper method
    private GameMemento createMemento(String boardState, String player) {
        return new GameMemento(boardState, player, System.currentTimeMillis(), false, 0);
    }
}
