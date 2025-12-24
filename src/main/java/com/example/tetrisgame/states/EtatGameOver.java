package com.example.tetrisgame.states;

import com.example.tetrisgame.core.JournalJeu;

/**
 * STATE PATTERN - État Game Over
 * Le joueur a perdu
 */
public class EtatGameOver implements EtatJeu {

    private int scoreTotal;

    public EtatGameOver(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    @Override
    public void gererEntree(ContexteJeu ctx, String touche) {
        if (touche.equals("ENTREE")) {
            // Retour au menu
            ctx.reinitialiser();
            ctx.changerEtat(new EtatMenu());
        }
    }

    @Override
    public void mettreAJour(ContexteJeu ctx) {
        // Rien à mettre à jour
    }

    @Override
    public void afficher(ContexteJeu ctx) {
        System.out.println("=== GAME OVER ===");
        System.out.println("Score final: " + scoreTotal);
        System.out.println("Appuyez sur ENTRÉE pour revenir au menu");
    }

    @Override
    public void entrer(ContexteJeu ctx) {
        JournalJeu.obtenirInstance().journaliserChangementEtat("EN_COURS", "GAME_OVER");
        JournalJeu.obtenirInstance().journaliserScore(scoreTotal);
        System.out.println("Entrée dans l'état Game Over");
    }

    @Override
    public void sortir(ContexteJeu ctx) {
        System.out.println("Sortie de l'état Game Over");
    }

    public int getScoreTotal() {
        return scoreTotal;
    }
}