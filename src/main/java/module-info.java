module com.example.tetrisgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;  // ← AJOUTE CETTE LIGNE

    opens com.example.tetrisgame to javafx.fxml;
    exports com.example.tetrisgame;
    exports com.example.tetrisgame.states;
    exports com.example.tetrisgame.composite;
    exports com.example.tetrisgame.composite.pieces;
    exports com.example.tetrisgame.decorators;
    exports com.example.tetrisgame.factory;
    exports com.example.tetrisgame.core;
}