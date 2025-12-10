package com.lamentofashes.controllers.ui;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.Enemy;
import java.util.ArrayList;

/**
 * Clase que gestiona la actualización de las barras de vida (ProgressBar) para
 * el jugador y un número fijo de enemigos en la interfaz de JavaFX. NOTA: Este
 * diseño está limitado a un máximo de tres enemigos.
 */
public class UIUpdater {

    private final ProgressBar healthBar;
    private final ProgressBar healthEnemy1;
    private final ProgressBar healthEnemy2;
    private final ProgressBar healthEnemy3;

    // Array para manejar las barras de enemigos de forma iterativa
    ProgressBar[] healthEnemies;

    public UIUpdater(ProgressBar healthBar, ProgressBar healthEnemy1, ProgressBar healthEnemy2, ProgressBar healthEnemy3) {
        // Inicializar el array para las barras de enemigos (asume un máximo de 3)
        this.healthEnemies = new ProgressBar[]{healthEnemy1, healthEnemy2, healthEnemy3};

        this.healthBar = healthBar;
        this.healthEnemy1 = healthEnemy1;
        this.healthEnemy2 = healthEnemy2;
        this.healthEnemy3 = healthEnemy3;
    }

    /**
     * Actualiza la barra de vida del jugador (Player), aplicando el color según
     * el porcentaje de vida restante.
     *
     * @param player El objeto Player.
     */
    public void actualizarBarraVida(Player player) {
        if (player.getMaxHealth() <= 0) {
            return;
        }

        double porcentajeVida = (double) player.getHealth() / player.getMaxHealth();
        // Asegura que el progreso esté entre 0 y 1
        healthBar.setProgress(Math.max(0, Math.min(1, porcentajeVida)));

        // Estilo plano para las barras de progreso
        String flatStyle
                = "-fx-background-color: #ffffff;"
                + // Fondo (blanco en este caso, pero a menudo se desea un fondo oscuro)
                "-fx-background-radius: 0;"
                + // Sin redondeo
                "-fx-background-insets: 0;"
                + // Sin espacios
                "-fx-border-insets: 0;"
                + // Sin espacios en borde
                "-fx-border-radius: 0;"
                + // Sin redondeo en borde
                "-fx-effect: null;";                     // Sin sombra

        // Cambiar el color de acento de la barra según el estado de salud
        if (porcentajeVida > 0.5) {
            healthBar.setStyle(flatStyle + "-fx-accent: #27ae60;"); // Verde
        } else if (porcentajeVida > 0.25) {
            healthBar.setStyle(flatStyle + "-fx-accent: #f39c12;"); // Amarillo
        } else {
            healthBar.setStyle(flatStyle + "-fx-accent: #e74c3c;"); // Rojo
        }
    }

    /**
     * Actualiza las barras de vida para la lista actual de enemigos. Itera solo
     * sobre las barras de enemigos predefinidas.
     *
     * @param enemies La lista de enemigos en la batalla.
     */
    public void actualizarBarraVidaEnemigos(ArrayList<Enemy> enemies) {
        // Itera sobre el número de enemigos activos o el máximo de barras disponibles (el menor)
        int limite = Math.min(enemies.size(), healthEnemies.length);

        for (int i = 0; i < limite; i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getMaxHealth() <= 0) {
                continue;
            }

            double porcentajeVida = (double) enemy.getHealth() / enemy.getMaxHealth();
            // Asegura que el progreso esté entre 0 y 1
            healthEnemies[i].setProgress(Math.max(0, Math.min(1, porcentajeVida)));

            // Estilo plano (replicado del método del jugador)
            String flatStyle
                    = "-fx-background-color: #ffffff;"
                    + "-fx-background-radius: 0;"
                    + "-fx-background-insets: 0;"
                    + "-fx-border-insets: 0;"
                    + "-fx-border-radius: 0;"
                    + "-fx-effect: null;";

            // Cambiar el color de acento de la barra según el estado de salud del enemigo
            if (porcentajeVida > 0.5) {
                healthEnemies[i].setStyle(flatStyle + "-fx-accent: #27ae60;"); // Verde
            } else if (porcentajeVida > 0.25) {
                healthEnemies[i].setStyle(flatStyle + "-fx-accent: #f39c12;"); // Amarillo
            } else {
                healthEnemies[i].setStyle(flatStyle + "-fx-accent: #e74c3c;"); // Rojo
            }
        }
    }

    /**
     * Función principal para actualizar toda la interfaz visual relevante en
     * batalla. Gestiona la visibilidad de los sprites de enemigos y actualiza
     * las barras.
     *
     * @param player El objeto Player.
     * @param heroe El ImageView del héroe (no se usa dentro, pero se mantiene
     * en la firma).
     * @param enemyViews Array de ImageView predefinidos para los enemigos
     * (máximo 3).
     * @param enemies La lista actual de enemigos en batalla.
     */
    public void actualizarInterfaz(Player player, ImageView heroe,
            ImageView[] enemyViews, ArrayList<Enemy> enemies) {

        // 1. Actualizar visibilidad de enemigos y barras
        for (int i = 0; i < enemyViews.length; i++) {
            if (i < enemies.size()) {
                Enemy enemy = enemies.get(i);

                // Actualiza la barra de vida del enemigo [i]
                actualizarBarraVidaEnemigos(enemies);

                // Si el enemigo está muerto, oculta su sprite.
                if (enemy.isDead()) {
                    enemyViews[i].setVisible(false);
                    // Oculta también su barra de vida correspondiente
                    if (i < healthEnemies.length) {
                        healthEnemies[i].setVisible(false);
                    }
                } else {
                    // Si está vivo, asegúrate de que sea visible
                    enemyViews[i].setVisible(true);
                    enemyViews[i].setOpacity(1.0);
                    if (i < healthEnemies.length) {
                        healthEnemies[i].setVisible(true);
                    }
                }
            } else {
                // Si no hay enemigo en esta posición, oculta sprite y barra
                enemyViews[i].setVisible(false);
                if (i < healthEnemies.length) {
                    healthEnemies[i].setVisible(false);
                }
            }
        }

        // 2. Actualizar la barra de vida del jugador
        actualizarBarraVida(player);
    }
}
