package fr.univ_amu.m1info.board_game_library.othello.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a completed game entry in the history.
 */
public class GameHistory {
    private final String winner;
    private final String player1Name;
    private final String player2Name;
    private final int player1Score;
    private final int player2Score;
    private final long durationSeconds;
    private final LocalDateTime gameDateTime;

    public GameHistory(String winner, String player1Name, String player2Name,
                      int player1Score, int player2Score, long durationSeconds) {
        this(winner, player1Name, player2Name, player1Score, player2Score, durationSeconds, LocalDateTime.now());
    }

    public GameHistory(String winner, String player1Name, String player2Name,
                      int player1Score, int player2Score, long durationSeconds, LocalDateTime gameDateTime) {
        this.winner = winner;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.durationSeconds = durationSeconds;
        this.gameDateTime = gameDateTime;
    }

    public String toJsonString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
            "{\"winner\":\"%s\",\"player1Name\":\"%s\",\"player2Name\":\"%s\"," +
            "\"player1Score\":%d,\"player2Score\":%d,\"durationSeconds\":%d,\"date\":\"%s\"}",
            escapeJson(winner),
            escapeJson(player1Name),
            escapeJson(player2Name),
            player1Score,
            player2Score,
            durationSeconds,
            gameDateTime.format(formatter)
        );
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    public String getWinner() { return winner; }
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }
    public long getDurationSeconds() { return durationSeconds; }
    public LocalDateTime getGameDateTime() { return gameDateTime; }
}
