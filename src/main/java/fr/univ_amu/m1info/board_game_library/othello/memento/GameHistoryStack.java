package fr.univ_amu.m1info.board_game_library.othello.memento;

import java.util.Stack;

/**
 * Gestionnaire de l'historique des états du jeu (Caretaker du pattern Memento).
 *
 * Permet d'implémenter les fonctionnalités Undo/Redo en conservant
 * une pile des états précédents.
 *
 * Utilisation:
 * - Avant chaque coup: sauvegarder l'état actuel
 * - Annuler: restaurer l'état précédent
 * - Refaire: avancer dans l'historique
 */
public class GameHistoryStack {

    private final Stack<GameMemento> undoStack;
    private final Stack<GameMemento> redoStack;
    private final int maxHistorySize;

    /**
     * Crée un gestionnaire d'historique.
     *
     * @param maxHistorySize Nombre maximum d'états conservés (défaut: 50)
     */
    public GameHistoryStack(int maxHistorySize) {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.maxHistorySize = maxHistorySize;
    }

    /**
     * Crée un gestionnaire d'historique avec taille par défaut.
     */
    public GameHistoryStack() {
        this(50);
    }

    /**
     * Sauvegarde l'état actuel du jeu.
     *
     * Cette méthode doit être appelée AVANT chaque coup.
     *
     * @param memento Le memento à sauvegarder
     */
    public void saveState(GameMemento memento) {
        // Limiter la taille de l'historique
        if (undoStack.size() >= maxHistorySize) {
            undoStack.remove(0); // Supprimer le plus ancien
        }

        undoStack.push(memento);

        // Un nouveau coup invalide l'historique de redo
        redoStack.clear();
    }

    /**
     * Annule le dernier coup et retourne l'état précédent.
     *
     * @return Le memento de l'état précédent, ou null si impossible
     */
    public GameMemento undo() {
        if (undoStack.isEmpty()) {
            return null;
        }

        // Sauvegarder l'état actuel dans la pile redo
        GameMemento current = undoStack.pop();
        redoStack.push(current);

        // Retourner l'état précédent (sans le retirer de la pile)
        return undoStack.isEmpty() ? null : undoStack.peek();
    }

    /**
     * Refait le dernier coup annulé.
     *
     * @return Le memento de l'état suivant, ou null si impossible
     */
    public GameMemento redo() {
        if (redoStack.isEmpty()) {
            return null;
        }

        GameMemento memento = redoStack.pop();
        undoStack.push(memento);
        return memento;
    }

    /**
     * Indique si on peut annuler un coup.
     *
     * @return true si un undo est possible
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Indique si on peut refaire un coup.
     *
     * @return true si un redo est possible
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Vide tout l'historique.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * Retourne le nombre d'états dans l'historique undo.
     *
     * @return Le nombre d'états disponibles pour undo
     */
    public int getUndoSize() {
        return undoStack.size();
    }

    /**
     * Retourne le nombre d'états dans l'historique redo.
     *
     * @return Le nombre d'états disponibles pour redo
     */
    public int getRedoSize() {
        return redoStack.size();
    }

    @Override
    public String toString() {
        return "GameHistoryStack{" +
                "undoSize=" + undoStack.size() +
                ", redoSize=" + redoStack.size() +
                '}';
    }
}
