package fr.univ_amu.m1info.board_game_library.othello.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHistoryTest {

    @Test
    void testGameHistoryCreation() {
        GameHistory history = new GameHistory(
            "Alice",
            "Alice",
            "Bob",
            32,
            28,
            420L
        );

        assertEquals("Alice", history.getWinner());
        assertEquals("Alice", history.getPlayer1Name());
        assertEquals("Bob", history.getPlayer2Name());
        assertEquals(32, history.getPlayer1Score());
        assertEquals(28, history.getPlayer2Score());
        assertEquals(420L, history.getDurationSeconds());
        assertNotNull(history.getGameDateTime());
    }

    @Test
    void testGameHistoryToJson() {
        GameHistory history = new GameHistory(
            "Bob",
            "Alice",
            "Bob",
            25,
            35,
            600L
        );

        String json = history.toJsonString();

        assertNotNull(json);
        assertTrue(json.contains("\"winner\":\"Bob\""));
        assertTrue(json.contains("\"player1Name\":\"Alice\""));
        assertTrue(json.contains("\"player2Name\":\"Bob\""));
        assertTrue(json.contains("\"player1Score\":25"));
        assertTrue(json.contains("\"player2Score\":35"));
        assertTrue(json.contains("\"durationSeconds\":600"));
        assertTrue(json.contains("\"date\":"));
    }

    @Test
    void testGameHistoryWithTie() {
        GameHistory history = new GameHistory(
            "Égalité",
            "Player1",
            "Player2",
            32,
            32,
            300L
        );

        assertEquals("Égalité", history.getWinner());
        assertEquals(32, history.getPlayer1Score());
        assertEquals(32, history.getPlayer2Score());
    }

    @Test
    void testGameHistoryWithLongDuration() {
        GameHistory history = new GameHistory(
            "Player1",
            "Player1",
            "IA",
            40,
            24,
            3600L // 1 hour
        );

        assertEquals(3600L, history.getDurationSeconds());
        String json = history.toJsonString();
        assertTrue(json.contains("\"durationSeconds\":3600"));
    }

    @Test
    void testGameHistoryWithShortDuration() {
        GameHistory history = new GameHistory(
            "Player2",
            "Player1",
            "Player2",
            20,
            44,
            45L // 45 seconds
        );

        assertEquals(45L, history.getDurationSeconds());
    }

    @Test
    void testGameHistoryJsonFormat() {
        GameHistory history = new GameHistory(
            "TestWinner",
            "TestPlayer1",
            "TestPlayer2",
            30,
            30,
            120L
        );

        String json = history.toJsonString();

        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
        assertTrue(json.contains("winner"));
        assertTrue(json.contains("player1Name"));
        assertTrue(json.contains("player2Name"));
        assertTrue(json.contains("player1Score"));
        assertTrue(json.contains("player2Score"));
        assertTrue(json.contains("durationSeconds"));
        assertTrue(json.contains("date"));
    }

    @Test
    void testGameHistoryWithSpecialCharacters() {
        GameHistory history = new GameHistory(
            "Player \"Pro\"",
            "Player \"Pro\"",
            "Bob's AI",
            50,
            14,
            200L
        );

        String json = history.toJsonString();
        assertNotNull(json);
        assertTrue(json.contains("Player"));
        assertTrue(json.contains("Bob"));
    }
}
