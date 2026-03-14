# Othello

Application de jeu Othello développée en Java avec JavaFX. Supporte les modes Joueur vs Joueur et Joueur vs IA avec trois niveaux de difficulté.

## Prérequis

- Java JDK 17 ou supérieur
- Gradle 8.0+ (optionnel, wrapper inclus)

Vérification :
```bash
java -version
```

## Installation

Extraire l'archive et naviguer dans le répertoire :
```bash
cd othello-game
```

## Exécution

### Linux/macOS
```bash
chmod +x gradlew
./gradlew clean
./gradlew run
```

### Windows
```cmd
gradlew.bat clean
gradlew.bat run
```

## Tests

### Exécuter tous les tests
```bash
./gradlew test
```

### Rapport de tests HTML
```bash
./gradlew test
```
Voir : `build/reports/tests/test/index.html`

### Rapport de couverture
```bash
./gradlew test jacocoTestReport
```
Voir : `build/reports/jacoco/test/html/index.html`

## Build

### Compiler le projet
```bash
./gradlew build
```

### Générer le JAR exécutable
```bash
./gradlew jar
```
Fichier généré : `build/libs/othello-game.jar`

### Exécuter le JAR
```bash
java -jar build/libs/othello-game.jar
```

### Distribution complète
```bash
./gradlew distZip
```
Archive générée : `build/distributions/othello-game.zip`

## Nettoyage
```bash
./gradlew clean
```

## Documentation Javadoc
```bash
./gradlew javadoc
```
Voir : `build/docs/javadoc/index.html`

## Configuration

Modifier `src/main/resources/application.properties` :
```properties
game.board.size=8
game.ai.timeout=2000
persistence.save.path=${user.home}/.othello/saves
persistence.history.max=15
ai.hard.minimax.depth=4
```

## Structure du projet
```
src/main/java/
├── graphics/          # Framework générique plateaux
│   ├── configuration/
│   └── javafx/
└── othello/           # Application Othello
    ├── model/         # Logique métier
    ├── ai/            # IA (3 niveaux)
    ├── player/        # Gestion joueurs
    ├── persistence/   # Sauvegarde JSON
    └── config/        # Configuration
```

## Technologies

- Java 17
- JavaFX 21
- Gradle 8.x
- Jackson 2.18.2
- JUnit 5

## Métriques

- 52 classes, 5 interfaces, 6 enums
- 6500 lignes de code
- 168 tests unitaires (100% réussite)
- 65% couverture de tests
- 12 packages

## Dépannage

### Erreur permissions (Linux/macOS)
```bash
chmod +x gradlew
```

### Variable JAVA_HOME non définie
```bash
# Linux/macOS
export JAVA_HOME=/path/to/jdk-17

# Windows
set JAVA_HOME=C:\path\to\jdk-17
```

### Rafraîchir les dépendances
```bash
./gradlew clean --refresh-dependencies
./gradlew run
```

## Fichiers de sauvegarde

- Linux/macOS : `~/.othello/saves/`
- Windows : `C:\Users\[username]\.othello\saves\`

## Auteur

Projet Othello développé en Java avec JavaFX.

---

Version 3.0 - Décembre 2025