package fr.univ_amu.m1info.board_game_library.othello.persistence;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the history of completed games.
 */
public class HistoryManager {
    private static final String SAVE_DIRECTORY = System.getProperty("user.home") + "/.othello/";
    private static final String HISTORY_FILE = "history.json";
    private static final int MAX_HISTORY_ENTRIES = 15;

    public static void addGameToHistory(GameHistory gameHistory) throws IOException {
        ensureDirectoryExists();
        List<GameHistory> history = loadHistory();
        history.add(0, gameHistory);

        if (history.size() > MAX_HISTORY_ENTRIES) {
            history = history.subList(0, MAX_HISTORY_ENTRIES);
        }

        saveHistory(history);
    }

    public static List<GameHistory> loadHistory() throws IOException {
        Path historyPath = Paths.get(SAVE_DIRECTORY + HISTORY_FILE);
        if (!Files.exists(historyPath)) {
            return new ArrayList<>();
        }

        String content = Files.readString(historyPath, StandardCharsets.UTF_8);
        return parseHistoryJson(content);
    }

    private static void saveHistory(List<GameHistory> history) throws IOException {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < history.size(); i++) {
            json.append(history.get(i).toJsonString());
            if (i < history.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        Path historyPath = Paths.get(SAVE_DIRECTORY + HISTORY_FILE);
        Files.writeString(historyPath, json.toString(), StandardCharsets.UTF_8,
                         StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static List<GameHistory> parseHistoryJson(String json) {
        List<GameHistory> history = new ArrayList<>();
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            return history;
        }

        json = json.substring(1, json.length() - 1);
        StringBuilder current = new StringBuilder();
        int braceCount = 0;

        for (char c : json.toCharArray()) {
            if (c == '{') {
                braceCount++;
                current.append(c);
            } else if (c == '}') {
                braceCount--;
                current.append(c);
                if (braceCount == 0 && current.length() > 0) {
                    GameHistory entry = parseHistoryEntry(current.toString());
                    if (entry != null) {
                        history.add(entry);
                    }
                    current = new StringBuilder();
                }
            } else if (braceCount > 0) {
                current.append(c);
            }
        }

        return history;
    }

    private static GameHistory parseHistoryEntry(String json) {
        try {
            String winner = extractJsonValue(json, "winner");
            String player1Name = extractJsonValue(json, "player1Name");
            String player2Name = extractJsonValue(json, "player2Name");
            int player1Score = Integer.parseInt(extractJsonValue(json, "player1Score"));
            int player2Score = Integer.parseInt(extractJsonValue(json, "player2Score"));
            long durationSeconds = Long.parseLong(extractJsonValue(json, "durationSeconds"));
            String dateStr = extractJsonValue(json, "date");

            LocalDateTime gameDateTime;
            if (dateStr != null && !dateStr.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    gameDateTime = LocalDateTime.parse(dateStr, formatter);
                } catch (Exception e) {
                    gameDateTime = LocalDateTime.now();
                }
            } else {
                gameDateTime = LocalDateTime.now();
            }

            return new GameHistory(winner, player1Name, player2Name, player1Score, player2Score, durationSeconds, gameDateTime);
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

    private static void ensureDirectoryExists() throws IOException {
        Path directory = Paths.get(SAVE_DIRECTORY);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    public static void copyHistoryToResources() {
        try {
            List<GameHistory> history = loadHistory();
            if (history.isEmpty()) {
                return;
            }

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < Math.min(history.size(), 10); i++) {
                json.append(history.get(i).toJsonString());
                if (i < Math.min(history.size(), 10) - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            Path resourcePath = Paths.get("src/main/resources/data/history.json");
            Files.writeString(resourcePath, json.toString(), StandardCharsets.UTF_8,
                             StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("Could not copy history to resources: " + e.getMessage());
        }
    }
}
