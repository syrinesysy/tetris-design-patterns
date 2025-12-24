package com.example.tetrisgame.composite.pieces;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.composite.Tetromino;

/**
 * Pièce J : █
 *           ███
 */
public class PieceJ extends Tetromino {

    public PieceJ() {
        super("BLEU");
    }

    public PieceJ(int x, int y) {
        super("BLEU");
        deplacer(x, y);
    }

    @Override
    protected void initialiserBlocs() {
        blocs.add(new Bloc(3, 0, couleur));
        blocs.add(new Bloc(3, 1, couleur));
        blocs.add(new Bloc(4, 1, couleur));
        blocs.add(new Bloc(5, 1, couleur));

        definirPivot(4, 1);
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