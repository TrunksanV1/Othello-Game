package fr.univ_amu.m1info.board_game_library.othello.player;

import fr.univ_amu.m1info.board_game_library.othello.model.Piece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour PlayerFactory (Pattern Factory)
 */
class PlayerFactoryTest {

    @Test
    @DisplayName("Test création HumanPlayer")
    void testCreateHumanPlayer() {
        // Act
        Player player = PlayerFactory.createHumanPlayer("Alice");

        // Assert
        assertNotNull(player);
        assertTrue(player instanceof HumanPlayer);
        assertEquals("Alice", player.getName());
        assertFalse(player.isAI());
    }

    @Test
    @DisplayName("Test création AIPlayer facile")
    void testCreateAIPlayer_Easy() {
        // Act
        Player player = PlayerFactory.createAIPlayer("EasyBot", "Facile", Piece.BLACK);

        // Assert
        assertNotNull(player);
        assertTrue(player instanceof AIPlayer);
        assertEquals("EasyBot", player.getName());
        assertTrue(player.isAI());
    }

    @Test
    @DisplayName("Test création AIPlayer moyen")
    void testCreateAIPlayer_Medium() {
        // Act
        Player player = PlayerFactory.createAIPlayer("MediumBot", "Moyen", Piece.WHITE);

        // Assert
        assertNotNull(player);
        assertTrue(player instanceof AIPlayer);
        assertEquals("MediumBot", player.getName());
        assertTrue(player.isAI());
    }

    @Test
    @DisplayName("Test création AIPlayer difficile")
    void testCreateAIPlayer_Hard() {
        // Act
        Player player = PlayerFactory.createAIPlayer("HardBot", "Difficile", Piece.BLACK);

        // Assert
        assertNotNull(player);
        assertTrue(player instanceof AIPlayer);
        assertEquals("HardBot", player.getName());
        assertTrue(player.isAI());
    }

    @Test
    @DisplayName("Test createPlayer mode PVP - joueur 1")
    void testCreatePlayer_PVP_Player1() {
        // Act
        Player player1 = PlayerFactory.createPlayer("PVP", "Alice", null, true);

        // Assert
        assertNotNull(player1);
        assertTrue(player1 instanceof HumanPlayer);
        assertEquals("Alice", player1.getName());
        assertFalse(player1.isAI());
    }

    @Test
    @DisplayName("Test createPlayer mode PVP - joueur 2")
    void testCreatePlayer_PVP_Player2() {
        // Act
        Player player2 = PlayerFactory.createPlayer("PVP", "Bob", null, false);

        // Assert
        assertNotNull(player2);
        assertTrue(player2 instanceof HumanPlayer);
        assertEquals("Bob", player2.getName());
        assertFalse(player2.isAI());
    }

    @Test
    @DisplayName("Test createPlayer mode PVE - joueur 1 humain")
    void testCreatePlayer_PVE_Player1() {
        // Act
        Player player1 = PlayerFactory.createPlayer("PVE", "Alice", "Moyen", true);

        // Assert
        assertNotNull(player1);
        assertTrue(player1 instanceof HumanPlayer);
        assertEquals("Alice", player1.getName());
        assertFalse(player1.isAI());
    }

    @Test
    @DisplayName("Test createPlayer mode PVE - joueur 2 IA")
    void testCreatePlayer_PVE_Player2() {
        // Act
        Player player2 = PlayerFactory.createPlayer("PVE", "IA", "Difficile", false);

        // Assert
        assertNotNull(player2);
        assertTrue(player2 instanceof AIPlayer);
        assertEquals("IA", player2.getName());
        assertTrue(player2.isAI());
    }

    @Test
    @DisplayName("Test createPlayer avec mode invalide lance exception")
    void testCreatePlayer_InvalidMode() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            PlayerFactory.createPlayer("InvalidMode", "Test", null, true);
        });
    }

    @Test
    @DisplayName("Test polymorphisme - tous implémentent Player")
    void testPolymorphism() {
        // Act
        Player human = PlayerFactory.createHumanPlayer("Alice");
        Player ai = PlayerFactory.createAIPlayer("Bot", "Moyen", Piece.BLACK);

        // Assert
        assertTrue(human instanceof Player);
        assertTrue(ai instanceof Player);

        // Les deux peuvent être traités de la même manière
        assertDoesNotThrow(() -> {
            String name1 = human.getName();
            String name2 = ai.getName();
            boolean isAI1 = human.isAI();
            boolean isAI2 = ai.isAI();
        });
    }

    @Test
    @DisplayName("Test mode PVP les deux joueurs sont humains")
    void testPVP_BothHuman() {
        // Act
        Player player1 = PlayerFactory.createPlayer("PVP", "Alice", null, true);
        Player player2 = PlayerFactory.createPlayer("PVP", "Bob", null, false);

        // Assert
        assertFalse(player1.isAI());
        assertFalse(player2.isAI());
    }

    @Test
    @DisplayName("Test mode PVE joueur 1 humain, joueur 2 IA")
    void testPVE_HumanVsAI() {
        // Act
        Player player1 = PlayerFactory.createPlayer("PVE", "Human", "Moyen", true);
        Player player2 = PlayerFactory.createPlayer("PVE", "IA", "Moyen", false);

        // Assert
        assertFalse(player1.isAI(), "Joueur 1 doit être humain");
        assertTrue(player2.isAI(), "Joueur 2 doit être IA");
    }

    @Test
    @DisplayName("Test createAIPlayer avec différentes difficultés")
    void testAIPlayerDifferentDifficulties() {
        // Act
        Player easy = PlayerFactory.createAIPlayer("Easy", "Facile", Piece.BLACK);
        Player medium = PlayerFactory.createAIPlayer("Medium", "Moyen", Piece.BLACK);
        Player hard = PlayerFactory.createAIPlayer("Hard", "Difficile", Piece.BLACK);

        // Assert
        assertTrue(easy.isAI());
        assertTrue(medium.isAI());
        assertTrue(hard.isAI());
    }

    @Test
    @DisplayName("Test createAIPlayer avec différentes pièces")
    void testAIPlayerDifferentPieces() {
        // Act
        Player blackAI = PlayerFactory.createAIPlayer("BlackBot", "Moyen", Piece.BLACK);
        Player whiteAI = PlayerFactory.createAIPlayer("WhiteBot", "Moyen", Piece.WHITE);

        // Assert
        assertEquals("BlackBot", blackAI.getName());
        assertEquals("WhiteBot", whiteAI.getName());
        assertTrue(blackAI.isAI());
        assertTrue(whiteAI.isAI());
    }

    @Test
    @DisplayName("Test Factory centralise la création")
    void testFactoryCentralizesCreation() {
        // Act - Toutes les créations passent par la factory
        Player human1 = PlayerFactory.createHumanPlayer("P1");
        Player human2 = PlayerFactory.createHumanPlayer("P2");
        Player ai1 = PlayerFactory.createAIPlayer("AI1", "Facile", Piece.BLACK);
        Player ai2 = PlayerFactory.createAIPlayer("AI2", "Difficile", Piece.WHITE);

        // Assert - Tous sont des instances valides de Player
        assertNotNull(human1);
        assertNotNull(human2);
        assertNotNull(ai1);
        assertNotNull(ai2);
    }
}
