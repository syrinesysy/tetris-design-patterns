package com.example.tetrisgame.states;

import com.example.tetrisgame.core.JournalJeu;

/**
 * STATE PATTERN - Contexte
 * Gère l'état actuel du jeu et les données globales (score, niveau, lignes)
 */
public class ContexteJeu {

    private EtatJeu etatActuel;
    private int score;
    private int niveau;
    private int lignesEffacees;

    public ContexteJeu() {
        this.score = 0;
        this.niveau = 1;
        this.lignesEffacees = 0;
        this.etatActuel = new EtatMenu();
        this.etatActuel.entrer(this);
    }

    /**
     * Change l'état actuel du jeu
     */
    public void changerEtat(EtatJeu nouvelEtat) {
        if (etatActuel != null) {
            etatActuel.sortir(this);
        }

        String ancienEtat = etatActuel.getClass().getSimpleName();
        this.etatActuel = nouvelEtat;
        String nouvelEtatNom = nouvelEtat.getClass().getSimpleName();

        JournalJeu.obtenirInstance().journaliserChangementEtat(ancienEtat, nouvelEtatNom);

        etatActuel.entrer(this);
    }

    /**
     * Obtenir l'état actuel
     */
    public EtatJeu obtenirEtat() {
        return etatActuel;
    }

    /**
     * Gérer les entrées clavier
     */
    public void gererEntree(String touche) {
        etatActuel.gererEntree(this, touche);
    }

    /**
     * Mettre à jour le jeu
     */
    public void mettreAJour() {
        etatActuel.mettreAJour(this);
    }

    /**
     * Afficher le jeu
     */
    public void afficher() {
        etatActuel.afficher(this);
    }

    /**
     * Augmenter le score
     */
    public void augmenterScore(int points) {
        this.score += points;
    }

    /**
     * Calculer le score selon le nombre de lignes effacées
     */
    public int calculerScore(int lignes) {
        int points = 0;
        switch (lignes) {
            case 1: points = 100; break;
            case 2: points = 300; break;
            case 3: points = 500; break;
            case 4: points = 800; break; // Tetris !
        }
        return points * niveau;
    }

    /**
     * Ajouter des lignes effacées et calculer le niveau
     */
    public void ajouterLignesEffacees(int lignes) {
        this.lignesEffacees += lignes;

        // Augmenter le niveau tous les 10 lignes
        int nouveauNiveau = (lignesEffacees / 10) + 1;
        if (nouveauNiveau > niveau) {
            niveau = nouveauNiveau;
            JournalJeu.obtenirInstance().journaliserEvenement("Niveau " + niveau + " atteint !");
        }

        // Ajouter les points
        int points = calculerScore(lignes);
        augmenterScore(points);

        JournalJeu.obtenirInstance().journaliserLignesEffacees(lignes);
    }

    /**
     * Obtenir le niveau actuel
     */
    public int obtenirNiveau() {
        return niveau;
    }

    /**
     * Réinitialiser le contexte (nouveau jeu)
     */
    public void reinitialiser() {
        this.score = 0;
        this.niveau = 1;
        this.lignesEffacees = 0;
        JournalJeu.obtenirInstance().journaliserEvenement("Nouveau jeu démarré");
    }

    // Getters
    public int getScore() { return score; }
    public int getNiveau() { return niveau; }
    public int getLignesEffacees() { return lignesEffacees; }
}