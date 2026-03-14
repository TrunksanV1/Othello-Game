package fr.univ_amu.m1info.board_game_library.graphics.javafx.view;

public class HistoryEntry {
    private int id;
    private String winner;
    private int durationSeconds;
    private String date;

    public HistoryEntry() {}

    public HistoryEntry(int id, String winner, int durationSeconds, String date) {
        this.id = id;
        this.winner = winner;
        this.durationSeconds = durationSeconds;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getWinner() {
        return winner;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

}