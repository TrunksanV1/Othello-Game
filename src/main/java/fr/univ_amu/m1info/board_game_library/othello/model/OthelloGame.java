package fr.univ_amu.m1info.board_game_library.othello.model;

import java.util.List;

/**
 * Manages the overall state and flow of the Othello game.
 */
public class OthelloGame {
    private final OthelloBoard board;
    private Piece currentPlayer;
    private boolean gameOver;
    private int consecutivePasses;
    private long startTime;

    public OthelloGame() {
        this.board = new OthelloBoard();
        this.currentPlayer = Piece.BLACK;
        this.gameOver = false;
        this.consecutivePasses = 0;
        this.startTime = System.currentTimeMillis();
    }

    public boolean makeMove(int row, int column) {
        if (gameOver) {
            return false;
        }

        boolean success = board.makeMove(row, column, currentPlayer);

        if (success) {
            consecutivePasses = 0;
            advanceToNextPlayer();
            checkGameOver();
        }

        return success;
    }

    public void pass() {
        if (gameOver) {
            return;
        }

        consecutivePasses++;
        advanceToNextPlayer();
        checkGameOver();
    }

    private void advanceToNextPlayer() {
        Piece nextPlayer = currentPlayer.opposite();

        if (board.hasValidMoves(nextPlayer)) {
            currentPlayer = nextPlayer;
        } else if (board.hasValidMoves(currentPlayer)) {
            consecutivePasses++;
        } else {
            gameOver = true;
        }
    }

    private void checkGameOver() {
        if (consecutivePasses >= 2 || board.isBoardFull() ||
            (!board.hasValidMoves(Piece.BLACK) && !board.hasValidMoves(Piece.WHITE))) {
            gameOver = true;
        }
    }

    public boolean isValidMove(int row, int column) {
        return !gameOver && board.isValidMove(row, column, currentPlayer);
    }

    public List<Position> getValidMoves() {
        return board.getValidMoves(currentPlayer);
    }

    public List<Position> getPiecesToFlip(int row, int column) {
        return board.getPiecesToFlip(row, column, currentPlayer);
    }

    public Piece getCurrentPlayer() {
        return currentPlayer;
    }

    public Piece getPieceAt(int row, int column) {
        return board.getPieceAt(row, column);
    }

    public int getBlackScore() {
        return board.countPieces(Piece.BLACK);
    }

    public int getWhiteScore() {
        return board.countPieces(Piece.WHITE);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Piece getWinner() {
        if (!gameOver) {
            return null;
        }

        int blackScore = getBlackScore();
        int whiteScore = getWhiteScore();

        if (blackScore > whiteScore) {
            return Piece.BLACK;
        } else if (whiteScore > blackScore) {
            return Piece.WHITE;
        } else {
            return Piece.EMPTY;
        }
    }

    public void reset() {
        board.reset();
        currentPlayer = Piece.BLACK;
        gameOver = false;
        consecutivePasses = 0;
        startTime = System.currentTimeMillis();
    }

    public OthelloBoard getBoard() {
        return board;
    }

    public long getGameDurationSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public String getGameState() {
        return board.getBoardState() + "|" + (currentPlayer == Piece.BLACK ? "B" : "W") + "|" + startTime;
    }

    public void setGameState(String state) {
        if (state == null || state.isEmpty()) {
            throw new IllegalArgumentException("État de jeu invalide: chaîne vide ou null");
        }

        String[] parts = state.split("\\|");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Format d'état de jeu invalide: parties insuffisantes");
        }

        if (parts[0].length() != 64) {
            throw new IllegalArgumentException("État du plateau invalide: longueur incorrecte");
        }

        if (!parts[1].equals("B") && !parts[1].equals("W")) {
            throw new IllegalArgumentException("Joueur actuel invalide: " + parts[1]);
        }

        board.setBoardState(parts[0]);
        currentPlayer = parts[1].equals("B") ? Piece.BLACK : Piece.WHITE;

        if (parts.length >= 3) {
            try {
                startTime = Long.parseLong(parts[2]);
                if (startTime < 0 || startTime > System.currentTimeMillis()) {
                    startTime = System.currentTimeMillis();
                }
            } catch (NumberFormatException e) {
                startTime = System.currentTimeMillis();
            }
        } else {
            startTime = System.currentTimeMillis();
        }

        consecutivePasses = 0;
        gameOver = false;
        checkGameOver();
    }
}
