package fr.univ_amu.m1info.board_game_library.othello.player;

import fr.univ_amu.m1info.board_game_library.othello.model.Piece;

/**
 * Factory pour créer des joueurs.
 *
 * Pattern Factory : Centralise la création des différents types de joueurs
 * et simplifie la logique de création dans le contrôleur.
 */
public class PlayerFactory {

    /**
     * Crée un joueur humain.
     *
     * @param name Le nom du joueur
     * @return Un nouveau joueur humain
     */
    public static Player createHumanPlayer(String name) {
        return new HumanPlayer(name);
    }

    /**
     * Crée un joueur IA.
     *
     * @param name Le nom de l'IA
     * @param difficulty Le niveau de difficulté ("Facile", "Moyen", "Difficile")
     * @param aiPiece La couleur des pions de l'IA
     * @return Un nouveau joueur IA
     */
    public static Player createAIPlayer(String name, String difficulty, Piece aiPiece) {
        return new AIPlayer(name, difficulty, aiPiece);
    }

    /**
     * Crée un joueur en fonction du mode de jeu.
     *
     * @param gameMode Le mode de jeu ("PVP" ou "PVE")
     * @param playerName Le nom du joueur
     * @param difficulty La difficulté de l'IA (utilisé uniquement en mode PVE)
     * @param isPlayer1 true si c'est le joueur 1, false si c'est le joueur 2
     * @return Le joueur créé
     */
    public static Player createPlayer(String gameMode, String playerName, String difficulty, boolean isPlayer1) {
        if ("PVP".equals(gameMode)) {
            return createHumanPlayer(playerName);
        } else if ("PVE".equals(gameMode)) {
            if (isPlayer1) {
                // Joueur 1 est toujours humain en mode PVE
                return createHumanPlayer(playerName);
            } else {
                // Joueur 2 est l'IA en mode PVE
                return createAIPlayer("IA", difficulty, Piece.WHITE);
            }
        }
        throw new IllegalArgumentException("Mode de jeu inconnu: " + gameMode);
    }
}
