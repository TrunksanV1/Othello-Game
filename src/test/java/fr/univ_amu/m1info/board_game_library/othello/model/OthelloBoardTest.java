package fr.univ_amu.m1info.board_game_library.othello.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OthelloBoardTest {

    private OthelloBoard board;

    @BeforeEach
    void setUp() {
        board = new OthelloBoard();
    }

    @Test
    void testInitialBoardState() {
        assertEquals(Piece.WHITE, board.getPieceAt(3, 3));
        assertEquals(Piece.BLACK, board.getPieceAt(3, 4));
        assertEquals(Piece.BLACK, board.getPieceAt(4, 3));
        assertEquals(Piece.WHITE, board.getPieceAt(4, 4));
        assertEquals(Piece.EMPTY, board.getPieceAt(0, 0));
    }

    @Test
    void testInitialPieceCounts() {
        assertEquals(2, board.countPieces(Piece.BLACK));
        assertEquals(2, board.countPieces(Piece.WHITE));
        assertEquals(60, board.countPieces(Piece.EMPTY));
    }

    @Test
    void testValidMoveDetection() {
        assertTrue(board.isValidMove(2, 3, Piece.BLACK));
        assertTrue(board.isValidMove(3, 2, Piece.BLACK));
        assertTrue(board.isValidMove(4, 5, Piece.BLACK));
        assertTrue(board.isValidMove(5, 4, Piece.BLACK));
    }

    @Test
    void testInvalidMoveDetection() {
        assertFalse(board.isValidMove(3, 3, Piece.BLACK));
        assertFalse(board.isValidMove(0, 0, Piece.BLACK));
        assertFalse(board.isValidMove(-1, 0, Piece.BLACK));
        assertFalse(board.isValidMove(8, 8, Piece.BLACK));
    }

    @Test
    void testMakeMove() {
        assertTrue(board.makeMove(2, 3, Piece.BLACK));
        assertEquals(Piece.BLACK, board.getPieceAt(2, 3));
        assertEquals(Piece.BLACK, board.getPieceAt(3, 3));
        assertEquals(4, board.countPieces(Piece.BLACK));
        assertEquals(1, board.countPieces(Piece.WHITE));
    }

    @Test
    void testGetValidMoves() {
        List<Position> validMoves = board.getValidMoves(Piece.BLACK);
        assertEquals(4, validMoves.size());

        validMoves = board.getValidMoves(Piece.WHITE);
        assertEquals(4, validMoves.size());
    }

    @Test
    void testPiecesToFlip() {
        List<Position> piecesToFlip = board.getPiecesToFlip(2, 3, Piece.BLACK);
        assertEquals(1, piecesToFlip.size());
        assertTrue(piecesToFlip.contains(new Position(3, 3)));
    }

    @Test
    void testBoardReset() {
        board.makeMove(2, 3, Piece.BLACK);
        board.reset();
        assertEquals(2, board.countPieces(Piece.BLACK));
        assertEquals(2, board.countPieces(Piece.WHITE));
        assertEquals(Piece.EMPTY, board.getPieceAt(2, 3));
    }

    @Test
    void testBoardStateSerialization() {
        String state = board.getBoardState();
        assertNotNull(state);
        assertEquals(64, state.length());

        board.makeMove(2, 3, Piece.BLACK);
        String newState = board.getBoardState();
        assertNotEquals(state, newState);

        OthelloBoard newBoard = new OthelloBoard();
        newBoard.setBoardState(newState);
        assertEquals(newState, newBoard.getBoardState());
    }

    @Test
    void testHasValidMoves() {
        assertTrue(board.hasValidMoves(Piece.BLACK));
        assertTrue(board.hasValidMoves(Piece.WHITE));
    }

    @Test
    void testOppositeColor() {
        assertEquals(Piece.WHITE, Piece.BLACK.opposite());
        assertEquals(Piece.BLACK, Piece.WHITE.opposite());
        assertEquals(Piece.EMPTY, Piece.EMPTY.opposite());
    }
}
