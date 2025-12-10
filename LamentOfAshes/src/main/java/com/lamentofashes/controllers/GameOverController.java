package com.lamentofashes.controllers;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Controlador para la escena de "Game Over". Gestiona las opciones de volver al
 * menú principal o ver los puntajes altos.
 */
public class GameOverController {

    // === Componentes FXML para Navegación y Botones ===
    @FXML
    private Label verPuntajesLabel; // Botón (Label) para ir a la escena de puntajes

    @FXML
    private Pane retryPane;        // Contenedor (Pane) que actúa como botón de reintento/volver al título
    @FXML
    private Label retryLabel;      // Etiqueta de texto dentro del retryPane
    @FXML
    private ImageView retryImage;  // Imagen dentro del retryPane

    /**
     * Manejador de eventos FXML para el botón "Ver Puntajes". Cambia la escena
     * actual a la escena de HighScores.
     */
    @FXML
    private void onVerPuntajes() {
        try {
            // Utiliza la clase App para cambiar la raíz de la escena a "HighScoresScene"
            App.setRoot("HighScoresScene");
        } catch (IOException ex) {
            // Captura y registra errores si la carga de la nueva escena falla
            ex.printStackTrace();
        }
    }

    /**
     * Manejador de eventos FXML para el botón de Reintento/Volver al Título.
     * Cambia la escena actual a la escena principal (TitleScene).
     */
    @FXML
    private void switchToTitleScene() {
        try {
            // Utiliza la clase App para cambiar la raíz de la escena a "TitleScene"
            App.setRoot("TitleScene");
        } catch (IOException ex) {
            // Captura y registra errores si la carga de la nueva escena falla
            ex.printStackTrace();
        }
    }

    /**
     * Aplica efectos de hover (escala, sombra) y maneja los sonidos y la acción
     * de click a cualquier nodo de JavaFX.
     *
     * * @param node El componente de la UI al que se aplican los efectos.
     * @param onClickAction La acción (Runnable) a ejecutar cuando se hace
     * click.
     */
    private void addHoverEffect(Node node, Runnable onClickAction) {
        // Validación de nulidad
        if (node == null) {
            return;
        }

        // --- Manejador de MouseEntered (Hover ON) ---
        node.setOnMouseEntered(e -> {
            // Aplica aumento de escala (zoom)
            node.setScaleX(1.07);
            node.setScaleY(1.07);
            // Aplica un efecto de sombra (dropshadow)
            node.setStyle("-fx-effect: dropshadow(gaussian, white, 15, 0.3, 0, 0);");
            // Reproduce el sonido de hover
            ButtonSoundManager.playHoverSound();
        });

        // --- Manejador de MouseExited (Hover OFF) ---
        node.setOnMouseExited(e -> {
            // Restaura la escala original
            node.setScaleX(1.0);
            node.setScaleY(1.0);
            // Elimina el efecto de sombra
            node.setStyle("-fx-effect: none;");
        });

        // --- Manejador de MouseClicked (Click) ---
        node.setOnMouseClicked(e -> {
            // Reproduce el sonido de click
            ButtonSoundManager.playClickSound();
            // Ejecuta la acción asociada si existe
            if (onClickAction != null) {
                onClickAction.run();
            }
        });
    }

    /**
     * Método de inicialización llamado automáticamente al cargar el FXML. Aquí
     * se aplican los efectos de hover a los elementos interactivos.
     */
    @FXML
    public void initialize() {
        // Aplica efectos al botón "Ver Puntajes" con su acción FXML
        addHoverEffect(verPuntajesLabel, this::onVerPuntajes);
        // Aplica efectos al contenedor de Reintento/Título con su acción FXML
        addHoverEffect(retryPane, this::switchToTitleScene);
    }
}
