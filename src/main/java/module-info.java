module fr.univ_amu.m1info.board_game_library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // Modules Jackson pour la sérialisation JSON
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;

    exports fr.univ_amu.m1info.board_game_library.graphics;
    exports fr.univ_amu.m1info.board_game_library;
    exports fr.univ_amu.m1info.board_game_library.graphics.javafx.app;
    exports fr.univ_amu.m1info.board_game_library.graphics.configuration;
    exports fr.univ_amu.m1info.board_game_library.graphics.javafx.view;
    exports fr.univ_amu.m1info.board_game_library.graphics.javafx.bar;
    exports fr.univ_amu.m1info.board_game_library.graphics.javafx.board;
    exports fr.univ_amu.m1info.board_game_library.othello;

    opens fr.univ_amu.m1info.board_game_library.othello to javafx.graphics;

    // Ouvrir les packages pour Jackson (sérialisation/désérialisation)
    opens fr.univ_amu.m1info.board_game_library.othello.persistence.dto to com.fasterxml.jackson.databind;
}