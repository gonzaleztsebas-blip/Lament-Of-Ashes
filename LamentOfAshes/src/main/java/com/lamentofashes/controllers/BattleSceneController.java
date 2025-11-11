package com.lamentofashes.controllers;

import com.lamentofashes.App;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import com.lamentofashes.model.entity.Player;
import javafx.scene.text.Font;
import com.lamentofashes.model.entity.enemy.Enemy;
import com.lamentofashes.logic.BattleManager;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
        
public class BattleSceneController {
    
    @FXML
    private ImageView heroe;
    @FXML
    private ImageView skelly1, skelly2, skelly3;
    @FXML
    private ProgressBar healthBar;
    
    private Player player;
    private Font pixelPlay;
    private BattleManager battleManager;
    
    @FXML
    public void initialize() {
        cargarFuente();
        
        // Configurar heroe
        battleManager = new BattleManager();
        player = battleManager.getPlayer();
        
        // Configurar enemigos
        configurarTooltipHeroe();
        configurarTooltipEnemigo();
        
        // Configurar eventos de clic en enemigos
        configurarEventosAtaque();
    }
    
    // Cargar fuente
    private void cargarFuente() {
        pixelPlay = Font.loadFont(
            getClass().getResourceAsStream("/fonts/pixelplay.ttf"), 16
        );
        
        if (pixelPlay != null) {
            System.out.println("Fuente cargada: " + pixelPlay.getName());
        } else {
            System.out.println("Error al cargar la fuente");
        }
    }
    
    // Configurar tooltip del héroe
    private void configurarTooltipHeroe() {
        Tooltip tooltip = crearTooltip(
            "NAME: " + player.getName(),
            "HP: " + player.getHealth() + "/" + player.getMaxHealth(),
            "ATK: " + player.getBaseDamage()
        );
        
        instalarTooltip(heroe, tooltip);
    }
    
    // Configurar tooltip de enemigo (reutilizable)
    private void configurarTooltipEnemigo() {
        ArrayList<Enemy> enemies = battleManager.getEnemies();
        ImageView[] enemyViews = {skelly1, skelly2, skelly3};
        
        for (int i = 0; i < enemies.size() && i < enemyViews.length; i++) {
            Enemy enemy = enemies.get(i);
            ImageView enemyView = enemyViews[i];
            
            Tooltip tooltip = crearTooltip(
                "ENEMY: " + enemy.getName(),
                "HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth(),
                "ATK: " + enemy.getBaseDamage()
            );
            
            instalarTooltip(enemyView, tooltip);
        }
    }
    
    private void configurarEventosAtaque() {
        ImageView[] enemyViews = {skelly1, skelly2, skelly3};
        
        for (int i = 0; i < enemyViews.length; i++) {
            final int index = i;
            ImageView enemyView = enemyViews[i];
            
            enemyView.setOnMouseClicked(e -> {
                atacarEnemigo(index);
            });
            
            // Efecto hover
            enemyView.setOnMouseEntered(e -> {
                enemyView.setOpacity(0.7);
                enemyView.setScaleX(1.1);
                enemyView.setScaleY(1.1);
            });
            
            enemyView.setOnMouseExited(e -> {
                enemyView.setOpacity(1.0);
                enemyView.setScaleX(1.0);
                enemyView.setScaleY(1.0);
            });
        }
    }   
        
     // Atacar enemigo
    private void atacarEnemigo(int index) {
        if (battleManager.isBattleOver()) {
            System.out.println("La batalla ha terminado");
            return;
        }
        
        ArrayList<Enemy> enemies = battleManager.getEnemies();
        
        if (index < enemies.size()) {
            System.out.println("Atacando enemigo " + (index + 1));
            
            // Jugador ataca
            battleManager.playerAttack(index);
            ImageView[] enemyViews = {skelly1, skelly2, skelly3};
            animarDamage(enemyViews[index]);
            // Turno de enemigos
            if (!battleManager.isBattleOver()) {
                battleManager.enemiesTurn();
            }
            
            // Actualizar interfaz
            actualizarInterfaz();
            
            // Verificar fin de batalla
            if (battleManager.isBattleOver()) {
                finalizarBatalla();
            }
        }
    }
    
    // Actualizar interfaz después de cada turno
    private void actualizarInterfaz() {
        
        double porcentajeVida = (double) player.getHealth() / player.getMaxHealth();
        healthBar.setProgress(porcentajeVida);
    
        // Cambiar color según vida
        if (porcentajeVida > 0.5) {
            healthBar.setStyle("-fx-accent: #27ae60;"); // Verde
        } else if (porcentajeVida > 0.25) {
            healthBar.setStyle("-fx-accent: #f39c12;"); // Amarillo
        } else {
            healthBar.setStyle("-fx-accent: #e74c3c;"); // Rojo
        }
    
        // Actualizar tooltip del héroe
        configurarTooltipHeroe();
        
        // Actualizar tooltips de enemigos
        ArrayList<Enemy> enemies = battleManager.getEnemies();
        ImageView[] enemyViews = {skelly1, skelly2, skelly3};
        
        // Ocultar enemigos muertos
        for (int i = 0; i < enemyViews.length; i++) {
            if (i < enemies.size()) {
                enemyViews[i].setVisible(true);
                
                Enemy enemy = enemies.get(i);
                Tooltip tooltip = crearTooltip(
                    "NAME: " + enemy.getName(),
                    "HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth(),
                    "ATK: " + enemy.getBaseDamage()
                );
                instalarTooltip(enemyViews[i], tooltip);
            } else {
                enemyViews[i].setVisible(false);
            }
        }
    }
    
    // Finalizar batalla
    private void finalizarBatalla() {
        if (player.isDead()) {
            System.out.println("DERROTA");
            // TODO: Mostrar mensaje de derrota
            
        } else {
            System.out.println("VICTORIA");
            // TODO: Mostrar mensaje de victoria
        }
        switchToGameOverScene();
    }
    
    @FXML
    private void switchToGameOverScene(){
        try {
            App.setRoot("GameOverScene");
        } catch (IOException ex) {
        }
    }
    
    // Crear tooltip con estilo
    private Tooltip crearTooltip(String linea1, String linea2, String linea3) {
        Tooltip tooltip = new Tooltip(linea1 + "\n" + linea2 + "\n" + linea3);
        
        tooltip.setStyle(
            "-fx-background-color: rgba(73, 77, 126, 0.95);" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #f2d3ab;" +
            "-fx-border-width: 2px;" +
            "-fx-border-style: solid;" +
            "-fx-text-fill: #fbf5ef;" +
            "-fx-font-size: 16px;" +
            "-fx-font-family: '" + pixelPlay.getFamily() + "';" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);"
        );
        
        return tooltip;
    }
    
    private void animarDamage(ImageView target) {
    // Parpadeo rojo
    FadeTransition fade = new FadeTransition(Duration.millis(100), target);
    fade.setFromValue(1.0);
    fade.setToValue(0.3);
    fade.setCycleCount(4);
    fade.setAutoReverse(true);
    
    // Sacudida
    ScaleTransition scale = new ScaleTransition(Duration.millis(100), target);
    scale.setByX(0.1);
    scale.setByY(0.1);
    scale.setCycleCount(4);
    scale.setAutoReverse(true);
    
    fade.play();
    scale.play();
}
    
    
    // Instalar tooltip en un ImageView
    private void instalarTooltip(ImageView imageView, Tooltip tooltip) {
        imageView.setPickOnBounds(true);
        imageView.setMouseTransparent(false);
        imageView.toFront();
        
        imageView.setOnMouseEntered(e -> {
            tooltip.show(imageView, e.getScreenX() + 10, e.getScreenY() + 10);
        });
        
        imageView.setOnMouseExited(e -> {
            tooltip.hide();
        });
    }
}