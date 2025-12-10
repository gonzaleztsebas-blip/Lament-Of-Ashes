package com.lamentofashes.controllers.ui;

import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.Enemy;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import java.util.HashMap;

/**
 * Gestiona y actualiza todos los elementos visuales de la interfaz de usuario
 * de la batalla (barras de vida, poder, sprites del héroe y enemigos).
 */
public class BattleUIManager {

    private final ProgressBar healthBar;
    private final ProgressBar powerBar;
    private final Label healthLabel;
    private final Label powerLabel;
    private final ImageView heroe;
    private final HBox enemyContainer;
    private final Font pixelFont;

    private HashMap<Enemy, EnemyUIElement> enemyUIElements = new HashMap<>();

    public BattleUIManager(ProgressBar healthBar, ProgressBar powerBar,
                           Label healthLabel, Label powerLabel,
                           ImageView heroe, HBox enemyContainer, Font pixelFont) {
        this.healthBar = healthBar;
        this.powerBar = powerBar;
        this.healthLabel = healthLabel;
        this.powerLabel = powerLabel;
        this.heroe = heroe;
        this.enemyContainer = enemyContainer;
        this.pixelFont = pixelFont;
    }

    /**
     * Actualiza la barra de vida y poder del jugador principal.
     */
    public void actualizarBarrasHero(Player player) {
        if (player == null) {
            return;
        }

        double healthPercent = (double) player.getHealth() / Math.max(1, player.getMaxHealth());
        if (healthBar != null) {
            healthBar.setProgress(clamp01(healthPercent));
            healthBar.setStyle(obtenerEstiloBarraVida(healthPercent));
        }
        if (healthLabel != null) {
            healthLabel.setText(player.getHealth() + "/" + player.getMaxHealth());
            if (pixelFont != null) {
                healthLabel.setFont(Font.font(pixelFont.getFamily(), 15));
            }
        }

        double powerPercent = (double) player.getPower() / Math.max(1, player.getMaxPower());
        if (powerBar != null) {
            powerBar.setProgress(clamp01(powerPercent));
            powerBar.setStyle(obtenerEstiloBarraPoder(powerPercent));
        }
        if (powerLabel != null) {
            powerLabel.setText(player.getPower() + "/" + player.getMaxPower());
            if (pixelFont != null) {
                powerLabel.setFont(Font.font(pixelFont.getFamily(), 15));
            }
        }
    }

    /**
     * Actualiza la barra de vida de un enemigo específico.
     */
    public void actualizarEnemigoUI(Enemy enemy) {
        EnemyUIElement uiElement = enemyUIElements.get(enemy);
        if (uiElement == null) {
            return;
        }

        double healthPercent = (double) enemy.getHealth() / Math.max(1, enemy.getMaxHealth());
        uiElement.healthBar.setProgress(clamp01(healthPercent));
        uiElement.hpLabel.setText(enemy.getHealth() + "/" + enemy.getMaxHealth());

        if (pixelFont != null) {
            uiElement.hpLabel.setFont(Font.font(pixelFont.getFamily(), 14));
        }

        uiElement.healthBar.setStyle(obtenerEstiloBarraVida(healthPercent));
    }

    /**
     * Genera dinámicamente la UI para la lista actual de enemigos en el contenedor.
     */
    public void generarEnemigosUI(java.util.ArrayList<Enemy> enemies,
                                 java.util.function.Consumer<Enemy> onEnemyClick) {
        enemyContainer.getChildren().clear();
        enemyUIElements.clear();

        if (enemyContainer != null) {
            enemyContainer.setSpacing(calcularEspaciado(enemies.size()));
        }

        for (Enemy enemy : enemies) {
            EnemyUIElement enemyUI = crearEnemyUIElement(enemy, onEnemyClick);
            enemyContainer.getChildren().add(enemyUI.container);
            enemyUIElements.put(enemy, enemyUI);
            actualizarEnemigoUI(enemy);
        }
    }

    /**
     * Anima la eliminación de un enemigo derrotado de la UI.
     */
    public void eliminarEnemigoUI(Enemy enemy, Runnable onComplete) {
        EnemyUIElement uiElement = enemyUIElements.get(enemy);
        if (uiElement != null) {
            javafx.animation.FadeTransition fade
                    = new javafx.animation.FadeTransition(javafx.util.Duration.millis(250), uiElement.container);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            fade.setOnFinished(e -> {
                enemyContainer.getChildren().remove(uiElement.container);
                enemyUIElements.remove(enemy);
                if (onComplete != null) {
                    onComplete.run();
                }
            });

            fade.play();
        }
    }

    /**
     * Carga el sprite del héroe desde los recursos.
     */
    public void cargarSpriteHero() {
        if (heroe != null) {
            try {
                // CORREGIDO: Ruta correcta del sprite
                Image heroImage = new Image(getClass().getResourceAsStream("/images/hero.png"));
                heroe.setImage(heroImage);
            } catch (Exception e) {
                // Intenta ruta alternativa
                try {
                    Image heroImage = new Image(getClass().getResourceAsStream("/hero.png"));
                    heroe.setImage(heroImage);
                } catch (Exception ex) {
                    // Manejo silencioso de error de carga
                }
            }
        }
    }

    public EnemyUIElement getEnemyUIElement(Enemy enemy) {
        return enemyUIElements.get(enemy);
    }

    public ImageView getHeroSprite() {
        return heroe;
    }

    /**
     * Crea un elemento visual completo para un enemigo (sprite, barras, etiquetas).
     */
    private EnemyUIElement crearEnemyUIElement(Enemy enemy,
                                               java.util.function.Consumer<Enemy> onEnemyClick) {

        boolean isBoss = enemy.getName() != null
                && (enemy.getName().equalsIgnoreCase("jefe demonio") || enemy.getName().equalsIgnoreCase("jefe"));

        double spriteSize = isBoss ? 256 : 80;
        double healthBarWidth = isBoss ? 300 : 70;
        int hpLabelFontSize = isBoss ? 28 : 14;
        int nameFontSize = isBoss ? 38 : 24;

        VBox container = new VBox(10);
        container.setFillWidth(false);

        Label nameLabel = new Label(enemy.getName());
        nameLabel.setStyle("-fx-text-fill: #fbf5ef; -fx-font-weight: bold;");
        if (pixelFont != null) {
            nameLabel.setFont(Font.font(pixelFont.getFamily(), nameFontSize));
        }

        HBox healthBox = new HBox(5);
        healthBox.setAlignment(Pos.CENTER_LEFT);

        ProgressBar enemyHealthBar = new ProgressBar();
        enemyHealthBar.setPrefWidth(healthBarWidth);
        enemyHealthBar.setPrefHeight(18);
        enemyHealthBar.setProgress((double) enemy.getHealth() / Math.max(1, enemy.getMaxHealth()));
        enemyHealthBar.setStyle(isBoss
                ? "-fx-accent: #ef4444; -fx-background-color: #333333;"
                : "-fx-accent: #4ade80; -fx-background-color: #333333;");

        Label hpLabel = new Label(enemy.getHealth() + "/" + enemy.getMaxHealth());
        hpLabel.setStyle("-fx-text-fill: #fbf5ef;");
        if (pixelFont != null) {
            hpLabel.setFont(Font.font(pixelFont.getFamily(), hpLabelFontSize));
        }

        healthBox.getChildren().addAll(enemyHealthBar, hpLabel);

        ImageView sprite = new ImageView();
        sprite.setFitWidth(spriteSize);
        sprite.setFitHeight(spriteSize);
        sprite.setPreserveRatio(true);
        cargarSpriteEnemigo(sprite, enemy);
        
        // Manejador de click en el enemigo (para ataque)
        sprite.setOnMouseClicked(ev -> {
            if (!sprite.isDisabled()) {
                onEnemyClick.accept(enemy);
            }
        });
        sprite.setStyle("-fx-cursor: hand;");

        if (isBoss) {
            container.getChildren().addAll(nameLabel, healthBox, sprite);
            container.setPrefHeight(spriteSize + 80);
            container.setAlignment(Pos.TOP_RIGHT);
            container.setTranslateX(50);
            container.setTranslateY(0);
        } else {
            container.getChildren().addAll(nameLabel, sprite, healthBox);
            container.setPrefHeight(spriteSize + 70);
            container.setAlignment(Pos.CENTER);
        }

        EnemyUIElement element = new EnemyUIElement();
        element.container = container;
        element.sprite = sprite;
        element.healthBar = enemyHealthBar;
        element.hpLabel = hpLabel;
        element.nameLabel = nameLabel;

        return element;
    }

    /**
     * Carga la imagen del enemigo según su nombre.
     */
    private void cargarSpriteEnemigo(ImageView sprite, Enemy enemy) {
        String imagePath = obtenerRutaImagenEnemigo(enemy.getName());
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            sprite.setImage(image);
        } catch (Exception e) {
            // Manejo silencioso de error de carga
        }
    }

    /**
     * Mapea el nombre del enemigo a la ruta de su imagen.
     */
    private String obtenerRutaImagenEnemigo(String enemyName) {
        if (enemyName == null) {
            return "/Squeleton.png";
        }

        switch (enemyName.toLowerCase()) {
            case "esqueleto":
                return "/Squeleton.png";
            case "mago oscuro":
                return "/Wizard.png";
            case "fantasma":
                return "/Ghost.png";
            case "boss":
            case "jefe demonio":
                return "/Boss.png";
            default:
                return "/Boss.png";
        }
    }

    /**
     * Calcula el espaciado entre sprites de enemigos en función de la cantidad.
     */
    private double calcularEspaciado(int numEnemies) {
        if (numEnemies == 1) {
            return 0;
        }
        if (numEnemies == 2) {
            return 100;
        }
        if (numEnemies == 3) {
            return 50;
        }
        if (numEnemies == 4) {
            return 30;
        }
        return 20;
    }

    /**
     * Determina el color de la barra de vida basado en el porcentaje (verde, naranja, rojo).
     */
    private String obtenerEstiloBarraVida(double percent) {
        if (percent > 0.6) {
            return "-fx-accent: #4ade80;";
        }
        if (percent > 0.3) {
            return "-fx-accent: #fb923c;";
        }
        return "-fx-accent: #ef4444;";
    }

    /**
     * Determina el color de la barra de poder basado en el porcentaje.
     */
    private String obtenerEstiloBarraPoder(double percent) {
        if (percent > 0.6) {
            return "-fx-accent: #b499c6;";
        }
        if (percent > 0.3) {
            return "-fx-accent: #8b6d9c;";
        }
        return "-fx-accent: #5c496b;";
    }

    /**
     * Limita un valor double entre 0 y 1.
     */
    private double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }

    /**
     * Habilita o deshabilita la interacción con los sprites de los enemigos.
     */
    public void setEnemigosHabilitados(boolean habilitado) {
        for (java.util.Map.Entry<Enemy, EnemyUIElement> entry : enemyUIElements.entrySet()) {
            Enemy enemy = entry.getKey();
            EnemyUIElement element = entry.getValue();

            if (element.sprite != null && !enemy.isDead()) {
                element.sprite.setDisable(!habilitado);
                // Cambiar cursor para indicar interactividad
                element.sprite.setStyle(habilitado ? "-fx-cursor: hand;" : "-fx-cursor: default;");
            }
        }
    }

    /**
     * Clase interna para agrupar los componentes UI de un solo enemigo.
     */
    public static class EnemyUIElement {
        public VBox container;
        public ImageView sprite;
        public ProgressBar healthBar;
        public Label hpLabel;
        public Label nameLabel;
    }
}