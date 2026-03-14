package fr.univ_amu.m1info.board_game_library.othello.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GamePersistenceTest {

    @BeforeEach
    void setUp() throws IOException {
        GamePersistence.deleteSavedGame();
    }

    @AfterEach
    void tearDown() throws IOException {
        GamePersistence.deleteSavedGame();
    }

    @Test
    void testSaveAndLoadGame() throws IOException {
        GameState originalState = new GameState(
            "PVP",
            "Alice",
            "Bob",
            "Moyen",
            "WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB",
            "B",
            System.currentTimeMillis()
        );

        GamePersistence.saveGame(originalState);
        assertTrue(GamePersistence.hasSavedGame());

        GameState loadedState = GamePersistence.loadGame();

        assertNotNull(loadedState);
        assertEquals(originalState.getGameMode(), loadedState.getGameMode());
        assertEquals(originalState.getPlayer1Name(), loadedState.getPlayer1Name());
        assertEquals(originalState.getPlayer2Name(), loadedState.getPlayer2Name());
        assertEquals(originalState.getDifficulty(), loadedState.getDifficulty());
        assertEquals(originalState.getBoardState(), loadedState.getBoardState());
        assertEquals(originalState.getCurrentPlayer(), loadedState.getCurrentPlayer());
    }

    @Test
    void testHasSavedGameWhenNoSave() throws IOException {
        GamePersistence.deleteSavedGame();
        assertFalse(GamePersistence.hasSavedGame());
    }

    @Test
    void testHasSavedGameWhenSaveExists() throws IOException {
        GameState state = new GameState(
            "PVE",
            "Player",
            "IA",
            "Difficile",
            "BBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWW",
            "W",
            System.currentTimeMillis()
        );

        GamePersistence.saveGame(state);
        assertTrue(GamePersistence.hasSavedGame());
    }

    @Test
    void testDeleteSavedGame() throws IOException {
        GameState state = new GameState(
            "PVP",
            "Test1",
            "Test2",
            "Facile",
            "WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB",
            "B",
            System.currentTimeMillis()
        );

        GamePersistence.saveGame(state);
        assertTrue(GamePersistence.hasSavedGame());

        GamePersistence.deleteSavedGame();
        assertFalse(GamePersistence.hasSavedGame());
    }

    @Test
    void testLoadGameWhenNoSave() throws IOException {
        GamePersistence.deleteSavedGame();
        GameState loaded = GamePersistence.loadGame();

        assertNull(loaded);
    }

    @Test
    void testOverwriteExistingSave() throws IOException {
        GameState state1 = new GameState(
            "PVP",
            "First",
            "Second",
            "Facile",
            "WWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBBWWBB",
            "B",
            1000L
        );

        GamePersistence.saveGame(state1);

        GameState state2 = new GameState(
            "PVE",
            "Third",
            "IA",
            "Difficile",
            "BBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWWBBBBBBBBWWWWWWWW",
            "W",
            2000L
        );

        GamePersistence.saveGame(state2);

        GameState loaded = GamePersistence.loadGame();

        assertNotNull(loaded);
        assertEquals("Third", loaded.getPlayer1Name());
        assertEquals("IA", loaded.getPlayer2Name());
        assertEquals(2000L, loaded.getStartTime());
    }
}
