package com.example.tetrisgame.core;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * SINGLETON PATTERN - Logger unique
 * Trace tous les événements du jeu dans un fichier log
 */
public class JournalJeu {

    private static JournalJeu instance;
    private Logger logger;
    private String cheminFichier;
    private DateTimeFormatter formatDate;

    /**
     * Constructeur privé (Singleton)
     */
    private JournalJeu() {
        this.cheminFichier = "logs/tetris.log";
        this.formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.logger = Logger.getLogger("TetrisLogger");

        configurer();
    }

    /**
     * Obtenir l'instance unique (Singleton)
     */
    public static JournalJeu obtenirInstance() {
        if (instance == null) {
            instance = new JournalJeu();
        }
        return instance;
    }

    /**
     * Configure le logger
     */
    private void configurer() {
        try {
            // Créer le dossier logs s'il n'existe pas
            java.io.File dossierLogs = new java.io.File("logs");
            if (!dossierLogs.exists()) {
                dossierLogs.mkdir();
            }

            // Handler pour écrire dans le fichier
            FileHandler fileHandler = new FileHandler(cheminFichier, true);
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    return record.getMessage() + "\n";
                }
            });

            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Ne pas afficher dans la console
            logger.setLevel(Level.ALL);

            // Message de démarrage
            journaliserEvenement("=== Nouvelle session de jeu ===");

        } catch (IOException e) {
            System.err.println("Erreur lors de la configuration du logger : " + e.getMessage());
        }
    }

    /**
     * Obtenir le timestamp actuel
     */
    private String obtenirTimestamp() {
        return LocalDateTime.now().format(formatDate);
    }

    /**
     * Journaliser un changement d'état
     */
    public void journaliserChangementEtat(String ancienEtat, String nouvelEtat) {
        String message = String.format("[%s] [STATE] %s -> %s",
                obtenirTimestamp(), ancienEtat, nouvelEtat);
        logger.info(message);
    }

    /**
     * Journaliser l'application d'un décorateur
     */
    public void journaliserDecorateurApplique(String decorateur, String cible) {
        String message = String.format("[%s] [DECORATOR] %s applied to %s",
                obtenirTimestamp(), decorateur, cible);
        logger.info(message);
    }

    /**
     * Journaliser la création d'une pièce
     */
    public void journaliserPieceCreee(String typePiece) {
        String message = String.format("[%s] [FACTORY] %s created",
                obtenirTimestamp(), typePiece);
        logger.info(message);
    }

    /**
     * Journaliser des lignes effacées
     */
    public void journaliserLignesEffacees(int lignes) {
        String message = String.format("[%s] [GAME] %d line(s) cleared",
                obtenirTimestamp(), lignes);
        logger.info(message);
    }

    /**
     * Journaliser un événement général
     */
    public void journaliserEvenement(String evenement) {
        String message = String.format("[%s] [INFO] %s",
                obtenirTimestamp(), evenement);
        logger.info(message);
    }

    /**
     * Journaliser le score
     */
    public void journaliserScore(int score) {
        String message = String.format("[%s] [SCORE] Final score: %d",
                obtenirTimestamp(), score);
        logger.info(message);
    }

    public String getCheminFichier() {
        return cheminFichier;
    }
}