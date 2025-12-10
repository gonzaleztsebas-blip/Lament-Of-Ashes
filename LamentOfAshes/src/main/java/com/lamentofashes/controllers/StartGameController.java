package com.lamentofashes.controllers;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.logic.GameStateManager;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Controlador para la escena principal o de título del juego (Start Game).
 * Gestiona la navegación a iniciar la partida, ver puntajes y salir.
 */
public class StartGameController {

    // === Componentes FXML de la UI ===
    @FXML
    private ImageView empezarImage; // Botón (Imagen) para iniciar la partida
    @FXML
    private Label salirButton;      // Botón (Label) para salir del juego
    @FXML
    private Pane mejoresPane;       // Contenedor para el botón de mejores puntajes
    @FXML
    private Label mejoresLabel;     // Etiqueta de texto para mejores puntajes
    @FXML
    private ImageView mejoresBorder; // Elemento visual del botón de mejores puntajes

    /**
     * Método de inicialización llamado automáticamente al cargar el FXML.
     * Aplica los efectos de hover a todos los elementos interactivos.
     */
    @FXML
    public void initialize() {
        // Aplica hover a cada elemento clickeable.
        // Aunque algunos elementos están anidados (Pane y Label),
        // se aplica a cada uno para garantizar el efecto visual en todos los componentes.
        addHoverEffect(empezarImage);
        addHoverEffect(salirButton);
        addHoverEffect(mejoresPane);
        addHoverEffect(mejoresLabel);
        addHoverEffect(mejoresBorder);
    }

    // ============================
    //        EFECTO DE HOVER
    // ============================
    /**
     * Aplica efectos visuales de hover (escala, sombra) y sonido de hover/click
     * a un nodo de JavaFX.
     *
     * @param node El componente de la UI al que se aplican los efectos.
     */
    private void addHoverEffect(Node node) {
        if (node == null) {
            return;
        }

        // Hover IN (Ratón entra)
        node.setOnMouseEntered(e -> {
            // Aumenta la escala del nodo para un efecto de zoom
            node.setScaleX(1.07);
            node.setScaleY(1.07);
            // Aplica un efecto de sombra (dropshadow)
            node.setStyle("-fx-effect: dropshadow(gaussian, white, 15, 0.3, 0, 0);");
            // Reproduce el sonido de hover
            ButtonSoundManager.playHoverSound();
        });

        // Hover OUT (Ratón sale)
        node.setOnMouseExited(e -> {
            // Restaura la escala original
            node.setScaleX(1.0);
            node.setScaleY(1.0);
            // Elimina el efecto de sombra
            node.setStyle("-fx-effect: none;");
        });

        // CLICK
        // Usar addEventHandler para NO reemplazar onMouseClicked de los métodos FXML
        // (Esto asegura que el sonido de click se reproduzca sin interferir con la navegación)
        node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            ButtonSoundManager.playClickSound();
        });
    }

    // ============================
    //        NAVEGACIÓN
    // ============================
    /**
     * Manejador FXML para iniciar una nueva partida. Reinicia el estado del
     * juego y navega a la escena de pedir nombre.
     */
    @FXML
    private void switchToAskName() throws IOException {
        // Reinicia todas las estadísticas y el estado del GameStateManager
        GameStateManager.getInstance().reset();
        // Navega a la escena de solicitud de nombre del jugador
        App.setRoot("AskName");
    }

    /**
     * Manejador FXML para salir del juego. Termina la aplicación.
     */
    @FXML
    private void exitGame() {
        System.exit(0);
    }

    /**
     * Manejador FXML para ver los puntajes altos. Navega a la escena de
     * HighScores.
     */
    @FXML
    private void onHighScores() {
        try {
            App.setRoot("HighScoresScene");
        } catch (IOException ex) {
            ex.printStackTrace();
            // Manejo de error al cambiar la escena
        }
    }
}
