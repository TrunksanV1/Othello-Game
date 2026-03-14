package fr.univ_amu.m1info.board_game_library.graphics.javafx.bar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


import java.util.HashMap;
import java.util.Map;

public class Bar extends HBox {
    private final Map<String, Labeled> labeledElements = new HashMap<>();
    private final Map<String, Button> buttons = new HashMap<>();

    public Bar() {
        super();
        setSpacing(12);
        setMinHeight(60);
        setPrefHeight(60);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(12, 15, 12, 15));
        setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e); " +
                "-fx-border-color: #1a252f; " +
                "-fx-border-width: 0 0 2 0;");
    }

    public synchronized void addLabel(String id, String initialText){
        if(labeledElements.containsKey(id)){
            return;
        }
        Label label = new Label(initialText);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        label.setStyle("-fx-text-fill: white; " +
                "-fx-padding: 6 10 6 10; " +
                "-fx-background-color: rgba(0, 0, 0, 0.2); " +
                "-fx-background-radius: 5;");
        label.setMaxHeight(36);
        label.setMinHeight(36);
        labeledElements.put(id, label);
        this.getChildren().add(label);
    }

    public synchronized void removeLabeledElement(String id){
        if(labeledElements.containsKey(id)){
            Labeled labeled = labeledElements.get(id);
            getChildren().remove(labeled);
            labeledElements.remove(id);
            buttons.remove(id);
        }
    }

    public void setButtonAction(String id, ButtonActionOnClick buttonActionOnClick){
        if(!buttons.containsKey(id)){
            throw new IllegalArgumentException("Button " + id + " does not exist");
        }
        buttons.get(id).setOnAction(event -> buttonActionOnClick.onClick());

    }

    public synchronized void addButton(String id, String label){
        Button button = new Button(label);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        button.setMinHeight(36);
        button.setMaxHeight(36);
        button.setPrefHeight(36);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 6 12 6 12; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 1);");

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: linear-gradient(to bottom, #5dade2, #3498db); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 6 12 6 12; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 2);")
        );

        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 6 12 6 12; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 1);")
        );

        labeledElements.put(id, button);
        buttons.put(id, button);
        this.getChildren().add(button);
    }

    public void updateLabel(String id, String newText){
        if(labeledElements.containsKey(id)){
            labeledElements.get(id).setText(newText);
        }
    }
}
