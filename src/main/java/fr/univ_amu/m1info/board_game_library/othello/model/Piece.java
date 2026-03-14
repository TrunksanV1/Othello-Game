package fr.univ_amu.m1info.board_game_library.othello.model;

/**
 * Represents a piece in the Othello game.
 */
public enum Piece {
    BLACK,
    WHITE,
    EMPTY;

    public Piece opposite() {
        if (this == BLACK) return WHITE;
        if (this == WHITE) return BLACK;
        return EMPTY;
    }
}
