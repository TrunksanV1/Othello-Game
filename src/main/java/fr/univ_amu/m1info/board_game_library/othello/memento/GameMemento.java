package fr.univ_amu.m1info.board_game_library.othello.memento;

/**
 * Pattern Memento : Sauvegarde l'état d'un objet sans violer l'encapsulation.
 *
 * Cette classe capture l'état complet du jeu à un instant T,
 * permettant de revenir en arrière (fonction "Annuler").
 *
 * Avantages:
 * - Permet d'implémenter Undo/Redo
 * - Préserve l'encapsulation (l'état est opaque)
 * - Facilite la sauvegarde d'états intermédiaires
 */
public class GameMemento {

    private final String boardState;
    private final String currentPlayer;
    private final long startTime;
    private final boolean gameOver;
    private final int consecutivePasses;

    /**
     * Crée un memento capturant l'état complet du jeu.
     *
     * @param boardState L'état du plateau (chaîne de 64 caractères)
     * @param currentPlayer Le joueur actuel ("B" ou "W")
     * @param startTime Le timestamp de début de partie
     * @param gameOver Indique si la partie est terminée
     * @param consecutivePasses Le nombre de passes consécutifs
     */
    public GameMemento(String boardState, String currentPlayer, long startTime,
                       boolean gameOver, int consecutivePasses) {
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.startTime = startTime;
        this.gameOver = gameOver;
        this.consecutivePasses = consecutivePasses;
    }

    // Getters (public pour permettre l'accès au controller)

    public String getBoardState() {
        return boardState;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getConsecutivePasses() {
        return consecutivePasses;
    }

    @Override
    public String toString() {
        return "GameMemento{" +
                "currentPlayer='" + currentPlayer + '\'' +
                ", gameOver=" + gameOver +
                ", consecutivePasses=" + consecutivePasses +
                '}';
    }
}
