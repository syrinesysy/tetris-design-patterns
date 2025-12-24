package com.example.tetrisgame.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestion des effets visuels
 */
public class EffetsVisuels {

    private List<Particule> particules;
    private int frameFlash;
    private int frameShake;
    private double offsetShakeX;
    private double offsetShakeY;
    private double hueGradient;

    // Textes flottants
    private List<TexteFlottant> textesFlottants;

    public EffetsVisuels() {
        this.particules = new ArrayList<>();
        this.textesFlottants = new ArrayList<>();
        this.frameFlash = 0;
        this.frameShake = 0;
        this.hueGradient = 0;
    }

    /**
     * Crée une explosion de particules
     */
    public void creerExplosion(double x, double y, Color couleur, int nombre) {
        for (int i = 0; i < nombre; i++) {
            particules.add(new Particule(x, y, couleur));
        }
    }

    /**
     * Ajoute un texte flottant
     */
    public void ajouterTexteFlottant(String texte, double x, double y, Color couleur) {
        textesFlottants.add(new TexteFlottant(texte, x, y, couleur));
    }

    /**
     * Démarre un effet de flash
     */
    public void demarrerFlash() {
        frameFlash = Constantes.DUREE_FLASH;
    }

    /**
     * Démarre un effet de shake (tremblement)
     */
    public void demarrerShake() {
        frameShake = Constantes.DUREE_SHAKE;
    }

    /**
     * Met à jour tous les effets
     */
    public void mettreAJour() {
        // Mettre à jour les particules
        particules.removeIf(Particule::estMorte);
        for (Particule p : particules) {
            p.mettreAJour();
        }

        // Mettre à jour les textes flottants
        textesFlottants.removeIf(TexteFlottant::estMort);
        for (TexteFlottant t : textesFlottants) {
            t.mettreAJour();
        }

        // Décrémenter les compteurs d'effets
        if (frameFlash > 0) frameFlash--;
        if (frameShake > 0) {
            frameShake--;
            offsetShakeX = (Math.random() - 0.5) * Constantes.INTENSITE_SHAKE;
            offsetShakeY = (Math.random() - 0.5) * Constantes.INTENSITE_SHAKE;
        } else {
            offsetShakeX = 0;
            offsetShakeY = 0;
        }

        // Animer le gradient
        hueGradient = (hueGradient + 0.5) % 360;
    }

    /**
     * Affiche tous les effets
     */
    public void afficher(GraphicsContext gc) {
        // Afficher les particules
        for (Particule p : particules) {
            p.afficher(gc);
        }

        // Afficher les textes flottants
        for (TexteFlottant t : textesFlottants) {
            t.afficher(gc);
        }

        // Effet de flash blanc
        if (frameFlash > 0) {
            double opacite = (double) frameFlash / Constantes.DUREE_FLASH * 0.3;
            gc.setGlobalAlpha(opacite);
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, Constantes.LARGEUR_GRILLE, Constantes.HAUTEUR_GRILLE);
            gc.setGlobalAlpha(1.0);
        }
    }

    // Getters pour shake
    public double getOffsetShakeX() { return offsetShakeX; }
    public double getOffsetShakeY() { return offsetShakeY; }
    public double getHueGradient() { return hueGradient; }

    /**
     * Classe interne pour les textes flottants
     */
    private static class TexteFlottant {
        String texte;
        double x, y;
        Color couleur;
        int vieRestante;
        double vy;

        TexteFlottant(String texte, double x, double y, Color couleur) {
            this.texte = texte;
            this.x = x;
            this.y = y;
            this.couleur = couleur;
            this.vieRestante = 60;
            this.vy = -2;
        }

        void mettreAJour() {
            y += vy;
            vy *= 0.95;
            vieRestante--;
        }

        void afficher(GraphicsContext gc) {
            double opacite = (double) vieRestante / 60;
            gc.setGlobalAlpha(opacite);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gc.setFill(couleur);
            gc.fillText(texte, x, y);
            gc.setGlobalAlpha(1.0);
        }

        boolean estMort() {
            return vieRestante <= 0;
        }
    }
}