package com.lamentofashes.controllers.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementa la consola de mensajes del juego, con efecto de máquina de escribir
 * y gestión de una cola de mensajes.
 */
public class GameTerminal {

    private Label label;
    private Font pixelFont;
    private Queue<String> messageQueue;
    private boolean isTyping;

    // Callbacks para controlar el estado de los botones del UI principal
    private Runnable onTypingStart;
    private Runnable onTypingEnd;

    public GameTerminal(Label label) {
        this.label = label;
        this.messageQueue = new LinkedList<>();
        this.isTyping = false;

        configurarLabel();
        cargarFuente();
    }

    /**
     * Establece el estilo CSS inicial del Label.
     */
    private void configurarLabel() {
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle(
                "-fx-background-color: #272744;"
                + "-fx-text-fill: #fbf5ef;"
                + "-fx-font-size: 24px;"
                + "-fx-alignment: center;"
                + "-fx-padding: 10px;"
                + "-fx-background-radius: 0;"
                + "-fx-border-width: 0px;"
        );
    }

    /**
     * Intenta cargar la fuente personalizada del juego.
     */
    private void cargarFuente() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/pixelplay.ttf");
            if (fontStream != null) {
                pixelFont = Font.loadFont(fontStream, 30);
                fontStream.close();

                Platform.runLater(() -> {
                    label.setFont(pixelFont);
                    label.setStyle(
                            "-fx-background-color: #272744;"
                            + "-fx-text-fill: #fbf5ef;"
                            + "-fx-font-family: '" + pixelFont.getFamily() + "';"
                            + "-fx-font-size: 24px;"
                            + "-fx-alignment: center;"
                            + "-fx-padding: 10px;"
                            + "-fx-background-radius: 0;"
                            + "-fx-border-width: 0px;"
                    );
                    label.applyCss();
                });
            } else {
                cargarFuenteAlternativa();
            }
        } catch (Exception e) {
            cargarFuenteAlternativa();
        }
    }

    /**
     * Aplica una fuente de sistema si la carga de la fuente personalizada falla.
     */
    private void cargarFuenteAlternativa() {
        Platform.runLater(() -> {
            label.setFont(Font.font("Consolas", 20));
        });
    }

    /**
     * Establece los callbacks para controlar los botones durante la escritura.
     */
    public void setTypingCallbacks(Runnable onStart, Runnable onEnd) {
        this.onTypingStart = onStart;
        this.onTypingEnd = onEnd;
    }

    /**
     * Muestra un mensaje instantáneamente.
     */
    public void print(String message) {
        Platform.runLater(() -> label.setText(message));
    }

    /**
     * Muestra un mensaje con el efecto de máquina de escribir (delay por defecto: 40ms).
     */
    public void printTyping(String message) {
        printTyping(message, 80);
    }

    /**
     * Muestra un mensaje con el efecto de máquina de escribir, añadiéndolo a la cola.
     */
    public void printTyping(String message, int delayMs) {
        messageQueue.offer(message);
        if (!isTyping) {
            processNextMessage(delayMs);
        }
    }

    /**
     * Procesa el siguiente mensaje en la cola con el efecto de escritura.
     */
    private void processNextMessage(int delayMs) {
        if (messageQueue.isEmpty()) {
            isTyping = false;
            // Habilita los botones al finalizar la cola
            if (onTypingEnd != null) {
                Platform.runLater(onTypingEnd);
            }
            return;
        }

        // Deshabilita los botones al iniciar el primer mensaje
        if (!isTyping && onTypingStart != null) {
            Platform.runLater(onTypingStart);
        }

        isTyping = true;
        String message = messageQueue.poll();
        Platform.runLater(() -> label.setText(""));

        StringBuilder currentText = new StringBuilder();
        Timeline timeline = new Timeline();

        // Crea un KeyFrame para cada carácter para simular la escritura
        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(delayMs * i),
                    event -> {
                        currentText.append(message.charAt(index));
                        Platform.runLater(() -> label.setText(currentText.toString()));
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // Al finalizar un mensaje, procesa el siguiente
        timeline.setOnFinished(e -> processNextMessage(delayMs));
        timeline.play();
    }

    // --- Métodos de Conveniencia ---

    public void printSeparator() {
        print("══════════════════\n");
    }

    public void printError(String message) {
        print("ERROR: " + message + "...\n");
    }

    public void printSystem(String message) {
        print("SYSTEM: " + message + "...\n");
    }

    public void printCombat(String message) {
        print("COMBAT: " + message + "...\n");
    }

    public void printDamage(String attacker, String target, int damage) {
        printTyping(attacker + " ataca a " + target + " causando " + damage + " de daño!...\n");
    }

    public void clear() {
        Platform.runLater(() -> label.setText(""));
    }

    public void printVictory() {
        printTyping("VICTORIA...\n");
    }

    public void printDefeat() {
        printTyping("GAME OVER...\n");
    }

    public void printBossWarning() {
        printTyping("RONDA DE JEFE...\n");
    }

    public void printEnemyTurn() {
        printTyping("Turno enemigo...\n");
    }

    public void printWaitingForAction() {
        print("Elige tu acción...\n");
    }

    public void printEnemyDefeated(String enemyName) {
        printTyping(enemyName + " derrotado!...\n");
    }

    public void printConsumableUsed(String consumableName) {
        printTyping("Usaste " + consumableName + "...\n");
    }

    public void printDefending(String playerName) {
        printTyping(playerName + " se defiende!...\n");
    }

    public void printAttack(String attackName) {
        printTyping("Usaste " + attackName + "!...\n");
    }

    public void printNotEnoughPower() {
        print("No tienes suficiente poder!...\n");
    }

    public void printRoundStart(int roundNumber) {
        printTyping("Ronda " + roundNumber + "...\n");
    }

    /**
     * Verifica si actualmente está en una animación de escritura o tiene mensajes pendientes.
     */
    public boolean isTyping() {
        return isTyping;
    }

    /**
     * Limpia la cola de mensajes pendientes.
     */
    public void clearQueue() {
        messageQueue.clear();
    }
}