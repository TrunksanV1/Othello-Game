package fr.univ_amu.m1info.board_game_library.othello;

import fr.univ_amu.m1info.board_game_library.graphics.*;
import fr.univ_amu.m1info.board_game_library.othello.ai.OthelloAI;
import fr.univ_amu.m1info.board_game_library.othello.model.*;
import fr.univ_amu.m1info.board_game_library.othello.persistence.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for the Othello game.
 * Manages game logic and interactions with the UI.
 */
public class OthelloController implements BoardGameController {

    private static final int BOARD_SIZE = 8;
    private static final int AI_MOVE_DELAY_MS = 500;

    private BoardGameView view;
    private final String gameMode;
    private final String player1Name;
    private final String player2Name;
    private final String difficulty;

    private OthelloGame game;
    private OthelloAI ai;
    private boolean isAIThinking = false;

    public OthelloController(String gameMode, String player1Name, String player2Name, String difficulty) {
        this.gameMode = gameMode;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.difficulty = difficulty;
        this.game = new OthelloGame();

        if (gameMode.equals("PVE")) {
            this.ai = new OthelloAI(difficulty);
        }
    }

    @Override
    public void initializeViewOnStart(BoardGameView view) {
        this.view = view;
        initializeBoard();
        updateScoreLabel();
        updateTurnLabel();
        showValidMoves();
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                view.setCellColor(row, col, Color.DARKGREEN);
                Piece piece = game.getPieceAt(row, col);
                updateCellPiece(row, col, piece);
            }
        }
    }

    private void updateCellPiece(int row, int col, Piece piece) {
        view.removeShapesAtCell(row, col);
        if (piece != Piece.EMPTY) {
            Color color = piece == Piece.BLACK ? Color.BLACK : Color.WHITE;
            view.addShapeAtCell(row, col, Shape.CIRCLE, color);
        }
    }

    private void showValidMoves() {
        List<Position> validMoves = game.getValidMoves();
        for (Position pos : validMoves) {
            view.setCellColor(pos.getRow(), pos.getColumn(), Color.LIGHTGREEN);
        }
    }

    private void hideValidMoves() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                view.setCellColor(row, col, Color.DARKGREEN);
            }
        }
    }

    @Override
    public void boardActionOnClick(int row, int column) {
        if (game.isGameOver()) {
            showAlert("Partie terminée", "La partie est déjà terminée. Utilisez le bouton 'Recommencer' pour démarrer une nouvelle partie.");
            return;
        }

        if (isAIThinking) {
            showAlert("IA en réflexion", "Veuillez patienter, l'IA est en train de réfléchir...");
            return;
        }

        if (gameMode.equals("PVE") && game.getCurrentPlayer() == Piece.WHITE) {
            showAlert("Pas votre tour", "C'est au tour de l'IA. Veuillez patienter...");
            return;
        }

        if (!game.isValidMove(row, column)) {
            int validMovesCount = game.getValidMoves().size();
            String message = validMovesCount > 0
                    ? String.format("Ce coup n'est pas valide. %d coup(s) valide(s) disponible(s) (cases vertes).", validMovesCount)
                    : "Aucun coup valide disponible. Votre tour sera passé automatiquement.";
            showAlert("Coup invalide", message);
            return;
        }

        makeMove(row, column);
    }

    private void makeMove(int row, int column) {
        hideValidMoves();

        if (game.makeMove(row, column)) {
            updateBoardAfterMove();
            updateScoreLabel();
            updateTurnLabel();

            if (game.isGameOver()) {
                handleGameOver();
            } else {
                showValidMoves();

                if (gameMode.equals("PVE") && game.getCurrentPlayer() == Piece.WHITE && !game.isGameOver()) {
                    scheduleAIMove();
                }
            }
        }
    }

    private void scheduleAIMove() {
        isAIThinking = true;
        view.updateLabeledElement("turn_label", "IA en réflexion... ⏳");

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(AI_MOVE_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).thenRun(() -> Platform.runLater(() -> {
            makeAIMove();
            isAIThinking = false;
        }));
    }

    private void makeAIMove() {
        Position aiMove = ai.getBestMove(game.getBoard(), Piece.WHITE);
        if (aiMove != null) {
            makeMove(aiMove.getRow(), aiMove.getColumn());
        } else if (hasNoValidMoves()) {
            showAlert("Tour passé", "L'IA n'a aucun coup valide. Son tour est passé.");
            game.pass();
            updateTurnLabel();
            if (game.isGameOver()) {
                handleGameOver();
            } else {
                showValidMoves();
            }
        }
    }

    private boolean hasNoValidMoves() {
        return game.getValidMoves().isEmpty();
    }

    private void updateBoardAfterMove() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = game.getPieceAt(row, col);
                updateCellPiece(row, col, piece);
            }
        }
    }

    private void handleGameOver() {
        hideValidMoves();

        Piece winner = game.getWinner();
        String winnerName;
        if (winner == Piece.EMPTY) {
            winnerName = "Égalité";
        } else if (winner == Piece.BLACK) {
            winnerName = player1Name;
        } else {
            winnerName = player2Name;
        }

        int blackScore = game.getBlackScore();
        int whiteScore = game.getWhiteScore();
        long duration = game.getGameDurationSeconds();

        saveGameToHistory(winnerName, blackScore, whiteScore, duration);

        showGameOverDialog(winnerName, blackScore, whiteScore);
    }

    private void saveGameToHistory(String winnerName, int blackScore, int whiteScore, long duration) {
        try {
            GameHistory history = new GameHistory(
                    winnerName,
                    player1Name,
                    player2Name,
                    blackScore,
                    whiteScore,
                    duration
            );
            HistoryManager.addGameToHistory(history);
            HistoryManager.copyHistoryToResources();
        } catch (IOException e) {
            System.err.println("Failed to save game to history: " + e.getMessage());
        }
    }

    private void showGameOverDialog(String winnerName, int blackScore, int whiteScore) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partie terminée");

            String headerText = winnerName.equals("Égalité")
                    ? "Match nul ! 🤝"
                    : String.format("Victoire de %s ! 🎉", winnerName);
            alert.setHeaderText(headerText);

            long duration = game.getGameDurationSeconds();
            int minutes = (int) (duration / 60);
            int seconds = (int) (duration % 60);
            String durationText = minutes > 0
                    ? String.format("%d min %d sec", minutes, seconds)
                    : String.format("%d sec", seconds);

            int totalPieces = blackScore + whiteScore;
            String content = String.format(
                    "Score final:\n" +
                            "  %s (Noir): %d pions\n" +
                            "  %s (Blanc): %d pions\n\n" +
                            "Durée: %s\n" +
                            "Cases occupées: %d/64",
                    player1Name, blackScore,
                    player2Name, whiteScore,
                    durationText,
                    totalPieces
            );
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    @Override
    public void buttonActionOnClick(String buttonId) {
        switch (buttonId) {
            case "restart_button" -> handleRestart();
            case "save_button" -> handleSave();
            case "menu_button" -> handleReturnToMenu();
            default -> System.out.println("Unknown button: " + buttonId);
        }
    }

    private void handleRestart() {
        if (isAIThinking) {
            showAlert("IA en réflexion", "Impossible de redémarrer pendant que l'IA réfléchit. Veuillez patienter...");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nouvelle partie");
        alert.setHeaderText("Que voulez-vous faire ?");
        alert.setContentText("Choisissez une option pour continuer :");

        ButtonType samConfigButton = new ButtonType("🔄 Rejouer (même configuration)");
        ButtonType changeConfigButton = new ButtonType("⚙ Changer la configuration");
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(samConfigButton, changeConfigButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == samConfigButton) {
                restartGame();
            } else if (result.get() == changeConfigButton) {
                returnToConfiguration();
            }
        }
    }

    private void restartGame() {
        game.reset();
        hideValidMoves();
        initializeBoard();
        updateScoreLabel();
        updateTurnLabel();
        showValidMoves();
    }

    private void returnToConfiguration() {
        Platform.runLater(() -> {
            try {
                // Obtenir le stage actuel
                JavaFXBoardGameApplicationLauncher launcher = JavaFXBoardGameApplicationLauncher.getInstance();
                Stage currentStage = launcher.getStage();

                // Créer et afficher la fenêtre de configuration
                OthelloConfigurationApp configApp = new OthelloConfigurationApp();
                Stage configStage = new Stage();
                configApp.start(configStage);

                // Fermer la fenêtre de jeu actuelle
                if (currentStage != null) {
                    currentStage.close();
                }
            } catch (Exception e) {
                showErrorAlert("Erreur",
                        "Impossible de retourner à la configuration",
                        "Une erreur s'est produite : " + e.getMessage());
            }
        });
    }

    private void handleReturnToMenu() {
        if (isAIThinking) {
            showAlert("IA en réflexion", "Impossible de retourner au menu pendant que l'IA réfléchit. Veuillez patienter...");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Retour au menu");
        alert.setHeaderText("Voulez-vous vraiment retourner au menu principal ?");

        String content = game.isGameOver()
                ? "La partie est terminée. Retourner au menu principal ?"
                : "La partie en cours sera perdue si vous n'avez pas sauvegardé.\n\nVoulez-vous continuer ?";
        alert.setContentText(content);

        ButtonType confirmButton = new ButtonType("Oui, retourner au menu");
        ButtonType cancelButton = new ButtonType("Non, rester", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            returnToMainMenu();
        }
    }

    private void returnToMainMenu() {
        Platform.runLater(() -> {
            try {
                JavaFXBoardGameApplicationLauncher launcher = JavaFXBoardGameApplicationLauncher.getInstance();
                Stage currentStage = launcher.getStage();

                fr.univ_amu.m1info.board_game_library.graphics.javafx.view.OthelloMainMenu mainMenu =
                        new fr.univ_amu.m1info.board_game_library.graphics.javafx.view.OthelloMainMenu();
                mainMenu.start(currentStage);

            } catch (Exception e) {
                showErrorAlert("Erreur",
                        "Impossible de retourner au menu principal",
                        "Une erreur s'est produite : " + e.getMessage());
            }
        });
    }

    private void handleSave() {
        if (game.isGameOver()) {
            showAlert("Partie terminée", "Impossible de sauvegarder une partie déjà terminée.\n\n" +
                    "La partie est automatiquement enregistrée dans l'historique.");
            return;
        }

        if (isAIThinking) {
            showAlert("IA en réflexion", "Impossible de sauvegarder pendant que l'IA réfléchit. Veuillez patienter...");
            return;
        }

        try {
            String gameStateStr = game.getGameState();
            String[] parts = gameStateStr.split("\\|");

            if (parts.length != 3) {
                throw new IllegalStateException("Format d'état de jeu invalide");
            }

            GameState state = new GameState(
                    gameMode,
                    player1Name,
                    player2Name,
                    difficulty,
                    parts[0],
                    parts[1],
                    Long.parseLong(parts[2])
            );

            boolean hadPreviousSave = GamePersistence.hasSavedGame();
            GamePersistence.saveGame(state);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sauvegarde");
            alert.setHeaderText("Partie sauvegardée avec succès ! ✓");

            String message = "Votre partie a été sauvegardée.\n\n" +
                    "Vous pouvez la reprendre plus tard depuis le menu principal " +
                    "en cliquant sur 'Reprendre partie'.";

            if (hadPreviousSave) {
                message += "\n\nNote: La sauvegarde précédente a été remplacée.";
            }

            alert.setContentText(message);
            alert.showAndWait();
        } catch (IOException e) {
            showErrorAlert("Erreur de sauvegarde",
                    "Impossible de sauvegarder la partie.",
                    "Erreur: " + e.getMessage() + "\n\n" +
                            "Vérifiez que vous avez les permissions d'écriture.");
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de données",
                    "Format de données invalide.",
                    "Les données de la partie sont corrompues.");
        } catch (IllegalStateException e) {
            showErrorAlert("Erreur d'état",
                    "État de jeu invalide.",
                    e.getMessage());
        }
    }

    private void updateTurnLabel() {
        String currentPlayer = game.getCurrentPlayer() == Piece.BLACK ? player1Name : player2Name;
        String color = game.getCurrentPlayer() == Piece.BLACK ? "Noir" : "Blanc";
        view.updateLabeledElement("turn_label", "Tour: " + currentPlayer + " (" + color + ")");
    }

    private void updateScoreLabel() {
        int blackScore = game.getBlackScore();
        int whiteScore = game.getWhiteScore();
        view.updateLabeledElement("score_label",
                String.format("Score - %s: %d | %s: %d", player1Name, blackScore, player2Name, whiteScore));
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    private void showErrorAlert(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public void loadGameState(GameState state) {
        try {
            if (state == null) {
                throw new IllegalArgumentException("L'état de jeu ne peut pas être null");
            }

            String boardState = state.getBoardState();
            String currentPlayer = state.getCurrentPlayer();
            long startTime = state.getStartTime();

            if (boardState == null || boardState.isEmpty()) {
                throw new IllegalArgumentException("État du plateau invalide");
            }

            if (currentPlayer == null || (!currentPlayer.equals("B") && !currentPlayer.equals("W"))) {
                throw new IllegalArgumentException("Joueur actuel invalide");
            }

            String fullState = boardState + "|" + currentPlayer + "|" + startTime;
            game.setGameState(fullState);

            initializeBoard();
            updateScoreLabel();
            updateTurnLabel();
            showValidMoves();

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Partie chargée");
                alert.setHeaderText("Reprise de la partie");
                alert.setContentText("Votre partie a été chargée avec succès !\n\n" +
                        "Bon jeu !");
                alert.showAndWait();
            });
        } catch (IllegalArgumentException e) {
            showErrorAlert("Erreur de chargement",
                    "Impossible de charger la partie.",
                    e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Erreur inattendue",
                    "Une erreur s'est produite lors du chargement.",
                    "Erreur: " + e.getMessage());
        }
    }
}
