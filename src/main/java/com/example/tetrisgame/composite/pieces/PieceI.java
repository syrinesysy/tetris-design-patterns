package com.example.tetrisgame.composite.pieces;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.composite.Tetromino;

/**
 * Pièce I : ████
 */
public class PieceI extends Tetromino {

    public PieceI() {
        super("CYAN");
    }

    public PieceI(int x, int y) {
        super("CYAN");
        deplacer(x, y);
    }

    @Override
    protected void initialiserBlocs() {
        // Position initiale : milieu haut de la grille
        blocs.add(new Bloc(3, 0, couleur));
        blocs.add(new Bloc(4, 0, couleur));
        blocs.add(new Bloc(5, 0, couleur));
        blocs.add(new Bloc(6, 0, couleur));

        // Pivot au centre (entre bloc 2 et 3)
        definirPivot(4, 0);
    }

    @Override
    public Tetromino cloner() {
        PieceI copie = new PieceI();

        // Copier EXACTEMENT tous les blocs avec leurs positions
        copie.blocs.clear();
        for (Bloc bloc : this.blocs) {
            copie.blocs.add(new Bloc(bloc.obtenirX(), bloc.obtenirY(), bloc.obtenirCouleur()));
        }

        // Copier le pivot
        copie.definirPivot(this.pivotX, this.pivotY);

        return copie;
    }
}