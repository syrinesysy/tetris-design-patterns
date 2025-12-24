package com.example.tetrisgame.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Particule pour les effets visuels d'explosion
 */
public class Particule {

    private double x, y;
    private double vx, vy;
    private Color couleur;
    private int vieRestante;
    private double taille;
    private double rotation;
    private double vitesseRotation;

    public Particule(double x, double y, Color couleur) {
        this.x = x;
        this.y = y;
        this.couleur = couleur;
        this.vieRestante = Constantes.DUREE_VIE_PARTICULE;
        this.taille = Math.random() * 6 + 3;

        // Vitesse aléatoire dans toutes les directions
        double angle = Math.random() * Math.PI * 2;
        double vitesse = Math.random() * Constantes.VITESSE_PARTICULE + 1;
        this.vx = Math.cos(angle) * vitesse;
        this.vy = Math.sin(angle) * vitesse - 2; // Tendance vers le haut

        this.rotation = Math.random() * 360;
        this.vitesseRotation = (Math.random() - 0.5) * 10;
    }

    /**
     * Met à jour la particule
     */
    public void mettreAJour() {
        x += vx;
        y += vy;
        vy += 0.2; // Gravité
        vieRestante--;
        rotation += vitesseRotation;

        // Réduire la vitesse
        vx *= 0.98;
        vy *= 0.98;
    }

    /**
     * Affiche la particule
     */
    public void afficher(GraphicsContext gc) {
        double opacite = (double) vieRestante / Constantes.DUREE_VIE_PARTICULE;

        gc.save();
        gc.translate(x, y);
        gc.rotate(rotation);

        // Effet de glow
        gc.setGlobalAlpha(opacite * 0.5);
        gc.setFill(couleur.brighter());
        gc.fillOval(-taille * 1.5, -taille * 1.5, taille * 3, taille * 3);

        // Particule centrale
        gc.setGlobalAlpha(opacite);
        gc.setFill(couleur);
        gc.fillOval(-taille / 2, -taille / 2, taille, taille);

        gc.restore();
    }

    /**
     * Vérifie si la particule est morte
     */
    public boolean estMorte() {
        return vieRestante <= 0;
    }
}