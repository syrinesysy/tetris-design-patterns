package com.example.tetrisgame.factory;

import com.example.tetrisgame.composite.Tetromino;
import com.example.tetrisgame.composite.pieces.*;
import com.example.tetrisgame.core.JournalJeu;
import java.util.Random;

/**
 * FACTORY PATTERN - Fabrique concrète
 * Crée des Tetrominos aléatoires parmi les 7 types
 */
public class FabriqueAleatoire implements FabriqueTetromino {

    private Random aleatoire;
    private String[] typesPieces;

    public FabriqueAleatoire() {
        this.aleatoire = new Random();
        this.typesPieces = new String[]{"I", "O", "T", "S", "Z", "J", "L"};
    }

    @Override
    public Tetromino creerTetromino() {
        String type = choisirTypeAleatoire();
        Tetromino piece = null;

        switch (type) {
            case "I":
                piece = creerPieceI();
                break;
            case "O":
                piece = creerPieceO();
                break;
            case "T":
                piece = creerPieceT();
                break;
            case "S":
                piece = creerPieceS();
                break;
            case "Z":
                piece = creerPieceZ();
                break;
            case "J":
                piece = creerPieceJ();
                break;
            case "L":
                piece = creerPieceL();
                break;
        }

        // Logger la création
        JournalJeu.obtenirInstance().journaliserPieceCreee("Piece" + type);

        return piece;
    }

    /**
     * Choisit un type de pièce aléatoire
     */
    private String choisirTypeAleatoire() {
        int index = aleatoire.nextInt(typesPieces.length);
        return typesPieces[index];
    }

    // Méthodes privées pour créer chaque type de pièce

    private PieceI creerPieceI() {
        return new PieceI();
    }

    private PieceO creerPieceO() {
        return new PieceO();
    }

    private PieceT creerPieceT() {
        return new PieceT();
    }

    private PieceS creerPieceS() {
        return new PieceS();
    }

    private PieceZ creerPieceZ() {
        return new PieceZ();
    }

    private PieceJ creerPieceJ() {
        return new PieceJ();
    }

    private PieceL creerPieceL() {
        return new PieceL();
    }
}