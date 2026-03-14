package fr.univ_amu.m1info.board_game_library.othello.player;

import fr.univ_amu.m1info.board_game_library.othello.model.OthelloGame;
import fr.univ_amu.m1info.board_game_library.othello.model.Position;

/**
 * Interface représentant un joueur abstrait dans le jeu Othello.
 *
 * Pattern Strategy : Permet de plugger différents types de joueurs
 * (humain, IA facile, IA difficile, etc.) sans modifier le modèle du jeu.
 *
 * Avantages :
 * - Séparation entre la logique métier (jeu) et la technique (IA)
 * - Facilite l'ajout de nouveaux types de joueurs
 * - Le modèle ne connaît que l'abstraction "Player"
 */
public interface Player {

    /**
     * Demande au joueur de choisir son prochain coup.
     *
     * Pour un joueur humain : retourne null (attend l'input de l'UI)
     * Pour une IA : calcule et retourne le meilleur coup
     *
     * @param game L'état actuel du jeu
     * @return La position choisie, ou null si le joueur doit passer ou attend l'input
     */
    Position chooseMove(OthelloGame game);

    /**
     * Retourne le nom du joueur.
     *
     * @return Le nom du joueur
     */
    String getName();

    /**
     * Indique si ce joueur est contrôlé par l'ordinateur.
     *
     * @return true si c'est une IA, false si c'est un humain
     */
    boolean isAI();
}
