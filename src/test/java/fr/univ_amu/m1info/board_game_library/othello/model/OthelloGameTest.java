package fr.univ_amu.m1info.board_game_library.othello.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OthelloGameTest {

    private OthelloGame game;

    @BeforeEach
    void setUp() {
        game = new OthelloGame();
    }

    @Test
    void testInitialState() {
        assertEquals(Piece.BLACK, game.getCurrentPlayer());
        assertFalse(game.isGameOver());
        assertEquals(2, game.getBlackScore());
        assertEquals(2, game.getWhiteScore());
    }

    @Test
    void testMakeValidMove() {
        assertTrue(game.makeMove(2, 3));
        assertEquals(Piece.WHITE, game.getCurrentPlayer());
        assertEquals(4, game.getBlackScore());
        assertEquals(1, game.getWhiteScore());
    }

    @Test
    void testMakeInvalidMove() {
        assertFalse(game.makeMove(0, 0));
        assertEquals(Piece.BLACK, game.getCurrentPlayer());
    }

    @Test
    void testGameFlow() {
        game.makeMove(2, 3);
        assertEquals(Piece.WHITE, game.getCurrentPlayer());

        game.makeMove(2, 2);
        assertEquals(Piece.BLACK, game.getCurrentPlayer());

        assertFalse(game.isGameOver());
    }

    @Test
    void testScoreCalculation() {
        game.makeMove(2, 3);
        assertEquals(4, game.getBlackScore());
        assertEquals(1, game.getWhiteScore());

        game.makeMove(2, 2);
        assertTrue(game.getWhiteScore() >= 2);
    }

    @Test
    void testGameReset() {
        game.makeMove(2, 3);
        game.reset();

        assertEquals(Piece.BLACK, game.getCurrentPlayer());
        assertEquals(2, game.getBlackScore());
        assertEquals(2, game.getWhiteScore());
        assertFalse(game.isGameOver());
    }

    @Test
    void testGetValidMoves() {
        assertFalse(game.getValidMoves().isEmpty());
        assertEquals(4, game.getValidMoves().size());
    }

    @Test
    void testIsValidMove() {
        assertTrue(game.isValidMove(2, 3));
        assertFalse(game.isValidMove(0, 0));
        assertFalse(game.isValidMove(3, 3));
    }

    @Test
    void testGameStateSerialization() {
        game.makeMove(2, 3);
        String state = game.getGameState();
        assertNotNull(state);
        assertTrue(state.contains("|"));

        OthelloGame newGame = new OthelloGame();
        newGame.setGameState(state);
        assertEquals(game.getBlackScore(), newGame.getBlackScore());
        assertEquals(game.getWhiteScore(), newGame.getWhiteScore());
    }

    @Test
    void testGetPieceAt() {
        assertEquals(Piece.BLACK, game.getPieceAt(3, 4));
        assertEquals(Piece.WHITE, game.getPieceAt(3, 3));
        assertEquals(Piece.EMPTY, game.getPieceAt(0, 0));
    }
}
