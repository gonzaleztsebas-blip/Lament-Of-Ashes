/*
 * Clase de utilidad para abrir la ventana de consumibles.
 */
package com.lamentofashes.controllers.ui;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ConsumiblesWindowController;
import com.lamentofashes.logic.battle.BattleManager;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.consumable.Consumable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Helper para abrir ventana de consumibles en batalla como un diálogo modal.
 */
public class ConsumableDialogHelper {

    /**
     * Abre la ventana modal de selección y uso de consumibles. Pasa todas las
     * dependencias de la batalla al controlador de la ventana.
     */
    public static void abrirVentanaConsumibles(
            BattleManager battleManager,
            GameTerminal terminal,
            Font pixelFont,
            Runnable actualizarBarras,
            Runnable resetearSeleccion,
            Runnable configurarBotones,
            Runnable finalizarBatalla,
            AnimationManager animationManager,
            BattleUIManager uiManager,
            BattleTurnManager turnManager) {

        Player player = battleManager.getPlayer();
        ArrayList<Consumable> consumibles = player.getInventory();

        // Verificar si el jugador tiene algún consumible
        boolean tieneConsumibles = false;
        for (Consumable c : consumibles) {
            if (c != null) {
                tieneConsumibles = true;
                break;
            }
        }

        if (!tieneConsumibles) {
            terminal.printTyping("No tienes consumibles.");
            return;
        }

        try {
            // Carga el FXML de la ventana
            FXMLLoader loader = new FXMLLoader(
                    ConsumableDialogHelper.class.getResource(
                            "/com/lamentofashes/fxml/ConsumiblesWindow.fxml"
                    )
            );
            VBox root = loader.load();

            // Configura el Stage como modal
            Stage ventana = new Stage();
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setTitle("Consumibles");
            ventana.setResizable(false);

            // Carga el icono de la aplicación
            Image icon = new Image(App.class.getResourceAsStream("/ICON.png"));
            ventana.getIcons().add(icon);

            // Inyecta todas las dependencias en el controlador
            ConsumiblesWindowController controller = loader.getController();
            controller.setData(
                    battleManager, terminal, pixelFont, ventana,
                    actualizarBarras, resetearSeleccion,
                    configurarBotones, finalizarBatalla,
                    animationManager, uiManager, turnManager
            );

            Scene scene = new Scene(root);
            ventana.setScene(scene);
            // Muestra y espera a que la ventana se cierre
            ventana.showAndWait();
        } catch (IOException e) {
            // Manejo de error al cargar el FXML
            terminal.printSystem("Error al abrir ventana de consumibles: " + e.getMessage());
        }
    }
}
