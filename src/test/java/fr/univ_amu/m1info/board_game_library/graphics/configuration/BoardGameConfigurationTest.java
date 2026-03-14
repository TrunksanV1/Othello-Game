package fr.univ_amu.m1info.board_game_library.graphics.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de configuration du jeu")
class BoardGameConfigurationTest {

    @Test
    @DisplayName("Test 1 : Configuration valide")
    void testValidConfiguration() {
        String title = "OTHELLO";
        BoardGameDimensions dimensions = new BoardGameDimensions(8, 8);
        List<LabeledElementConfiguration> elements = new ArrayList<>();
        elements.add(new LabeledElementConfiguration("Joueur 1", "player1", LabeledElementKind.TEXT));

        BoardGameConfiguration config = new BoardGameConfiguration(title, dimensions, elements);

        assertNotNull(config);
        assertEquals("OTHELLO", config.title());
        assertEquals(8, config.dimensions().rowCount());
        assertEquals(8, config.dimensions().columnCount());
        assertEquals(1, config.labeledElementConfigurations().size());
    }

    @Test
    @DisplayName("Test 2 : Configuration Othello PVP")
    void testOthelloConfigurationPVP() {
        List<LabeledElementConfiguration> elements = new ArrayList<>();
        elements.add(new LabeledElementConfiguration("Alice (Noir) vs Bob (Blanc)",
                "players_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Tour: Alice",
                "turn_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Score - Noir: 2 | Blanc: 2",
                "score_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Nouvelle partie",
                "restart_button", LabeledElementKind.BUTTON));

        BoardGameConfiguration config = new BoardGameConfiguration(
                "OTHELLO",
                new BoardGameDimensions(8, 8),
                elements
        );

        assertEquals(4, config.labeledElementConfigurations().size());
        assertTrue(hasElementWithId(config, "players_label"));
        assertTrue(hasElementWithId(config, "turn_label"));
        assertTrue(hasElementWithId(config, "score_label"));
        assertTrue(hasElementWithId(config, "restart_button"));
    }

    @Test
    @DisplayName("Test 3 : Configuration Othello PVE")
    void testOthelloConfigurationPVE() {
        List<LabeledElementConfiguration> elements = new ArrayList<>();
        elements.add(new LabeledElementConfiguration("Alice (Noir) vs IA (Blanc)",
                "players_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Difficulté: Moyen",
                "difficulty_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Tour: Alice",
                "turn_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Score - Noir: 2 | Blanc: 2",
                "score_label", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Nouvelle partie",
                "restart_button", LabeledElementKind.BUTTON));

        BoardGameConfiguration config = new BoardGameConfiguration(
                "OTHELLO",
                new BoardGameDimensions(8, 8),
                elements
        );

        assertEquals(5, config.labeledElementConfigurations().size());
        assertTrue(hasElementWithId(config, "difficulty_label"));
    }

    @Test
    @DisplayName("Test 4 : Dimensions valides")
    void testValidDimensions() {
        BoardGameDimensions dimensions = new BoardGameDimensions(8, 8);

        assertEquals(8, dimensions.rowCount());
        assertEquals(8, dimensions.columnCount());
    }

    @Test
    @DisplayName("Test 5 : Élément TEXT")
    void testTextElement() {
        LabeledElementConfiguration element = new LabeledElementConfiguration(
                "Score: 0",
                "score_label",
                LabeledElementKind.TEXT
        );

        assertEquals("Score: 0", element.label());
        assertEquals("score_label", element.id());
        assertEquals(LabeledElementKind.TEXT, element.kind());
    }

    @Test
    @DisplayName("Test 6 : Élément BUTTON")
    void testButtonElement() {
        LabeledElementConfiguration element = new LabeledElementConfiguration(
                "Recommencer",
                "restart_button",
                LabeledElementKind.BUTTON
        );

        assertEquals("Recommencer", element.label());
        assertEquals("restart_button", element.id());
        assertEquals(LabeledElementKind.BUTTON, element.kind());
    }

    @Test
    @DisplayName("Test 7 : Configuration liste vide")
    void testConfigurationWithEmptyList() {
        List<LabeledElementConfiguration> emptyList = new ArrayList<>();

        BoardGameConfiguration config = new BoardGameConfiguration(
                "Test",
                new BoardGameDimensions(8, 8),
                emptyList
        );

        assertNotNull(config);
        assertEquals(0, config.labeledElementConfigurations().size());
    }

    @Test
    @DisplayName("Test 8 : Différentes tailles de plateau")
    void testDifferentBoardSizes() {
        BoardGameDimensions small = new BoardGameDimensions(4, 4);
        assertEquals(4, small.rowCount());
        assertEquals(4, small.columnCount());

        BoardGameDimensions standard = new BoardGameDimensions(8, 8);
        assertEquals(8, standard.rowCount());
        assertEquals(8, standard.columnCount());

        BoardGameDimensions large = new BoardGameDimensions(10, 10);
        assertEquals(10, large.rowCount());
        assertEquals(10, large.columnCount());
    }

    @Test
    @DisplayName("Test 9 : Plusieurs boutons")
    void testConfigurationWithMultipleButtons() {
        List<LabeledElementConfiguration> elements = new ArrayList<>();
        elements.add(new LabeledElementConfiguration("Recommencer", "restart", LabeledElementKind.BUTTON));
        elements.add(new LabeledElementConfiguration("Annuler", "undo", LabeledElementKind.BUTTON));
        elements.add(new LabeledElementConfiguration("Quitter", "quit", LabeledElementKind.BUTTON));

        BoardGameConfiguration config = new BoardGameConfiguration(
                "Test",
                new BoardGameDimensions(8, 8),
                elements
        );

        assertEquals(3, config.labeledElementConfigurations().size());
        long buttonCount = config.labeledElementConfigurations().stream()
                .filter(e -> e.kind() == LabeledElementKind.BUTTON)
                .count();
        assertEquals(3, buttonCount);
    }

    @Test
    @DisplayName("Test 10 : Plusieurs labels")
    void testConfigurationWithMultipleLabels() {
        List<LabeledElementConfiguration> elements = new ArrayList<>();
        elements.add(new LabeledElementConfiguration("Joueur 1", "player1", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Score: 0", "score", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Tour: 1", "turn", LabeledElementKind.TEXT));

        BoardGameConfiguration config = new BoardGameConfiguration(
                "Test",
                new BoardGameDimensions(8, 8),
                elements
        );

        long textCount = config.labeledElementConfigurations().stream()
                .filter(e -> e.kind() == LabeledElementKind.TEXT)
                .count();
        assertEquals(3, textCount);
    }

    @Test
    @DisplayName("Test 11 : Ordre des éléments")
    void testElementOrder() {
        List<LabeledElementConfiguration> elements = new ArrayList<>();
        elements.add(new LabeledElementConfiguration("Premier", "first", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Deuxième", "second", LabeledElementKind.TEXT));
        elements.add(new LabeledElementConfiguration("Troisième", "third", LabeledElementKind.TEXT));

        BoardGameConfiguration config = new BoardGameConfiguration(
                "Test",
                new BoardGameDimensions(8, 8),
                elements
        );

        List<LabeledElementConfiguration> configElements = config.labeledElementConfigurations();
        assertEquals("first", configElements.get(0).id());
        assertEquals("second", configElements.get(1).id());
        assertEquals("third", configElements.get(2).id());
    }

    @Test
    @DisplayName("Test 12 : Titre configuration")
    void testConfigurationTitle() {
        BoardGameConfiguration config1 = new BoardGameConfiguration(
                "OTHELLO", new BoardGameDimensions(8, 8), new ArrayList<>()
        );
        BoardGameConfiguration config2 = new BoardGameConfiguration(
                "Mon Jeu", new BoardGameDimensions(8, 8), new ArrayList<>()
        );

        assertEquals("OTHELLO", config1.title());
        assertEquals("Mon Jeu", config2.title());
    }

    private boolean hasElementWithId(BoardGameConfiguration config, String id) {
        return config.labeledElementConfigurations().stream()
                .anyMatch(e -> e.id().equals(id));
    }
}