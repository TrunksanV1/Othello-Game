package fr.univ_amu.m1info.board_game_library.othello.persistence;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Handles saving and loading game states to/from files.
 */
public class GamePersistence {
    private static final String SAVE_DIRECTORY = System.getProperty("user.home") + "/.othello/saves/";
    private static final String SAVE_FILE = "current_game.json";

    public static void saveGame(GameState gameState) throws IOException {
        ensureSaveDirectoryExists();
        Path savePath = Paths.get(SAVE_DIRECTORY + SAVE_FILE);
        String json = gameState.toJsonString();
        Files.writeString(savePath, json, StandardCharsets.UTF_8,
                         StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static GameState loadGame() throws IOException {
        Path savePath = Paths.get(SAVE_DIRECTORY + SAVE_FILE);
        if (!Files.exists(savePath)) {
            return null;
        }

        String json = Files.readString(savePath, StandardCharsets.UTF_8);
        return GameState.fromJsonString(json);
    }

    public static boolean hasSavedGame() {
        Path savePath = Paths.get(SAVE_DIRECTORY + SAVE_FILE);
        return Files.exists(savePath);
    }

    public static void deleteSavedGame() throws IOException {
        Path savePath = Paths.get(SAVE_DIRECTORY + SAVE_FILE);
        if (Files.exists(savePath)) {
            Files.delete(savePath);
        }
    }

    private static void ensureSaveDirectoryExists() throws IOException {
        Path directory = Paths.get(SAVE_DIRECTORY);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }
}
