package com.example.tetrisgame.states;

/**
 * STATE PATTERN - Interface
 * Interface définissant le comportement de tous les états du jeu.
 */
public interface EtatJeu {
    void gererEntree(ContexteJeu ctx, String touche);
    void mettreAJour(ContexteJeu ctx);
    void afficher(ContexteJeu ctx);
    void entrer(ContexteJeu ctx);
    void sortir(ContexteJeu ctx);
}