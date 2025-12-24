package com.example.tetrisgame.core;

import com.example.tetrisgame.composite.Bloc;
import com.example.tetrisgame.composite.Tetromino;
import com.example.tetrisgame.states.EtatEnCours;
import com.example.tetrisgame.states.EtatGameOver;
import com.example.tetrisgame.states.EtatMenu;
import com.example.tetrisgame.utils.Constantes;
import com.example.tetrisgame.utils.EffetsVisuels;
import com.example.tetrisgame.utils.HighScoreManager;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Interface Galaxy Theme - AVEC EXPLOSION HIGH SCORE
 */
public class InterfaceJeu {

    private Jeu jeu;
    private Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;
    private Label labelScore;
    private Label labelHighScore;
    private Label labelNiveau;
    private Label labelLignes;
    private Label labelCombo;
    private VBox panneauDroit;
    private Canvas canvasSuivante;

    private AnimationTimer timer;
    private EffetsVisuels effets;
    private HighScoreManager highScoreManager;

    private int combo;
    private int lignesPrecedentes;
    private List<Etoile> etoiles;
    private List<ParticuleJolie> particulesJolies;
    private double animationOffset;

    // ✨ NOUVEAUX ATTRIBUTS POUR HIGH SCORE
    private boolean nouveauRecordAtteint = false;
    private int framesClignotement = 0;
    private int highScoreAuDebut = 0;

    // DIMENSIONS CORRECTES basées sur Bloc.TAILLE = 30px
    private static final int LARGEUR_CANVAS = Constantes.COLONNES * Bloc.TAILLE;
    private static final int HAUTEUR_CANVAS = Constantes.LIGNES * Bloc.TAILLE;

    // Particule jolie colorée
    private static class ParticuleJolie {
        double x, y, vx, vy, vie, taille;
        Color couleur;

        ParticuleJolie(double x, double y, Color couleur) {
            this.x = x;
            this.y = y;
            this.couleur = couleur;
            this.vie = 1.0;
            this.taille = Math.random() * 8 + 4;
            double angle = Math.random() * Math.PI * 2;
            double vitesse = Math.random() * 4 + 2;
            this.vx = Math.cos(angle) * vitesse;
            this.vy = Math.sin(angle) * vitesse - 3;
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.15;
            vie -= 0.02;
            vx *= 0.98;
        }

        boolean estMorte() {
            return vie <= 0;
        }

        void afficher(GraphicsContext gc) {
            gc.setGlobalAlpha(vie);
            gc.setFill(couleur);
            gc.fillOval(x - taille/2, y - taille/2, taille, taille);
            gc.setGlobalAlpha(1.0);
        }
    }

    // Étoile scintillante
    private static class Etoile {
        double x, y, taille, opacite, vitesse;

        Etoile(double x, double y) {
            this.x = x;
            this.y = y;
            this.taille = Math.random() * 2 + 1;
            this.opacite = Math.random() * 0.5 + 0.3;
            this.vitesse = Math.random() * 0.02 + 0.01;
        }

        void update() {
            opacite += vitesse;
            if (opacite > 0.8) vitesse = -Math.abs(vitesse);
            if (opacite < 0.3) vitesse = Math.abs(vitesse);
        }
    }

    public InterfaceJeu(Jeu jeu) {
        this.jeu = jeu;
        this.effets = new EffetsVisuels();
        this.highScoreManager = HighScoreManager.obtenirInstance();
        this.combo = 0;
        this.lignesPrecedentes = 0;
        this.etoiles = new ArrayList<>();
        this.particulesJolies = new ArrayList<>();
        this.animationOffset = 0;
        creerEtoiles();
    }

    private void creerEtoiles() {
        Random rand = new Random();
        for (int i = 0; i < 80; i++) {
            etoiles.add(new Etoile(
                    rand.nextDouble() * LARGEUR_CANVAS,
                    rand.nextDouble() * HAUTEUR_CANVAS
            ));
        }
    }

    public void initialiser(Stage stage) {
        this.stage = stage;
        creerInterface();
        demarrerBoucleJeu();
    }

    private void creerInterface() {
        BorderPane root = new BorderPane();

        String style = "-fx-background-color: linear-gradient(to bottom right, " +
                "#e0c3fc, #8ec5fc, #c3e0fc);" +
                "-fx-padding: 20;";
        root.setStyle(style);

        canvas = new Canvas(LARGEUR_CANVAS, HAUTEUR_CANVAS);
        canvas.setEffect(new DropShadow(20, Color.rgb(100, 100, 200, 0.4)));
        gc = canvas.getGraphicsContext2D();

        panneauDroit = new VBox(12);
        panneauDroit.setAlignment(Pos.TOP_CENTER);
        panneauDroit.setPadding(new Insets(18));
        panneauDroit.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.85);" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-color: rgba(150, 180, 255, 0.5);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(100, 150, 255, 0.3), 15, 0, 0, 0);"
        );
        panneauDroit.setPrefWidth(240);
        panneauDroit.setMinWidth(240);
        panneauDroit.setMaxWidth(240);
        panneauDroit.setPrefHeight(HAUTEUR_CANVAS);
        panneauDroit.setMaxHeight(HAUTEUR_CANVAS);

        Label titre = creerLabel("✨ TETRIS ✨", 28, Color.rgb(100, 120, 200));

        int highScore = highScoreManager.getHighScore();
        String highScoreText = highScore == 0 ? "🏆 RECORD: ---" : "🏆 RECORD: " + highScore;
        labelHighScore = creerLabel(highScoreText, 15, Color.rgb(200, 150, 100));

        labelScore = creerLabel("💎 SCORE: 0", 17, Color.rgb(80, 150, 200));
        labelNiveau = creerLabel("⚡ NIVEAU: 1", 15, Color.rgb(150, 100, 200));
        labelLignes = creerLabel("📊 LIGNES: 0", 15, Color.rgb(200, 100, 150));
        labelCombo = creerLabel("🔥 COMBO: x1", 15, Color.rgb(255, 150, 50));
        labelCombo.setVisible(false);

        Label labelSuivante = creerLabel("⏭️  PROCHAINE", 14, Color.rgb(120, 120, 150));
        canvasSuivante = new Canvas(170, 140);
        canvasSuivante.setEffect(new DropShadow(10, Color.rgb(150, 180, 255, 0.3)));

        VBox controles = creerControles();

        panneauDroit.getChildren().addAll(
                titre,
                labelHighScore,
                creerSeparateur(),
                labelScore,
                labelNiveau,
                labelLignes,
                labelCombo,
                creerSeparateur(),
                labelSuivante,
                canvasSuivante,
                creerSeparateur(),
                controles
        );

        root.setCenter(canvas);
        root.setRight(panneauDroit);
        BorderPane.setMargin(canvas, new Insets(20));
        BorderPane.setMargin(panneauDroit, new Insets(20, 20, 20, 10));

        Scene scene = new Scene(root,
                LARGEUR_CANVAS + 320,
                HAUTEUR_CANVAS + 150);
        scene.setOnKeyPressed(event -> gererClavier(event.getCode()));

        stage.setScene(scene);
        stage.setTitle("✨ Tetris Galaxy Dream ✨");
        stage.setResizable(false);
        stage.show();
    }

    private Label creerLabel(String texte, int taille, Color couleur) {
        Label label = new Label(texte);
        label.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, taille));
        label.setTextFill(couleur);
        DropShadow ombre = new DropShadow(3, Color.rgb(0, 0, 0, 0.15));
        label.setEffect(ombre);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private VBox creerControles() {
        VBox controles = new VBox(8);
        controles.setAlignment(Pos.CENTER_LEFT);
        controles.setPadding(new Insets(12));
        controles.setStyle(
                "-fx-background-color: rgba(240, 245, 255, 0.8);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: rgba(150, 180, 255, 0.3);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 15;"
        );

        Label titre = creerLabel("🎮 CONTRÔLES", 14, Color.rgb(100, 120, 180));

        controles.getChildren().addAll(
                titre,
                creerLigneControle("← →", "Déplacer"),
                creerLigneControle("↓", "Descente"),
                creerLigneControle("ESPACE", "Pivoter"),
                creerLigneControle("W", "Drop Direct"),
                creerLigneControle("P", "Pause"),
                creerLigneControle("ECHAP", "Menu"),
                creerLigneControle("ENTRÉE", "Start")
        );

        return controles;
    }

    private Label creerLigneControle(String touche, String action) {
        Label label = new Label(String.format("  %-9s  →  %s", touche, action));
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        label.setTextFill(Color.rgb(80, 90, 120));
        return label;
    }

    private VBox creerSeparateur() {
        VBox sep = new VBox();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: rgba(150, 180, 255, 0.2);");
        return sep;
    }

    private void demarrerBoucleJeu() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                jeu.mettreAJour();
                mettreAJourAffichage();
                verifierCombo();
                animationOffset += 0.5;
            }
        };
        timer.start();
    }

    private void verifierCombo() {
        int lignesActuelles = jeu.getContexte().getLignesEffacees();

        if (lignesActuelles > lignesPrecedentes) {
            int lignesEffacees = lignesActuelles - lignesPrecedentes;
            combo++;

            for (int i = 0; i < 30; i++) {
                Color couleur = Color.hsb(Math.random() * 360, 0.6, 1.0);
                particulesJolies.add(new ParticuleJolie(
                        Math.random() * LARGEUR_CANVAS,
                        HAUTEUR_CANVAS / 2,
                        couleur
                ));
            }

            if (combo > 1) {
                labelCombo.setVisible(true);
                labelCombo.setText("🔥 COMBO x" + combo + " 🔥");
            }

            lignesPrecedentes = lignesActuelles;
        }
    }

    private void afficherOverlayPause() {
        gc.setGlobalAlpha(0.75);
        LinearGradient overlay = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(230, 240, 255, 0.95)),
                new Stop(1, Color.rgb(200, 220, 255, 0.95))
        );
        gc.setFill(overlay);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setGlobalAlpha(1.0);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 55));

        gc.setFill(Color.rgb(150, 180, 255, 0.3));
        gc.fillText("⏸ PAUSE ⏸", LARGEUR_CANVAS / 2 + 3, 253);

        LinearGradient textGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(150, 100, 200)),
                new Stop(1, Color.rgb(100, 150, 220))
        );
        gc.setFill(textGradient);
        gc.fillText("⏸ PAUSE ⏸", LARGEUR_CANVAS / 2, 250);

        gc.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 18));
        gc.setFill(Color.rgb(120, 140, 180));
        gc.fillText("Appuyez sur P pour continuer", LARGEUR_CANVAS / 2, 320);

        gc.setTextAlign(TextAlignment.LEFT);
    }

    public void mettreAJourAffichage() {
        particulesJolies.removeIf(ParticuleJolie::estMorte);
        for (ParticuleJolie p : particulesJolies) {
            p.update();
        }

        afficherFondJoli();

        if (jeu.getContexte().obtenirEtat() instanceof EtatMenu) {
            afficherMenu();
        } else if (jeu.getContexte().obtenirEtat() instanceof EtatEnCours) {
            afficherJeuComplet();
            afficherPieceSuivante();
        } else if (jeu.getContexte().obtenirEtat() instanceof com.example.tetrisgame.states.EtatPause) {
            afficherJeuComplet();
            afficherPieceSuivante();
            afficherOverlayPause();
        } else if (jeu.getContexte().obtenirEtat() instanceof EtatGameOver) {
            afficherGameOver();
        }

        for (ParticuleJolie p : particulesJolies) {
            p.afficher(gc);
        }

        mettreAJourScore(jeu.getContexte().getScore());
        mettreAJourNiveau(jeu.getContexte().getNiveau());
        labelLignes.setText("📊 LIGNES: " + jeu.getContexte().getLignesEffacees());
    }

    /**
     * ✨ NOUVEAU : Affiche le message "NEW HIGH SCORE!" pendant le jeu
     */
    private void afficherMessageHighScore() {
        framesClignotement++;

        // Effet de clignotement (apparaît/disparaît)
        if (framesClignotement % 30 < 20) {  // Visible 20 frames, invisible 10 frames
            gc.setTextAlign(TextAlignment.CENTER);

            // Calcul de l'opacité pulsante
            double pulse = Math.sin(framesClignotement * 0.1) * 0.3 + 0.7;

            // Ombre dorée éclatante
            gc.setGlobalAlpha(pulse * 0.6);
            gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
            gc.setFill(Color.rgb(255, 215, 0, 0.8));
            gc.fillText("★ NEW HIGH SCORE! ★", LARGEUR_CANVAS / 2 + 2, 52);

            // Texte principal doré
            gc.setGlobalAlpha(pulse);
            LinearGradient goldGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 240, 150)),
                    new Stop(0.5, Color.rgb(255, 215, 0)),
                    new Stop(1, Color.rgb(200, 150, 0))
            );
            gc.setFill(goldGradient);
            gc.fillText("★ NEW HIGH SCORE! ★", LARGEUR_CANVAS / 2, 50);

            gc.setGlobalAlpha(1.0);
            gc.setTextAlign(TextAlignment.LEFT);
        }

        // Arrêter l'animation après 5 secondes (300 frames à 60fps)
        if (framesClignotement > 300) {
            nouveauRecordAtteint = false;
            framesClignotement = 0;
        }
    }

    private void afficherFondJoli() {
        double offset = animationOffset % 360;
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.hsb((offset + 200) % 360, 0.15, 0.95)),
                new Stop(0.5, Color.hsb((offset + 240) % 360, 0.12, 0.97)),
                new Stop(1, Color.hsb((offset + 280) % 360, 0.15, 0.95))
        );
        gc.setFill(gradient);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Etoile e : etoiles) {
            e.update();
            gc.setFill(Color.rgb(255, 255, 255, e.opacite * 0.6));
            gc.fillOval(e.x, e.y, e.taille, e.taille);
        }

        for (int x = 0; x <= Constantes.COLONNES; x++) {
            gc.setStroke(Color.rgb(200, 210, 255, 0.15));
            gc.setLineWidth(1);
            gc.strokeLine(x * Bloc.TAILLE, 0, x * Bloc.TAILLE, HAUTEUR_CANVAS);
        }
        for (int y = 0; y <= Constantes.LIGNES; y++) {
            gc.setStroke(Color.rgb(200, 210, 255, 0.15));
            gc.setLineWidth(1);
            gc.strokeLine(0, y * Bloc.TAILLE, LARGEUR_CANVAS, y * Bloc.TAILLE);
        }
    }

    private void afficherJeuComplet() {
        Bloc[][] cellules = jeu.getGrille().cellules;
        for (int x = 0; x < jeu.getGrille().getCOLONNES(); x++) {
            for (int y = 0; y < jeu.getGrille().getLIGNES(); y++) {
                if (cellules[x][y] != null) {
                    afficherBlocJoli(cellules[x][y].obtenirX(), cellules[x][y].obtenirY(),
                            cellules[x][y].obtenirCouleur());
                }
            }
        }

        if (jeu.getPieceActuelle() != null) {
            Tetromino piece = jeu.getPieceActuelle();
            Tetromino ghost = piece.cloner();

            while (!jeu.getGrille().verifierCollision(ghost, 0, 1)) {
                ghost.deplacer(0, 1);
            }

            for (Bloc bloc : ghost.obtenirBlocs()) {
                int x = bloc.obtenirX();
                int y = bloc.obtenirY();
                if (x >= 0 && x < jeu.getGrille().getCOLONNES() &&
                        y >= 0 && y < jeu.getGrille().getLIGNES()) {
                    afficherBlocGhost(x, y, bloc.obtenirCouleur());
                }
            }

            for (Bloc bloc : piece.obtenirBlocs()) {
                afficherBlocJoli(bloc.obtenirX(), bloc.obtenirY(), bloc.obtenirCouleur());
            }
        }
    }

    private void afficherBlocGhost(int x, int y, String couleurNom) {
        Color couleur = Constantes.Theme.getCouleurPastel(couleurNom);
        gc.setGlobalAlpha(0.25);
        gc.setFill(couleur);
        gc.fillRoundRect(
                x * Bloc.TAILLE + 3,
                y * Bloc.TAILLE + 3,
                Bloc.TAILLE - 6,
                Bloc.TAILLE - 6,
                10, 10
        );
        gc.setStroke(couleur.darker());
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(
                x * Bloc.TAILLE + 3,
                y * Bloc.TAILLE + 3,
                Bloc.TAILLE - 6,
                Bloc.TAILLE - 6,
                10, 10
        );
        gc.setGlobalAlpha(1.0);
    }

    private void afficherBlocJoli(int x, int y, String couleurNom) {
        double px = x * Bloc.TAILLE;
        double py = y * Bloc.TAILLE;

        Color couleur = Constantes.Theme.getCouleurPastel(couleurNom);

        gc.setFill(Color.rgb(0, 0, 0, 0.1));
        gc.fillRoundRect(px + 4, py + 4, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);

        LinearGradient degrade = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, couleur.brighter().brighter()),
                new Stop(0.5, couleur),
                new Stop(1, couleur.darker())
        );
        gc.setFill(degrade);
        gc.fillRoundRect(px + 3, py + 3, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);

        RadialGradient brillance = new RadialGradient(
                0, 0, 0.3, 0.3, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.4)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(brillance);
        gc.fillRoundRect(px + 3, py + 3, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);

        gc.setStroke(couleur.darker().darker());
        gc.setLineWidth(2);
        gc.strokeRoundRect(px + 3, py + 3, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);
    }

    private void afficherMenu() {
        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 55));
        gc.setFill(Color.rgb(100, 120, 200));
        gc.fillText("TETRIS", LARGEUR_CANVAS / 2, 200);

        gc.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 22));
        gc.setFill(Color.rgb(150, 100, 180));
        gc.fillText("GALAXY DREAM", LARGEUR_CANVAS / 2, 270);

        gc.setFont(Font.font("Segoe UI", 20));
        gc.setFill(Color.rgb(80, 150, 200));
        gc.fillText("Appuyez sur ENTRÉE", LARGEUR_CANVAS / 2, 380);

        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void afficherGameOver() {
        afficherJeuComplet();

        gc.setGlobalAlpha(0.85);
        LinearGradient overlay = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(230, 240, 255, 0.9)),
                new Stop(1, Color.rgb(200, 220, 255, 0.9))
        );
        gc.setFill(overlay);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setGlobalAlpha(1.0);

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 45));
        gc.setFill(Color.rgb(200, 100, 150));
        gc.fillText("GAME OVER", LARGEUR_CANVAS / 2, 220);

        int scoreFinal = jeu.getContexte().getScore();

        if (scoreFinal > highScoreAuDebut) {

            if (!nouveauRecordAtteint) {
                for (int i = 0; i < 150; i++) {
                    Color couleurDoree = Color.hsb(45 + Math.random() * 30, 0.8, 1.0);
                    particulesJolies.add(new ParticuleJolie(
                            LARGEUR_CANVAS / 2,
                            220,
                            couleurDoree
                    ));
                }
                nouveauRecordAtteint = true;
                framesClignotement = 0;
            }

            // Sauvegarder le nouveau record
            highScoreManager.verifierEtMettreAJour(scoreFinal);

            // Message avec animation qui pulse
            double pulse = Math.sin(framesClignotement * 0.12) * 0.3 + 0.7;

            gc.setGlobalAlpha(pulse * 0.6);
            gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
            gc.setFill(Color.rgb(255, 215, 0, 0.8));
            gc.fillText("★ NEW HIGH SCORE! ★", LARGEUR_CANVAS / 2 + 2, 272);

            gc.setGlobalAlpha(pulse);
            LinearGradient goldGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 240, 150)),
                    new Stop(0.5, Color.rgb(255, 215, 0)),
                    new Stop(1, Color.rgb(200, 150, 0))
            );
            gc.setFill(goldGradient);
            gc.fillText("★ NEW HIGH SCORE! ★", LARGEUR_CANVAS / 2, 270);
            gc.setGlobalAlpha(1.0);

            framesClignotement++;

            labelHighScore.setText("🏆 RECORD: " + scoreFinal);
        }

        // Score en dessous
        gc.setFont(Font.font("Segoe UI", 24));
        gc.setFill(Color.rgb(80, 150, 200));
        gc.fillText("Score: " + scoreFinal, LARGEUR_CANVAS / 2, 340);

        gc.setFont(Font.font("Segoe UI", 18));
        gc.setFill(Color.rgb(120, 120, 150));
        gc.fillText("ENTRÉE pour rejouer", LARGEUR_CANVAS / 2, 450);

        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void afficherPieceSuivante() {
        GraphicsContext gcS = canvasSuivante.getGraphicsContext2D();

        LinearGradient bg = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(240, 245, 255)),
                new Stop(1, Color.rgb(230, 240, 255))
        );
        gcS.setFill(bg);
        gcS.fillRect(0, 0, 170, 140);

        Tetromino suivante = jeu.getPieceSuivante();
        if (suivante != null) {
            for (Bloc bloc : suivante.obtenirBlocs()) {
                double px = (bloc.obtenirX() - 3) * Bloc.TAILLE + 45;
                double py = bloc.obtenirY() * Bloc.TAILLE + 45;

                Color couleur = Constantes.Theme.getCouleurPastel(bloc.obtenirCouleur());

                gcS.setFill(Color.rgb(0, 0, 0, 0.1));
                gcS.fillRoundRect(px + 3, py + 3, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);

                LinearGradient degrade = new LinearGradient(
                        0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, couleur.brighter().brighter()),
                        new Stop(0.5, couleur),
                        new Stop(1, couleur.darker())
                );
                gcS.setFill(degrade);
                gcS.fillRoundRect(px, py, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);

                RadialGradient brillance = new RadialGradient(
                        0, 0, 0.3, 0.3, 0.5, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(255, 255, 255, 0.4)),
                        new Stop(1, Color.TRANSPARENT)
                );
                gcS.setFill(brillance);
                gcS.fillRoundRect(px, py, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);

                gcS.setStroke(couleur.darker().darker());
                gcS.setLineWidth(2);
                gcS.strokeRoundRect(px, py, Bloc.TAILLE - 6, Bloc.TAILLE - 6, 10, 10);
            }
        }
    }

    public void mettreAJourScore(int score) {
        labelScore.setText("💎 SCORE: " + score);

        int highScore = highScoreManager.getHighScore();

        // Style normal - pas d'explosion pendant le jeu
        if (score > highScore) {
            highScoreManager.verifierEtMettreAJour(score);
            labelHighScore.setText("🏆 RECORD: " + score);

            labelHighScore.setStyle(
                    "-fx-text-fill: gold; " +
                            "-fx-font-size: 18px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.8), 15, 0, 0, 0);"
            );
        } else {
            labelHighScore.setText("🏆 RECORD: " + highScore);
            labelHighScore.setStyle("");
        }
    }

    public void mettreAJourNiveau(int niveau) {
        labelNiveau.setText("⚡ NIVEAU: " + niveau);
    }

    private void gererClavier(KeyCode code) {
        String touche = "";

        switch (code) {
            case LEFT: touche = "GAUCHE"; break;
            case RIGHT: touche = "DROITE"; break;
            case DOWN: touche = "BAS"; break;
            case SPACE: touche = "ESPACE"; break;
            case W: touche = "W"; break;
            case P: touche = "P"; break;
            case ESCAPE: touche = "ECHAP"; break;
            case ENTER:
                touche = "ENTREE";
                if (jeu.getContexte().obtenirEtat() instanceof EtatMenu) {
                    jeu.demarrer();
                    combo = 0;
                    lignesPrecedentes = 0;
                    labelCombo.setVisible(false);
                    highScoreAuDebut = highScoreManager.getHighScore();
                    nouveauRecordAtteint = false;
                    framesClignotement = 0;
                }
                break;
        }

        if (!touche.isEmpty()) {
            jeu.gererEntree(touche);
        }
    }
}