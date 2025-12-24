package com.example.tetrisgame.composite.pieces;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.composite.Tetromino;

/**
 * Pièce O : ██
 *           ██
 */
public class PieceO extends Tetromino {

    public PieceO() {
        super("JAUNE");
    }

    public PieceO(int x, int y) {
        super("JAUNE");
        deplacer(x, y);
    }

    @Override
    protected void initialiserBlocs() {
        blocs.add(new Bloc(4, 0, couleur));
        blocs.add(new Bloc(5, 0, couleur));
        blocs.add(new Bloc(4, 1, couleur));
        blocs.add(new Bloc(5, 1, couleur));

        // Pivot au centre du carré
        definirPivot(4, 0);
    }

    @Override
    public void pivoter() {
        // Le carré ne change pas en pivotant !
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