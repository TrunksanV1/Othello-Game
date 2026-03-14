package fr.univ_amu.m1info.board_game_library.othello;

import fr.univ_amu.m1info.board_game_library.graphics.javafx.view.OthelloMainMenu;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Application JavaFX pour la configuration du jeu Othello.
 * Permet de choisir le mode de jeu, les noms des joueurs et la difficulté de l'IA.
 */
public class OthelloConfigurationApp extends Application {

    private ToggleGroup gameModeGroup;
    private TextField player1Field;
    private TextField player2Field;
    private ComboBox<String> difficultyComboBox;
    private VBox player2Box;
    private VBox difficultyBox;

    // Variables pour stocker la configuration choisie
    private static String gameMode = "PVP"; // "PVP" ou "PVE"
    private static String player1Name = "";
    private static String player2Name = "";
    private static String difficulty = "Facile";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Othello - Configuration");

        // Container principal avec disposition en grille
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1a2a3a, #2d3e50);"
        );

        // Titre
        Label titleLabel = new Label("OTHELLO");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 2, 2);"
        );

        Label subtitleLabel = new Label("Configuration de la partie");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setTextFill(Color.rgb(200, 200, 200));

        VBox titleBox = new VBox(8, titleLabel, subtitleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 15, 0));

        // Section Mode de jeu
        VBox gameModeBox = createGameModeSection();

        // Section Joueurs
        VBox playersBox = createPlayersSection();

        // Section Difficulté IA
        difficultyBox = createDifficultySection();
        difficultyBox.setVisible(false);
        difficultyBox.setManaged(false);

        // Boutons en ligne (Retour et Démarrer côte à côte)
        Button backButton = new Button("← Retour");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setPrefWidth(180);
        backButton.setPrefHeight(45);
        backButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #95a5a6, #7f8c8d); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0, 0, 2);"
        );
        backButton.setOnMouseEntered(e ->
                backButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #a5b5b6, #8f9c9d); " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 8; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 6, 0, 0, 3);"
                )
        );
        backButton.setOnMouseExited(e ->
                backButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #95a5a6, #7f8c8d); " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 8; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0, 0, 2);"
                )
        );
        backButton.setOnAction(e -> {
            try {
                Stage menuStage = new Stage();
                new OthelloMainMenu().start(menuStage);
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // Bouton Démarrer
        Button startButton = new Button("Démarrer la partie");
        startButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        startButton.setPrefWidth(320);
        startButton.setPrefHeight(45);
        startButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #27ae60, #229954); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0, 0, 2);"
        );
        startButton.setOnMouseEntered(e ->
                startButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #2ecc71, #27ae60); " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 8; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 6, 0, 0, 3);"
                )
        );
        startButton.setOnMouseExited(e ->
                startButton.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #27ae60, #229954); " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 8; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0, 0, 2);"
                )
        );
        startButton.setOnAction(e -> handleStartButton(primaryStage));

        // Conteneur pour les boutons côte à côte
        HBox buttonsBox = new HBox(15, backButton, startButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10, 0, 0, 0));

        // Ajout de tous les éléments
        mainContainer.getChildren().addAll(
                titleBox,
                gameModeBox,
                playersBox,
                difficultyBox,
                buttonsBox
        );

        // ScrollPane pour permettre le scroll si nécessaire
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Scene scene = new Scene(scrollPane, 650, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private VBox createGameModeSection() {
        Label sectionLabel = new Label("🎯 Mode de jeu");
        sectionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        sectionLabel.setTextFill(Color.web("#4ECCA3"));
        sectionLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(78,204,163,0.4), 8, 0, 0, 0);");

        gameModeGroup = new ToggleGroup();

        RadioButton pvpButton = new RadioButton();
        pvpButton.setToggleGroup(gameModeGroup);
        pvpButton.setSelected(true);
        pvpButton.setUserData("PVP");

        Label pvpLabel = new Label("🎮 Humain vs Humain");
        pvpLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        pvpLabel.setTextFill(Color.WHITE);

        Label pvpDesc = new Label("Jouer contre un autre joueur sur le même appareil");
        pvpDesc.setFont(Font.font("Arial", 13));
        pvpDesc.setTextFill(Color.rgb(200, 200, 200));
        pvpDesc.setWrapText(true);

        VBox pvpBox = new VBox(5, pvpLabel, pvpDesc);
        HBox pvpContainer = new HBox(15, pvpButton, pvpBox);
        pvpContainer.setAlignment(Pos.CENTER_LEFT);
        pvpContainer.setPadding(new Insets(15));
        pvpContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.08); " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: rgba(70, 200, 120, 0.5); " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        RadioButton pveButton = new RadioButton();
        pveButton.setToggleGroup(gameModeGroup);
        pveButton.setUserData("PVE");

        Label pveLabel = new Label("🤖 Humain vs IA");
        pveLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        pveLabel.setTextFill(Color.WHITE);

        Label pveDesc = new Label("Affronter l'ordinateur avec différents niveaux de difficulté");
        pveDesc.setFont(Font.font("Arial", 13));
        pveDesc.setTextFill(Color.rgb(200, 200, 200));
        pveDesc.setWrapText(true);

        VBox pveBox = new VBox(5, pveLabel, pveDesc);
        HBox pveContainer = new HBox(15, pveButton, pveBox);
        pveContainer.setAlignment(Pos.CENTER_LEFT);
        pveContainer.setPadding(new Insets(15));
        pveContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.08); " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: rgba(100, 150, 255, 0.5); " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        gameModeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateSectionsVisibility(newVal.getUserData().toString());
            }
        });

        VBox buttonBox = new VBox(15, pvpContainer, pveContainer);
        buttonBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(18, sectionLabel, buttonBox);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.05); " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: rgba(78, 204, 163, 0.3); " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 5);"
        );
        box.setPrefWidth(550);
        box.setMaxWidth(550);

        return box;
    }

    private VBox createPlayersSection() {
        Label sectionLabel = new Label("👥 Joueurs");
        sectionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        sectionLabel.setTextFill(Color.web("#4ECCA3"));
        sectionLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(78,204,163,0.4), 8, 0, 0, 0);");

        // Joueur 1
        Label player1Label = new Label("⚫ Joueur 1 (Pions Noirs)");
        player1Label.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        player1Label.setTextFill(Color.WHITE);

        Label player1Hint = new Label("Commence toujours la partie");
        player1Hint.setFont(Font.font("Arial", 12));
        player1Hint.setTextFill(Color.rgb(180, 180, 180));

        player1Field = new TextField();
        player1Field.setPromptText("Entrez le nom du joueur 1");
        player1Field.setFont(Font.font("Arial", 15));
        player1Field.setPrefHeight(45);
        player1Field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 12; " +
                        "-fx-border-color: #4ECCA3; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(78,204,163,0.2), 5, 0, 0, 2);"
        );

        VBox player1Box = new VBox(8, player1Label, player1Hint, player1Field);

        // Joueur 2
        Label player2Label = new Label("⚪ Joueur 2 (Pions Blancs)");
        player2Label.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        player2Label.setTextFill(Color.WHITE);

        Label player2Hint = new Label("Joue en second");
        player2Hint.setFont(Font.font("Arial", 12));
        player2Hint.setTextFill(Color.rgb(180, 180, 180));

        player2Field = new TextField();
        player2Field.setPromptText("Entrez le nom du joueur 2");
        player2Field.setFont(Font.font("Arial", 15));
        player2Field.setPrefHeight(45);
        player2Field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 12; " +
                        "-fx-border-color: #36D1DC; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(54,209,220,0.2), 5, 0, 0, 2);"
        );

        player2Box = new VBox(8, player2Label, player2Hint, player2Field);

        VBox box = new VBox(22, sectionLabel, player1Box, player2Box);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(25));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.05); " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: rgba(78, 204, 163, 0.3); " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 5);"
        );
        box.setPrefWidth(550);
        box.setMaxWidth(550);

        return box;
    }

    private VBox createDifficultySection() {
        Label sectionLabel = new Label("🎮 Difficulté de l'IA");
        sectionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        sectionLabel.setTextFill(Color.web("#4ECCA3"));
        sectionLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(78,204,163,0.4), 8, 0, 0, 0);");

        Label difficultyHint = new Label("Choisissez le niveau de défi que vous souhaitez affronter");
        difficultyHint.setFont(Font.font("Arial", 13));
        difficultyHint.setTextFill(Color.rgb(190, 190, 190));
        difficultyHint.setWrapText(true);
        difficultyHint.setMaxWidth(500);
        difficultyHint.setAlignment(Pos.CENTER);
        difficultyHint.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll(
                "⭐ Facile - Débutant (coups aléatoires)",
                "⭐⭐ Moyen - Intermédiaire (stratégie basique)",
                "⭐⭐⭐ Difficile - Expert (IA avancée)"
        );
        difficultyComboBox.setValue("⭐ Facile - Débutant (coups aléatoires)");
        difficultyComboBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #FC466B; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(252,70,107,0.2), 5, 0, 0, 2);"
        );
        difficultyComboBox.setPrefWidth(480);
        difficultyComboBox.setPrefHeight(50);

        VBox box = new VBox(15, sectionLabel, difficultyHint, difficultyComboBox);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.05); " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: rgba(78, 204, 163, 0.3); " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 5);"
        );
        box.setPrefWidth(550);
        box.setMaxWidth(550);

        return box;
    }

    private void updateSectionsVisibility(String mode) {
        if (mode.equals("PVE")) {
            player2Box.setVisible(false);
            player2Box.setManaged(false);
            difficultyBox.setVisible(true);
            difficultyBox.setManaged(true);
        } else {
            player2Box.setVisible(true);
            player2Box.setManaged(true);
            difficultyBox.setVisible(false);
            difficultyBox.setManaged(false);
        }
    }

    private void handleStartButton(Stage stage) {
        Toggle selectedToggle = gameModeGroup.getSelectedToggle();
        if (selectedToggle != null) {
            gameMode = selectedToggle.getUserData().toString();
        }

        player1Name = player1Field.getText().trim();

        if (player1Name.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer le nom du Joueur 1");
            return;
        }

        if (gameMode.equals("PVP")) {
            player2Name = player2Field.getText().trim();
            if (player2Name.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer le nom du Joueur 2");
                return;
            }
        } else {
            player2Name = "IA";
            String selectedDifficulty = difficultyComboBox.getValue();
            if (selectedDifficulty.contains("Facile")) {
                difficulty = "Facile";
            } else if (selectedDifficulty.contains("Moyen")) {
                difficulty = "Moyen";
            } else {
                difficulty = "Difficile";
            }
        }

        OthelloLauncher.launchGame(stage, gameMode, player1Name, player2Name, difficulty);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String getGameMode() { return gameMode; }
    public static String getPlayer1Name() { return player1Name; }
    public static String getPlayer2Name() { return player2Name; }
    public static String getDifficulty() { return difficulty; }

    public static void main(String[] args) {
        launch(args);
    }
}