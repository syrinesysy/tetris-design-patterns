package com.example.tetrisgame.decorators;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.core.Grille;
import com.example.tetrisgame.core.JournalJeu;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * DECORATOR PATTERN - Bloc Doré
 * Donne des points bonus quand la ligne est complétée
 */
public class BlocDore extends DecorateurBloc {

    private int pointsBonus;

    public BlocDore(Bloc bloc) {
        super(bloc);
        this.pointsBonus = 100; // Points bonus

        JournalJeu.obtenirInstance().journaliserDecorateurApplique(
                "BlocDore",
                "Bloc(" + bloc.obtenirX() + "," + bloc.obtenirY() + ")"
        );
    }

    @Override
    public void afficher(GraphicsContext gc) {
        // Dessiner le bloc avec couleur dorée
        gc.setFill(Color.GOLD);
        gc.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);

        // Contour brillant
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(3);
        gc.strokeRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);

        // Ajouter l'étoile ⭐
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("⭐", x * TAILLE + 5, y * TAILLE + 23);
    }

    @Override
    public void appliquerEffet(Grille grille) {
        JournalJeu.obtenirInstance().journaliserEvenement(
                "⭐ Bloc Doré collecté : +" + pointsBonus + " points !"
        );
        // L'ajout des points sera géré par ContexteJeu
    }

    public int obtenirPointsBonus() {
        return pointsBonus;
    }
}