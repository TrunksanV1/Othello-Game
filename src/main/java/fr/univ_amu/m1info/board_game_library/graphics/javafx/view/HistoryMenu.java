package fr.univ_amu.m1info.board_game_library.graphics.javafx.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryMenu extends Application {

    private static final String RESOURCE_PATH = "/data/history.json";
    private static final int MAX_BYTES = 200 * 1024;

    @Override
    public void start(Stage stage) {
        Image backgroundImage = new Image(getClass().getResource("/images/WindowBackground.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setAlignment(Pos.CENTER);

        Text title = new Text("OTHELLO");
        title.setFont(Font.font("Georgia", 65));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 15, 0, 3, 3);");

        Text subtitle = new Text("📜 Historique des parties");
        subtitle.setFont(Font.font("Arial", 24));
        subtitle.setFill(Color.rgb(220, 220, 220));

        VBox titleBox = new VBox(8, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        Button back = new Button("← Retour au menu");
        back.setOnAction(e -> {
            try {
                new OthelloMainMenu().start(stage);
            } catch (Exception ex) {
                stage.close();
            }
        });

        for (Button b : new Button[]{back}) {
            b.setFont(Font.font("Arial", FontWeight.BOLD, 17));
            b.setPrefWidth(220);
            b.setPrefHeight(48);
            b.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255, 255, 255, 0.9), rgba(240, 240, 240, 0.9)); " +
                            "-fx-text-fill: black; " +
                            "-fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2);"
            );
            b.setOnMouseEntered(e -> b.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255,255,255,1), rgba(255,255,255,0.95)); " +
                            "-fx-text-fill: #1a4d2e; " +
                            "-fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0, 0, 2);"
            ));
            b.setOnMouseExited(e -> b.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255, 255, 255, 0.9), rgba(240, 240, 240, 0.9)); " +
                            "-fx-text-fill: black; " +
                            "-fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2);"
            ));
        }

        HBox topButtons = new HBox(10, back);
        topButtons.setAlignment(Pos.CENTER);

        VBox entriesBox = new VBox(12);
        entriesBox.setPadding(new Insets(10));
        entriesBox.setAlignment(Pos.TOP_CENTER);

        List<HistoryEntry> history = loadHistory();

        int index = 1;
        for (HistoryEntry entry : history) {
            HBox row = createEntryRow(index++, entry);
            entriesBox.getChildren().add(row);
        }

        if (history.isEmpty()) {
            Label empty = new Label("Aucune partie sauvegardée.");
            empty.setTextFill(Color.WHITE);
            empty.setFont(Font.font("Arial", 18));
            entriesBox.getChildren().add(empty);
        }

        ScrollPane scroll = new ScrollPane(entriesBox);
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(360);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox menuBox = new VBox(20, titleBox, topButtons, scroll);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(25));
        menuBox.setMaxWidth(750);

        overlay.getChildren().add(menuBox);

        StackPane root = new StackPane();
        root.setBackground(new Background(background));
        root.getChildren().add(overlay);

        Scene scene = new Scene(root, 950, 650);
        stage.setScene(scene);
        stage.setTitle("Othello - Historique");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private HBox createEntryRow(int number, HistoryEntry entry) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.10); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: rgba(255,255,255,0.15); " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 1; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 1);"
        );
        row.setMaxWidth(700);

        Label num = new Label(String.format(Locale.FRANCE, "%d.", number));
        num.setTextFill(Color.WHITE);
        num.setFont(Font.font("Arial", 18));
        num.setPrefWidth(40);

        Label winner = new Label("Gagnant : " + entry.getWinner());
        winner.setTextFill(Color.WHITE);
        winner.setFont(Font.font("Arial", 16));
        winner.setPrefWidth(220);

        Label duration = new Label("Durée : " + formatDuration(entry.getDurationSeconds()));
        duration.setTextFill(Color.WHITE);
        duration.setFont(Font.font("Arial", 16));
        duration.setPrefWidth(160);

        Label date = new Label("Date : " + entry.getDate());
        date.setTextFill(Color.WHITE);
        date.setFont(Font.font("Arial", 14));
        date.setPrefWidth(200);

        row.getChildren().addAll(num, winner, duration, date);
        return row;
    }

    private String formatDuration(int seconds) {
        Duration d = Duration.ofSeconds(seconds);
        long mins = d.toMinutes();
        long secs = d.minusMinutes(mins).getSeconds();
        return String.format("%02d:%02d", mins, secs);
    }

    private List<HistoryEntry> loadHistory() {
        try {
            String historyPath = System.getProperty("user.home") + "/.othello/history.json";
            File historyFile = new File(historyPath);
            if (historyFile.exists()) {
                String s = readLimitedFile(historyFile);
                return parseJsonList(s);
            }
        } catch (Exception e) {
            System.err.println("Failed to read history file: " + e.getMessage());
        }

        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is != null) {
                String s = readLimitedStream(is);
                return parseJsonList(s);
            }
        } catch (Exception e) {
            System.err.println("Failed to read resource history: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private String readLimitedStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int read;
        int total = 0;
        while ((read = is.read(buf)) != -1) {
            total += read;
            if (total > MAX_BYTES) throw new IOException("File too large");
            baos.write(buf, 0, read);
        }
        return baos.toString(StandardCharsets.UTF_8.name());
    }

    private String readLimitedFile(File f) throws IOException {
        try (InputStream is = new FileInputStream(f)) {
            return readLimitedStream(is);
        }
    }

    private List<HistoryEntry> parseJsonList(String json) {
        List<HistoryEntry> list = new ArrayList<>();
        Pattern objPattern = Pattern.compile("\\{([^}]*)\\}", Pattern.DOTALL);
        Matcher m = objPattern.matcher(json);
        int id = 1;
        while (m.find()) {
            String obj = m.group(1);
            String winner = matchString(obj, "\"winner\"\\s*:\\s*\"([^\"]*)\"");
            Integer dur = matchInt(obj, "\"durationSeconds\"\\s*:\\s*(\\d+)");
            String date = matchString(obj, "\"date\"\\s*:\\s*\"([^\"]*)\"");
            if (winner != null && dur != null && date != null) {
                list.add(new HistoryEntry(id++, winner, dur, date));
            }
        }
        return list;
    }

    private Integer matchInt(String source, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String matchString(String source, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private List<HistoryEntry> sampleData() {
        List<HistoryEntry> dummy = new ArrayList<>();
        dummy.add(new HistoryEntry(1, "Dummy", 420, "2025-01-05 19:12"));
        dummy.add(new HistoryEntry(2, "Dummy", 315, "2025-01-12 20:03"));
        dummy.add(new HistoryEntry(3, "Dummy", 560, "2025-02-02 18:45"));
        return dummy;
    }

    public static void main(String[] args) {
        launch();
    }
}