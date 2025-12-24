package com.example.tetrisgame.decorators;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.core.Grille;
import com.example.tetrisgame.core.JournalJeu;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * DECORATOR PATTERN - Bloc Bombe
 * Explose et détruit les blocs autour quand la ligne est complétée
 */
public class BlocBombe extends DecorateurBloc {

    private int rayonExplosion;

    public BlocBombe(Bloc bloc) {
        super(bloc);
        this.rayonExplosion = 1; // Détruit les blocs dans un rayon de 1 case

        JournalJeu.obtenirInstance().journaliserDecorateurApplique(
                "BlocBombe",
                "Bloc(" + bloc.obtenirX() + "," + bloc.obtenirY() + ")"
        );
    }

    @Override
    public void afficher(GraphicsContext gc) {
        // Dessiner le bloc normal d'abord
        blocDecore.afficher(gc);

        // Ajouter le symbole de bombe 💣
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("💣", x * TAILLE + 5, y * TAILLE + 23);
    }

    @Override
    public void appliquerEffet(Grille grille) {
        exploser(grille, this.x, this.y);
    }

    /**
     * Fait exploser les blocs autour
     */
    public void exploser(Grille grille, int centreX, int centreY) {
        JournalJeu.obtenirInstance().journaliserEvenement(
                "💣 EXPLOSION à (" + centreX + "," + centreY + ") !"
        );

        // Détruire tous les blocs dans le rayon
        for (int dx = -rayonExplosion; dx <= rayonExplosion; dx++) {
            for (int dy = -rayonExplosion; dy <= rayonExplosion; dy++) {
                int nx = centreX + dx;
                int ny = centreY + dy;

                // Vérifier les limites
                if (nx >= 0 && nx < grille.getCOLONNES() &&
                        ny >= 0 && ny < grille.getLIGNES()) {
                    grille.definirCellule(nx, ny, null); // Détruire le bloc
                }
            }
        }
    }

    public int getRayonExplosion() {
        return rayonExplosion;
    }
}