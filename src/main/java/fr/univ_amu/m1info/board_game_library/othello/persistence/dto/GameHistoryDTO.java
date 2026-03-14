package fr.univ_amu.m1info.board_game_library.othello.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Data Transfer Object pour l'historique d'une partie terminée.
 *
 * Pattern DTO:
 * - Fige les données d'une partie terminée
 * - Sépare les objets terminés (DTO) des objets en cours (entités métier)
 * - Facilite la validation et la sérialisation
 */
public class GameHistoryDTO {

    @JsonProperty("winner")
    private String winner;

    @JsonProperty("player1Name")
    private String player1Name;

    @JsonProperty("player2Name")
    private String player2Name;

    @JsonProperty("player1Score")
    private int player1Score;

    @JsonProperty("player2Score")
    private int player2Score;

    @JsonProperty("durationSeconds")
    private long durationSeconds;

    @JsonProperty("gameDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gameDateTime;

    // Constructeur par défaut requis par Jackson
    public GameHistoryDTO() {
    }

    public GameHistoryDTO(String winner, String player1Name, String player2Name,
                          int player1Score, int player2Score, long durationSeconds,
                          LocalDateTime gameDateTime) {
        this.winner = winner;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.durationSeconds = durationSeconds;
        this.gameDateTime = gameDateTime;
    }

    // Getters et Setters

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public LocalDateTime getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(LocalDateTime gameDateTime) {
        this.gameDateTime = gameDateTime;
    }
}
