package fr.univ_amu.m1info.board_game_library.othello;

import fr.univ_amu.m1info.board_game_library.graphics.BoardGameView;
import fr.univ_amu.m1info.board_game_library.graphics.Color;
import fr.univ_amu.m1info.board_game_library.graphics.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Tests du contrôleur Othello")
class OthelloControllerTest {

    @Mock
    private BoardGameView mockView;

    private OthelloController controllerPVP;
    private OthelloController controllerPVE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controllerPVP = new OthelloController("PVP", "Alice", "Bob", "Facile");
        controllerPVE = new OthelloController("PVE", "Alice", "IA", "Moyen");
    }

    @Test
    @DisplayName("Test 1 : Initialisation vue PVP")
    void testInitializeViewPVP() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView, times(64)).setCellColor(anyInt(), anyInt(), eq(Color.DARKGREEN));
        verify(mockView, times(4)).addShapeAtCell(anyInt(), anyInt(), eq(Shape.CIRCLE), any(Color.class));
    }

    @Test
    @DisplayName("Test 2 : Initialisation vue PVE")
    void testInitializeViewPVE() {
        controllerPVE.initializeViewOnStart(mockView);

        verify(mockView, times(64)).setCellColor(anyInt(), anyInt(), eq(Color.DARKGREEN));
        verify(mockView, times(4)).addShapeAtCell(anyInt(), anyInt(), eq(Shape.CIRCLE), any(Color.class));
    }

    @Test
    @DisplayName("Test 3 : Pion initial position (3,3) Blanc")
    void testInitialPieceAt33() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView).addShapeAtCell(3, 3, Shape.CIRCLE, Color.WHITE);
    }

    @Test
    @DisplayName("Test 4 : Pion initial position (3,4) Noir")
    void testInitialPieceAt34() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView).addShapeAtCell(3, 4, Shape.CIRCLE, Color.BLACK);
    }

    @Test
    @DisplayName("Test 5 : Pion initial position (4,3) Noir")
    void testInitialPieceAt43() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView).addShapeAtCell(4, 3, Shape.CIRCLE, Color.BLACK);
    }

    @Test
    @DisplayName("Test 6 : Pion initial position (4,4) Blanc")
    void testInitialPieceAt44() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView).addShapeAtCell(4, 4, Shape.CIRCLE, Color.WHITE);
    }

    @Test
    @DisplayName("Test 7 : Couleur du plateau")
    void testBoardColorInitialization() {
        controllerPVP.initializeViewOnStart(mockView);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                verify(mockView).setCellColor(row, col, Color.DARKGREEN);
            }
        }
    }

    @Test
    @DisplayName("Test 9 : Mode PVP")
    void testPVPMode() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView, times(64)).setCellColor(anyInt(), anyInt(), eq(Color.DARKGREEN));
    }

    @Test
    @DisplayName("Test 10 : Mode PVE")
    void testPVEMode() {
        controllerPVE.initializeViewOnStart(mockView);

        verify(mockView, times(64)).setCellColor(anyInt(), anyInt(), eq(Color.DARKGREEN));
    }

    @Test
    @DisplayName("Test 11 : Score initial")
    void testInitialScore() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView, times(4)).addShapeAtCell(anyInt(), anyInt(), eq(Shape.CIRCLE), any(Color.class));
        verify(mockView, times(2)).addShapeAtCell(anyInt(), anyInt(), eq(Shape.CIRCLE), eq(Color.WHITE));
        verify(mockView, times(2)).addShapeAtCell(anyInt(), anyInt(), eq(Shape.CIRCLE), eq(Color.BLACK));
    }

    @Test
    @DisplayName("Test 12 : Clic sur case")
    void testBoardActionOnClick() {
        controllerPVP.initializeViewOnStart(mockView);
        reset(mockView);

        controllerPVP.boardActionOnClick(2, 3);

        verify(mockView, atLeastOnce()).addShapeAtCell(eq(2), eq(3), eq(Shape.CIRCLE), any(Color.class));
    }

    @Test
    @DisplayName("Test 14 : Dimensions 8x8")
    void testBoardDimensions() {
        controllerPVP.initializeViewOnStart(mockView);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                verify(mockView).setCellColor(row, col, Color.DARKGREEN);
            }
        }

        verify(mockView, never()).setCellColor(eq(8), anyInt(), any());
        verify(mockView, never()).setCellColor(anyInt(), eq(8), any());
    }

    @Test
    @DisplayName("Test 15 : Forme des pions")
    void testPieceShape() {
        controllerPVP.initializeViewOnStart(mockView);

        verify(mockView, times(4)).addShapeAtCell(anyInt(), anyInt(), eq(Shape.CIRCLE), any(Color.class));
        verify(mockView, never()).addShapeAtCell(anyInt(), anyInt(), eq(Shape.SQUARE), any(Color.class));
        verify(mockView, never()).addShapeAtCell(anyInt(), anyInt(), eq(Shape.DIAMOND), any(Color.class));
    }
}