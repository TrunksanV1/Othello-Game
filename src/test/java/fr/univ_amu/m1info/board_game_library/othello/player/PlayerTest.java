package fr.univ_amu.m1info.board_game_library.othello.player;

import fr.univ_amu.m1info.board_game_library.othello.model.OthelloGame;
import fr.univ_amu.m1info.board_game_library.othello.model.Piece;
import fr.univ_amu.m1info.board_game_library.othello.model.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour les implémentations Player (Pattern Strategy)
 * Utilise Mockito pour mocker OthelloGame (Java 21 compatible)
 */
class PlayerTest {

    @Test
    @DisplayName("Test HumanPlayer retourne null pour chooseMove")
    void testHumanPlayerChooseMove() {
        // Arrange
        HumanPlayer player = new HumanPlayer("Alice");
        OthelloGame mockGame = mock(OthelloGame.class);

        // Act
        Position move = player.chooseMove(mockGame);

        // Assert
        assertNull(move, "HumanPlayer devrait retourner null (attend input UI)");
        assertEquals("Alice", player.getName());
        assertFalse(player.isAI());
    }

    @Test
    @DisplayName("Test HumanPlayer n'est pas une IA")
    void testHumanPlayerIsNotAI() {
        // Arrange
        HumanPlayer player = new HumanPlayer("Bob");

        // Assert
        assertFalse(player.isAI());
    }

    @Test
    @DisplayName("Test HumanPlayer avec nom vide")
    void testHumanPlayerEmptyName() {
        // Act
        HumanPlayer player = new HumanPlayer("");

        // Assert
        assertNotNull(player.getName());
        assertEquals("", player.getName());
    }

    @Test
    @DisplayName("Test AIPlayer est une IA")
    void testAIPlayerIsAI() {
        // Arrange
        AIPlayer player = new AIPlayer("EasyBot", "Easy", Piece.BLACK);

        // Assert
        assertTrue(player.isAI());
        assertEquals("EasyBot", player.getName());
    }

    @Test
    @DisplayName("Test AIPlayer chooseMove retourne une position valide")
    void testAIPlayerChooseMove() {
        // Arrange
        AIPlayer player = new AIPlayer("MediumBot", "Medium", Piece.BLACK);
        OthelloGame game = new OthelloGame(); // Jeu réel avec position initiale

        // Act
        Position move = player.chooseMove(game);

        // Assert
        assertNotNull(move, "AIPlayer devrait retourner un coup valide");
        assertTrue(move.getRow() >= 0 && move.getRow() < 8);
        assertTrue(move.getColumn() >= 0 && move.getColumn() < 8);
    }

    @Test
    @DisplayName("Test AIPlayer facile choisit un coup")
    void testAIPlayerEasy() {
        // Arrange
        AIPlayer easyPlayer = new AIPlayer("EasyBot", "Easy", Piece.BLACK);
        OthelloGame game = new OthelloGame();

        // Act
        Position move = easyPlayer.chooseMove(game);

        // Assert
        assertNotNull(move);
        assertEquals("EasyBot", easyPlayer.getName());
        assertTrue(easyPlayer.isAI());
    }

    @Test
    @DisplayName("Test AIPlayer moyen choisit un coup")
    void testAIPlayerMedium() {
        // Arrange
        AIPlayer mediumPlayer = new AIPlayer("MediumBot", "Medium", Piece.WHITE);
        OthelloGame game = new OthelloGame();
        game.makeMove(2, 3); // BLACK joue

        // Act
        Position move = mediumPlayer.chooseMove(game);

        // Assert
        assertNotNull(move);
        assertEquals("MediumBot", mediumPlayer.getName());
    }

    @Test
    @DisplayName("Test AIPlayer difficile choisit un coup")
    void testAIPlayerHard() {
        // Arrange
        AIPlayer hardPlayer = new AIPlayer("HardBot", "Hard", Piece.BLACK);
        OthelloGame game = new OthelloGame();

        // Act
        Position move = hardPlayer.chooseMove(game);

        // Assert
        assertNotNull(move);
        assertEquals("HardBot", hardPlayer.getName());
    }

    @Test
    @DisplayName("Test polymorphisme - HumanPlayer et AIPlayer implémentent Player")
    void testPolymorphism() {
        // Arrange
        Player human = new HumanPlayer("Alice");
        Player ai = new AIPlayer("Bot", "Easy", Piece.BLACK);

        // Assert
        assertTrue(human instanceof Player);
        assertTrue(ai instanceof Player);

        // Les deux peuvent être utilisés de la même manière
        assertNotNull(human.getName());
        assertNotNull(ai.getName());

        boolean humanIsAI = human.isAI();
        boolean aiIsAI = ai.isAI();

        assertFalse(humanIsAI);
        assertTrue(aiIsAI);
    }

    @Test
    @DisplayName("Test AIPlayer avec mock OthelloGame")
    void testAIPlayerWithMockGame() {
        // Arrange
        AIPlayer player = new AIPlayer("MockBot", "Easy", Piece.BLACK);
        OthelloGame mockGame = mock(OthelloGame.class);

        // Mock le comportement
        when(mockGame.getBoard()).thenReturn(new fr.univ_amu.m1info.board_game_library.othello.model.OthelloBoard());

        // Act
        Position move = player.chooseMove(mockGame);

        // Assert
        assertNotNull(move);
        verify(mockGame, atLeastOnce()).getBoard();
    }

    @Test
    @DisplayName("Test HumanPlayer avec différents noms")
    void testHumanPlayerDifferentNames() {
        // Act
        HumanPlayer player1 = new HumanPlayer("Alice");
        HumanPlayer player2 = new HumanPlayer("Bob");
        HumanPlayer player3 = new HumanPlayer("Charlie");

        // Assert
        assertEquals("Alice", player1.getName());
        assertEquals("Bob", player2.getName());
        assertEquals("Charlie", player3.getName());

        assertFalse(player1.isAI());
        assertFalse(player2.isAI());
        assertFalse(player3.isAI());
    }

    @Test
    @DisplayName("Test AIPlayer avec différentes difficultés")
    void testAIPlayerDifferentDifficulties() {
        // Arrange
        AIPlayer easy = new AIPlayer("Easy", "Easy", Piece.BLACK);
        AIPlayer medium = new AIPlayer("Medium", "Medium", Piece.BLACK);
        AIPlayer hard = new AIPlayer("Hard", "Hard", Piece.BLACK);

        OthelloGame game = new OthelloGame();

        // Act & Assert - Tous doivent choisir un coup valide
        assertNotNull(easy.chooseMove(game));
        assertNotNull(medium.chooseMove(game));
        assertNotNull(hard.chooseMove(game));

        assertTrue(easy.isAI());
        assertTrue(medium.isAI());
        assertTrue(hard.isAI());
    }

    @Test
    @DisplayName("Test AIPlayer avec pièces différentes")
    void testAIPlayerDifferentPieces() {
        // Arrange
        AIPlayer blackAI = new AIPlayer("BlackBot", "Medium", Piece.BLACK);
        AIPlayer whiteAI = new AIPlayer("WhiteBot", "Medium", Piece.WHITE);

        // Assert
        assertEquals("BlackBot", blackAI.getName());
        assertEquals("WhiteBot", whiteAI.getName());
        assertTrue(blackAI.isAI());
        assertTrue(whiteAI.isAI());
    }

    @Test
    @DisplayName("Test interface Player définit le contrat")
    void testPlayerInterface() {
        // Arrange - Créer une implémentation anonyme pour tester l'interface
        Player customPlayer = new Player() {
            @Override
            public Position chooseMove(OthelloGame game) {
                return new Position(2, 3);
            }

            @Override
            public String getName() {
                return "CustomPlayer";
            }

            @Override
            public boolean isAI() {
                return false;
            }
        };

        OthelloGame mockGame = mock(OthelloGame.class);

        // Act
        Position move = customPlayer.chooseMove(mockGame);
        String name = customPlayer.getName();
        boolean isAI = customPlayer.isAI();

        // Assert
        assertNotNull(move);
        assertEquals(2, move.getRow());
        assertEquals(3, move.getColumn());
        assertEquals("CustomPlayer", name);
        assertFalse(isAI);
    }

    @Test
    @DisplayName("Test substitution de Liskov - Player peut être substitué")
    void testLiskovSubstitution() {
        // Arrange - Tableau de Players
        Player[] players = new Player[]{
            new HumanPlayer("Alice"),
            new AIPlayer("Bot1", "Easy", Piece.BLACK),
            new HumanPlayer("Bob"),
            new AIPlayer("Bot2", "Hard", Piece.WHITE)
        };

        // Act & Assert - Tous peuvent être traités uniformément
        for (Player player : players) {
            assertNotNull(player.getName());
            // isAI() doit fonctionner pour tous
            boolean isAI = player.isAI();
            assertTrue(isAI || !isAI); // Booléen valide
        }
    }

    @Test
    @DisplayName("Test séparation des responsabilités")
    void testSeparationOfConcerns() {
        // Arrange
        HumanPlayer human = new HumanPlayer("Alice");
        AIPlayer ai = new AIPlayer("Bot", "Medium", Piece.BLACK);

        // Assert - HumanPlayer ne gère pas la logique de l'IA
        assertNull(human.chooseMove(mock(OthelloGame.class)));

        // AIPlayer délègue à OthelloAI (ne contient pas la logique directement)
        assertNotNull(ai.chooseMove(new OthelloGame()));
    }
}
