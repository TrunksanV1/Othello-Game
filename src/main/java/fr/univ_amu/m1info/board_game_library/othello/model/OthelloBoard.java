package fr.univ_amu.m1info.board_game_library.othello.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Othello game board and handles game logic.
 */
public class OthelloBoard {
    private static final int BOARD_SIZE = 8;
    private final Piece[][] board;

    public OthelloBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = Piece.EMPTY;
            }
        }

        board[3][3] = Piece.WHITE;
        board[3][4] = Piece.BLACK;
        board[4][3] = Piece.BLACK;
        board[4][4] = Piece.WHITE;
    }

    public Piece getPieceAt(int row, int column) {
        if (!isValidPosition(row, column)) {
            return Piece.EMPTY;
        }
        return board[row][column];
    }

    public void setPieceAt(int row, int column, Piece piece) {
        if (isValidPosition(row, column)) {
            board[row][column] = piece;
        }
    }

    public boolean isValidPosition(int row, int column) {
        return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
    }

    public boolean isValidMove(int row, int column, Piece player) {
        if (!isValidPosition(row, column) || board[row][column] != Piece.EMPTY) {
            return false;
        }

        return !getPiecesToFlip(row, column, player).isEmpty();
    }

    public List<Position> getPiecesToFlip(int row, int column, Piece player) {
        List<Position> piecesToFlip = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            List<Position> piecesInDirection = getPiecesToFlipInDirection(row, column, player, direction);
            piecesToFlip.addAll(piecesInDirection);
        }

        return piecesToFlip;
    }

    private List<Position> getPiecesToFlipInDirection(int row, int column, Piece player, Direction direction) {
        List<Position> piecesToFlip = new ArrayList<>();
        int currentRow = row + direction.getRowDelta();
        int currentCol = column + direction.getColumnDelta();

        while (isValidPosition(currentRow, currentCol)) {
            Piece currentPiece = board[currentRow][currentCol];

            if (currentPiece == Piece.EMPTY) {
                return new ArrayList<>();
            }

            if (currentPiece == player.opposite()) {
                piecesToFlip.add(new Position(currentRow, currentCol));
                currentRow += direction.getRowDelta();
                currentCol += direction.getColumnDelta();
            } else if (currentPiece == player) {
                return piecesToFlip;
            }
        }

        return new ArrayList<>();
    }

    public boolean makeMove(int row, int column, Piece player) {
        if (!isValidMove(row, column, player)) {
            return false;
        }

        board[row][column] = player;
        List<Position> piecesToFlip = getPiecesToFlip(row, column, player);

        for (Position pos : piecesToFlip) {
            board[pos.getRow()][pos.getColumn()] = player;
        }

        return true;
    }

    public List<Position> getValidMoves(Piece player) {
        List<Position> validMoves = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isValidMove(row, col, player)) {
                    validMoves.add(new Position(row, col));
                }
            }
        }

        return validMoves;
    }

    public boolean hasValidMoves(Piece player) {
        return !getValidMoves(player).isEmpty();
    }

    public int countPieces(Piece piece) {
        int count = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == piece) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isBoardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == Piece.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        initializeBoard();
    }

    public String getBoardState() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                char c = switch (board[row][col]) {
                    case BLACK -> 'B';
                    case WHITE -> 'W';
                    case EMPTY -> '_';
                };
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void setBoardState(String state) {
        if (state == null) {
            throw new IllegalArgumentException("L'état du plateau ne peut pas être null");
        }

        if (state.length() != BOARD_SIZE * BOARD_SIZE) {
            throw new IllegalArgumentException(
                String.format("L'état du plateau doit contenir exactement %d caractères (reçu: %d)",
                    BOARD_SIZE * BOARD_SIZE, state.length())
            );
        }

        int index = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                char c = state.charAt(index++);
                board[row][col] = switch (c) {
                    case 'B', 'b' -> Piece.BLACK;
                    case 'W', 'w' -> Piece.WHITE;
                    case '_', '-', '.', 'E', 'e' -> Piece.EMPTY;
                    default -> throw new IllegalArgumentException(
                        String.format("Caractère invalide '%c' à la position %d", c, index - 1)
                    );
                };
            }
        }
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }
}
