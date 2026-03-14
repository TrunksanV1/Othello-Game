package fr.univ_amu.m1info.board_game_library.othello.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.univ_amu.m1info.board_game_library.othello.config.ConfigurationManager;
import fr.univ_amu.m1info.board_game_library.othello.persistence.dto.GameHistoryDTO;
import fr.univ_amu.m1info.board_game_library.othello.persistence.dto.GameStateDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service de persistance utilisant Jackson pour la sérialisation JSON.
 *
 * Avantages par rapport à l'ancien système:
 * - Jackson garantit que le JSON est bien formé
 * - Validation automatique des données
 * - Gestion des erreurs de format
 * - Support natif des dates (LocalDateTime)
 * - Plus maintenable et robuste
 *
 * Utilise les DTOs pour séparer les objets vivants des objets sauvegardés.
 */
public class JsonPersistenceService {

    private final ObjectMapper objectMapper;
    private final ConfigurationManager config;

    public JsonPersistenceService() {
        this.config = ConfigurationManager.getInstance();
        this.objectMapper = createObjectMapper();
    }

    /**
     * Crée et configure l'ObjectMapper Jackson.
     *
     * @return L'ObjectMapper configuré
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Support des dates Java 8 (LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());
        // Format JSON indenté pour la lisibilité
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Ne pas écrire les dates en timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    // ========================================
    // GESTION DE L'ÉTAT DU JEU
    // ========================================

    /**
     * Sauvegarde l'état actuel du jeu.
     *
     * @param gameState Le DTO de l'état du jeu
     * @throws IOException En cas d'erreur d'écriture
     */
    public void saveGameState(GameStateDTO gameState) throws IOException {
        ensureSaveDirectoryExists();
        String directory = config.getString(ConfigurationManager.SAVE_DIRECTORY);
        String filename = config.getString(ConfigurationManager.SAVE_FILENAME);
        Path savePath = Paths.get(directory, filename);

        objectMapper.writeValue(savePath.toFile(), gameState);
    }

    /**
     * Charge l'état sauvegardé du jeu.
     *
     * @return Le DTO de l'état du jeu, ou null si aucune sauvegarde
     * @throws IOException En cas d'erreur de lecture
     */
    public GameStateDTO loadGameState() throws IOException {
        String directory = config.getString(ConfigurationManager.SAVE_DIRECTORY);
        String filename = config.getString(ConfigurationManager.SAVE_FILENAME);
        Path savePath = Paths.get(directory, filename);

        if (!Files.exists(savePath)) {
            return null;
        }

        return objectMapper.readValue(savePath.toFile(), GameStateDTO.class);
    }

    /**
     * Vérifie si une sauvegarde existe.
     *
     * @return true si une sauvegarde existe
     */
    public boolean hasSavedGame() {
        String directory = config.getString(ConfigurationManager.SAVE_DIRECTORY);
        String filename = config.getString(ConfigurationManager.SAVE_FILENAME);
        Path savePath = Paths.get(directory, filename);
        return Files.exists(savePath);
    }

    /**
     * Supprime la sauvegarde actuelle.
     *
     * @throws IOException En cas d'erreur de suppression
     */
    public void deleteSavedGame() throws IOException {
        String directory = config.getString(ConfigurationManager.SAVE_DIRECTORY);
        String filename = config.getString(ConfigurationManager.SAVE_FILENAME);
        Path savePath = Paths.get(directory, filename);

        if (Files.exists(savePath)) {
            Files.delete(savePath);
        }
    }

    // ========================================
    // GESTION DE L'HISTORIQUE
    // ========================================

    /**
     * Ajoute une partie terminée à l'historique.
     *
     * @param historyEntry Le DTO de l'entrée d'historique
     * @throws IOException En cas d'erreur d'écriture
     */
    public void addToHistory(GameHistoryDTO historyEntry) throws IOException {
        ensureHistoryDirectoryExists();
        List<GameHistoryDTO> history = loadHistory();

        // Ajouter en première position (plus récent en premier)
        history.add(0, historyEntry);

        // Limiter le nombre d'entrées
        int maxEntries = config.getInt(ConfigurationManager.HISTORY_MAX_ENTRIES, 15);
        if (history.size() > maxEntries) {
            history = history.subList(0, maxEntries);
        }

        saveHistory(history);
    }

    /**
     * Charge tout l'historique des parties.
     *
     * @return La liste des entrées d'historique
     * @throws IOException En cas d'erreur de lecture
     */
    public List<GameHistoryDTO> loadHistory() throws IOException {
        String directory = config.getString(ConfigurationManager.HISTORY_DIRECTORY);
        String filename = config.getString(ConfigurationManager.HISTORY_FILENAME);
        Path historyPath = Paths.get(directory, filename);

        if (!Files.exists(historyPath)) {
            return new ArrayList<>();
        }

        GameHistoryDTO[] historyArray = objectMapper.readValue(
            historyPath.toFile(),
            GameHistoryDTO[].class
        );

        return new ArrayList<>(Arrays.asList(historyArray));
    }

    /**
     * Sauvegarde l'historique complet.
     *
     * @param history La liste des entrées d'historique
     * @throws IOException En cas d'erreur d'écriture
     */
    private void saveHistory(List<GameHistoryDTO> history) throws IOException {
        String directory = config.getString(ConfigurationManager.HISTORY_DIRECTORY);
        String filename = config.getString(ConfigurationManager.HISTORY_FILENAME);
        Path historyPath = Paths.get(directory, filename);

        objectMapper.writeValue(historyPath.toFile(), history);
    }

    /**
     * Copie l'historique vers les resources (pour distribution).
     */
    public void copyHistoryToResources() {
        try {
            List<GameHistoryDTO> history = loadHistory();
            if (history.isEmpty()) {
                return;
            }

            // Limiter à 10 entrées pour les resources
            List<GameHistoryDTO> limitedHistory = history.subList(0, Math.min(history.size(), 10));

            Path resourcePath = Paths.get("src/main/resources/data/history.json");
            objectMapper.writeValue(resourcePath.toFile(), limitedHistory);
        } catch (Exception e) {
            System.err.println("Impossible de copier l'historique vers les resources: " + e.getMessage());
        }
    }

    // ========================================
    // UTILITAIRES
    // ========================================

    /**
     * Crée le répertoire de sauvegarde s'il n'existe pas.
     *
     * @throws IOException En cas d'erreur de création
     */
    private void ensureSaveDirectoryExists() throws IOException {
        String directory = config.getString(ConfigurationManager.SAVE_DIRECTORY);
        Path directoryPath = Paths.get(directory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }

    /**
     * Crée le répertoire d'historique s'il n'existe pas.
     *
     * @throws IOException En cas d'erreur de création
     */
    private void ensureHistoryDirectoryExists() throws IOException {
        String directory = config.getString(ConfigurationManager.HISTORY_DIRECTORY);
        Path directoryPath = Paths.get(directory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }

    /**
     * Retourne l'ObjectMapper pour des utilisations avancées.
     *
     * @return L'ObjectMapper configuré
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
