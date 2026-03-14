package fr.univ_amu.m1info.board_game_library.othello.player;

import fr.univ_amu.m1info.board_game_library.othello.model.OthelloGame;
import fr.univ_amu.m1info.board_game_library.othello.model.Position;

/**
 * Représente un joueur humain.
 *
 * Un joueur humain ne choisit pas son coup automatiquement,
 * il attend que l'utilisateur clique sur le plateau via l'interface graphique.
 */
public class HumanPlayer implements Player {

    private final String name;

    /**
     * Crée un joueur humain.
     *
     * @param name Le nom du joueur
     */
    public HumanPlayer(String name) {
        this.name = name;
    }

    @Override
    public Position chooseMove(OthelloGame game) {
        // Un joueur humain ne choisit pas automatiquement
        // Le coup sera fait via l'UI (boardActionOnClick)
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAI() {
        return false;
    }

    @Override
    public String toString() {
        return "HumanPlayer{name='" + name + "'}";
    }
}
