package com.example.tetrisgame.decorators;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.core.Grille;
import javafx.scene.canvas.GraphicsContext;
import java.util.List;

/**
 * DECORATOR PATTERN - Décorateur abstrait
 * Ajoute des effets spéciaux aux blocs normaux
 */
public abstract class DecorateurBloc extends Bloc {

    protected Bloc blocDecore;

    public DecorateurBloc(Bloc bloc) {
        super(bloc.obtenirX(), bloc.obtenirY(), bloc.obtenirCouleur());
        this.blocDecore = bloc;
        this.estSpecial = true;
    }

    @Override
    public void deplacer(int dx, int dy) {
        blocDecore.deplacer(dx, dy);
        this.x = blocDecore.obtenirX();
        this.y = blocDecore.obtenirY();
    }

    @Override
    public List<int[]> obtenirPositions() {
        return blocDecore.obtenirPositions();
    }

    @Override
    public String obtenirCouleur() {
        return blocDecore.obtenirCouleur();
    }

    /**
     * Méthode abstraite : chaque décorateur définit son effet
     */
    public abstract void appliquerEffet(Grille grille);

    /**
     * Obtenir le bloc décoré original
     */
    public Bloc getBlocDecore() {
        return blocDecore;
    }
}