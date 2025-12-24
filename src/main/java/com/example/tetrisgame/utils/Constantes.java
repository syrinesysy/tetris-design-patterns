package com.example.tetrisgame.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class Constantes {

    // ========================================
    // 🎮 CONFIGURATION DE LA TAILLE (Change ici!)
    // ========================================

    // PETIT (pour petits écrans) - 800x600
    // public static final int TAILLE_BLOC = 25;
    // public static final int LIGNES = 16;

    // MOYEN (recommandé) - 900x700
    public static final int TAILLE_BLOC = 28;
    public static final int LIGNES = 18;

    // GRAND (grands écrans) - 1000x800
    // public static final int TAILLE_BLOC = 32;
    // public static final int LIGNES = 20;

    public static final int COLONNES = 10;
    public static final int LARGEUR_GRILLE = COLONNES * TAILLE_BLOC;
    public static final int HAUTEUR_GRILLE = LIGNES * TAILLE_BLOC;

    // Couleurs pastel douces
    public static class Theme {
        public static final Color PASTEL_CYAN = Color.rgb(174, 234, 255);
        public static final Color PASTEL_YELLOW = Color.rgb(255, 251, 150);
        public static final Color PASTEL_PURPLE = Color.rgb(216, 191, 255);
        public static final Color PASTEL_GREEN = Color.rgb(183, 255, 212);
        public static final Color PASTEL_RED = Color.rgb(255, 183, 197);
        public static final Color PASTEL_BLUE = Color.rgb(159, 206, 255);
        public static final Color PASTEL_ORANGE = Color.rgb(255, 214, 165);

        public static Color getCouleurPastel(String couleur) {
            switch (couleur.toUpperCase()) {
                case "CYAN": return PASTEL_CYAN;
                case "JAUNE": return PASTEL_YELLOW;
                case "VIOLET": return PASTEL_PURPLE;
                case "VERT": return PASTEL_GREEN;
                case "ROUGE": return PASTEL_RED;
                case "BLEU": return PASTEL_BLUE;
                case "ORANGE": return PASTEL_ORANGE;
                default: return Color.LIGHTGRAY;
            }
        }

        public static Color getOmbreDouce(String couleur) {
            Color base = getCouleurPastel(couleur);
            return base.darker();
        }
    }

    // Scores
    public static final int POINTS_1_LIGNE = 100;
    public static final int POINTS_2_LIGNES = 300;
    public static final int POINTS_3_LIGNES = 500;
    public static final int POINTS_4_LIGNES = 800;

    // Vitesses
    public static final long VITESSE_INITIALE = 1000;
    public static final long VITESSE_RAPIDE = 50;

    // Particules
    public static final int PARTICULES_PAR_LIGNE = 15;
    public static final double VITESSE_PARTICULE = 2.0;
    public static final int DUREE_VIE_PARTICULE = 50;

    // Effets
    public static final int DUREE_FLASH = 8;
    public static final int DUREE_SHAKE = 10;
    public static final double INTENSITE_SHAKE = 3.0;

    // High Score
    public static final String FICHIER_HIGH_SCORE = "highscore.txt";
}