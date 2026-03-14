package fr.univ_amu.m1info.board_game_library.othello.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour ConfigurationManager (Pattern Singleton)
 */
class ConfigurationManagerTest {

    @Test
    @DisplayName("Test getInstance retourne toujours la même instance")
    void testSingleton() {
        // Act
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();

        // Assert
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Les deux instances doivent être identiques (Singleton)");
    }

    @Test
    @DisplayName("Test chargement configuration par défaut")
    void testDefaultConfiguration() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();

        // Assert
        assertNotNull(config.getString(ConfigurationManager.SAVE_DIRECTORY));
        assertNotNull(config.getString(ConfigurationManager.SAVE_FILENAME));
        assertNotNull(config.getString(ConfigurationManager.HISTORY_FILENAME));

        assertTrue(config.getInt(ConfigurationManager.HISTORY_MAX_ENTRIES, 0) > 0);
        assertTrue(config.getInt(ConfigurationManager.AI_HARD_DEPTH, 0) > 0);
        assertTrue(config.getInt(ConfigurationManager.AI_DELAY_MS, -1) >= 0);
    }

    @Test
    @DisplayName("Test getString retourne valeur correcte")
    void testGetString() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        String saveDir = config.getString(ConfigurationManager.SAVE_DIRECTORY);

        // Assert
        assertNotNull(saveDir);
        assertFalse(saveDir.isEmpty());
    }

    @Test
    @DisplayName("Test getInt retourne valeur correcte")
    void testGetInt() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        int maxEntries = config.getInt(ConfigurationManager.HISTORY_MAX_ENTRIES, 10);

        // Assert
        assertTrue(maxEntries > 0);
        assertEquals(15, maxEntries); // Valeur par défaut dans le fichier properties
    }

    @Test
    @DisplayName("Test getBoolean retourne valeur correcte")
    void testGetBoolean() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        boolean useJackson = config.getBoolean(ConfigurationManager.USE_JACKSON, false);

        // Assert - Par défaut devrait être true
        assertTrue(useJackson);
    }

    @Test
    @DisplayName("Test getString avec clé inexistante retourne null")
    void testGetString_NonExistent() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        String value = config.getString("non.existent.key");

        // Assert
        assertNull(value);
    }

    @Test
    @DisplayName("Test getInt avec valeur par défaut")
    void testGetInt_WithDefault() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        int value = config.getInt("non.existent.key", 42);

        // Assert
        assertEquals(42, value);
    }

    @Test
    @DisplayName("Test getBoolean avec valeur par défaut")
    void testGetBoolean_WithDefault() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        boolean value = config.getBoolean("non.existent.key", true);

        // Assert
        assertTrue(value);
    }

    @Test
    @DisplayName("Test résolution variables système ${user.home}")
    void testSystemVariableResolution() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        String saveDir = config.getString(ConfigurationManager.SAVE_DIRECTORY);

        // Assert
        assertNotNull(saveDir);
        assertFalse(saveDir.contains("${user.home}"),
            "Les variables système devraient être résolues");

        String userHome = System.getProperty("user.home");
        assertTrue(saveDir.contains(userHome),
            "Le chemin devrait contenir le répertoire home de l'utilisateur");
    }

    @Test
    @DisplayName("Test valeurs par défaut du projet")
    void testProjectDefaults() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();

        // Assert
        assertEquals("current_game.json", config.getString(ConfigurationManager.SAVE_FILENAME));
        assertEquals("history.json", config.getString(ConfigurationManager.HISTORY_FILENAME));
        assertEquals(15, config.getInt(ConfigurationManager.HISTORY_MAX_ENTRIES, 10));
        assertEquals(4, config.getInt(ConfigurationManager.AI_HARD_DEPTH, 3));
        assertEquals(500, config.getInt(ConfigurationManager.AI_DELAY_MS, 0));
        assertEquals(8, config.getInt(ConfigurationManager.BOARD_SIZE, 8));
    }

    @Test
    @DisplayName("Test constantes de clés de configuration")
    void testConfigurationKeys() {
        // Assert - Vérifier que les constantes existent
        assertNotNull(ConfigurationManager.SAVE_DIRECTORY);
        assertNotNull(ConfigurationManager.SAVE_FILENAME);
        assertNotNull(ConfigurationManager.HISTORY_FILENAME);
        assertNotNull(ConfigurationManager.HISTORY_MAX_ENTRIES);
        assertNotNull(ConfigurationManager.AI_DELAY_MS);
        assertNotNull(ConfigurationManager.AI_HARD_DEPTH);
    }

    @Test
    @DisplayName("Test getString avec valeur par défaut")
    void testGetString_WithDefault() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();
        String value = config.getString("non.existent.key", "default_value");

        // Assert
        assertEquals("default_value", value);
    }

    @Test
    @DisplayName("Test thread-safety du singleton")
    void testThreadSafety() throws InterruptedException {
        // Arrange
        final ConfigurationManager[] instances = new ConfigurationManager[10];
        Thread[] threads = new Thread[10];

        // Act - Créer plusieurs threads qui accèdent au singleton
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = ConfigurationManager.getInstance();
            });
            threads[i].start();
        }

        // Attendre que tous les threads terminent
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - Toutes les instances doivent être identiques
        for (int i = 1; i < instances.length; i++) {
            assertSame(instances[0], instances[i],
                "Toutes les instances doivent être identiques même en multithreading");
        }
    }

    @Test
    @DisplayName("Test toutes les propriétés UI sont accessibles")
    void testUIProperties() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();

        // Assert
        assertNotNull(config.getString(ConfigurationManager.UI_TITLE));
        assertTrue(config.getInt(ConfigurationManager.UI_WIDTH, 0) > 0);
        assertTrue(config.getInt(ConfigurationManager.UI_HEIGHT, 0) > 0);
    }

    @Test
    @DisplayName("Test propriétés AI sont configurables")
    void testAIProperties() {
        // Act
        ConfigurationManager config = ConfigurationManager.getInstance();

        // Assert
        assertNotNull(config.getString(ConfigurationManager.AI_DIFFICULTIES));
        assertTrue(config.getInt(ConfigurationManager.AI_HARD_DEPTH, 0) > 0);
        assertTrue(config.getInt(ConfigurationManager.AI_DELAY_MS, -1) >= 0);
    }
}
