package com.lamentofashes.controllers;

import com.lamentofashes.controllers.ui.AnimationManager;
import com.lamentofashes.controllers.ui.BattleUIManager;
import com.lamentofashes.controllers.ui.BattleTurnManager;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.controllers.ui.GameTerminal;
import com.lamentofashes.logic.battle.BattleManager;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.consumable.Consumable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controlador para la ventana emergente de selección y uso de consumibles durante la batalla.
 * Muestra el inventario del jugador y permite ejecutar la lógica de turno tras el uso.
 */
public class ConsumiblesWindowController implements Initializable {

    // === Componentes FXML de la Ventana ===
    @FXML
    private VBox contenedorPrincipal; // Contenedor raíz de la escena
    @FXML
    private Label titulo;
    @FXML
    private VBox contenedorConsumibles; // Contenedor dinámico donde se listan los HBox de cada consumible
    @FXML
    private Button btnCancelar;

    // === Dependencias de la Lógica del Juego y Batalla ===
    private BattleManager battleManager;
    private GameTerminal terminal; // Para imprimir mensajes en la consola de la batalla
    private Font pixelFont;
    private Stage ventana; // Referencia al Stage (ventana) actual para poder cerrarla

    // === Callbacks (Runnables) para interactuar con el controlador principal de la Batalla ===
    private Runnable actualizarBarrasHero;     // Actualiza la vida/mana del héroe en la UI principal
    private Runnable resetearSeleccion;        // Limpia la selección de ataque/habilidad
    private Runnable configurarBotonesAtaque;  // Rehabilita y configura los botones de acción del héroe
    private Runnable finalizarBatalla;         // Llama a la lógica de fin de batalla

    // === Dependencias del Sistema de UI y Turnos (Inyectadas) ===
    private AnimationManager animationManager; // Gestiona las animaciones visuales (e.g., curación)
    private BattleUIManager uiManager;        // Provee acceso a componentes UI, como el sprite del héroe
    private BattleTurnManager turnManager;    // Orquesta la secuencia de turnos, crucial para el turno enemigo

    /**
     * Se llama automáticamente al cargar el FXML. Usado principalmente para inicializaciones básicas.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización automática después de cargar el FXML (actualmente vacío)
    }

    /**
     * Configura el controlador con todas las dependencias y referencias necesarias del entorno de batalla.
     */
    public void setData(
            BattleManager battleManager,
            GameTerminal terminal,
            Font pixelFont,
            Stage ventana,
            Runnable actualizarBarrasHero,
            Runnable resetearSeleccion,
            Runnable configurarBotonesAtaque,
            Runnable finalizarBatalla,
            AnimationManager animationManager,
            BattleUIManager uiManager,
            BattleTurnManager turnManager) {

        // Asignación de dependencias
        this.battleManager = battleManager;
        this.terminal = terminal;
        this.pixelFont = pixelFont;
        this.ventana = ventana;
        this.actualizarBarrasHero = actualizarBarrasHero;
        this.resetearSeleccion = resetearSeleccion;
        this.configurarBotonesAtaque = configurarBotonesAtaque;
        this.finalizarBatalla = finalizarBatalla;
        this.animationManager = animationManager;
        this.uiManager = uiManager;
        this.turnManager = turnManager;

        // Aplicar fuente personalizada al título y botón cancelar
        if (pixelFont != null) {
            Font tituloFont = Font.font(pixelFont.getFamily(), 18);
            Font botonFont = Font.font(pixelFont.getFamily(), 14);
            titulo.setFont(tituloFont);
            btnCancelar.setFont(botonFont);
        }

        cargarConsumibles();    // Rellena el contenedor con las opciones de consumibles
        ajustarTamañoVentana(); // Ajusta dinámicamente el tamaño de la ventana
    }

    /**
     * Obtiene el inventario del jugador y crea los componentes HBox para cada consumible.
     */
    private void cargarConsumibles() {
        Player player = battleManager.getPlayer();
        ArrayList<Consumable> consumibles = player.getInventory();

        contenedorConsumibles.getChildren().clear(); // Limpia los consumibles anteriores

        for (int i = 0; i < consumibles.size(); i++) {
            Consumable c = consumibles.get(i);
            if (c == null) {
                continue; // Ignorar espacios nulos en el inventario
            }

            int index = i;
            String consumableName = c.getName();
            String efectoTexto = c.getDescription();

            // Crea la fila HBox para este consumible y la añade al contenedor principal
            HBox consumibleBox = crearConsumibleBox(c, index, consumableName, efectoTexto);
            contenedorConsumibles.getChildren().add(consumibleBox);
        }
    }

    /**
     * Construye y estiliza el HBox que representa un único consumible en la lista.
     */
    private HBox crearConsumibleBox(Consumable c, int index, String consumableName, String efectoTexto) {
        HBox consumibleBox = new HBox(15);
        consumibleBox.setPadding(new Insets(10));
        // Estilo CSS en línea para el fondo y el borde del elemento de la lista
        consumibleBox.setStyle(
                "-fx-background-color: #3a3a5d; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
        consumibleBox.setAlignment(Pos.CENTER_LEFT);
        consumibleBox.setMinWidth(380);
        consumibleBox.setPrefWidth(380);

        // Creación de subcomponentes: Imagen, Info (Nombre/Desc) y Botón
        ImageView imagenConsumible = crearImagenConsumible(c);
        VBox infoBox = crearInfoBox(c);
        Button btnUsar = crearBotonUsar(index, consumableName, efectoTexto);

        consumibleBox.getChildren().addAll(imagenConsumible, infoBox, btnUsar);
        return consumibleBox;
    }

    /**
     * Crea y configura la ImageView del consumible, incluyendo manejo de errores de ruta.
     */
    private ImageView crearImagenConsumible(Consumable c) {
        ImageView imagenConsumible = new ImageView();
        imagenConsumible.setFitWidth(60);
        imagenConsumible.setFitHeight(60);
        imagenConsumible.setPreserveRatio(true);

        String rutaImagen = c.getImagePath();
        try {
            // Intenta cargar la imagen específica
            Image img = new Image(getClass().getResourceAsStream(rutaImagen));
            imagenConsumible.setImage(img);
        } catch (Exception e) {
            try {
                // Fallback: intenta cargar una imagen por defecto
                Image defaultImg = new Image(getClass().getResourceAsStream("/consumible_default.png"));
                imagenConsumible.setImage(defaultImg);
            } catch (Exception ex) {
                // Fallback final: si falla la imagen por defecto, usa un VBox de color
                imagenConsumible.setStyle("-fx-background-color: #f2d3ab; -fx-background-radius: 5;");
            }
        }

        return imagenConsumible;
    }

    /**
     * Crea el VBox que contiene el nombre y la descripción del consumible.
     */
    private VBox crearInfoBox(Consumable c) {
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(220);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label nombreLabel = new Label(c.getName());
        nombreLabel.setStyle("-fx-text-fill: #f2d3ab; -fx-font-weight: bold;");
        nombreLabel.setWrapText(true);
        if (pixelFont != null) {
            nombreLabel.setFont(Font.font(pixelFont.getFamily(), 16));
        }

        Label descripcionLabel = new Label(c.getDescription());
        descripcionLabel.setStyle("-fx-text-fill: #d0d0d0;");
        descripcionLabel.setWrapText(true);
        if (pixelFont != null) {
            descripcionLabel.setFont(Font.font(pixelFont.getFamily(), 12));
        }

        infoBox.getChildren().addAll(nombreLabel, descripcionLabel);
        return infoBox;
    }

    /**
     * Ajusta la altura de la ventana en función del número de consumibles cargados,
     * limitando la altura máxima para evitar desbordamiento.
     */
    private void ajustarTamañoVentana() {
        if (ventana != null) {
            int cantidadConsumibles = contenedorConsumibles.getChildren().size();
            double alturaPorConsumible = 90; // Altura estimada de cada fila
            double alturaBase = 200;         // Altura de título, padding, botón Cancelar
            double alturaTotal = alturaBase + (cantidadConsumibles * alturaPorConsumible);
            double alturaMaxima = 600;
            alturaTotal = Math.min(alturaTotal, alturaMaxima); // Aplica altura máxima

            ventana.setHeight(alturaTotal);
            ventana.setWidth(450);
        }
    }

    /**
     * Crea el botón 'USAR' para un consumible específico, aplicando estilos y handlers.
     */
    private Button crearBotonUsar(int index, String consumableName, String efectoTexto) {
        Button btnUsar = new Button("USAR");
        btnUsar.setMinWidth(80);
        btnUsar.setPrefWidth(80);
        btnUsar.setMinHeight(50);
        btnUsar.setPrefHeight(50);

        if (pixelFont != null) {
            btnUsar.setFont(Font.font(pixelFont.getFamily(), 14));
        }

        // Estilos normales
        btnUsar.setStyle(
                "-fx-background-color: #f2d3ab; "
                + "-fx-text-fill: #272744; "
                + "-fx-font-weight: bold; "
                + "-fx-background-radius: 8; "
                + "-fx-border-radius: 8;"
        );

        // Estilo al entrar (Hover)
        btnUsar.setOnMouseEntered(e -> {
            btnUsar.setStyle(
                    "-fx-background-color: #e0c199; " // Color más oscuro
                    + "-fx-text-fill: #272744; "
                    + "-fx-font-weight: bold; "
                    + "-fx-background-radius: 8; "
                    + "-fx-border-radius: 8;"
            );
            if (pixelFont != null) {
                btnUsar.setFont(Font.font(pixelFont.getFamily(), 14));
            }
        });

        // Estilo al salir (Restaurar)
        btnUsar.setOnMouseExited(e -> {
            btnUsar.setStyle(
                    "-fx-background-color: #f2d3ab; " // Color original
                    + "-fx-text-fill: #272744; "
                    + "-fx-font-weight: bold; "
                    + "-fx-background-radius: 8; "
                    + "-fx-border-radius: 8;"
            );
            if (pixelFont != null) {
                btnUsar.setFont(Font.font(pixelFont.getFamily(), 14));
            }
        });

        // Manejadores de sonido (Hover y Click)
        btnUsar.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, e -> {
            ButtonSoundManager.playHoverSound();
        });

        btnUsar.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            ButtonSoundManager.playClickSound();
        });

        // Acción principal: llama al método de uso del consumible
        btnUsar.setOnAction(e -> usarConsumible(index, consumableName, efectoTexto));

        return btnUsar;
    }

    /**
     * Maneja la secuencia completa de usar un consumible:
     * 1. Lógica del juego (curación/efecto y remoción del inventario).
     * 2. Feedback en la terminal.
     * 3. Animación de curación del héroe.
     * 4. Actualización de la UI después de la animación.
     * 5. Cierre de la ventana.
     * 6. Ejecución del turno enemigo y reanudación de la batalla.
     */
    private void usarConsumible(int index, String consumableName, String efectoTexto) {
        Player player = battleManager.getPlayer();

        // 1. Usar el consumible (aplica efecto y elimina del inventario)
        player.useConsumable(index);

        // 2. Mostrar mensajes en terminal
        terminal.printSeparator();
        terminal.printTyping("Usaste " + consumableName + ".");
        terminal.printTyping(efectoTexto);

        // 3. ANIMACIÓN DE CURACIÓN (ejecutada inmediatamente)
        if (animationManager != null && uiManager != null) {
            animationManager.animarCuracion(uiManager.getHeroSprite());
        }

        // 4. Temporizador 1: Espera 500ms (para que la animación de curación sea visible)
        PauseTransition healDelay = new PauseTransition(Duration.millis(500));
        healDelay.setOnFinished(e -> {
            // Actualiza barras de vida/mana del héroe
            if (actualizarBarrasHero != null) {
                actualizarBarrasHero.run();
            }

            // 5. Temporizador 2: Pequeño delay de 200ms antes de cerrar
            PauseTransition closeDelay = new PauseTransition(Duration.millis(200));
            closeDelay.setOnFinished(ev -> {
                // 6. Cerrar ventana del inventario
                ventana.close();

                // 7. TURNO ENEMIGO CON ANIMACIONES (si la batalla aún no ha terminado)
                if (!battleManager.isBattleOver()) {
                    if (turnManager != null) {
                        // Inicia la secuencia del turno enemigo
                        turnManager.ejecutarTurnoEnemigo(() -> {
                            // Este callback se ejecuta DESPUÉS de que el turno enemigo termina
                            // 8. Temporizador 3: Delay final de 300ms antes de habilitar controles
                            PauseTransition finalDelay = new PauseTransition(Duration.millis(300));
                            finalDelay.setOnFinished(evt -> {
                                // 9. Verificar estado del juego (héroe o enemigo muertos)
                                if (player.isDead() || battleManager.isBattleOver()) {
                                    if (finalizarBatalla != null) {
                                        finalizarBatalla.run();
                                    }
                                } else {
                                    // 10. Resetear selección y reconfigurar botones para el turno del héroe
                                    if (resetearSeleccion != null) {
                                        resetearSeleccion.run();
                                    }
                                    if (configurarBotonesAtaque != null) {
                                        configurarBotonesAtaque.run();
                                    }
                                }
                            });
                            finalDelay.play();
                        });
                    }
                } else {
                    // Si la batalla terminó justo después de usar el consumible (raro, pero posible)
                    if (resetearSeleccion != null) {
                        resetearSeleccion.run();
                    }
                    if (configurarBotonesAtaque != null) {
                        configurarBotonesAtaque.run();
                    }
                    if (battleManager.isBattleOver() && finalizarBatalla != null) {
                        finalizarBatalla.run();
                    }
                }
            });
            closeDelay.play();
        });
        healDelay.play(); // Inicia el primer delay
    }

    // ---------------- Manejadores de Eventos FXML del Botón CANCELAR ----------------

    /**
     * Manejador FXML para el botón Cancelar. Simplemente cierra la ventana de consumibles.
     */
    @FXML
    private void onCancelar() {
        ventana.close();
    }

    /**
     * Manejador FXML para el evento MouseEntered del botón Cancelar (Estilo Hover).
     */
    @FXML
    private void onCancelarHover() {
        btnCancelar.setStyle(
                "-fx-background-color: #3a3d5f; "
                + "-fx-text-fill: #fbf5ef; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
        if (pixelFont != null) {
            btnCancelar.setFont(Font.font(pixelFont.getFamily(), 14));
        }
    }

    /**
     * Manejador FXML para el evento MouseExited del botón Cancelar (Estilo Normal).
     */
    @FXML
    private void onCancelarExit() {
        btnCancelar.setStyle(
                "-fx-background-color: #494d7e; "
                + "-fx-text-fill: #fbf5ef; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #f2d3ab; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 0; "
                + "-fx-border-radius: 0;"
        );
        if (pixelFont != null) {
            btnCancelar.setFont(Font.font(pixelFont.getFamily(), 14));
        }
    }
}