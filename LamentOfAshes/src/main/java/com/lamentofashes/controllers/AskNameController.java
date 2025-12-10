package com.lamentofashes.controllers;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.logic.GameStateManager;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Controlador para la escena inicial que solicita el nombre del jugador.
 * Gestiona la carga de recursos, la configuración de la UI y la transición al
 * juego.
 */
public class AskNameController {

    // === 1. Componentes FXML ===
    @FXML
    private Label textLabel; // Etiqueta donde se muestra el texto con efecto typing
    @FXML
    private TextField nameField; // Campo donde el jugador introduce su nombre

    // === 2. Variables de Estado y Recursos ===
    private Font pixelFont;
    private String playerName;
    private GameStateManager gameState; // Singleton para la gestión global del estado del juego

    // === 3. Inicialización ===
    /**
     * Método de inicialización llamado automáticamente por JavaFX.
     */
    @FXML
    public void initialize() {
        System.out.println(">>> Inicializando AskNameController");

        // Obtener instancia del GameStateManager (Patrón Singleton)
        gameState = GameStateManager.getInstance();

        cargarFuente();
        configurarTextArea();
        configurarTextField();

        // Iniciar la interacción con el efecto de máquina de escribir
        printTyping("Aun... recuerdas tu nombre?...");
    }

    /**
     * Carga la fuente personalizada (pixelplay.ttf).
     */
    private void cargarFuente() {
        pixelFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/pixelplay.ttf"), 60
        );
    }

    /**
     * Configura la apariencia del Label de texto.
     */
    private void configurarTextArea() {
        if (textLabel != null) {
            if (pixelFont != null) {
                textLabel.setFont(pixelFont);
            }
            // Estilos CSS para fondo transparente y texto blanco
            textLabel.setStyle(
                    "-fx-background-color: transparent;"
                    + "-fx-text-fill: #fbf5ef;"
                    + "-fx-border-color: transparent;"
            );
            textLabel.setWrapText(true);
        }
    }

    /**
     * Configura la apariencia del campo de entrada de texto.
     */
    private void configurarTextField() {
        if (nameField != null) {
            if (pixelFont != null) {
                nameField.setFont(pixelFont);
            }
            // Estilos para campo de texto transparente, borde inferior y texto blanco
            nameField.setStyle(
                    "-fx-background-color: transparent;"
                    + "-fx-text-fill: #fbf5ef;"
                    + "-fx-prompt-text-fill: #fbf5ef80;" // Color del texto de ayuda (prompt)
                    + "-fx-border-color: #fbf5ef;"
                    + "-fx-border-width: 0 0 2 0;" // Solo borde inferior de 2px
            );
            nameField.setVisible(false); // Inicialmente oculto hasta que termine la introducción
        }
    }

    // === 4. Lógica de Efectos (Typing) ===
    /**
     * Versión simplificada de printTyping con un delay predeterminado.
     *
     * @param message El texto a mostrar.
     */
    private void printTyping(String message) {
        printTyping(message, 200);
    }

    /**
     * Muestra el texto con el efecto de máquina de escribir (typing).
     *
     * @param message El texto completo a mostrar.
     * @param delayMs El tiempo en milisegundos entre la aparición de cada
     * carácter.
     */
    private void printTyping(String message, int delayMs) {
        textLabel.setText(""); // Limpiar el texto inicial
        StringBuilder currentText = new StringBuilder();
        Timeline timeline = new Timeline();

        // Crear un KeyFrame para cada carácter con un delay acumulativo
        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(delayMs * i), // Duración acumulativa
                    event -> {
                        // Usar Platform.runLater para garantizar la actualización en el hilo de JavaFX
                        Platform.runLater(() -> {
                            currentText.append(message.charAt(index));
                            textLabel.setText(currentText.toString());
                        });
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // Acciones al finalizar el efecto typing inicial
        timeline.setOnFinished(e -> {
            PauseTransition pause = new PauseTransition(Duration.millis(500)); // Pausa breve
            pause.setOnFinished(event -> {
                nameField.setVisible(true); // Mostrar el campo de entrada
                nameField.requestFocus(); // Establecer el foco en el campo
            });
            pause.play();
        });

        timeline.play();
    }

    // === 5. Manejadores de Eventos de la UI ===
    /**
     * Maneja el evento cuando el usuario presiona Enter en el campo de texto.
     */
    @FXML
    private void onEnterPressed() {
        playerName = nameField.getText().trim();

        if (playerName.isEmpty()) {
            // Validación: Si el nombre está vacío, mostrar un mensaje de error/ayuda.
            nameField.setPromptText("Por favor, escribe tu nombre...");
            nameField.clear();
            return;
        }

        // Ocultar el campo de entrada una vez que el nombre ha sido aceptado
        nameField.setVisible(false);

        // Iniciar el juego en GameStateManager con el nombre del jugador
        GameStateManager.getInstance().startNewGame(playerName);

        // Mostrar mensaje de confirmación con efecto typing y transición a la siguiente escena
        printTypingAndSwitch("Bienvenido, " + playerName + "...");
    }

    /**
     * Muestra el texto con efecto typing y, al finalizar, cambia a la siguiente
     * escena.
     *
     * @param message El mensaje de despedida/confirmación.
     */
    private void printTypingAndSwitch(String message) {
        textLabel.setText("");
        StringBuilder currentText = new StringBuilder();
        Timeline timeline = new Timeline();

        // Crea KeyFrames con un delay ligeramente más rápido (150ms)
        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(150 * i),
                    event -> {
                        Platform.runLater(() -> {
                            currentText.append(message.charAt(index));
                            textLabel.setText(currentText.toString());
                        });
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // Después de mostrar el mensaje, esperar y cambiar de escena
        timeline.setOnFinished(e -> {
            ButtonSoundManager.playClickSound(); // Reproducir sonido de confirmación
            PauseTransition pause = new PauseTransition(Duration.millis(1500)); // Pausa más larga antes de la transición
            pause.setOnFinished(event -> {
                try {
                    // Asegurar que el estado del jugador esté listo antes de cambiar
                    gameState.initializePlayerIfNeeded();

                    // Cambiar a la escena de selección de equipamiento
                    App.setRoot("EquipmentSelectionScene");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Error al cargar la escena EquipmentSelectionScene: " + ex.getMessage());
                }
            });
            pause.play();
        });

        timeline.play();
    }

    // === 6. Métodos de Acceso (Getters) ===
    /**
     * Obtiene el nombre del jugador (solo disponible después de
     * onEnterPressed).
     *
     * @return El nombre ingresado por el jugador.
     */
    public String getPlayerName() {
        return playerName;
    }
}
