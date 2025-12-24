package com.example.tetrisgame.states;

import com.example.tetrisgame.core.JournalJeu;

/**
 * STATE PATTERN - État Menu
 * L'écran d'accueil du jeu
 */
public class EtatMenu implements EtatJeu {

    @Override
    public void gererEntree(ContexteJeu ctx, String touche) {
        if (touche.equals("ENTREE")) {
            // Passer à l'état EnCours
            ctx.changerEtat(new EtatEnCours());
        }
    }

    @Override
    public void mettreAJour(ContexteJeu ctx) {
        // Rien à mettre à jour dans le menu
    }

    @Override
    public void afficher(ContexteJeu ctx) {
        // L'affichage sera géré par InterfaceJeu
        System.out.println("=== TETRIS - MENU ===");
        System.out.println("Appuyez sur ENTRÉE pour jouer");
    }

    @Override
    public void entrer(ContexteJeu ctx) {
        JournalJeu.obtenirInstance().journaliserChangementEtat("INCONNU", "MENU");
        System.out.println("Entrée dans l'état Menu");
    }

    @Override
    public void sortir(ContexteJeu ctx) {
        System.out.println("Sortie de l'état Menu");
    }
}