package fr.univ_amu.m1info.board_game_library.graphics.javafx.view;

import fr.univ_amu.m1info.board_game_library.graphics.BoardGameApplicationLauncher;
import fr.univ_amu.m1info.board_game_library.graphics.JavaFXBoardGameApplicationLauncher;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.BoardGameConfiguration;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.BoardGameDimensions;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.LabeledElementConfiguration;
import fr.univ_amu.m1info.board_game_library.graphics.configuration.LabeledElementKind;
import fr.univ_amu.m1info.board_game_library.othello.OthelloConfigurationApp;
import fr.univ_amu.m1info.board_game_library.othello.OthelloController;
import fr.univ_amu.m1info.board_game_library.othello.persistence.GamePersistence;
import fr.univ_amu.m1info.board_game_library.othello.persistence.GameState;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OthelloMainMenu extends Application {

    @Override
    public void start(Stage stage) {
        Image backgroundImage = new Image(getClass().getResource("/images/WindowBackground.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setAlignment(Pos.CENTER);

        Text title = new Text("OTHELLO");
        title.setFont(Font.font("Georgia", 85));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 20, 0, 4, 4);");

        Text subtitle = new Text("Jeu de stratégie classique");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setFill(Color.rgb(220, 220, 220));

        Button newGame = new Button("🎮 Nouvelle partie");
        Button loadGame = new Button("💾 Charger une partie");
        Button history = new Button("📜 Historique des parties");
        Button quit = new Button("🚪 Quitter");

        for (Button b : new Button[]{newGame, loadGame, history, quit}) {
            b.setFont(Font.font("Arial", FontWeight.BOLD, 19));
            b.setPrefWidth(320);
            b.setPrefHeight(55);
            b.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255, 255, 255, 0.9), rgba(240, 240, 240, 0.9)); " +
                            "-fx-text-fill: black; " +
                            "-fx-background-radius: 12; " +
                            "-fx-border-radius: 12; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 2);"
            );
            b.setOnMouseEntered(e -> b.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255,255,255,1), rgba(255,255,255,0.95)); " +
                            "-fx-text-fill: #1a4d2e; " +
                            "-fx-background-radius: 12; " +
                            "-fx-border-radius: 12; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 12, 0, 0, 3); " +
                            "-fx-scale-x: 1.03; " +
                            "-fx-scale-y: 1.03;"
            ));
            b.setOnMouseExited(e -> b.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255, 255, 255, 0.9), rgba(240, 240, 240, 0.9)); " +
                            "-fx-text-fill: black; " +
                            "-fx-background-radius: 12; " +
                            "-fx-border-radius: 12; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 2);"
            ));
        }

        history.setOnAction(e -> {
            try {
                new HistoryMenu().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        newGame.setOnAction(e -> {
            try {
                new OthelloConfigurationApp().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loadGame.setOnAction(e -> {
            try {
                loadSavedGame(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        quit.setOnAction(e -> stage.close());

        VBox titleBox = new VBox(8, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        VBox menuBox = new VBox(22, titleBox, newGame, loadGame, history, quit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(20));

        overlay.getChildren().add(menuBox);

        StackPane root = new StackPane();
        root.setBackground(new Background(background));
        root.getChildren().add(overlay);

        Scene scene = new Scene(root, 950, 650);
        stage.setScene(scene);
        stage.setTitle("Othello - Menu Principal");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void loadSavedGame(Stage stage) {
        try {
            if (!GamePersistence.hasSavedGame()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Aucune sauvegarde");
                alert.setHeaderText("Pas de partie sauvegardée");
                alert.setContentText("Aucune partie sauvegardée n'a été trouvée.");
                alert.showAndWait();
                return;
            }

            GameState gameState = GamePersistence.loadGame();
            if (gameState == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de chargement");
                alert.setContentText("Impossible de charger la partie sauvegardée.");
                alert.showAndWait();
                return;
            }

            List<LabeledElementConfiguration> elements = new ArrayList<>();
            String playersText = gameState.getPlayer1Name() + " (Noir) vs " + gameState.getPlayer2Name() + " (Blanc)";
            elements.add(new LabeledElementConfiguration(playersText, "players_label", LabeledElementKind.TEXT));

            if (gameState.getGameMode().equals("PVE")) {
                elements.add(new LabeledElementConfiguration("Difficulté: " + gameState.getDifficulty(), "difficulty_label", LabeledElementKind.TEXT));
            }

            elements.add(new LabeledElementConfiguration("Tour: Chargement...", "turn_label", LabeledElementKind.TEXT));
            elements.add(new LabeledElementConfiguration("Score - Noir: 0 | Blanc: 0", "score_label", LabeledElementKind.TEXT));
            elements.add(new LabeledElementConfiguration("Nouvelle partie", "restart_button", LabeledElementKind.BUTTON));
            elements.add(new LabeledElementConfiguration("Sauvegarder", "save_button", LabeledElementKind.BUTTON));
            elements.add(new LabeledElementConfiguration("🏠 Menu", "menu_button", LabeledElementKind.BUTTON));

            BoardGameConfiguration configuration = new BoardGameConfiguration(
                    "OTHELLO",
                    new BoardGameDimensions(8, 8),
                    elements
            );

            OthelloController controller = new OthelloController(
                    gameState.getGameMode(),
                    gameState.getPlayer1Name(),
                    gameState.getPlayer2Name(),
                    gameState.getDifficulty()
            );

            BoardGameApplicationLauncher launcher = JavaFXBoardGameApplicationLauncher.getInstance();
            launcher.setStage(stage);
            launcher.launchApplication(configuration, controller);

            controller.loadGameState(gameState);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement");
            alert.setContentText("Une erreur s'est produite lors du chargement: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}