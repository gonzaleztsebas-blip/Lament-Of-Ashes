package com.lamentofashes.controllers;

import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.logic.factorys.ConsumableFactory;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.consumable.Consumable;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controlador para la escena de selección de consumibles. Implementa
 * Initializable para configurar la UI al cargar el FXML.
 */
public class ConsumableSelectionController implements Initializable {

    // === Componentes FXML de Título e Información ===
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    // --- Opción 1 Componentes UI ---
    @FXML
    private VBox option1Box; // Contenedor principal de la opción 1 (clic y estilos)
    @FXML
    private ImageView option1Image;
    @FXML
    private Label option1Name;
    @FXML
    private Label option1Stats;

    // --- Opción 2 Componentes UI ---
    @FXML
    private VBox option2Box;
    @FXML
    private ImageView option2Image;
    @FXML
    private Label option2Name;
    @FXML
    private Label option2Stats;

    // --- Opción 3 Componentes UI ---
    @FXML
    private VBox option3Box;
    @FXML
    private ImageView option3Image;
    @FXML
    private Label option3Name;
    @FXML
    private Label option3Stats;

    // === Variables de Estado y Lógica ===
    private Player player; // Referencia al objeto Player del juego
    private Consumable[] options = new Consumable[3]; // Array para almacenar los 3 consumibles generados
    private Font pixelFontBase; // Fuente personalizada

    // Constantes de estilo (CSS en línea)
    private final String BOX_BG = "#8c6b96";
    private final String BOX_BG_SELECTED = "#6f5485";
    private final String BORDER_NORMAL = "#fbf5ef";
    private final String BORDER_HOVER = "#f2d3ab";
    private final String BORDER_SELECTED = "#f2d3ab";
    private final int BOX_PADDING = 20;

    // Callback para notificar a la clase invocadora que la selección ha terminado (usualmente para cerrar el Stage)
    private Runnable onConsumableSelectedCallback;

    /**
     * Método de inicialización llamado por el FXML Loader.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Cargar la fuente personalizada
        cargarFuente();

        // 2. Aplicar fuente y colores a todos los Labels
        aplicarFuenteYColorALabels();

        // 3. Configurar los efectos visuales de borde (normal, hover, seleccionado)
        configurarEfectosHover();

        // 4. Aplicar sonidos y efectos de escala (zoom) al pasar el ratón
        agregarSonidos(option1Box);
        agregarSonidos(option2Box);
        agregarSonidos(option3Box);
        addHoverEffect(option1Box);
        addHoverEffect(option2Box);
        addHoverEffect(option3Box);

        // 5. Establecer textos estáticos iniciales
        titleLabel.setText("¡Obtén un Consumible!");
        descriptionLabel.setText("Elige uno entre las tres opciones:");
    }

    // ---------------- Funciones de Configuración de UI ----------------
    /**
     * Carga la fuente TTF personalizada desde la ruta de recursos.
     */
    private void cargarFuente() {
        try {
            // Se intenta cargar la fuente
            pixelFontBase = Font.loadFont(
                    getClass().getResourceAsStream("/fonts/pixelplay.ttf"), 32
            );
            if (pixelFontBase != null) {
                // Registrar la familia de fuentes en JavaFX para su uso posterior
                new Text().setFont(pixelFontBase);
            }
        } catch (Exception e) {
            // Manejo de errores de carga de fuente (usará fuente por defecto)
        }
    }

    /**
     * Asocia los sonidos de hover y click a un contenedor VBox.
     *
     * @param box El VBox que actuará como botón.
     */
    private void agregarSonidos(VBox box) {
        // Sonido al entrar (hover)
        box.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, e -> {
            ButtonSoundManager.playHoverSound();
        });

        // Sonido al hacer click
        box.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            ButtonSoundManager.playClickSound();
        });
    }

    /**
     * Aplica la fuente cargada y el color de texto a todos los Labels de la
     * escena.
     */
    private void aplicarFuenteYColorALabels() {
        // Obtener la familia de la fuente cargada o la predeterminada si falló
        String family = pixelFontBase != null ? pixelFontBase.getFamily() : Font.getDefault().getFamily();

        // Estilos para Labels
        titleLabel.setFont(Font.font(family, 32));
        titleLabel.setTextFill(Color.WHITE);

        descriptionLabel.setFont(Font.font(family, 18));
        descriptionLabel.setTextFill(Color.web("#dcd6e8"));

        for (Label lbl : Arrays.asList(option1Name, option2Name, option3Name)) {
            lbl.setFont(Font.font(family, 18));
            lbl.setTextFill(Color.WHITE);
        }

        for (Label lbl : Arrays.asList(option1Stats, option2Stats, option3Stats)) {
            lbl.setFont(Font.font(family, 14));
            lbl.setTextFill(Color.web("#e6e0ee"));
        }
    }

    // ---------------- Lógica de Datos ----------------
    /**
     * Establece la referencia al objeto Player y desencadena la carga de datos.
     * Este es el método principal que se llama desde la clase que abre el
     * diálogo.
     *
     * @param player La instancia del jugador actual.
     */
    public void setData(Player player) {
        this.player = player;
        generateConsumibles(); // Genera los 3 consumibles
        loadUI(); // Carga los datos de los consumibles en la UI
    }

    /**
     * Genera tres objetos Consumable aleatorios usando ConsumableFactory.
     */
    private void generateConsumibles() {
        ConsumableFactory factory = new ConsumableFactory();
        for (int i = 0; i < 3; i++) {
            options[i] = factory.generateConsumable();
        }
    }

    /**
     * Carga la información de los consumibles generados en los componentes de
     * la UI.
     */
    private void loadUI() {
        loadCard(option1Name, option1Stats, option1Image, options[0]);
        loadCard(option2Name, option2Stats, option2Image, options[1]);
        loadCard(option3Name, option3Stats, option3Image, options[2]);
    }

    /**
     * Rellena una tarjeta de consumible con su nombre, descripción e imagen.
     */
    private void loadCard(Label name, Label stats, ImageView img, Consumable c) {
        name.setText(c.getName());
        name.setStyle("-fx-font-weight: bold; -fx-letter-spacing: 0.5px;");
        stats.setText(c.getDescription()); // Se asume que getDescripcion() contiene las stats

        String imagePath = c.getImagePath();

        if (imagePath != null) {
            try {
                var stream = getClass().getResourceAsStream(imagePath);

                if (stream != null) {
                    Image image = new Image(stream);
                    img.setImage(image);
                    img.setPreserveRatio(true);
                    img.setFitWidth(120);
                    img.setFitHeight(120);
                } else {
                    loadDefaultImage(img); // Recurso no encontrado, carga por defecto
                }

            } catch (Exception e) {
                // Error al cargar la imagen, usa la imagen por defecto
                loadDefaultImage(img);
            }
        } else {
            // imagePath es null, usa la imagen por defecto
            loadDefaultImage(img);
        }
    }

    /**
     * Carga una imagen por defecto (e.g., /images/potion.png) si falla la carga
     * específica.
     */
    private void loadDefaultImage(ImageView img) {
        try {
            var stream = getClass().getResourceAsStream("/images/potion.png");
            if (stream == null) {
                stream = getClass().getResourceAsStream("/potion.png"); // Fallback
            }

            if (stream != null) {
                img.setImage(new Image(stream));
                img.setPreserveRatio(true);
                img.setFitWidth(120);
                img.setFitHeight(120);
            }
        } catch (Exception e) {
            // Falla la carga de imagen por defecto
        }
    }

    // ---------------- Lógica de Interacción (Hover y Selección Visual) ----------------
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
     * Configura los MouseEvent handlers para cambiar los estilos de borde en
     * función de si está en hover o seleccionado.
     */
    private void configurarEfectosHover() {
        VBox[] boxes = {option1Box, option2Box, option3Box};

        for (VBox box : boxes) {
            applyBoxStyle(box, false);
            box.setUserData(Boolean.FALSE); // Almacena el estado de selección (false por defecto)

            // Manejador de borde al pasar el ratón
            box.setOnMouseEntered(e -> {
                boolean selected = ((Boolean) box.getUserData());
                if (!selected) {
                    // Solo aplica el estilo hover si no está ya seleccionado
                    box.setStyle(buildBoxStyle(false, true));
                }
            });

            // Restablecer estilo al salir del hover
            box.setOnMouseExited(e -> {
                boolean selected = ((Boolean) box.getUserData());
                applyBoxStyle(box, selected);
            });
        }
    }

    /**
     * Aplica el estilo CSS base a un VBox (fondo, padding y borde).
     */
    private void applyBoxStyle(VBox box, boolean selected) {
        box.setStyle(buildBoxStyle(selected, false));
        box.setPadding(new Insets(BOX_PADDING));
    }

    /**
     * Genera la cadena CSS para el estilo de la caja basándose en su estado.
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
     *
     * @param index Índice de la opción que debe marcarse como seleccionada.
     */
    private void seleccionarVisual(int index) {
        VBox[] boxes = {option1Box, option2Box, option3Box};

        for (int i = 0; i < boxes.length; i++) {
            boolean selected = (i == index);
            boxes[i].setUserData(Boolean.valueOf(selected)); // Actualiza el estado de selección
            applyBoxStyle(boxes[i], selected); // Aplica el estilo correspondiente
        }
    }

    // ---------------- Manejadores de Eventos FXML ----------------
    @FXML
    private void onOption1Clicked() {
        seleccionarVisual(0); // Actualiza el estilo visual (borde/fondo)
        select(0); // Ejecuta la lógica del juego (añadir al inventario y cerrar)
    }

    @FXML
    private void onOption2Clicked() {
        seleccionarVisual(1);
        select(1);
    }

    @FXML
    private void onOption3Clicked() {
        seleccionarVisual(2);
        select(2);
    }

    /**
     * Lógica final de selección: añade el consumible al inventario del jugador
     * y notifica al invocador para cerrar el diálogo.
     *
     * @param index Índice del consumible elegido en el array `options`.
     */
    private void select(int index) {
        Consumable chosen = options[index];

        // 1. Añadir el consumible al inventario
        player.getInventory().add(chosen);

        // 2. Ejecutar el callback (que generalmente cierra el Stage)
        if (onConsumableSelectedCallback != null) {
            onConsumableSelectedCallback.run();
        }
    }

    /**
     * Setter para la acción de callback que se ejecuta al seleccionar un
     * consumible.
     *
     * @param callback La acción Runnable a ejecutar.
     */
    public void setOnConsumableSelected(Runnable callback) {
        this.onConsumableSelectedCallback = callback;
    }

    /**
     * Manejador FXML que se usaría si hubiera un botón de "Confirmar
     * Selección". En este diseño, la lógica de `select()` ya se ejecuta al
     * hacer clic en la opción.
     */
    @FXML
    private void onConsumableButtonClicked() {
        // Ejecuta el callback, asumiendo que la opción deseada ya fue seleccionada y procesada
        if (onConsumableSelectedCallback != null) {
            onConsumableSelectedCallback.run();
        }
    }
}
