package fr.univ_amu.m1info.board_game_library.graphics;

import fr.univ_amu.m1info.board_game_library.graphics.configuration.BoardGameConfiguration;
import fr.univ_amu.m1info.board_game_library.graphics.javafx.app.JavaFXBoardGameApplication;
import fr.univ_amu.m1info.board_game_library.graphics.javafx.view.BoardGameConfigurator;
import fr.univ_amu.m1info.board_game_library.graphics.javafx.view.BoardGameControllableView;
import fr.univ_amu.m1info.board_game_library.graphics.javafx.view.JavaFXBoardGameViewBuilder;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Singleton class responsible for launching a board game application using JavaFX.
 * It implements the {@link BoardGameApplicationLauncher} interface and manages the configuration, controller,
 * and view initializer for the game.
 */
public class JavaFXBoardGameApplicationLauncher implements BoardGameApplicationLauncher {

    /** The singleton instance of the launcher. */
    private static JavaFXBoardGameApplicationLauncher instance = null;

    /** The configuration of the board game. */
    private BoardGameConfiguration configuration;

    /** The controller that manages game interactions. */
    private BoardGameController controller;

    /** The stage used for the game. */
    private Stage stage;

    /** Private constructor to prevent direct instantiation. */
    private JavaFXBoardGameApplicationLauncher() {}

    /**
     * Retrieves the singleton instance of the {@code JavaFXBoardGameApplicationLauncher}.
     * If the instance does not already exist, it is created in a thread-safe manner.
     *
     * @return the singleton instance of the launcher.
     */
    public static JavaFXBoardGameApplicationLauncher getInstance() {
        JavaFXBoardGameApplicationLauncher result = instance;
        if (result != null) {
            return result;
        }
        synchronized(JavaFXBoardGameApplicationLauncher.class) {
            if (instance == null) {
                instance = new JavaFXBoardGameApplicationLauncher();
            }
            return instance;
        }
    }

    /**
     * Retrieves the current board game configuration.
     *
     * @return the {@link BoardGameConfiguration} for the game.
     */
    public BoardGameConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Retrieves the current controller managing the game.
     *
     * @return the {@link BoardGameController} for the game.
     */
    public BoardGameController getController() {
        return controller;
    }

    /**
     * Sets the stage to be used for the game.
     *
     * @param stage the JavaFX stage
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Retrieves the current stage used for the game.
     *
     * @return the JavaFX stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Launches the JavaFX board game application with the specified configuration and controller.
     * This method sets the internal fields and starts the JavaFX application.
     *
     * @param configuration the configuration of the board game, represented by {@link BoardGameConfiguration}.
     * @param controller    the controller that manages game interactions, implemented by {@link BoardGameController}.
     */
    @Override
    public void launchApplication(BoardGameConfiguration configuration,
                                  BoardGameController controller) {
        this.configuration = configuration;
        this.controller = controller;

        // Si un stage existe déjà, on l'utilise pour afficher le jeu
        if (stage != null) {
            final JavaFXBoardGameViewBuilder viewBuilder = new JavaFXBoardGameViewBuilder(stage);
            new BoardGameConfigurator().configure(viewBuilder, configuration);
            BoardGameControllableView view = viewBuilder.getView();
            view.setController(controller);
            controller.initializeViewOnStart(view);
            stage.show();
        } else {
            // Sinon, on lance une nouvelle application
            Application.launch(JavaFXBoardGameApplication.class);
        }
    }
}