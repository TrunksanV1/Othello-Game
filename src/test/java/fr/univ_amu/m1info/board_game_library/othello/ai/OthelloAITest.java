package fr.univ_amu.m1info.board_game_library.othello.ai;

import fr.univ_amu.m1info.board_game_library.othello.model.OthelloBoard;
import fr.univ_amu.m1info.board_game_library.othello.model.Piece;
import fr.univ_amu.m1info.board_game_library.othello.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OthelloAITest {

    private OthelloBoard board;

    @BeforeEach
    void setUp() {
        board = new OthelloBoard();
    }

    @Test
    void testEasyAIReturnsValidMove() {
        OthelloAI easyAI = new OthelloAI("Facile");
        Position move = easyAI.getBestMove(board, Piece.BLACK);

        assertNotNull(move);
        assertTrue(board.isValidMove(move.getRow(), move.getColumn(), Piece.BLACK));
    }

    @Test
    void testMediumAIReturnsValidMove() {
        OthelloAI mediumAI = new OthelloAI("Moyen");
        Position move = mediumAI.getBestMove(board, Piece.BLACK);

        assertNotNull(move);
        assertTrue(board.isValidMove(move.getRow(), move.getColumn(), Piece.BLACK));
    }

    @Test
    void testHardAIReturnsValidMove() {
        OthelloAI hardAI = new OthelloAI("Difficile");
        Position move = hardAI.getBestMove(board, Piece.BLACK);

        assertNotNull(move);
        assertTrue(board.isValidMove(move.getRow(), move.getColumn(), Piece.BLACK));
    }

    @Test
    void testAIWithNoValidMoves() {
        OthelloBoard emptyBoard = new OthelloBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                emptyBoard.setPieceAt(row, col, Piece.WHITE);
            }
        }

        OthelloAI ai = new OthelloAI("Difficile");
        Position move = ai.getBestMove(emptyBoard, Piece.BLACK);

        assertNull(move);
    }

    @Test
    void testMediumAIChoosesBestFlip() {
        board.makeMove(2, 3, Piece.BLACK);
        board.makeMove(2, 2, Piece.WHITE);

        OthelloAI mediumAI = new OthelloAI("Moyen");
        Position move = mediumAI.getBestMove(board, Piece.BLACK);

        assertNotNull(move);
        int flips = board.getPiecesToFlip(move.getRow(), move.getColumn(), Piece.BLACK).size();
        assertTrue(flips > 0);
    }

    @Test
    void testHardAIConsidersMultipleMoves() {
        OthelloAI hardAI = new OthelloAI("Difficile");
        Position move1 = hardAI.getBestMove(board, Piece.BLACK);

        assertNotNull(move1);
        assertTrue(board.isValidMove(move1.getRow(), move1.getColumn(), Piece.BLACK));
    }

    @Test
    void testDifficultyParsing() {
        OthelloAI easy1 = new OthelloAI("facile");
        OthelloAI easy2 = new OthelloAI("FACILE");
        OthelloAI medium1 = new OthelloAI("moyen");
        OthelloAI medium2 = new OthelloAI("medium");
        OthelloAI hard1 = new OthelloAI("difficile");
        OthelloAI hard2 = new OthelloAI("hard");

        assertNotNull(easy1.getBestMove(board, Piece.BLACK));
        assertNotNull(easy2.getBestMove(board, Piece.BLACK));
        assertNotNull(medium1.getBestMove(board, Piece.BLACK));
        assertNotNull(medium2.getBestMove(board, Piece.BLACK));
        assertNotNull(hard1.getBestMove(board, Piece.BLACK));
        assertNotNull(hard2.getBestMove(board, Piece.BLACK));
    }

    @Test
    void testEasyAIRandomness() {
        OthelloAI easyAI = new OthelloAI("Facile");
        boolean foundDifferentMoves = false;
        Position firstMove = null;

        for (int i = 0; i < 20; i++) {
            OthelloBoard testBoard = new OthelloBoard();
            Position move = easyAI.getBestMove(testBoard, Piece.BLACK);

            if (firstMove == null) {
                firstMove = move;
            } else if (!firstMove.equals(move)) {
                foundDifferentMoves = true;
                break;
            }
        }

        assertTrue(foundDifferentMoves, "Easy AI should produce different moves");
    }

    @Test
    void testAIPlaysCompleteMoves() {
        OthelloAI ai = new OthelloAI("Moyen");

        for (int i = 0; i < 5; i++) {
            Position move = ai.getBestMove(board, i % 2 == 0 ? Piece.BLACK : Piece.WHITE);
            if (move != null) {
                assertTrue(board.makeMove(move.getRow(), move.getColumn(),
                          i % 2 == 0 ? Piece.BLACK : Piece.WHITE));
            }
        }

        assertTrue(board.countPieces(Piece.BLACK) + board.countPieces(Piece.WHITE) > 4);
    }

    @Test
    void testHardAIPerformance() {
        OthelloAI hardAI = new OthelloAI("Difficile");

        long startTime = System.currentTimeMillis();
        Position move = hardAI.getBestMove(board, Piece.BLACK);
        long endTime = System.currentTimeMillis();

        assertNotNull(move);
        assertTrue(endTime - startTime < 5000, "Hard AI should complete in less than 5 seconds");
    }
}
