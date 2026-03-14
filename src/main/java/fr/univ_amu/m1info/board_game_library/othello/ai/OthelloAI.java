package fr.univ_amu.m1info.board_game_library.othello.ai;

import fr.univ_amu.m1info.board_game_library.othello.model.OthelloBoard;
import fr.univ_amu.m1info.board_game_library.othello.model.Piece;
import fr.univ_amu.m1info.board_game_library.othello.model.Position;

import java.util.List;
import java.util.Random;

/**
 * AI player for Othello with multiple difficulty levels.
 */
public class OthelloAI {
    private final Difficulty difficulty;
    private final Random random;

    private static final int[][] POSITION_WEIGHTS = {
        {100, -20,  10,   5,   5,  10, -20, 100},
        {-20, -50,  -2,  -2,  -2,  -2, -50, -20},
        { 10,  -2,   1,   1,   1,   1,  -2,  10},
        {  5,  -2,   1,   0,   0,   1,  -2,   5},
        {  5,  -2,   1,   0,   0,   1,  -2,   5},
        { 10,  -2,   1,   1,   1,   1,  -2,  10},
        {-20, -50,  -2,  -2,  -2,  -2, -50, -20},
        {100, -20,  10,   5,   5,  10, -20, 100}
    };

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public OthelloAI(String difficultyStr) {
        this.difficulty = parseDifficulty(difficultyStr);
        this.random = new Random();
    }

    private Difficulty parseDifficulty(String difficultyStr) {
        return switch (difficultyStr.toLowerCase()) {
            case "moyen", "medium" -> Difficulty.MEDIUM;
            case "difficile", "hard", "expert" -> Difficulty.HARD;
            default -> Difficulty.EASY;
        };
    }

    public Position getBestMove(OthelloBoard board, Piece aiPlayer) {
        List<Position> validMoves = board.getValidMoves(aiPlayer);

        if (validMoves.isEmpty()) {
            return null;
        }

        return switch (difficulty) {
            case EASY -> getRandomMove(validMoves);
            case MEDIUM -> getGreedyMove(board, aiPlayer, validMoves);
            case HARD -> getMinimaxMove(board, aiPlayer, validMoves);
        };
    }

    private Position getRandomMove(List<Position> validMoves) {
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    private Position getGreedyMove(OthelloBoard board, Piece aiPlayer, List<Position> validMoves) {
        Position bestMove = validMoves.get(0);
        int maxFlips = 0;

        for (Position move : validMoves) {
            int flips = board.getPiecesToFlip(move.getRow(), move.getColumn(), aiPlayer).size();
            if (flips > maxFlips) {
                maxFlips = flips;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private Position getMinimaxMove(OthelloBoard board, Piece aiPlayer, List<Position> validMoves) {
        Position bestMove = validMoves.get(0);
        int bestScore = Integer.MIN_VALUE;

        for (Position move : validMoves) {
            OthelloBoard tempBoard = cloneBoard(board);
            tempBoard.makeMove(move.getRow(), move.getColumn(), aiPlayer);

            int score = minimax(tempBoard, 3, false, aiPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(OthelloBoard board, int depth, boolean isMaximizing, Piece aiPlayer, int alpha, int beta) {
        if (depth == 0 || isTerminal(board)) {
            return evaluateBoard(board, aiPlayer);
        }

        Piece currentPlayer = isMaximizing ? aiPlayer : aiPlayer.opposite();
        List<Position> moves = board.getValidMoves(currentPlayer);

        if (moves.isEmpty()) {
            return minimax(board, depth - 1, !isMaximizing, aiPlayer, alpha, beta);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Position move : moves) {
                OthelloBoard tempBoard = cloneBoard(board);
                tempBoard.makeMove(move.getRow(), move.getColumn(), currentPlayer);
                int eval = minimax(tempBoard, depth - 1, false, aiPlayer, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Position move : moves) {
                OthelloBoard tempBoard = cloneBoard(board);
                tempBoard.makeMove(move.getRow(), move.getColumn(), currentPlayer);
                int eval = minimax(tempBoard, depth - 1, true, aiPlayer, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private int evaluateBoard(OthelloBoard board, Piece aiPlayer) {
        int score = 0;

        int aiPieces = board.countPieces(aiPlayer);
        int opponentPieces = board.countPieces(aiPlayer.opposite());
        score += (aiPieces - opponentPieces) * 10;

        for (int row = 0; row < OthelloBoard.getBoardSize(); row++) {
            for (int col = 0; col < OthelloBoard.getBoardSize(); col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece == aiPlayer) {
                    score += POSITION_WEIGHTS[row][col];
                } else if (piece == aiPlayer.opposite()) {
                    score -= POSITION_WEIGHTS[row][col];
                }
            }
        }

        int aiMobility = board.getValidMoves(aiPlayer).size();
        int opponentMobility = board.getValidMoves(aiPlayer.opposite()).size();
        score += (aiMobility - opponentMobility) * 5;

        return score;
    }

    private boolean isTerminal(OthelloBoard board) {
        return !board.hasValidMoves(Piece.BLACK) && !board.hasValidMoves(Piece.WHITE);
    }

    private OthelloBoard cloneBoard(OthelloBoard original) {
        OthelloBoard clone = new OthelloBoard();
        clone.setBoardState(original.getBoardState());
        return clone;
    }
}
