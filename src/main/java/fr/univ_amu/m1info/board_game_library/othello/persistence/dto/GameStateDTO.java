package fr.univ_amu.m1info.board_game_library.othello.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.univ_amu.m1info.board_game_library.othello.model.OthelloGame;
import fr.univ_amu.m1info.board_game_library.othello.model.Piece;

import java.time.LocalDateTime;

/**
 * Data Transfer Object pour l'état d'une partie en cours.
 *
 * Pattern DTO (Data Transfer Object):
 * - Sépare les objets "vivants" (OthelloGame) des objets "figés" (GameStateDTO)
 * - Utilisé uniquement pour la sérialisation/désérialisation
 * - Garantit l'intégrité des données sauvegardées
 * - Facilite la migration vers d'autres formats (XML, Protobuf, etc.)
 *
 * Avantages:
 * - Réutilisable: OthelloGame ↔ GameStateDTO ↔ JSON
 * - Séparation propre des responsabilités
 * - Validation des données à la désérialisation
 */
public class GameStateDTO {

    @JsonProperty("gameMode")
    private String gameMode;

    @JsonProperty("player1Name")
    private String player1Name;

    @JsonProperty("player2Name")
    private String player2Name;

    @JsonProperty("difficulty")
    private String difficulty;

    @JsonProperty("boardState")
    private String boardState;

    @JsonProperty("currentPlayer")
    private String currentPlayer;

    @JsonProperty("startTime")
    private long startTime;

    @JsonProperty("saveDateTime")
    private LocalDateTime saveDateTime;

    // Constructeur par défaut requis par Jackson
    public GameStateDTO() {
    }

    public GameStateDTO(String gameMode, String player1Name, String player2Name,
                        String difficulty, String boardState, String currentPlayer,
                        long startTime, LocalDateTime saveDateTime) {
        this.gameMode = gameMode;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.difficulty = difficulty;
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.startTime = startTime;
        this.saveDateTime = saveDateTime;
    }

    /**
     * Convertit un OthelloGame en DTO.
     *
     * @param game Le jeu à convertir
     * @param gameMode Le mode de jeu
     * @param player1Name Nom du joueur 1
     * @param player2Name Nom du joueur 2
     * @param difficulty Difficulté de l'IA
     * @return Le DTO correspondant
     */
    public static GameStateDTO fromGame(OthelloGame game, String gameMode,
                                        String player1Name, String player2Name,
                                        String difficulty) {
        String[] parts = game.getGameState().split("\\|");
        return new GameStateDTO(
            gameMode,
            player1Name,
            player2Name,
            difficulty,
            parts[0], // boardState
            parts[1], // currentPlayer
            Long.parseLong(parts[2]), // startTime
            LocalDateTime.now()
        );
    }

    /**
     * Reconstitue l'état du jeu à partir du DTO.
     *
     * @return La chaîne d'état pour OthelloGame.setGameState()
     */
    public String toGameState() {
        return boardState + "|" + currentPlayer + "|" + startTime;
    }

    // Getters et Setters

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getSaveDateTime() {
        return saveDateTime;
    }

    public void setSaveDateTime(LocalDateTime saveDateTime) {
        this.saveDateTime = saveDateTime;
    }
}
