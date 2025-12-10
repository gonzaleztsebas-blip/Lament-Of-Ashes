package com.lamentofashes.controllers.ui;

import com.lamentofashes.App;
import com.lamentofashes.controllers.ConsumableSelectionController;
import com.lamentofashes.controllers.UpgradeSelectionController;
import com.lamentofashes.logic.GameStateManager;
import com.lamentofashes.model.entity.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Helper para abrir ventanas modales de selección (upgrades/consumables) al
 * final de la ronda. Controla la transición al siguiente BattleScene después de
 * una selección válida.
 */
public class SelectionDialogHelper {

    // Flag para evitar procesamiento doble en el Stage.setOnHidden
    private static boolean upgradeProcesado = false;
    private static boolean consumableProcesado = false;

    /**
     * Abre la ventana modal para que el jugador elija una mejora.
     */
    public static void abrirSeleccionMejoras(GameStateManager gameState, Player player) {
        upgradeProcesado = false;

        try {
            FXMLLoader loader = new FXMLLoader(
                    SelectionDialogHelper.class.getResource("/com/lamentofashes/fxml/UpgradeSelection.fxml")
            );
            Parent root = loader.load();

            UpgradeSelectionController controller = loader.getController();
            controller.setData(player, gameState.getCurrentRound());

            Stage stage = new Stage();
            stage.setTitle("Elige una mejora");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(App.getStage());

            Image icon = new Image(App.class.getResourceAsStream("/ICON.png"));
            stage.getIcons().add(icon);

            // Callback al seleccionar una mejora
            controller.setOnUpgradeSelected(() -> {
                upgradeProcesado = true;
                stage.close();
            });

            stage.show();

            // Lógica a ejecutar al cerrar la ventana (solo si se procesó una selección)
            stage.setOnHidden(e -> {
                if (!upgradeProcesado) {
                    return;
                }

                // 1. Avanzar la ronda
                gameState.advanceRound();

                // 2. Crear el RoundManager para la nueva ronda
                gameState.createCurrentRound();

                // 3. Cargar BattleScene
                Platform.runLater(() -> {
                    try {
                        App.setRoot("BattleScene");
                    } catch (IOException ex) {
                        // Manejo silencioso de error
                    }
                });
            });

        } catch (IOException ex) {
            // Manejo silencioso de error
        }
    }

    /**
     * Abre la ventana modal para que el jugador elija un consumible.
     */
    public static void abrirSeleccionConsumibles(GameStateManager gameState, Player player) {
        consumableProcesado = false;

        try {
            FXMLLoader loader = new FXMLLoader(
                    SelectionDialogHelper.class.getResource("/com/lamentofashes/fxml/ConsumableSelection.fxml")
            );
            Parent root = loader.load();

            ConsumableSelectionController controller = loader.getController();
            controller.setData(player);

            Stage stage = new Stage();
            stage.setTitle("Elige un Consumible!");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(App.getStage());

            Image icon = new Image(App.class.getResourceAsStream("/ICON.png"));
            stage.getIcons().add(icon);

            // Callback al seleccionar un consumible
            controller.setOnConsumableSelected(() -> {
                consumableProcesado = true;
                stage.close();
            });

            stage.show();

            // Lógica a ejecutar al cerrar la ventana (solo si se procesó una selección)
            stage.setOnHidden(e -> {
                if (!consumableProcesado) {
                    return;
                }

                // 1. Avanzar la ronda
                gameState.advanceRound();

                // 2. Crear el RoundManager para la nueva ronda
                gameState.createCurrentRound();

                // 3. Cargar BattleScene
                Platform.runLater(() -> {
                    try {
                        App.setRoot("BattleScene");
                    } catch (IOException ex) {
                        // Manejo silencioso de error
                    }
                });
            });

        } catch (IOException ex) {
            // Manejo silencioso de error
        }
    }
}
