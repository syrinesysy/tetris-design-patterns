package com.example.tetrisgame.states;

import com.example.tetrisgame.core.JournalJeu;

/**
 * STATE PATTERN - État En Cours
 * L'état quand le jeu est actif et que les pièces tombent
 */
public class EtatEnCours implements EtatJeu {

    private long derniereMiseAJour;
    private int vitesseChute;

    public EtatEnCours() {
        this.derniereMiseAJour = System.currentTimeMillis();
        this.vitesseChute = 1000; // 1 seconde entre chaque chute
    }

    @Override
    public void gererEntree(ContexteJeu ctx, String touche) {
        switch (touche) {
            case "P":
                // ✅ Mettre en pause
                ctx.changerEtat(new EtatPause());
                break;
            case "ECHAP":
                // ✅ Retour au menu principal
                ctx.reinitialiser();  // Réinitialise score/niveau/lignes
                ctx.changerEtat(new EtatMenu());
                break;
            case "GAUCHE":
                System.out.println("Déplacer à gauche");
                break;
            case "DROITE":
                System.out.println("Déplacer à droite");
                break;
            case "BAS":
                System.out.println("Descente rapide");
                break;
            case "ESPACE":
                System.out.println("Rotation");
                break;
        }
    }

    @Override
    public void mettreAJour(ContexteJeu ctx) {
        long maintenant = System.currentTimeMillis();

        // Faire tomber la pièce automatiquement selon la vitesse
        if (maintenant - derniereMiseAJour >= vitesseChute) {
            System.out.println("Pièce tombe...");
            derniereMiseAJour = maintenant;
        }

        // Vérifier si game over
        // (Cette logique sera dans la classe Jeu)
    }

    @Override
    public void afficher(ContexteJeu ctx) {
        System.out.println("=== JEU EN COURS ===");
        System.out.println("Score: " + ctx.getScore());
        System.out.println("Niveau: " + ctx.getNiveau());
    }

    @Override
    public void entrer(ContexteJeu ctx) {
        JournalJeu.obtenirInstance().journaliserChangementEtat("MENU", "EN_COURS");
        System.out.println("Entrée dans l'état En Cours");
        derniereMiseAJour = System.currentTimeMillis();
    }

    @Override
    public void sortir(ContexteJeu ctx) {
        System.out.println("Sortie de l'état En Cours");
    }

    public int getVitesseChute() {
        return vitesseChute;
    }

    public void setVitesseChute(int vitesse) {
        this.vitesseChute = vitesse;
    }
}