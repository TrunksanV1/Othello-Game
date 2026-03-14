package fr.univ_amu.m1info.board_game_library.othello;

import fr.univ_amu.m1info.board_game_library.graphics.*;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.BoardGameConfiguration;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.LabeledElementConfiguration;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.LabeledElementKind;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.BoardGameDimensions;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsable du lancement du jeu Othello avec les paramètres de configuration.
 */
public class OthelloLauncher {

    /**
     * Lance le jeu Othello avec les paramètres de configuration.
     *
     * @param stage le stage JavaFX actuel à réutiliser
     * @param gameMode "PVP" pour Humain vs Humain, "PVE" pour Humain vs IA
     * @param player1Name nom du joueur 1 (Noir)
     * @param player2Name nom du joueur 2 (Blanc) ou "IA"
     * @param difficulty difficulté de l'IA ("Facile", "Moyen", "Difficile")
     */
    public static void launchGame(Stage stage, String gameMode, String player1Name, String player2Name, String difficulty) {
        // Créer la liste des éléments de l'interface
        List<LabeledElementConfiguration> elements = new ArrayList<>();

        // Label pour le tour actuel (en premier, le plus important)
        elements.add(new LabeledElementConfiguration("Tour: " + player1Name + " (Noir)", "turn_label", LabeledElementKind.TEXT));

        // Label pour le score (deuxième position)
        elements.add(new LabeledElementConfiguration("Score - " + player1Name + ": 2 | " + player2Name + ": 2", "score_label", LabeledElementKind.TEXT));

        // Si mode IA, afficher la difficulté compacte
        if (gameMode.equals("PVE")) {
            elements.add(new LabeledElementConfiguration("IA: " + difficulty, "difficulty_label", LabeledElementKind.TEXT));
        }

        // Boutons d'action
        elements.add(new LabeledElementConfiguration("🔄 Recommencer", "restart_button", LabeledElementKind.BUTTON));
        elements.add(new LabeledElementConfiguration("💾 Sauvegarder", "save_button", LabeledElementKind.BUTTON));
        elements.add(new LabeledElementConfiguration("🏠 Menu", "menu_button", LabeledElementKind.BUTTON));

        // Créer la configuration du jeu
        BoardGameConfiguration configuration = new BoardGameConfiguration(
                "OTHELLO",
                new BoardGameDimensions(8, 8),
                elements
        );

        // Créer le contrôleur
        BoardGameController controller = new OthelloController(gameMode, player1Name, player2Name, difficulty);

        // Lancer l'application en réutilisant le stage existant
        BoardGameApplicationLauncher launcher = JavaFXBoardGameApplicationLauncher.getInstance();
        launcher.setStage(stage);
        launcher.launchApplication(configuration, controller);
    }
}