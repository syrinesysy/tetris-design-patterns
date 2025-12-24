package com.example.tetrisgame.composite;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE PATTERN - Composite
 * Représente une pièce Tetris composée de 4 blocs
 */
public abstract class Tetromino implements ComposantTetris {

    public List<Bloc> blocs;
    protected int pivotX;
    protected int pivotY;
    protected String couleur;

    public Tetromino(String couleur) {
        this.couleur = couleur;
        this.blocs = new ArrayList<>();
        initialiserBlocs();
    }

    /**
     * Méthode abstraite : chaque pièce définit sa forme
     */
    protected abstract void initialiserBlocs();

    @Override
    public void deplacer(int dx, int dy) {
        // Déplacer tous les blocs ensemble
        for (Bloc bloc : blocs) {
            bloc.deplacer(dx, dy);
        }
        pivotX += dx;
        pivotY += dy;
    }

    @Override
    public void pivoter() {
        // Faire pivoter tous les blocs autour du pivot
        for (Bloc bloc : blocs) {
            bloc.pivoter(pivotX, pivotY);
        }
    }

    @Override
    public void afficher(GraphicsContext gc) {
        // Afficher tous les blocs
        for (Bloc bloc : blocs) {
            bloc.afficher(gc);
        }
    }

    @Override
    public List<int[]> obtenirPositions() {
        List<int[]> positions = new ArrayList<>();
        for (Bloc bloc : blocs) {
            positions.add(new int[]{bloc.obtenirX(), bloc.obtenirY()});
        }
        return positions;
    }

    @Override
    public String obtenirCouleur() {
        return couleur;
    }

    /**
     * Obtient la liste des blocs
     */
    public List<Bloc> obtenirBlocs() {
        return blocs;
    }

    /**
     * Clone la pièce (utile pour l'aperçu "pièce suivante")
     */
    public abstract Tetromino cloner();

    /**
     * Méthode utilitaire pour définir le pivot au centre
     */
    public void definirPivot(int x, int y) {
        this.pivotX = x;
        this.pivotY = y;
    }
}