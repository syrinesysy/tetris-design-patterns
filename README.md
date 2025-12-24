# 🎮 Tetris Galaxy Dream - Design Patterns Project

> **Projet académique** développé dans le cadre du module **Design Patterns**  
> **Professeur** : Mr Haythem Ghazouani  
> **Année** : 2025-2026

---

## 📝 Description

**Tetris Galaxy Dream** est une implémentation complète du jeu Tetris en Java avec JavaFX, démontrant l'application de **4 design patterns obligatoires** et **1 pattern bonus**.

Le projet met l'accent sur une architecture logicielle solide, maintenable et extensible, tout en offrant une expérience utilisateur moderne avec des effets visuels avancés.

---

## 🎯 Design Patterns Implémentés

### 1. **STATE PATTERN** (6 classes)
Gestion des différents états du jeu avec transitions fluides.

- **Interface** : `EtatJeu`
- **États concrets** : 
  - `EtatMenu` - Écran d'accueil
  - `EtatEnCours` - Jeu actif
  - `EtatPause` - Jeu en pause
  - `EtatGameOver` - Fin de partie
- **Contexte** : `ContexteJeu` - Gère l'état actuel et les données globales

**Avantage** : Transitions d'états claires et séparation des responsabilités.

---

### 2. **COMPOSITE PATTERN** (10 classes)
Structure hiérarchique des pièces Tetris.

- **Interface** : `ComposantTetris`
- **Feuille** : `Bloc` - Élément de base
- **Composite** : `Tetromino` (classe abstraite)
- **7 Pièces concrètes** : `PieceI`, `PieceO`, `PieceT`, `PieceS`, `PieceZ`, `PieceJ`, `PieceL`

**Avantage** : Traitement uniforme des blocs simples et des pièces composites.

---

### 3. **DECORATOR PATTERN** (3 classes)
Ajout dynamique de comportements spéciaux aux blocs.

- **Décorateur abstrait** : `DecorateurBloc`
- **Décorateurs concrets** :
  - `BlocBombe` 💣 - Explose et détruit les blocs voisins
  - `BlocDore` 🌟 - Donne des points bonus

**Avantage** : Extension des fonctionnalités sans modifier les classes existantes.

---

### 4. **FACTORY PATTERN** (2 classes)
Création centralisée et aléatoire des Tetrominos.

- **Interface** : `FabriqueTetromino`
- **Factory concrète** : `FabriqueAleatoire` - Génère aléatoirement les 7 types de pièces

**Avantage** : Encapsulation de la logique de création et facilité d'extension.

---

### 5. **SINGLETON PATTERN** ⭐ (Bonus)
Instance unique du système de logging.

- **Classe** : `JournalJeu` - Logger unique pour toute l'application
- **Fichier de sortie** : `logs/tetris.log`

**Avantage** : Point d'accès global au système de logs avec traçabilité complète.

---

## 🏗️ Architecture du Projet

```
src/
├── com.example.tetrisgame/
│   ├── core/                 # Classes principales
│   │   ├── Jeu.java          # Moteur du jeu
│   │   ├── Grille.java       # Plateau de jeu
│   │   ├── InterfaceJeu.java # Interface graphique
│   │   └── JournalJeu.java   # Logger Singleton
│   │
│   ├── states/               # STATE PATTERN
│   │   ├── EtatJeu.java
│   │   ├── ContexteJeu.java
│   │   ├── EtatMenu.java
│   │   ├── EtatEnCours.java
│   │   ├── EtatPause.java
│   │   └── EtatGameOver.java
│   │
│   ├── composite/            # COMPOSITE PATTERN
│   │   ├── ComposantTetris.java
│   │   ├── Bloc.java
│   │   ├── Tetromino.java
│   │   └── pieces/
│   │       ├── PieceI.java
│   │       ├── PieceO.java
│   │       ├── PieceT.java
│   │       ├── PieceS.java
│   │       ├── PieceZ.java
│   │       ├── PieceJ.java
│   │       └── PieceL.java
│   │
│   ├── decorators/           # DECORATOR PATTERN
│   │   ├── DecorateurBloc.java
│   │   ├── BlocBombe.java
│   │   └── BlocDore.java
│   │
│   ├── factory/              # FACTORY PATTERN
│   │   ├── FabriqueTetromino.java
│   │   └── FabriqueAleatoire.java
│   │
│   └── utils/                # Classes utilitaires
│       ├── Constantes.java
│       ├── EffetsVisuels.java
│       ├── HighScoreManager.java
│       └── Particule.java
│
└── Main.java                 # Point d'entrée
```

**Total** : 26 fichiers Java, ~2500 lignes de code

---

## ✨ Fonctionnalités

### Gameplay
- ✅ 7 types de Tetrominos classiques
- ✅ Système de rotation des pièces
- ✅ Ghost piece (aperçu de la position finale)
- ✅ Hard drop avec touche W
- ✅ Système de scoring progressif
- ✅ Niveaux avec difficulté croissante
- ✅ Combo de lignes
- ✅ High score persistant

### Interface
- 🎨 Thème Galaxy Dream avec dégradés pastel
- ⭐ Étoiles scintillantes animées
- 🎆 Particules physiques avec gravité
- 💎 Blocs avec dégradés et brillance
- 🏆 Explosion dorée pour nouveau record
- 📊 Panneau d'informations en temps réel
- 🎮 Aperçu de la pièce suivante

### Technique
- 📝 Logging complet de tous les événements
- 💾 Sauvegarde automatique du high score
- ⏸️ Système de pause
- 🔄 Architecture modulaire et extensible

---

## 🎮 Contrôles

| Touche | Action |
|--------|--------|
| `←` `→` | Déplacer la pièce |
| `↓` | Descente rapide |
| `ESPACE` | Pivoter |
| `W` | Hard drop (chute instantanée) |
| `P` | Pause / Reprendre |
| `ECHAP` | Retour au menu |
| `ENTRÉE` | Démarrer / Rejouer |

---

## 🚀 Installation & Lancement

### Prérequis
- **Java JDK 11+** (compatible avec JavaFX)
- **JavaFX SDK** (inclus dans le projet)
- **IDE recommandé** : IntelliJ IDEA / Eclipse

### Lancement
1. Cloner le repository :
```bash
git clone https://github.com/TON_USERNAME/tetris-design-patterns.git
cd tetris-design-patterns
```

2. Ouvrir dans votre IDE

3. Configurer JavaFX (si nécessaire)

4. Lancer `Main.java`

---

## 📹 Vidéo de Démonstration

Une vidéo explicatives est fournie :

**Démonstration** 
   - Gameplay complet
   - Fonctionnalités en action
   - Effets visuels

---

## 🎯 Points Forts du Projet

✅ **Architecture solide** : Respect strict des principes SOLID  
✅ **Patterns bien implémentés** : 4 obligatoires + 1 bonus fonctionnels  
✅ **Code propre** : Nommage clair, commentaires, organisation logique  
✅ **Extensibilité** : Facile d'ajouter de nouveaux états, pièces, ou décorateurs  
✅ **UX soignée** : Interface moderne avec animations fluides  
✅ **Traçabilité** : Système de logging complet  
✅ **Documentation** : README détaillé, code commenté, diagramme UML  

---

## 📄 Principes SOLID Respectés

- **S** - Single Responsibility : Chaque classe a une responsabilité unique
- **O** - Open/Closed : Extensible via héritage et interfaces
- **L** - Liskov Substitution : Les sous-classes sont substituables
- **I** - Interface Segregation : Interfaces spécifiques et ciblées
- **D** - Dependency Inversion : Dépendances sur abstractions

---

## 👨‍💻 Auteur

**Syrine Trabelsi**  
Étudiante en IRM-2-1  
Projet réalisé pour le module Design Patterns

---

## 📜 Licence

Ce projet est réalisé dans un cadre académique.  
© 2025-2026 - Tous droits réservés

---

## 🙏 Remerciements

- Professeur Haythem Ghazouani pour l'enseignement des Design Patterns
- Documentation officielle JavaFX
- Communauté Java pour les ressources

---