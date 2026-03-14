package fr.univ_amu.m1info.board_game_library.othello.memento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour GameMemento (Pattern Memento)
 * Test la sauvegarde d'état du jeu
 */
class GameMementoTest {

    @Test
    @DisplayName("Test création GameMemento avec état valide")
    void testCreateMemento() {
        // Arrange
        String boardState = "WWWWWWWW............BBBBBBBB";
        String currentPlayer = "BLACK";
        long startTime = System.currentTimeMillis();
        boolean gameOver = false;
        int consecutivePasses = 0;

        // Act
        GameMemento memento = new GameMemento(
            boardState,
            currentPlayer,
            startTime,
            gameOver,
            consecutivePasses
        );

        // Assert
        assertNotNull(memento);
        assertEquals(boardState, memento.getBoardState());
        assertEquals(currentPlayer, memento.getCurrentPlayer());
        assertEquals(startTime, memento.getStartTime());
        assertFalse(memento.isGameOver());
        assertEquals(0, memento.getConsecutivePasses());
    }

    @Test
    @DisplayName("Test GameMemento préserve l'état exact")
    void testMementoPreservesState() {
        // Arrange
        String complexBoardState = "BWBWBWBWWBWBWBWB....WBWBWBWBBWBWBWBW";
        String player = "WHITE";
        long time = 1234567890L;
        boolean gameOver = true;
        int passes = 2;

        // Act
        GameMemento memento = new GameMemento(complexBoardState, player, time, gameOver, passes);

        // Assert
        assertEquals(complexBoardState, memento.getBoardState());
        assertEquals(player, memento.getCurrentPlayer());
        assertEquals(time, memento.getStartTime());
        assertTrue(memento.isGameOver());
        assertEquals(2, memento.getConsecutivePasses());
    }

    @Test
    @DisplayName("Test GameMemento avec jeu terminé")
    void testMementoGameOver() {
        // Act
        GameMemento memento = new GameMemento(
            "WWWWWWWWWWWWWWWW....BBBBBBBBBBBBBBBB",
            "BLACK",
            System.currentTimeMillis(),
            true,  // gameOver = true
            2
        );

        // Assert
        assertTrue(memento.isGameOver());
    }

    @Test
    @DisplayName("Test GameMemento avec passes consécutifs")
    void testMementoConsecutivePasses() {
        // Act
        GameMemento memento = new GameMemento(
            "WWWW............BBBB",
            "WHITE",
            System.currentTimeMillis(),
            false,
            1  // un pass
        );

        // Assert
        assertEquals(1, memento.getConsecutivePasses());
    }

    @Test
    @DisplayName("Test immutabilité du Memento")
    void testMementoImmutability() {
        // Arrange
        String boardState = "WWWW............BBBB";
        String player = "BLACK";
        long time = System.currentTimeMillis();

        // Act
        GameMemento memento = new GameMemento(boardState, player, time, false, 0);

        // Assert - Vérifier que les getters retournent toujours la même valeur
        String state1 = memento.getBoardState();
        String state2 = memento.getBoardState();
        assertEquals(state1, state2);

        // Le memento ne devrait pas avoir de setters
        assertEquals(5, GameMemento.class.getDeclaredFields().length,
            "GameMemento devrait avoir exactement 5 champs privés");
    }

    @Test
    @DisplayName("Test GameMemento avec plateau vide")
    void testMementoEmptyBoard() {
        // Arrange - Plateau vide (tous les points)
        String emptyBoard = "................................................................";

        // Act
        GameMemento memento = new GameMemento(emptyBoard, "BLACK", 0, false, 0);

        // Assert
        assertEquals(emptyBoard, memento.getBoardState());
        assertEquals(64, memento.getBoardState().length()); // 8x8 = 64
    }

    @Test
    @DisplayName("Test GameMemento avec plateau complet")
    void testMementoFullBoard() {
        // Arrange - Plateau rempli
        String fullBoard = "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

        // Act
        GameMemento memento = new GameMemento(fullBoard, "WHITE", 0, true, 0);

        // Assert
        assertEquals(fullBoard, memento.getBoardState());
        assertTrue(memento.isGameOver());
    }

    @Test
    @DisplayName("Test comparaison de deux Mementos différents")
    void testTwoDifferentMementos() {
        // Arrange
        GameMemento memento1 = new GameMemento(
            "WWWW............BBBB",
            "BLACK",
            1000,
            false,
            0
        );

        GameMemento memento2 = new GameMemento(
            "WWWWW...........BBBB",
            "WHITE",
            2000,
            false,
            1
        );

        // Assert
        assertNotEquals(memento1.getBoardState(), memento2.getBoardState());
        assertNotEquals(memento1.getCurrentPlayer(), memento2.getCurrentPlayer());
        assertNotEquals(memento1.getStartTime(), memento2.getStartTime());
        assertNotEquals(memento1.getConsecutivePasses(), memento2.getConsecutivePasses());
    }

    @Test
    @DisplayName("Test encapsulation - état opaque")
    void testEncapsulation() {
        // Arrange
        String originalState = "WWWW............BBBB";
        GameMemento memento = new GameMemento(originalState, "BLACK", 0, false, 0);

        // Act - Modifier la string originale
        originalState = "BBBB............WWWW";

        // Assert - Le memento doit conserver l'état original
        assertEquals("WWWW............BBBB", memento.getBoardState());
    }
}
