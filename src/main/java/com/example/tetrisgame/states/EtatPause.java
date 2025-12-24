package com.example.tetrisgame.states;

import com.example.tetrisgame.core.JournalJeu;

/**
 * STATE PATTERN - État Pause
 * L'état quand le jeu est en pause
 */
public class EtatPause implements EtatJeu {

    @Override
    public void entrer(ContexteJeu contexte) {
        JournalJeu.obtenirInstance().journaliserChangementEtat("EN_COURS", "PAUSE");
        System.out.println("🎮 Jeu mis en PAUSE");
    }

    @Override
    public void sortir(ContexteJeu contexte) {
        JournalJeu.obtenirInstance().journaliserChangementEtat("PAUSE", "EN_COURS");
        System.out.println("▶️ Jeu REPRIS");
    }

    @Override
    public void mettreAJour(ContexteJeu contexte) {
        // Aucune mise à jour en pause (le jeu est gelé)
    }

    @Override
    public void gererEntree(ContexteJeu contexte, String touche) {
        // ✅ CORRECTION ICI : Gérer la touche P pour reprendre
        if (touche.equals("P") || touche.equals("ECHAP")) {
            // Retourner à l'état En Cours
            contexte.changerEtat(new EtatEnCours());
        }
    }

    @Override
    public void afficher(ContexteJeu contexte) {
        // L'affichage est géré dans InterfaceJeu.java avec l'overlay
        System.out.println("⏸️ PAUSE - Appuyez sur P pour continuer");
    }
}