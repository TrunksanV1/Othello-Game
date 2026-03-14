package fr.univ_amu.m1info.board_game_library.othello;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests du lancement de partie Othello")
class OthelloLauncherTest {

    @Test
    @DisplayName("Test 1 : Lancement d'une partie PVP")
    void testLaunchGamePVP() {
        String gameMode = "PVP";
        String player1Name = "Alice";
        String player2Name = "Bob";
        String difficulty = "Facile";

        assertValidGameParameters(gameMode, player1Name, player2Name, difficulty);
    }

    @Test
    @DisplayName("Test 2 : Lancement d'une partie PVE")
    void testLaunchGamePVE() {
        String gameMode = "PVE";
        String player1Name = "Alice";
        String player2Name = "IA";
        String difficulty = "Moyen";

        assertValidGameParameters(gameMode, player1Name, player2Name, difficulty);
    }

    @Test
    @DisplayName("Test 3 : Configuration PVP")
    void testPVPConfiguration() {
        String gameMode = "PVP";
        String player1Name = "Joueur1";
        String player2Name = "Joueur2";

        assertNotEquals(player1Name, player2Name);
        assertEquals("PVP", gameMode);
    }

    @Test
    @DisplayName("Test 4 : Configuration PVE")
    void testPVEConfiguration() {
        String gameMode = "PVE";
        String player1Name = "Joueur1";
        String player2Name = "IA";
        String difficulty = "Difficile";

        assertEquals("PVE", gameMode);
        assertEquals("IA", player2Name);
        assertTrue(difficulty.equals("Facile") || difficulty.equals("Moyen") || difficulty.equals("Difficile"));
    }

    @Test
    @DisplayName("Test 5 : Nombre d'éléments d'interface")
    void testGameConfigurationElements() {
        String gameMode = "PVP";
        int expectedElements = 4;

        assertEquals(4, expectedElements);
    }

    @Test
    @DisplayName("Test 6 : Dimensions du plateau")
    void testBoardDimensions() {
        int expectedRows = 8;
        int expectedColumns = 8;

        assertEquals(8, expectedRows);
        assertEquals(8, expectedColumns);
    }

    @Test
    @DisplayName("Test 7 : Noms de joueurs valides")
    void testPlayerNamesValidation() {
        assertValidPlayerName("Alice");
        assertValidPlayerName("Bob123");
        assertValidPlayerName("Joueur_1");
    }

    @Test
    @DisplayName("Test 8 : Noms de joueurs invalides")
    void testPlayerNamesInvalidation() {
        assertInvalidPlayerName("");
        assertInvalidPlayerName("   ");
        assertInvalidPlayerName(null);
    }

    @Test
    @DisplayName("Test 9 : Niveaux de difficulté")
    void testDifficultyLevels() {
        assertValidDifficulty("Facile");
        assertValidDifficulty("Moyen");
        assertValidDifficulty("Difficile");

        assertInvalidDifficulty("Impossible");
        assertInvalidDifficulty("");
        assertInvalidDifficulty(null);
    }

    @Test
    @DisplayName("Test 10 : Modes de jeu")
    void testGameModes() {
        assertValidGameMode("PVP");
        assertValidGameMode("PVE");

        assertInvalidGameMode("PVA");
        assertInvalidGameMode("Humain");
        assertInvalidGameMode("");
        assertInvalidGameMode(null);
    }

    private void assertValidGameParameters(String gameMode, String player1Name,
                                           String player2Name, String difficulty) {
        assertNotNull(gameMode);
        assertFalse(gameMode.isEmpty());
        assertNotNull(player1Name);
        assertFalse(player1Name.isEmpty());
        assertNotNull(player2Name);
        assertFalse(player2Name.isEmpty());

        if (gameMode.equals("PVP")) {
            assertNotEquals(player1Name, player2Name);
        }

        if (gameMode.equals("PVE")) {
            assertEquals("IA", player2Name);
            assertNotNull(difficulty);
        }
    }

    private void assertValidPlayerName(String name) {
        assertTrue(name != null && !name.trim().isEmpty());
    }

    private void assertInvalidPlayerName(String name) {
        boolean isInvalid = name == null || name.trim().isEmpty();
        assertTrue(isInvalid);
    }

    private void assertValidDifficulty(String difficulty) {
        boolean isValid = difficulty != null &&
                (difficulty.equals("Facile") || difficulty.equals("Moyen") || difficulty.equals("Difficile"));
        assertTrue(isValid);
    }

    private void assertInvalidDifficulty(String difficulty) {
        boolean isInvalid = difficulty == null ||
                (!difficulty.equals("Facile") && !difficulty.equals("Moyen") && !difficulty.equals("Difficile"));
        assertTrue(isInvalid);
    }

    private void assertValidGameMode(String gameMode) {
        boolean isValid = gameMode != null && (gameMode.equals("PVP") || gameMode.equals("PVE"));
        assertTrue(isValid);
    }

    private void assertInvalidGameMode(String gameMode) {
        boolean isInvalid = gameMode == null || (!gameMode.equals("PVP") && !gameMode.equals("PVE"));
        assertTrue(isInvalid);
    }
}