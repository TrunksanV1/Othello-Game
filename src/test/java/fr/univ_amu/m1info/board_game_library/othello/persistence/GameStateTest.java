package fr.univ_amu.m1info.board_game_library.othello.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void testGameStateCreation() {
        GameState state = new GameState(
            "PVP",
            "Alice",
            "Bob",
            "Facile",
            "WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB",
            "B",
            System.currentTimeMillis()
        );

        assertEquals("PVP", state.getGameMode());
        assertEquals("Alice", state.getPlayer1Name());
        assertEquals("Bob", state.getPlayer2Name());
        assertEquals("Facile", state.getDifficulty());
        assertEquals("B", state.getCurrentPlayer());
    }

    @Test
    void testGameStateToJson() {
        GameState state = new GameState(
            "PVE",
            "Player1",
            "IA",
            "Difficile",
            "WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB",
            "W",
            1234567890L
        );

        String json = state.toJsonString();

        assertNotNull(json);
        assertTrue(json.contains("PVE"));
        assertTrue(json.contains("Player1"));
        assertTrue(json.contains("IA"));
        assertTrue(json.contains("Difficile"));
        assertTrue(json.contains("\"currentPlayer\":\"W\""));
        assertTrue(json.contains("\"startTime\":1234567890"));
    }

    @Test
    void testGameStateFromJson() {
        String json = "{\"gameMode\":\"PVP\",\"player1Name\":\"Alice\",\"player2Name\":\"Bob\"," +
                     "\"difficulty\":\"Moyen\",\"boardState\":\"WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB\"," +
                     "\"currentPlayer\":\"B\",\"startTime\":1234567890}";

        GameState state = GameState.fromJsonString(json);

        assertNotNull(state);
        assertEquals("PVP", state.getGameMode());
        assertEquals("Alice", state.getPlayer1Name());
        assertEquals("Bob", state.getPlayer2Name());
        assertEquals("Moyen", state.getDifficulty());
        assertEquals("B", state.getCurrentPlayer());
        assertEquals(1234567890L, state.getStartTime());
    }

    @Test
    void testGameStateRoundTrip() {
        GameState original = new GameState(
            "PVE",
            "TestPlayer",
            "IA",
            "Facile",
            "BBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWW",
            "W",
            9876543210L
        );

        String json = original.toJsonString();
        GameState restored = GameState.fromJsonString(json);

        assertNotNull(restored);
        assertEquals(original.getGameMode(), restored.getGameMode());
        assertEquals(original.getPlayer1Name(), restored.getPlayer1Name());
        assertEquals(original.getPlayer2Name(), restored.getPlayer2Name());
        assertEquals(original.getDifficulty(), restored.getDifficulty());
        assertEquals(original.getBoardState(), restored.getBoardState());
        assertEquals(original.getCurrentPlayer(), restored.getCurrentPlayer());
        assertEquals(original.getStartTime(), restored.getStartTime());
    }

    @Test
    void testGameStateWithSpecialCharacters() {
        GameState state = new GameState(
            "PVP",
            "Alice \"The Queen\"",
            "Bob's Player",
            "Difficile",
            "WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB",
            "B",
            System.currentTimeMillis()
        );

        String json = state.toJsonString();
        GameState restored = GameState.fromJsonString(json);

        assertNotNull(restored);
        assertTrue(restored.getPlayer1Name().contains("Alice"));
        assertTrue(restored.getPlayer2Name().contains("Bob"));
    }

    @Test
    void testInvalidJsonReturnsNull() {
        String invalidJson = "{invalid json}";
        GameState state = GameState.fromJsonString(invalidJson);

        assertNull(state);
    }

    @Test
    void testEmptyJsonReturnsNull() {
        String emptyJson = "";
        GameState state = GameState.fromJsonString(emptyJson);

        assertNull(state);
    }
}
