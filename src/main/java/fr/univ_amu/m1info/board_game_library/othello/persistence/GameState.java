package fr.univ_amu.m1info.board_game_library.othello.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a saved game state that can be persisted and loaded.
 */
public class GameState {
    private final String gameMode;
    private final String player1Name;
    private final String player2Name;
    private final String difficulty;
    private final String boardState;
    private final String currentPlayer;
    private final long startTime;
    private final LocalDateTime saveDateTime;

    public GameState(String gameMode, String player1Name, String player2Name, String difficulty,
                     String boardState, String currentPlayer, long startTime) {
        this.gameMode = gameMode;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.difficulty = difficulty;
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.startTime = startTime;
        this.saveDateTime = LocalDateTime.now();
    }

    public String toJsonString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(
            "{\"gameMode\":\"%s\",\"player1Name\":\"%s\",\"player2Name\":\"%s\"," +
            "\"difficulty\":\"%s\",\"boardState\":\"%s\",\"currentPlayer\":\"%s\"," +
            "\"startTime\":%d,\"saveDateTime\":\"%s\"}",
            escapeJson(gameMode),
            escapeJson(player1Name),
            escapeJson(player2Name),
            escapeJson(difficulty),
            boardState,
            currentPlayer,
            startTime,
            saveDateTime.format(formatter)
        );
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    public static GameState fromJsonString(String json) {
        try {
            String gameMode = extractJsonValue(json, "gameMode");
            String player1Name = extractJsonValue(json, "player1Name");
            String player2Name = extractJsonValue(json, "player2Name");
            String difficulty = extractJsonValue(json, "difficulty");
            String boardState = extractJsonValue(json, "boardState");
            String currentPlayer = extractJsonValue(json, "currentPlayer");
            long startTime = Long.parseLong(extractJsonValue(json, "startTime"));

            return new GameState(gameMode, player1Name, player2Name, difficulty,
                               boardState, currentPlayer, startTime);
        } catch (Exception e) {
            return null;
        }
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\"?([^\",}]*)\"?";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public String getGameMode() { return gameMode; }
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public String getDifficulty() { return difficulty; }
    public String getBoardState() { return boardState; }
    public String getCurrentPlayer() { return currentPlayer; }
    public long getStartTime() { return startTime; }
    public LocalDateTime getSaveDateTime() { return saveDateTime; }
}
