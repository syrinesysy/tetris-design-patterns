package com.example.tetrisgame.composite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE PATTERN - Feuille
 * Représente un bloc simple (carré) de 30x30 pixels
 */
public class Bloc implements ComposantTetris {

    protected int x;
    protected int y;
    protected String couleur;
    protected boolean estSpecial;

    public static final int TAILLE = 30; // Taille d'un bloc en pixels

    public Bloc(int x, int y, String couleur) {
        this.x = x;
        this.y = y;
        this.couleur = couleur;
        this.estSpecial = false;
    }

    @Override
    public void deplacer(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Pivoter un bloc autour d'un point pivot
     */
    public void pivoter(int pivotX, int pivotY) {
        int relX = x - pivotX;
        int relY = y - pivotY;

        // Rotation de 90° dans le sens horaire
        int nouveauX = pivotX - relY;
        int nouveauY = pivotY + relX;

        x = nouveauX;
        y = nouveauY;
    }

    @Override
    public void pivoter() {
        // Un bloc seul ne pivote pas
    }

    @Override
    public void afficher(GraphicsContext gc) {
        Color couleurJavaFX = obtenirCouleurJavaFX();

        // Dessiner le bloc rempli
        gc.setFill(couleurJavaFX);
        gc.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);

        // Dessiner le contour noir
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
    }

    @Override
    public List<int[]> obtenirPositions() {
        List<int[]> positions = new ArrayList<>();
        positions.add(new int[]{x, y});
        return positions;
    }

    @Override
    public String obtenirCouleur() {
        return couleur;
    }

    /**
     * Convertit la couleur String en Color JavaFX
     */
    public Color obtenirCouleurJavaFX() {
        switch (couleur.toUpperCase()) {
            case "CYAN": return Color.CYAN;
            case "JAUNE": return Color.YELLOW;
            case "VIOLET": return Color.PURPLE;
            case "VERT": return Color.GREEN;
            case "ROUGE": return Color.RED;
            case "BLEU": return Color.BLUE;
            case "ORANGE": return Color.ORANGE;
            default: return Color.GRAY;
        }
    }

    // Getters et Setters
    public int obtenirX() { return x; }
    public int obtenirY() { return y; }
    public boolean estSpecial() { return estSpecial; }

    public void definirPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void definirSpecial(boolean special) {
        this.estSpecial = special;
    }
}