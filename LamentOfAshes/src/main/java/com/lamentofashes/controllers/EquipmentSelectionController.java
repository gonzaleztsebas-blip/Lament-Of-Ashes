package com.lamentofashes.controllers;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.logic.GameStateManager;
import com.lamentofashes.model.item.equipable.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controlador para la escena de selección de equipamiento (Arma, Armadura,
 * Escudo). Esta escena se utiliza para configurar el equipo inicial o
 * intermedio del jugador.
 */
public class EquipmentSelectionController {

    // === Componentes FXML de Título y Contenedor Principal ===
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private AnchorPane rootPane; // Contenedor raíz

    // --- Componentes FXML para las 3 opciones ---
    @FXML
    private VBox option1Box, option2Box, option3Box;
    @FXML
    private ImageView option1Image, option2Image, option3Image;
    @FXML
    private Label option1Name, option2Name, option3Name;
    @FXML
    private Label option1Stats, option2Stats, option3Stats;

    // === Variables de Estado y Lógica ===
    private Font pixelFontBase;
    private GameStateManager gameState; // Manejador central del estado del juego
    private ArrayList<Equipable> equipmentOptions; // Opciones generadas para el tipo actual
    private EquipableType currentType; // Tipo de equipamiento que se está seleccionando actualmente (WEAPON, ARMOR, SHIELD)

    // Constantes de estilo (Idéntico a ConsumableSelectionController)
    private final String BOX_BG = "#8c6b96";
    private final String BOX_BG_SELECTED = "#6f5485";
    private final String BORDER_NORMAL = "#fbf5ef";
    private final String BORDER_HOVER = "#f2d3ab";
    private final String BORDER_SELECTED = "#f2d3ab";
    private final int BOX_PADDING = 20;

    private boolean isSelecting = false; // Flag de protección para evitar doble click durante la transición/carga

    /**
     * Método de inicialización llamado automáticamente al cargar el FXML.
     */
    @FXML
    public void initialize() {
        gameState = GameStateManager.getInstance();
        cargarFuente();
        aplicarFuenteYColorALabels();

        // 1. Aplicar efectos de escala al pasar el ratón (zoom sutil)
        addHoverEffect(option1Box);
        addHoverEffect(option2Box);
        addHoverEffect(option3Box);

        // 2. Configurar estilos de borde y manejo de click/hover
        configurarEfectosHover();

        // Aplicar sonidos de click y hover a las cajas de selección
        ButtonSoundManager.applyToNode(option1Box);
        ButtonSoundManager.applyToNode(option2Box);
        ButtonSoundManager.applyToNode(option3Box);

        cargarOpciones(); // Comienza la lógica de carga de opciones de equipamiento
    }

    // ---------------- Fuente y Labels (Idéntico a ConsumibleSelectionController) ----------------
    /**
     * Carga la fuente TTF personalizada desde la ruta de recursos.
     */
    private void cargarFuente() {
        try {
            pixelFontBase = Font.loadFont(getClass().getResourceAsStream("/fonts/pixelplay.ttf"), 32);
            if (pixelFontBase != null) {
                // Registrar la fuente en JavaFX
                new Text().setFont(pixelFontBase);
            }
        } catch (Exception e) {
            // Manejo de errores de carga de fuente (se usará la fuente por defecto)
        }
    }

    /**
     * Aplica la fuente cargada y el color de texto a todos los Labels de la
     * escena.
     */
    private void aplicarFuenteYColorALabels() {
        // Determinar la familia de la fuente
        String family = pixelFontBase != null ? pixelFontBase.getFamily() : Font.getDefault().getFamily();

        // Aplicar estilos al título y descripción
        titleLabel.setFont(Font.font(family, 32));
        titleLabel.setTextFill(Color.WHITE);

        descriptionLabel.setFont(Font.font(family, 18));
        descriptionLabel.setTextFill(Color.web("#dcd6e8"));

        // Aplicar estilos a los nombres de las opciones
        for (Label lbl : Arrays.asList(option1Name, option2Name, option3Name)) {
            lbl.setFont(Font.font(family, 18));
            lbl.setTextFill(Color.WHITE);
        }

        // Aplicar estilos a las estadísticas de las opciones
        for (Label lbl : Arrays.asList(option1Stats, option2Stats, option3Stats)) {
            lbl.setFont(Font.font(family, 14));
            lbl.setTextFill(Color.web("#e6e0ee"));
        }
    }

    // ---------------- Lógica de Equipamiento ----------------
    /**
     * Determina el tipo de equipamiento a seleccionar, genera las opciones y
     * las muestra en la UI. Si la selección está completa, pasa a la batalla.
     */
    private void cargarOpciones() {
        currentType = gameState.getCurrentEquipmentType();

        // Si currentType es null, significa que la selección de equipo está completa
        if (currentType == null) {
            try {
                // Finaliza la configuración del equipamiento en el GameStateManager
                gameState.finalizeEquipment();
                // Crea la ronda de batalla actual
                gameState.createCurrentRound();
                // Navega a la escena de batalla
                App.setRoot("BattleScene");
            } catch (IOException e) {
                // Manejo de error al cambiar de escena
                e.printStackTrace();
            }
            return; // Detiene la ejecución aquí
        }

        // Obtiene las 3 opciones de equipamiento para el tipo actual
        equipmentOptions = gameState.getEquipmentOptions(currentType);

        configurarTitulo(); // Actualiza el texto de título y descripción

        // Carga y muestra los datos de cada opción en sus respectivos VBox
        mostrarOpcion(0, option1Name, option1Stats, option1Image, option1Box);
        mostrarOpcion(1, option2Name, option2Stats, option2Image, option2Box);
        mostrarOpcion(2, option3Name, option3Stats, option3Image, option3Box);
    }

    /**
     * Configura los textos del título y descripción de la escena según el tipo
     * de equipamiento actual.
     */
    private void configurarTitulo() {
        switch (currentType) {
            case WEAPON:
                titleLabel.setText("Elige tu Arma");
                descriptionLabel.setText("Tu arma determinará tu poder de ataque");
                break;
            case ARMOR:
                titleLabel.setText("Elige tu Armadura");
                descriptionLabel.setText("Tu armadura te protegerá del daño");
                break;
            case SHIELD:
                titleLabel.setText("Elige tu Escudo");
                descriptionLabel.setText("Tu escudo regenerara tus estadísticas");
                break;
        }
    }

    /**
     * Rellena una tarjeta de opción con los datos del equipamiento
     * correspondiente.
     */
    private void mostrarOpcion(int index, Label nameLabel, Label statsLabel, ImageView imageView, VBox box) {

        // Oculta la caja si no hay una opción disponible para ese índice
        if (index >= equipmentOptions.size()) {
            box.setVisible(false);
            return;
        }
        box.setVisible(true);

        Equipable eq = equipmentOptions.get(index);

        // Nombre del equipamiento (basado en la Calidad)
        nameLabel.setText(eq.getQuality().name());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-letter-spacing: 0.5px;");

        // Estadísticas y pasivas
        statsLabel.setText(obtenerEstadisticas(eq));

        // Imagen
        cargarImagenEquipamiento(imageView, eq);

        // Estilo inicial de la caja (no seleccionada)
        applyBoxStyle(box, false);
    }

    /**
     * Genera la cadena de texto con las estadísticas específicas de cada tipo
     * de equipamiento.
     */
    private String obtenerEstadisticas(Equipable e) {
        StringBuilder sb = new StringBuilder();

        if (e instanceof Weapon) {
            Weapon w = (Weapon) e;
            sb.append("DAÑO: +").append(w.getStat()).append("\n");
            // Convierte la pasiva de float (probabilidad) a porcentaje entero
            sb.append("CRITICO: ").append((int) (w.getPassive() * 100)).append("%");
        } else if (e instanceof Armor) {
            Armor a = (Armor) e;
            sb.append("VIDA: +").append(a.getStat()).append("\n");
            // Muestra la pasiva (reducción de daño)
            sb.append("RED DE DAÑO: ").append((int) (a.getPassive()*100)).append("%");
        } else if (e instanceof Shield) {
            Shield s = (Shield) e;
            sb.append("REGEN VIDA: +").append(s.getStat()).append("\n");
            // Muestra la pasiva (regeneración de poder)
            sb.append("REGEN PODER: ").append((int) (s.getPassive()));
        }

        return sb.toString();
    }

    /**
     * Carga la imagen de equipamiento en el ImageView dado, usando una ruta por
     * defecto si falla.
     */
    private void cargarImagenEquipamiento(ImageView iv, Equipable e) {
        String path = obtenerRutaImagen(e);
        try {
            Image img;
            // Intenta cargar la imagen específica
            if (getClass().getResourceAsStream(path) != null) {
                img = new Image(getClass().getResourceAsStream(path));
            } else {
                // Fallback: imagen de placeholder si la ruta específica no existe
                img = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
            }
            iv.setImage(img);
            iv.setPreserveRatio(true);
            iv.setFitWidth(120);
            iv.setFitHeight(120);
        } catch (Exception ex) {
            // Manejo de error de carga
        }
    }

    /**
     * Determina la ruta de la imagen en base al tipo y calidad del
     * equipamiento.
     */
    private String obtenerRutaImagen(Equipable e) {
        String base;
        if (e.getType() == EquipableType.WEAPON) {
            base = "/images/weapons/";
        } else if (e.getType() == EquipableType.ARMOR) {
            base = "/images/armors/";
        } else {
            base = "/images/shields/";
        }
        // Construye la ruta final: /images/{tipo}/{calidad_en_minusculas}.png
        return base + e.getQuality().name().toLowerCase() + ".png";
    }

    // ---------------- Hover + Selección (Estética Idéntica a Consumible) ----------------
    /**
     * Añade un efecto de escala (zoom sutil) al pasar el ratón sobre el VBox.
     */
    private void addHoverEffect(VBox box) {
        box.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), box);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        box.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), box);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    /**
     * Configura los MouseEvent handlers para cambiar los estilos de borde y
     * manejar el click.
     */
    private void configurarEfectosHover() {
        VBox[] boxes = {option1Box, option2Box, option3Box};
        final int[] indexMap = {0, 1, 2};

        for (int i = 0; i < boxes.length; i++) {
            VBox box = boxes[i];
            final int index = indexMap[i];

            // Inicialización de estilo y estado (no seleccionado)
            applyBoxStyle(box, false);
            box.setUserData(Boolean.FALSE);

            // Manejador de borde al pasar el ratón (hover)
            box.setOnMouseEntered(e -> {
                boolean selected = ((Boolean) box.getUserData());
                if (!selected) {
                    // Aplica estilo hover si no está ya seleccionado
                    box.setStyle(buildBoxStyle(false, true));
                }
            });

            // Restablecer estilo al salir del hover
            box.setOnMouseExited(e -> {
                boolean selected = ((Boolean) box.getUserData());
                // Aplica estilo normal o seleccionado
                applyBoxStyle(box, selected);
            });

            // Manejador de click (Selección visual y lógica)
            box.setOnMouseClicked(e -> {
                seleccionarVisual(index); // Cambia el estilo visual a seleccionado
                seleccionarEquipamiento(index); // Ejecuta la lógica del juego
            });
        }
    }

    /**
     * Aplica el estilo CSS base (fondo, padding y borde) a un VBox.
     */
    private void applyBoxStyle(VBox box, boolean selected) {
        box.setStyle(buildBoxStyle(selected, false));
        box.setPadding(new Insets(BOX_PADDING));
    }

    /**
     * Genera la cadena CSS para el estilo de la caja basándose en su estado
     * (seleccionado/hover).
     */
    private String buildBoxStyle(boolean selected, boolean hover) {
        String bg = selected ? BOX_BG_SELECTED : BOX_BG;
        String border;
        int width;

        if (selected) {
            border = BORDER_SELECTED;
            width = 4;
        } else if (hover) {
            border = BORDER_HOVER;
            width = 3;
        } else {
            border = BORDER_NORMAL;
            width = 2;
        }

        return "-fx-background-color: " + bg + ";"
                + "-fx-border-color: " + border + ";"
                + "-fx-border-width: " + width + ";"
                + "-fx-border-radius: 6;"
                + "-fx-background-radius: 6;";
    }

    /**
     * Actualiza el estilo visual para mostrar qué opción está seleccionada.
     */
    private void seleccionarVisual(int index) {
        VBox[] boxes = {option1Box, option2Box, option3Box};

        for (int i = 0; i < boxes.length; i++) {
            boolean selected = (i == index);
            boxes[i].setUserData(Boolean.valueOf(selected)); // Almacena el estado de selección
            applyBoxStyle(boxes[i], selected); // Aplica el estilo
        }
    }

    /**
     * Maneja la lógica de selección de equipamiento: asigna el objeto al
     * jugador y gestiona la transición a la siguiente fase (siguiente equipo o
     * batalla).
     */
    private void seleccionarEquipamiento(int index) {

        // CRÍTICO: Protección contra doble click o clicks rápidos
        if (isSelecting) {
            return;
        }

        // Validación de índice
        if (equipmentOptions == null || index >= equipmentOptions.size()) {
            return;
        }

        isSelecting = true; // Bloquea la selección

        Equipable selected = equipmentOptions.get(index);
        // Registra el equipamiento seleccionado en el GameStateManager
        gameState.selectEquipment(selected, currentType);

        try {
            // Verifica si se han seleccionado todos los tipos de equipamiento (Arma, Armadura, Escudo)
            if (gameState.isEquipmentSelectionComplete()) {

                // Finaliza la configuración (e.g., aplica stats permanentes al jugador)
                gameState.finalizeEquipment();

                // Crea la primera ronda de batalla
                gameState.createCurrentRound();

                // Navega a la escena de batalla
                App.setRoot("BattleScene");
            } else {
                // Si aún falta equipamiento por seleccionar, recarga la escena con el siguiente tipo

                cargarOpciones(); // Carga el siguiente set de opciones y actualiza el título
                seleccionarVisual(-1); // Limpia la selección visual

                isSelecting = false; // Desbloquea la selección para el siguiente item
            }
        } catch (IOException e) {
            // Error al cargar la nueva escena
            e.printStackTrace();
            isSelecting = false; // Liberar en caso de error
        }
    }
}
