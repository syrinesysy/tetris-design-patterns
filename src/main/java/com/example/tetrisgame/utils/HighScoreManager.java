package com.example.tetrisgame.utils;

import java.io.*;

/**
 * SINGLETON - Gestion du High Score
 */
public class HighScoreManager {

    private static HighScoreManager instance;
    private int highScore;
    private String cheminFichier;

    private HighScoreManager() {
        this.cheminFichier = Constantes.FICHIER_HIGH_SCORE;
        chargerHighScore();
    }

    public static HighScoreManager obtenirInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    /**
     * Charge le high score depuis le fichier
     */
    private void chargerHighScore() {
        try {
            File fichier = new File(cheminFichier);
            if (fichier.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(fichier));
                String ligne = reader.readLine();
                if (ligne != null) {
                    highScore = Integer.parseInt(ligne);
                }
                reader.close();
            } else {
                highScore = 0;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du high score: " + e.getMessage());
            highScore = 0;
        }
    }

    /**
     * Sauvegarde le high score dans le fichier
     */
    private void sauvegarderHighScore() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier));
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde du high score: " + e.getMessage());
        }
    }

    /**
     * Vérifie et met à jour le high score
     */
    public boolean verifierEtMettreAJour(int score) {
        if (score > highScore) {
            highScore = score;
            sauvegarderHighScore();
            return true; // Nouveau record !
        }
        return false;
    }

    public int getHighScore() {
        return highScore;
    }
}