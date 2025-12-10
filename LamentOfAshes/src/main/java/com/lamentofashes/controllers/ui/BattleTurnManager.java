package com.lamentofashes.controllers.ui;

import com.lamentofashes.logic.battle.BattleManager;
import com.lamentofashes.model.entity.enemy.Enemy;
import com.lamentofashes.model.event.AttackResult;
import com.lamentofashes.model.event.Event;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase responsable de gestionar la secuencia de eventos durante el turno del
 * enemigo y del jugador. Coordina la lógica de batalla (BattleManager), la
 * salida de texto (GameTerminal) y las animaciones (AnimationManager) para
 * garantizar que los eventos se ejecuten de forma secuencial y visual.
 */
public class BattleTurnManager {

    private final BattleManager battleManager;
    private final GameTerminal terminal;
    private final AnimationManager animationManager;
    private final BattleUIManager uiManager;

    // Rastrear qué enemigos ya han atacado en este turno.
    // Necesario en batallas con múltiples enemigos para asociar el AttackResult genérico 
    // con la instancia correcta de Enemy que aún no ha atacado.
    private Set<Enemy> enemigosQueYaAtacaron = new HashSet<>();

    /**
     * Constructor que inyecta las dependencias necesarias para la gestión del
     * turno.
     */
    public BattleTurnManager(BattleManager battleManager, GameTerminal terminal,
            AnimationManager animationManager, BattleUIManager uiManager) {
        this.battleManager = battleManager;
        this.terminal = terminal;
        this.animationManager = animationManager;
        this.uiManager = uiManager;
    }

    /**
     * Inicia la ejecución del turno enemigo. 1. Solicita al BattleManager que
     * calcule los eventos del turno. 2. Inicia el procesamiento secuencial de
     * estos eventos.
     *
     * @param onComplete El callback a ejecutar cuando todos los eventos del
     * turno enemigo hayan finalizado.
     */
    public void ejecutarTurnoEnemigo(Runnable onComplete) {
        terminal.print("");
        terminal.printSystem("--- Turno enemigo ---");

        // Limpiar el set de enemigos que atacaron al inicio del turno
        enemigosQueYaAtacaron.clear();

        // Obtiene la lista de eventos generados por la lógica del turno enemigo
        ArrayList<Event> enemyEvents = battleManager.enemiesTurn();

        // Comienza el procesamiento recursivo y secuencial de los eventos
        procesarEventos(enemyEvents, 0, onComplete);
    }

    /**
     * Procesa una lista de eventos de forma secuencial, manejando las
     * animaciones y los delays entre cada evento.
     *
     * @param events La lista de eventos a procesar.
     * @param index El índice del evento actual a procesar.
     * @param onComplete El callback final del turno.
     */
    private void procesarEventos(ArrayList<Event> events, int index, Runnable onComplete) {
        if (index >= events.size()) {
            // ✅ ACTUALIZAR LA BARRA SOLO AL FINAL DE TODOS LOS ATAQUES
            uiManager.actualizarBarrasHero(battleManager.getPlayer());

            // Pequeño delay antes de ejecutar onComplete
            PauseTransition finalDelay = new PauseTransition(Duration.millis(300));
            finalDelay.setOnFinished(e -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            finalDelay.play();
            return;
        }

        Event evento = events.get(index);
        Enemy enemigo = null;
        BattleUIManager.EnemyUIElement enemyUI = null;
        boolean esDefensa = false;

        // Detectar si es un ataque
        if (evento instanceof AttackResult) {
            AttackResult attackResult = (AttackResult) evento;
            ArrayList<Enemy> aliveEnemies = battleManager.getAliveEnemies();

            for (Enemy e : aliveEnemies) {
                if (e.getName().equals(attackResult.getCharacter())
                        && !enemigosQueYaAtacaron.contains(e)) {
                    enemigo = e;
                    enemyUI = uiManager.getEnemyUIElement(e);

                    if (enemyUI != null) {
                        enemigosQueYaAtacaron.add(e);
                        break;
                    }
                }
            }
        }

        // 🛡️ NUEVO: Detectar si es defensa/bloqueo
        String eventoTexto = evento.toString().toLowerCase();
        if (eventoTexto.contains("se defiende") || eventoTexto.contains("bloquea")
                || eventoTexto.contains("defensa") || eventoTexto.contains("se protege")) {
            esDefensa = true;

            // Buscar el enemigo que se defiende
            ArrayList<Enemy> aliveEnemies = battleManager.getAliveEnemies();
            for (Enemy e : aliveEnemies) {
                if (eventoTexto.contains(e.getName().toLowerCase())) {
                    enemigo = e;
                    enemyUI = uiManager.getEnemyUIElement(e);
                    break;
                }
            }
        }

        terminal.printTyping(evento.toString());

        final Enemy finalEnemigo = enemigo;
        final BattleUIManager.EnemyUIElement finalEnemyUI = enemyUI;
        final boolean esBloqueoDef = esDefensa;

        // Delay inicial antes de la animación
        PauseTransition initialDelay = new PauseTransition(Duration.millis(400));
        initialDelay.setOnFinished(e -> {
            if (evento instanceof AttackResult && finalEnemyUI != null && !esBloqueoDef) {
                // 1. Animación de ataque enemigo
                animationManager.animarAtaqueEnemigo(finalEnemyUI.sprite, () -> {
                    // 2. Animación de daño en héroe
                    animationManager.animarDamage(uiManager.getHeroSprite());

                    // 3. Esperar a que termine la animación de daño (480ms)
                    PauseTransition damageDelay = new PauseTransition(Duration.millis(500));
                    damageDelay.setOnFinished(ev -> {
                        // 4. ❌ NO ACTUALIZAR BARRA AQUÍ - se hace al final de todos los eventos

                        // 5. Pequeño delay antes del siguiente evento
                        PauseTransition nextDelay = new PauseTransition(Duration.millis(400));
                        nextDelay.setOnFinished(evt -> {
                            procesarEventos(events, index + 1, onComplete);
                        });
                        nextDelay.play();
                    });
                    damageDelay.play();
                });
            } else if (esBloqueoDef && finalEnemyUI != null) {
                // 🛡️ ANIMACIÓN DE DEFENSA/BLOQUEO
                animationManager.animarDefensa(finalEnemyUI.sprite);

                PauseTransition nextDelay = new PauseTransition(Duration.millis(500));
                nextDelay.setOnFinished(ev -> procesarEventos(events, index + 1, onComplete));
                nextDelay.play();
            } else {
                // No es ataque ni defensa, siguiente evento
                PauseTransition nextDelay = new PauseTransition(Duration.millis(300));
                nextDelay.setOnFinished(ev -> procesarEventos(events, index + 1, onComplete));
                nextDelay.play();
            }
        });
        initialDelay.play();
    }
}
