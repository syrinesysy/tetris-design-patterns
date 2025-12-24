package com.example.tetrisgame.factory;

import com.example.tetrisgame.composite.Tetromino;

/**
 * FACTORY PATTERN - Interface
 * Définit la méthode de création des Tetrominos
 */
public interface FabriqueTetromino {

    /**
     * Crée un nouveau Tetromino
     * @return Un Tetromino aléatoire ou selon une logique définie
     */
    Tetromino creerTetromino();
}