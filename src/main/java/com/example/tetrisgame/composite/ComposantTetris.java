package com.example.tetrisgame.composite;

import javafx.scene.canvas.GraphicsContext;
import java.util.List;

/**
 * COMPOSITE PATTERN - Interface
 * Interface commune pour les Blocs (feuilles) et Tetrominos (composites)
 */
public interface ComposantTetris {

    /**
     * Déplace le composant
     */
    void deplacer(int dx, int dy);

    /**
     * Fait pivoter le composant
     */
    void pivoter();

    /**
     * Affiche le composant sur le canvas
     */
    void afficher(GraphicsContext gc);

    /**
     * Obtient les positions [x, y] du composant
     */
    List<int[]> obtenirPositions();

    /**
     * Obtient la couleur du composant
     */
    String obtenirCouleur();
}