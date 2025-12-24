package com.example.tetrisgame.core;

import com.example.tetrisgame.composite.Tetromino;
import com.example.tetrisgame.factory.FabriqueAleatoire;
import com.example.tetrisgame.factory.FabriqueTetromino;
import com.example.tetrisgame.states.ContexteJeu;
import com.example.tetrisgame.states.EtatEnCours;
import com.example.tetrisgame.states.EtatGameOver;
import com.example.tetrisgame.states.EtatMenu;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

/**
 * Classe Jeu - Le moteur principal
 * Coordonne tous les éléments du jeu
 */
public class Jeu {

    private ContexteJeu contexte;
    private Grille grille;
    private Tetromino pieceActuelle;
    private Tetromino pieceSuivante;
    private FabriqueTetromino fabrique;
    private JournalJeu journal;
    private AnimationTimer timer;
    private int vitesseChute;

    private long dernierTempsChute;
    private static final long INTERVALLE_CHUTE = 1_000_000_000;

    public Jeu() {
        this.contexte = new ContexteJeu();
        this.grille = new Grille();
        this.fabrique = new FabriqueAleatoire();
        this.journal = JournalJeu.obtenirInstance();
        this.vitesseChute = 1000;
        this.dernierTempsChute = System.nanoTime();

        journal.journaliserEvenement("Jeu initialisé");
    }

    /**
     * Démarre le jeu
     */
    public void demarrer() {
        genererNouvellePiece();
        journal.journaliserEvenement("Jeu démarré");
    }

    /**
     * Arrête le jeu
     */
    public void arreter() {
        if (timer != null) {
            timer.stop();
        }
        journal.journaliserEvenement("Jeu arrêté");
    }

    /**
     * Boucle principale du jeu (appelée par AnimationTimer)
     */
    public void mettreAJour() {
        // Mettre à jour l'état actuel
        contexte.mettreAJour();

        // Si on est en cours de jeu
        if (contexte.obtenirEtat() instanceof EtatEnCours) {
            long tempsActuel = System.nanoTime();

            // Faire tomber la pièce automatiquement
            if (tempsActuel - dernierTempsChute >= INTERVALLE_CHUTE) {
                if (!deplacerPiece(0, 1)) {
                    // La pièce ne peut plus descendre
                    verrouillerPiece();
                    contexte.augmenterScore(10);

                    // Vérifier si game over
                    if (verifierFinJeu()) {
                        contexte.changerEtat(new EtatGameOver(contexte.getScore()));
                    } else {
                        genererNouvellePiece();
                    }
                }
                dernierTempsChute = tempsActuel;
            }
        }
    }

    /**
     * Gère les entrées clavier
     */
    public void gererEntree(String touche) {
        // Déléguer au contexte
        contexte.gererEntree(touche);

        // Si on revient au menu depuis GameOver, réinitialiser
        if (touche.equals("ENTREE") && contexte.obtenirEtat() instanceof EtatMenu) {
            grille.vider();
            genererNouvellePiece();
        }

        if (touche.equals("ECHAP") && contexte.obtenirEtat() instanceof EtatMenu) {
            grille.vider();
        }
        // Gestion spécifique du jeu en cours
        if (contexte.obtenirEtat() instanceof EtatEnCours && pieceActuelle != null) {
            switch (touche) {
                case "GAUCHE":
                    deplacerPiece(-1, 0);
                    break;
                case "DROITE":
                    deplacerPiece(1, 0);
                    break;
                case "BAS":
                    deplacerPiece(0, 1);
                    break;
                case "ESPACE":
                    pivoterPiece();
                    break;
                case "W":  // Hard drop
                    hardDrop();
                    break;
            }
        }
    }

    /**
     * Affiche le jeu
     */
    public void afficher(GraphicsContext gc) {
        // Afficher la grille
        grille.afficher(gc);

        // Afficher la pièce actuelle
        if (pieceActuelle != null) {
            pieceActuelle.afficher(gc);
        }
    }

    /**
     * Génère une nouvelle pièce
     */
    public void genererNouvellePiece() {
        if (pieceSuivante == null) {
            pieceSuivante = fabrique.creerTetromino();
        }

        pieceActuelle = pieceSuivante;
        pieceSuivante = fabrique.creerTetromino();
    }

    /**
     * Déplace la pièce actuelle
     */
    public boolean deplacerPiece(int dx, int dy) {
        if (pieceActuelle == null) return false;

        // Sauvegarder la position actuelle
        int ancienX = pieceActuelle.obtenirBlocs().get(0).obtenirX();
        int ancienY = pieceActuelle.obtenirBlocs().get(0).obtenirY();

        // Déplacer
        pieceActuelle.deplacer(dx, dy);

        // Vérifier collision
        if (grille.verifierCollision(pieceActuelle, 0, 0)) {
            // Annuler le déplacement
            pieceActuelle.deplacer(-dx, -dy);
            return false;
        }

        return true;
    }

    /**
     * Effectue un hard drop (la pièce tombe instantanément au fond)
     */
    public void hardDrop() {
        if (pieceActuelle == null) return;

        // Faire tomber la pièce tant que possible
        while (deplacerPiece(0, 1)) {
            // Juste descendre, pas de score par case
        }

        // Verrouiller la pièce immédiatement
        verrouillerPiece();
        contexte.augmenterScore(20);

        // Vérifier si game over
        if (verifierFinJeu()) {
            contexte.changerEtat(new EtatGameOver(contexte.getScore()));
        } else {
            genererNouvellePiece();
        }

        // Remettre à jour le temps de chute pour éviter la chute immédiate
        dernierTempsChute = System.nanoTime();
    }

    /**
     * Fait pivoter la pièce actuelle
     */
    public boolean pivoterPiece() {
        if (pieceActuelle == null) return false;

        // Pivoter
        pieceActuelle.pivoter();

        // Vérifier collision
        if (grille.verifierCollision(pieceActuelle, 0, 0)) {
            // Annuler la rotation (pivoter 3 fois pour revenir)
            pieceActuelle.pivoter();
            pieceActuelle.pivoter();
            pieceActuelle.pivoter();
            return false;
        }

        return true;
    }

    /**
     * Verrouille la pièce dans la grille
     */
    public void verrouillerPiece() {
        if (pieceActuelle != null) {
            grille.placerTetromino(pieceActuelle);

            // Effacer les lignes complètes
            int lignesEffacees = grille.effacerLignesCompletes();
            if (lignesEffacees > 0) {
                contexte.ajouterLignesEffacees(lignesEffacees);
            }

            pieceActuelle = null;
        }
    }

    /**
     * Vérifie si le jeu est terminé
     */
    public boolean verifierFinJeu() {
        // Vérifier si des blocs sont en haut de la grille
        for (int x = 0; x < grille.getCOLONNES(); x++) {
            if (grille.obtenirCellule(x, 0) != null) {
                return true; // Game Over
            }
        }
        return false;
    }

    // Getters
    public ContexteJeu getContexte() { return contexte; }
    public Grille getGrille() { return grille; }
    public Tetromino getPieceActuelle() { return pieceActuelle; }
    public Tetromino getPieceSuivante() { return pieceSuivante; }
}