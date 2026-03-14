package fr.univ_amu.m1info.board_game_library.othello.player;

import fr.univ_amu.m1info.board_game_library.othello.ai.OthelloAI;
import fr.univ_amu.m1info.board_game_library.othello.model.OthelloGame;
import fr.univ_amu.m1info.board_game_library.othello.model.Piece;
import fr.univ_amu.m1info.board_game_library.othello.model.Position;

/**
 * Représente un joueur contrôlé par l'ordinateur (IA).
 *
 * Cette classe encapsule l'algorithme d'IA (OthelloAI) et implémente
 * l'interface Player pour permettre au modèle de jeu de traiter
 * uniformément les joueurs humains et IA.
 */
public class AIPlayer implements Player {

    private final String name;
    private final OthelloAI ai;
    private final Piece aiPiece;

    /**
     * Crée un joueur IA.
     *
     * @param name Le nom de l'IA (ex: "IA", "Ordinateur")
     * @param difficulty Le niveau de difficulté ("Facile", "Moyen", "Difficile")
     * @param aiPiece La couleur des pions de l'IA (BLACK ou WHITE)
     */
    public AIPlayer(String name, String difficulty, Piece aiPiece) {
        this.name = name;
        this.ai = new OthelloAI(difficulty);
        this.aiPiece = aiPiece;
    }

    @Override
    public Position chooseMove(OthelloGame game) {
        // L'IA calcule son meilleur coup
        return ai.getBestMove(game.getBoard(), aiPiece);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAI() {
        return true;
    }

    /**
     * Retourne l'algorithme d'IA utilisé.
     *
     * @return L'instance d'OthelloAI
     */
    public OthelloAI getAI() {
        return ai;
    }

    /**
     * Retourne la couleur des pions de l'IA.
     *
     * @return La pièce de l'IA (BLACK ou WHITE)
     */
    public Piece getAIPiece() {
        return aiPiece;
    }

    @Override
    public String toString() {
        return "AIPlayer{name='" + name + "', piece=" + aiPiece + "}";
    }
}
