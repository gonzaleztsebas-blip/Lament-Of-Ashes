package com.lamentofashes.controllers;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.logic.ScoreManager;
import com.lamentofashes.model.GameStats;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;

/**
 * Controlador para la escena de Puntajes Altos (High Scores). Carga, muestra y
 * permite limpiar la lista de estadísticas de partidas guardadas.
 */
public class HighScoresController implements Initializable {

    // === Componentes FXML ===
    @FXML
    private Label titulo;

    @FXML
    private VBox scoresContainer; // Contenedor principal donde se añaden las filas de puntajes

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnLimpiar;
    @FXML
    private AnchorPane root; // Contenedor raíz para aplicar sonidos

    // === Lógica y Dependencias ===
    private ScoreManager scoreManager;
    private Font pixelFont;
    // Escena a la que se debe volver al presionar 'Volver'
    private String escenaOrigen = "TitleScene";

    /**
     * Se llama automáticamente después de que el FXML ha sido cargado.
     * Inicializa el ScoreManager, carga la fuente, aplica estilos y muestra los
     * scores.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scoreManager = new ScoreManager();
        cargarFuente();
        aplicarFuentes();
        cargarScores();
        // Aplica sonidos de click y hover a todos los nodos dentro del contenedor raíz
        ButtonSoundManager.applyToAll(root);
    }

    /**
     * Permite establecer desde dónde se abrió esta escena para volver a la
     * escena correcta (e.g., TitleScene o GameOverScene).
     */
    public void setEscenaOrigen(String escenaOrigen) {
        this.escenaOrigen = escenaOrigen;
    }

    /**
     * Carga la fuente personalizada (pixelplay.ttf).
     */
    private void cargarFuente() {
        try {
            pixelFont = Font.loadFont(
                    getClass().getResourceAsStream("/fonts/pixelplay.ttf"), 14
            );
        } catch (Exception ex) {
            // Manejo de error si la fuente no se carga
            pixelFont = null;
        }
    }

    /**
     * Aplica la fuente personalizada a los textos principales (título y
     * botones).
     */
    private void aplicarFuentes() {
        if (pixelFont != null) {
            String fontFamily = pixelFont.getFamily();
            titulo.setFont(Font.font(fontFamily, 48));
            btnVolver.setFont(Font.font(fontFamily, 18));
            btnLimpiar.setFont(Font.font(fontFamily, 18));
        }
    }

    /**
     * Obtiene la lista de puntajes altos del ScoreManager y las muestra en la
     * UI.
     */
    private void cargarScores() {
        List<GameStats> scores = scoreManager.getHighScores();

        // Limpiar filas de scores anteriores (deja el primer hijo, que es el encabezado del FXML)
        while (scoresContainer.getChildren().size() > 1) {
            scoresContainer.getChildren().remove(1);
        }

        if (scores.isEmpty()) {
            mostrarMensajeVacio(); // Muestra un mensaje si no hay scores
            return;
        }

        // Agregar cada score como una fila, limitado a 10 entradas (Top 10)
        for (int i = 0; i < scores.size() && i < 10; i++) {
            GameStats stat = scores.get(i);
            HBox fila = crearFilaScore(i + 1, stat);
            scoresContainer.getChildren().add(fila);
        }
    }

    /**
     * Crea una fila HBox para un puntaje individual, aplicando estilos.
     *
     * @param posicion La posición en el ranking (1-10).
     * @param stat Los datos de la partida.
     * @return El HBox que representa la fila.
     */
    private HBox crearFilaScore(int posicion, GameStats stat) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);

        // Alternar colores de fondo para mejorar la legibilidad de la lista
        String bgColor = (posicion % 2 == 0) ? "#494d7e" : "#3a3a5d";
        fila.setStyle("-fx-background-color: " + bgColor + "; -fx-padding: 10;");

        // Color especial para el texto del Top 3 (Oro, Plata, Bronce)
        String textColor = "#fbf5ef";
        if (posicion == 1) {
            textColor = "#ffd700"; // Oro
        } else if (posicion == 2) {
            textColor = "#c0c0c0"; // Plata
        } else if (posicion == 3) {
            textColor = "#cd7f32"; // Bronce
        }
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";

        // Creación y estilización de Labels para cada columna
        // Posición
        Label lblPos = crearLabel(String.valueOf(posicion), 40, textColor, fontFamily, 16, true);

        // Nombre del jugador
        Label lblNombre = crearLabel(stat.getPlayerName(), 150, textColor, fontFamily, 16, false);

        // Ronda alcanzada (Color verde)
        Label lblRonda = crearLabel(String.valueOf(stat.getRoundReached()), 80, "#4ade80", fontFamily, 16, false);
        lblRonda.setAlignment(Pos.CENTER);

        // Enemigos derrotados (Color naranja)
        Label lblEnemigos = crearLabel(String.valueOf(stat.getEnemiesDefeated()), 100, "#fb923c", fontFamily, 16, false);
        lblEnemigos.setAlignment(Pos.CENTER);

        // Jefes derrotados (Color rojo)
        Label lblJefes = crearLabel(String.valueOf(stat.getBossesDefeated()), 80, "#ef4444", fontFamily, 16, false);
        lblJefes.setAlignment(Pos.CENTER);

        // Daño total (Formateado, Color morado)
        Label lblDano = crearLabel(formatNumber(stat.getDamageDealt()), 100, "#8b6d9c", fontFamily, 16, false);
        lblDano.setAlignment(Pos.CENTER);

        // Tiempo jugado (Formateado a MM:SS, Color azul)
        Label lblTiempo = crearLabel(formatTime(stat.getPlayTime()), 100, "#60a5fa", fontFamily, 16, false);
        lblTiempo.setAlignment(Pos.CENTER);

        // Score total (Formateado, color especial Top 3, Fuente grande y negrita)
        Label lblScore = crearLabel(formatNumber(stat.calculateScore()), 120, textColor, fontFamily, 18, true);
        lblScore.setAlignment(Pos.CENTER);

        // Añade todos los Labels a la fila
        fila.getChildren().addAll(
                lblPos, lblNombre, lblRonda, lblEnemigos,
                lblJefes, lblDano, lblTiempo, lblScore
        );

        return fila;
    }

    /**
     * Función utilitaria para crear un Label con estilos predefinidos.
     */
    private Label crearLabel(String text, double width, String color,
            String fontFamily, double fontSize, boolean bold) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setMinWidth(width);
        label.setMaxWidth(width);

        // Construcción del estilo CSS en línea
        String style = "-fx-text-fill: " + color + "; "
                + "-fx-font-size: " + fontSize + "px; "
                + "-fx-font-family: '" + fontFamily + "';";

        if (bold) {
            style += " -fx-font-weight: bold;";
        }

        label.setStyle(style);
        return label;
    }

    /**
     * Muestra un mensaje en el contenedor si no hay puntajes guardados.
     */
    private void mostrarMensajeVacio() {
        Label mensaje = new Label("No hay puntajes registrados aún.");
        mensaje.setStyle(
                "-fx-text-fill: #fbf5ef; "
                + "-fx-font-size: 24px; "
                + "-fx-font-family: '" + (pixelFont != null ? pixelFont.getFamily() : "System") + "'; "
                + "-fx-padding: 50;"
        );
        mensaje.setAlignment(Pos.CENTER);
        scoresContainer.getChildren().add(mensaje);
    }

    /**
     * Formatea un número entero con separadores de miles.
     */
    private String formatNumber(int number) {
        return String.format("%,d", number);
    }

    /**
     * Formatea una cantidad de segundos a una cadena con formato MM:SS
     * (Minutos:Segundos).
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    // === EVENT HANDLERS ===
    /**
     * Manejador para el botón "Volver". Regresa a la escena de origen
     * predefinida.
     */
    @FXML
    private void onVolver() {
        try {
            App.setRoot(escenaOrigen);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Error al cambiar la escena
        }
    }

    /**
     * Manejador para el botón "Limpiar". Muestra un diálogo de confirmación
     * antes de borrar todos los puntajes.
     */
    @FXML
    private void onLimpiar() {
        // Mostrar diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Limpiar todos los puntajes?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Si el usuario confirma, borra los scores
            scoreManager.clearAllScores();
            // Vuelve a cargar la lista, que ahora estará vacía
            cargarScores();

            // Muestra un diálogo de información sobre la acción completada
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Completado");
            info.setHeaderText(null);
            info.setContentText("Todos los puntajes han sido eliminados.");
            info.showAndWait();
        }
    }

    // --- Estilos Hover del Botón VOLVER ---
    @FXML
    private void onVolverHover() {
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        // Aplica el estilo de hover para el botón Volver
        btnVolver.setStyle(
                "-fx-background-color: #3a3d5f; "
                + "-fx-text-fill: #fbf5ef; "
                + "-fx-font-size: 18px; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
    }

    @FXML
    private void onVolverExit() {
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        // Restaura el estilo normal del botón Volver
        btnVolver.setStyle(
                "-fx-background-color: #494d7e; "
                + "-fx-text-fill: #fbf5ef; "
                + "-fx-font-size: 18px; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
    }

    // --- Estilos Hover del Botón LIMPIAR ---
    @FXML
    private void onLimpiarHover() {
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        // Aplica el estilo de hover (rojo más oscuro) para el botón Limpiar
        btnLimpiar.setStyle(
                "-fx-background-color: #dc2626; "
                + "-fx-text-fill: #fbf5ef; "
                + "-fx-font-size: 18px; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
    }

    @FXML
    private void onLimpiarExit() {
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        // Restaura el estilo normal (rojo) del botón Limpiar
        btnLimpiar.setStyle(
                "-fx-background-color: #ef4444; "
                + "-fx-text-fill: #fbf5ef; "
                + "-fx-font-size: 18px; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
    }
}
