package fr.univ_amu.m1info.board_game_library.othello.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestionnaire de configuration centralisé.
 *
 * Pattern Singleton: Une seule instance de configuration pour toute l'application.
 *
 * Avantages:
 * - Toute la configuration est centralisée dans un fichier properties
 * - Facile de changer les paramètres sans recompilation
 * - Sépare la configuration du code
 * - Facilite les tests (on peut charger une config de test)
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;
    private final Properties properties;

    private static final String CONFIG_FILE = "othello-config.properties";

    // Clés de configuration
    public static final String SAVE_DIRECTORY = "game.save.directory";
    public static final String SAVE_FILENAME = "game.save.filename";
    public static final String HISTORY_DIRECTORY = "game.history.directory";
    public static final String HISTORY_FILENAME = "game.history.filename";
    public static final String HISTORY_MAX_ENTRIES = "game.history.maxEntries";
    public static final String BOARD_SIZE = "game.board.size";
    public static final String AI_DELAY_MS = "game.ai.delayMs";
    public static final String AI_HARD_DEPTH = "game.ai.hard.depth";
    public static final String AI_DIFFICULTIES = "game.ai.difficulties";
    public static final String UI_TITLE = "game.ui.title";
    public static final String UI_WIDTH = "game.ui.width";
    public static final String UI_HEIGHT = "game.ui.height";
    public static final String SAVE_FORMAT = "game.save.format";
    public static final String USE_JACKSON = "game.save.useJackson";

    private ConfigurationManager() {
        properties = new Properties();
        loadConfiguration();
    }

    /**
     * Retourne l'instance unique du gestionnaire de configuration.
     *
     * @return L'instance du ConfigurationManager
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    /**
     * Charge la configuration depuis le fichier properties.
     */
    private void loadConfiguration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Fichier de configuration introuvable: " + CONFIG_FILE);
                loadDefaultConfiguration();
                return;
            }
            properties.load(input);
            resolveSystemProperties();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la configuration: " + e.getMessage());
            loadDefaultConfiguration();
        }
    }

    /**
     * Charge une configuration par défaut si le fichier est introuvable.
     */
    private void loadDefaultConfiguration() {
        properties.setProperty(SAVE_DIRECTORY, System.getProperty("user.home") + "/.othello/saves/");
        properties.setProperty(SAVE_FILENAME, "current_game.json");
        properties.setProperty(HISTORY_DIRECTORY, System.getProperty("user.home") + "/.othello/");
        properties.setProperty(HISTORY_FILENAME, "history.json");
        properties.setProperty(HISTORY_MAX_ENTRIES, "15");
        properties.setProperty(BOARD_SIZE, "8");
        properties.setProperty(AI_DELAY_MS, "500");
        properties.setProperty(AI_HARD_DEPTH, "4");
        properties.setProperty(AI_DIFFICULTIES, "Facile,Moyen,Difficile");
        properties.setProperty(UI_TITLE, "Othello");
        properties.setProperty(UI_WIDTH, "800");
        properties.setProperty(UI_HEIGHT, "600");
        properties.setProperty(SAVE_FORMAT, "JSON");
        properties.setProperty(USE_JACKSON, "true");
    }

    /**
     * Résout les variables système comme ${user.home}.
     */
    private void resolveSystemProperties() {
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (value.contains("${user.home}")) {
                value = value.replace("${user.home}", System.getProperty("user.home"));
                properties.setProperty(key, value);
            }
        }
    }

    /**
     * Récupère une propriété sous forme de String.
     *
     * @param key La clé de la propriété
     * @return La valeur de la propriété
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Récupère une propriété sous forme de String avec valeur par défaut.
     *
     * @param key La clé de la propriété
     * @param defaultValue La valeur par défaut
     * @return La valeur de la propriété ou la valeur par défaut
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Récupère une propriété sous forme d'entier.
     *
     * @param key La clé de la propriété
     * @return La valeur de la propriété en int
     */
    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * Récupère une propriété sous forme d'entier avec valeur par défaut.
     *
     * @param key La clé de la propriété
     * @param defaultValue La valeur par défaut
     * @return La valeur de la propriété en int ou la valeur par défaut
     */
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Récupère une propriété sous forme de boolean.
     *
     * @param key La clé de la propriété
     * @return La valeur de la propriété en boolean
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * Récupère une propriété sous forme de boolean avec valeur par défaut.
     *
     * @param key La clé de la propriété
     * @param defaultValue La valeur par défaut
     * @return La valeur de la propriété en boolean ou la valeur par défaut
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Charge une configuration de test à partir d'un fichier spécifique.
     * Utile pour les tests unitaires.
     *
     * @param testConfigFile Le fichier de configuration de test
     */
    public void loadTestConfiguration(String testConfigFile) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(testConfigFile)) {
            if (input != null) {
                properties.load(input);
                resolveSystemProperties();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la configuration de test: " + e.getMessage());
        }
    }
}
