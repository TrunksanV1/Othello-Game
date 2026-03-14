package fr.univ_amu.m1info.board_game_library.othello;

/**
 * Point d'entrée principal de l'application Othello.
 * Lance l'interface de configuration JavaFX.
 */
public class OthelloMain {

    public static void main(String[] args) {
        // Lancer l'application de configuration
        javafx.application.Application.launch(OthelloConfigurationApp.class, args);
    }
}