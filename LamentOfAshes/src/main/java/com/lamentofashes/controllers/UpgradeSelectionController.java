package com.lamentofashes.controllers;

import com.lamentofashes.logic.GameStateManager;
import com.lamentofashes.logic.factorys.UpgradeFactory;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.equipable.EquipableType;
import com.lamentofashes.model.item.equipable.Upgrade;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Controlador para la escena de selección de mejoras (Upgrades). Muestra dos
 * opciones de mejora generadas por UpgradeFactory y permite al jugador
 * seleccionar una para aplicarla a sus atributos.
 */
public class UpgradeSelectionController implements Initializable {

    // === Componentes FXML ===
    @FXML
    private Label titulo;

    // Tarjeta de Upgrade 1
    @FXML
    private VBox upgradeCard1;
    @FXML
    private ImageView upgradeIcon1;
    @FXML
    private Label upgradeName1;
    @FXML
    private Label upgradeStat1;
    @FXML
    private Label upgradePassive1;
    @FXML
    private Button selectButton1;

    // Tarjeta de Upgrade 2
    @FXML
    private VBox upgradeCard2;
    @FXML
    private ImageView upgradeIcon2;
    @FXML
    private Label upgradeName2;
    @FXML
    private Label upgradeStat2;
    @FXML
    private Label upgradePassive2;
    @FXML
    private Button selectButton2;

    // === Variables de Estado ===
    private List<Upgrade> upgrades = new ArrayList<>();
    private GameStateManager gameState;
    private Player player;
    private int round;
    private Font pixelFont;

    // Almacena la mejora seleccionada para que el caller pueda acceder a ella si es necesario
    private Upgrade selectedUpgrade = null;

    // Callback para notificar al caller (probablemente un helper de diálogo) que la selección ha terminado
    private Runnable onUpgradeSelectedCallback;

    /**
     * Inicializa el controlador al cargar el FXML.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameStateManager.getInstance();
        cargarFuente();
        // Aplica el efecto de hover de escala (ScaleTransition)
        addHoverEffect(upgradeCard1);
        addHoverEffect(upgradeCard2);
        // Configura los efectos de borde para hover y selección (cambio de color)
        configurarEfectosHover();
    }

    /**
     * Establece el callback a ejecutar cuando se selecciona un upgrade.
     */
    public void setOnUpgradeSelected(Runnable callback) {
        this.onUpgradeSelectedCallback = callback;
    }

    // === EFECTO HOVER DE ESCALA ===
    /**
     * Aplica una transición de escala suave al entrar/salir el ratón del VBox.
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

    // === BORDES Y SELECCIÓN VISUAL (IGUAL QUE CONSUMABLES) ===
    // Definiciones de colores para la estilización del borde y fondo
    private final String BOX_BG = "#8c6b96";
    private final String BOX_BG_SELECTED = "#6f5485";
    private final String BORDER_NORMAL = "#fbf5ef";
    private final String BORDER_HOVER = "#f2d3ab";
    private final String BORDER_SELECTED = "#f2d3ab";

    /**
     * Configura los manejadores de eventos para los cambios de estilo de borde
     * en las tarjetas de mejora al pasar el ratón o al ser seleccionadas.
     */
    private void configurarEfectosHover() {
        VBox[] boxes = {upgradeCard1, upgradeCard2};

        for (VBox box : boxes) {
            // UserData se usa para almacenar el estado de selección (true/false)
            box.setUserData(Boolean.FALSE);
            applyBoxStyle(box, false);

            box.setOnMouseEntered(e -> {
                boolean selected = (Boolean) box.getUserData();
                if (!selected) {
                    // Solo aplica el estilo hover si no está seleccionado
                    box.setStyle(buildBoxStyle(false, true));
                }
            });

            box.setOnMouseExited(e -> {
                boolean selected = (Boolean) box.getUserData();
                // Restaura el estilo: normal si no está seleccionado, o seleccionado si lo está
                applyBoxStyle(box, selected);
            });
        }
    }

    /**
     * Aplica el estilo base (normal o seleccionado) a un VBox.
     */
    private void applyBoxStyle(VBox box, boolean selected) {
        box.setStyle(buildBoxStyle(selected, false));
    }

    /**
     * Construye la cadena de estilo CSS para el VBox, determinando color de
     * fondo y borde basado en el estado.
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
     * Actualiza el estado visual de selección para las tarjetas.
     *
     * @param index El índice de la tarjeta que debe marcarse como seleccionada.
     */
    private void seleccionarVisual(int index) {
        VBox[] boxes = {upgradeCard1, upgradeCard2};

        for (int i = 0; i < boxes.length; i++) {
            boolean selected = (i == index);
            boxes[i].setUserData(selected);
            applyBoxStyle(boxes[i], selected);
        }
    }

    /**
     * Carga la fuente personalizada.
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
     * Método público para establecer los datos del jugador y la ronda. Esto
     * dispara la generación y carga de las mejoras.
     */
    public void setData(Player player, int round) {
        this.player = player;
        this.round = round;

        generateUpgrades();
        loadUpgradesIntoUI();
        aplicarFuentes();
    }

    /**
     * Genera las mejoras utilizando la UpgradeFactory.
     */
    private void generateUpgrades() {
        UpgradeFactory uf = new UpgradeFactory(round, player);
        // La fábrica devuelve un array de 2 mejoras
        Upgrade[] arr = uf.generateUpgrades();

        upgrades.clear();
        for (Upgrade u : arr) {
            if (u != null) {
                upgrades.add(u);
            }
        }

        // Manejo de caso donde se generan menos de 2 upgrades
        while (upgrades.size() < 2) {
            // Solución de fallback: si hay al menos una mejora generada, se duplica.
            if (!upgrades.isEmpty()) {
                upgrades.add(upgrades.get(0));
            } else {
                // Si no se pudo generar ninguna, la lógica de juego debería manejar este error.
                break;
            }
        }
    }

    /**
     * Carga las primeras dos mejoras generadas en los componentes FXML.
     */
    private void loadUpgradesIntoUI() {
        if (upgrades.size() >= 2) {
            loadCard(upgradeIcon1, upgradeName1, upgradeStat1, upgradePassive1, upgrades.get(0));
            loadCard(upgradeIcon2, upgradeName2, upgradeStat2, upgradePassive2, upgrades.get(1));
        } else {
            // Advertencia si la lista no tiene al menos 2 elementos
        }
    }

    /**
     * Rellena una tarjeta de mejora con la información del objeto Upgrade.
     */
    private void loadCard(ImageView icon, Label name, Label stat, Label passive, Upgrade upgrade) {
        // Cargar icono según el tipo de equipable
        cargarIcono(icon, upgrade.getType());

        // Nombre del tipo (e.g., "ARMA", "ARMADURA")
        String tipoCorrecto = obtenerNombreTipo(upgrade.getType());
        name.setText(tipoCorrecto);

        // Estadística principal (e.g., "+ X Daño")
        String statText = obtenerTextoEstadistica(upgrade);
        stat.setText(statText);

        // Pasiva (e.g., "+ Y% Crítico")
        String passiveText = obtenerTextoPasiva(upgrade);
        passive.setText(passiveText);
    }

    /**
     * Carga el icono de la imagen de un equipable.
     */
    private void cargarIcono(ImageView icon, EquipableType type) {
        String rutaIcono = obtenerRutaIcono(type);
        try {
            // Carga la imagen desde el stream de recursos
            Image image = new Image(getClass().getResourceAsStream(rutaIcono));
            icon.setImage(image);
        } catch (Exception e) {
            // Error al cargar imagen
        }
    }

    /**
     * Determina la ruta del icono a mostrar. Si el jugador ya tiene el
     * equipable del tipo, usa la imagen de ese equipable; de lo contrario, usa
     * un icono por defecto.
     */
    private String obtenerRutaIcono(EquipableType type) {
        switch (type) {
            case WEAPON:
                if (player.getWeapon() != null && player.getWeapon().getImagePath() != null) {
                    return player.getWeapon().getImagePath();
                }
                return "/icons/weapon_icon.png";

            case ARMOR:
                if (player.getArmor() != null && player.getArmor().getImagePath() != null) {
                    return player.getArmor().getImagePath();
                }
                return "/icons/armor_icon.png";

            case SHIELD:
                if (player.getShield() != null && player.getShield().getImagePath() != null) {
                    return player.getShield().getImagePath();
                }
                return "/icons/shield_icon.png";

            default:
                return "/icons/default_icon.png";
        }
    }

    /**
     * Traduce el EquipableType a una cadena de texto amigable.
     */
    private String obtenerNombreTipo(EquipableType type) {
        switch (type) {
            case WEAPON:
                return "ARMA";
            case ARMOR:
                return "ARMADURA";
            case SHIELD:
                return "ESCUDO";
            default:
                return type.name();
        }
    }

    /**
     * Genera la descripción de la mejora de estadística principal.
     */
    private String obtenerTextoEstadistica(Upgrade upgrade) {
        int statUpgrade = upgrade.getStatUpgrade();

        switch (upgrade.getType()) {
            case WEAPON:
                return "+ " + statUpgrade + " Daño";
            case ARMOR:
                return "+ " + statUpgrade + " Vida Máxima";
            case SHIELD:
                return "+ " + statUpgrade + " Defensa";
            default:
                return "+ " + statUpgrade;
        }
    }

    /**
     * Genera la descripción de la mejora pasiva/secundaria.
     */
    private String obtenerTextoPasiva(Upgrade upgrade) {
        // Se asume que passiveUpgrade es un valor raw (e.g., 0.1 para 10%)
        double passiveUpgrade = upgrade.getPassiveUpgrade();

        switch (upgrade.getType()) {
            case WEAPON:
                // Pasiva del arma: Probabilidad crítica
                return "+ " + String.format("%.0f", (passiveUpgrade * 100)) + "% Crítico";
            case ARMOR:
                // Pasiva de la armadura: Reducción de daño
                return "+ " + String.format("%.0f", (passiveUpgrade * 100)) + "% Reducción";
            case SHIELD:
                // Pasiva del escudo: Regeneración de poder/maná
                return "+ " + String.format("%.1f", passiveUpgrade) + " Regeneración Poder";
            default:
                return "+ " + String.format("%.1f", passiveUpgrade);
        }
    }

    /**
     * Aplica la fuente personalizada a todos los Labels y Buttons de la escena.
     */
    private void aplicarFuentes() {
        if (pixelFont != null) {
            String fontFamily = pixelFont.getFamily();

            titulo.setFont(Font.font(fontFamily, 28));

            // Definición de tamaños de fuente
            Font small = Font.font(fontFamily, 14);
            Font medium = Font.font(fontFamily, 22);
            Font button = Font.font(fontFamily, 20);

            // Aplicación a la tarjeta 1
            upgradeName1.setFont(medium);
            upgradeStat1.setFont(small);
            upgradePassive1.setFont(small);
            selectButton1.setFont(button);

            // Aplicación a la tarjeta 2
            upgradeName2.setFont(medium);
            upgradeStat2.setFont(small);
            upgradePassive2.setFont(small);
            selectButton2.setFont(button);
        }
    }

    // === EVENT HANDLERS DE SELECCIÓN ===
    /**
     * Manejador de click para el botón de la mejora 1.
     */
    @FXML
    private void onSelect1(ActionEvent e) {
        seleccionarVisual(0);
        selectUpgrade(0, (Node) e.getSource());
    }

    /**
     * Manejador de click para el botón de la mejora 2.
     */
    @FXML
    private void onSelect2(ActionEvent e) {
        seleccionarVisual(1);
        selectUpgrade(1, (Node) e.getSource());
    }

    /**
     * Aplica la mejora seleccionada al GameStateManager y notifica al caller.
     * La lógica de cerrar el diálogo está delegada al callback.
     */
    private void selectUpgrade(int index, Node source) {
        Upgrade chosen = upgrades.get(index);
        selectedUpgrade = chosen;

        // 1. Aplicar la lógica de la mejora al estado del juego
        gameState.applyUpgrade(chosen);

        // 2. Llamar al callback para que el caller (dialogo) cierre la ventana o continúe
        if (onUpgradeSelectedCallback != null) {
            onUpgradeSelectedCallback.run();
        }

        // 3. El Stage no se cierra aquí, sino en el callback.
    }

    /**
     * Método público para obtener el upgrade seleccionado (útil si el caller
     * necesita saber cuál se escogió).
     */
    public Upgrade getSelectedUpgrade() {
        return selectedUpgrade;
    }

    // === Métodos de Hover del Botón de Selección (Mantener igual para el estilo) ===
    @FXML
    private void onButton1Hover() {
        // Aplica el estilo de hover (color un poco más oscuro) al botón 1
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        selectButton1.setStyle(
                "-fx-background-color: #e0c199; "
                + "-fx-text-fill: #272744; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-size: 20px; "
                + "-fx-background-radius: 8; "
                + "-fx-border-radius: 8;"
        );
    }

    @FXML
    private void onButton1Exit() {
        // Restaura el estilo normal (color claro) al botón 1
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        selectButton1.setStyle(
                "-fx-background-color: #f2d3ab; "
                + "-fx-text-fill: #272744; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-size: 20px; "
                + "-fx-background-radius: 8; "
                + "-fx-border-radius: 8;"
        );
    }

    @FXML
    private void onButton2Hover() {
        // Aplica el estilo de hover (color un poco más oscuro) al botón 2
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        selectButton2.setStyle(
                "-fx-background-color: #e0c199; "
                + "-fx-text-fill: #272744; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-size: 20px; "
                + "-fx-background-radius: 8; "
                + "-fx-border-radius: 8;"
        );
    }

    @FXML
    private void onButton2Exit() {
        // Restaura el estilo normal (color claro) al botón 2
        String fontFamily = pixelFont != null ? pixelFont.getFamily() : "System";
        selectButton2.setStyle(
                "-fx-background-color: #f2d3ab; "
                + "-fx-text-fill: #272744; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: '" + fontFamily + "'; "
                + "-fx-font-size: 20px; "
                + "-fx-background-radius: 8; "
                + "-fx-border-radius: 8;"
        );
    }

}
