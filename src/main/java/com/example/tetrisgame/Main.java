package com.example.tetrisgame;

import com.example.tetrisgame.core.InterfaceJeu;
import com.example.tetrisgame.core.Jeu;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe Main - Point d'entrée de l'application
 * Lance le jeu Tetris avec JavaFX
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer le jeu
        Jeu jeu = new Jeu();

        // Créer l'interface
        InterfaceJeu interfaceJeu = new InterfaceJeu(jeu);

        // Initialiser l'interface
        interfaceJeu.initialiser(primaryStage);

        System.out.println("=================================");
        System.out.println("   TETRIS - Design Patterns");
        System.out.println("=================================");
        System.out.println("Projet développé avec :");
        System.out.println("- STATE PATTERN (6 classes)");
        System.out.println("- COMPOSITE PATTERN (10 classes)");
        System.out.println("- DECORATOR PATTERN (3 classes)");
        System.out.println("- FACTORY PATTERN (2 classes)");
        System.out.println("- SINGLETON PATTERN (1 classe)");
        System.out.println("=================================");
        System.out.println("Total : 25 classes");
        System.out.println("Logs disponibles dans : logs/tetris.log");
        System.out.println("=================================");
    }

    @Override
    public void stop() {
        System.out.println("Fermeture du jeu...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}