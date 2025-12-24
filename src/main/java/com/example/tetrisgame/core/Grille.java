package com.example.tetrisgame.core;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.composite.Tetromino;
import com.example.tetrisgame.decorators.BlocBombe;
import com.example.tetrisgame.decorators.BlocDore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Grille
 * Gère la grille de jeu 10x20 où tombent les pièces
 */
public class Grille {

    private final int LIGNES = 18;
    private final int COLONNES = 10;
    public Bloc[][] cellules;

    public Grille() {
        this.cellules = new Bloc[COLONNES][LIGNES];
    }

    /**
     * Place un Tetromino dans la grille (verrouille la pièce)
     */
    public void placerTetromino(Tetromino piece) {
        for (Bloc bloc : piece.obtenirBlocs()) {
            int x = bloc.obtenirX();
            int y = bloc.obtenirY();

            if (x >= 0 && x < COLONNES && y >= 0 && y < LIGNES) {
                cellules[x][y] = bloc;
            }
        }
    }

    /**
     * Efface les lignes complètes et retourne le nombre de lignes effacées
     */
    public int effacerLignesCompletes() {
        List<Integer> lignesAEffacer = new ArrayList<>();
        int pointsBonus = 0;

        // Trouver les lignes complètes
        for (int y = 0; y < LIGNES; y++) {
            if (ligneEstComplete(y)) {
                lignesAEffacer.add(y);

                // Vérifier les blocs spéciaux sur cette ligne
                for (int x = 0; x < COLONNES; x++) {
                    Bloc bloc = cellules[x][y];
                    if (bloc != null && bloc.estSpecial()) {
                        if (bloc instanceof BlocBombe) {
                            ((BlocBombe) bloc).appliquerEffet(this);
                        } else if (bloc instanceof BlocDore) {
                            ((BlocDore) bloc).appliquerEffet(this);
                        }
                    }
                }
            }
        }

        // Effacer les lignes complètes
        for (int ligne : lignesAEffacer) {
            effacerLigne(ligne);
        }

        return lignesAEffacer.size();
    }

    /**
     * Vérifie si une ligne est complète
     */
    public boolean ligneEstComplete(int ligne) {
        for (int x = 0; x < COLONNES; x++) {
            if (cellules[x][ligne] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Efface une ligne et fait descendre les lignes au-dessus
     */
    private void effacerLigne(int ligne) {
        // Faire descendre toutes les lignes au-dessus
        for (int y = ligne; y > 0; y--) {
            for (int x = 0; x < COLONNES; x++) {
                cellules[x][y] = cellules[x][y - 1];
                if (cellules[x][y] != null) {
                    cellules[x][y].definirPosition(x, y);
                }
            }
        }

        // Vider la première ligne
        for (int x = 0; x < COLONNES; x++) {
            cellules[x][0] = null;
        }
    }

    /**
     * Vérifie si la position du Tetromino est valide
     */
    public boolean positionValide(Tetromino piece) {
        for (Bloc bloc : piece.obtenirBlocs()) {
            int x = bloc.obtenirX();
            int y = bloc.obtenirY();

            // Vérifier les limites
            if (x < 0 || x >= COLONNES || y >= LIGNES) {
                return false;
            }

            // Vérifier collision avec blocs existants
            if (y >= 0 && cellules[x][y] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie collision si on déplace la pièce
     */
    public boolean verifierCollision(Tetromino piece, int dx, int dy) {
        for (Bloc bloc : piece.obtenirBlocs()) {
            int nouveauX = bloc.obtenirX() + dx;
            int nouveauY = bloc.obtenirY() + dy;

            // Vérifier limites (CORRIGÉ - le bug était ici !)
            if (nouveauX < 0 || nouveauX >= COLONNES || nouveauY >= LIGNES) {
                return true; // Collision avec les bords
            }

            // Vérifier collision avec blocs existants
            if (nouveauY >= 0 && cellules[nouveauX][nouveauY] != null) {
                return true; // Collision avec un bloc
            }
        }
        return false; // Pas de collision
    }

    /**
     * Obtient une cellule
     */
    public Bloc obtenirCellule(int x, int y) {
        if (x >= 0 && x < COLONNES && y >= 0 && y < LIGNES) {
            return cellules[x][y];
        }
        return null;
    }

    /**
     * Définit une cellule
     */
    public void definirCellule(int x, int y, Bloc bloc) {
        if (x >= 0 && x < COLONNES && y >= 0 && y < LIGNES) {
            cellules[x][y] = bloc;
        }
    }

    /**
     * Vide la grille
     */
    public void vider() {
        cellules = new Bloc[COLONNES][LIGNES];
    }

    /**
     * Affiche la grille sur le canvas
     */
    /**
     * Affiche la grille sur le canvas
     */
    public void afficher(GraphicsContext gc) {
        // Dessiner le fond de la grille
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, COLONNES * Bloc.TAILLE, LIGNES * Bloc.TAILLE);

        // Dessiner les lignes de la grille
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(1);
        for (int x = 0; x <= COLONNES; x++) {
            gc.strokeLine(x * Bloc.TAILLE, 0, x * Bloc.TAILLE, LIGNES * Bloc.TAILLE);
        }
        for (int y = 0; y <= LIGNES; y++) {
            gc.strokeLine(0, y * Bloc.TAILLE, COLONNES * Bloc.TAILLE, y * Bloc.TAILLE);
        }

        // Dessiner tous les blocs placés
        for (int x = 0; x < COLONNES; x++) {
            for (int y = 0; y < LIGNES; y++) {
                if (cellules[x][y] != null) {
                    cellules[x][y].afficher(gc);
                }
            }
        }
    }

    // Getters
    public int getLIGNES() { return LIGNES; }
    public int getCOLONNES() { return COLONNES; }
}