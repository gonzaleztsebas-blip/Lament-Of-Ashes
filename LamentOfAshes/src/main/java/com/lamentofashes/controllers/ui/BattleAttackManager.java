package com.lamentofashes.controllers.ui;

import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.skills.Attack;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import java.util.ArrayList;

/**
 * Clase que gestiona la interacción y el estado visual de los botones de ataque
 * y acción en la interfaz de la batalla (UI de botones de habilidad/comando).
 * Se encarga de habilitar/deshabilitar, aplicar efectos de hover y mostrar la
 * información de coste.
 */
public class BattleAttackManager {

    // Referencias a los botones de ataque (ImageViews)
    private final ImageView lightAttack;
    private final ImageView heavyAttack;
    private final ImageView specialAttack;
    private final ImageView defend;
    private final ImageView consumablesButton;

    // Referencias a las etiquetas de información de coste y daño para cada ataque
    private final Label attack1Cost, attack1Damage;
    private final Label attack2Cost, attack2Damage;
    private final Label attack3Cost, attack3Damage;

    /**
     * Constructor que inyecta todas las dependencias de la UI necesarias.
     */
    public BattleAttackManager(ImageView lightAttack, ImageView heavyAttack,
            ImageView specialAttack, ImageView defend, ImageView consumablesButton,
            Label attack1Cost, Label attack1Damage,
            Label attack2Cost, Label attack2Damage,
            Label attack3Cost, Label attack3Damage) {
        this.lightAttack = lightAttack;
        this.heavyAttack = heavyAttack;
        this.specialAttack = specialAttack;
        this.defend = defend;
        this.consumablesButton = consumablesButton;
        this.attack1Cost = attack1Cost;
        this.attack1Damage = attack1Damage;
        this.attack2Cost = attack2Cost;
        this.attack2Damage = attack2Damage;
        this.attack3Cost = attack3Cost;
        this.attack3Damage = attack3Damage;
    }

    /*
     * Nota: Los métodos 'configurarBotonAtaque', 'configurarBotonDefensa' y
     * 'configurarBotonConsumibles' parecen ser métodos iniciales de configuración
     * que fueron reemplazados por el método más completo 'configurarBotonesAtaque'.
     * Se mantienen como estaban en la fuente original.
     */
    private void configurarBotonAtaque(ImageView button, Attack attack, Player player,
            int attackIndex, java.util.function.Consumer<Integer> onAttackClick) {

        button.setDisable(false);
        button.setOpacity(1.0);

        // Verificar si tiene suficiente poder
        boolean tienePoder = player.getPower() >= attack.getPowerCost();
        if (!tienePoder) {
            button.setOpacity(0.5);
            aplicarEfectoDeshabilitado(button);
        } else {
            button.setOpacity(1.0);
            aplicarEfectosHover(button);
        }

        button.setOnMouseClicked(e -> {
            if (tienePoder) {
                onAttackClick.accept(attackIndex);
            }
        });
    }

    private void configurarBotonDefensa(ImageView button, Runnable onDefendClick) {
        button.setDisable(false);
        button.setOpacity(1.0);

        aplicarEfectosHover(button);

        button.setOnMouseClicked(e -> onDefendClick.run());
    }

    private void configurarBotonConsumibles(ImageView button, Runnable onConsumablesClick) {
        button.setDisable(false);
        button.setOpacity(1.0);

        aplicarEfectosHover(button);

        button.setOnMouseClicked(e -> onConsumablesClick.run());
    }

    // NUEVO: Aplicar efectos hover visuales
    /**
     * Configura la animación de hover para un ImageView: - Aumenta la escala
     * (ScaleTransition) al entrar. - Aplica brillo (ColorAdjust) al entrar. -
     * Restaura la escala y quita el brillo al salir.
     *
     * @param button El ImageView al que se le aplicará el efecto.
     */
    private void aplicarEfectosHover(ImageView button) {
        // Efecto de brillo al pasar el mouse
        ColorAdjust colorAdjust = new ColorAdjust();

        button.setOnMouseEntered(e -> {
            if (!button.isDisabled()) {
                // Animación de escala (zoom in)
                ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
                scale.setToX(1.1);
                scale.setToY(1.1);
                scale.play();

                // Efecto de brillo
                colorAdjust.setBrightness(0.3);
                button.setEffect(colorAdjust);
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isDisabled()) {
                // Restaurar escala (zoom out)
                ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();

                // Quitar brillo
                colorAdjust.setBrightness(0);
                button.setEffect(null);
            }
        });
    }

    /**
     * Aplica un efecto visual de "deshabilitado" a un ImageView (escala de
     * grises y oscuro). También limpia cualquier handler de mouse existente.
     *
     * @param button El ImageView a deshabilitar visualmente.
     */
    private void aplicarEfectoDeshabilitado(ImageView button) {
        ColorAdjust grayScale = new ColorAdjust();
        grayScale.setSaturation(-0.8); // Escala de grises
        grayScale.setBrightness(-0.3); // Oscurecer
        button.setEffect(grayScale);

        // Sin efectos hover cuando está deshabilitado
        button.setOnMouseEntered(null);
        button.setOnMouseExited(null);
    }

    /**
     * Deshabilita completamente todos los botones de acción, reseteando su
     * apariencia (opacidad, efectos y handlers de hover).
     */
    public void deshabilitarTodo() {
        // Deshabilitar la interacción
        lightAttack.setDisable(true);
        heavyAttack.setDisable(true);
        specialAttack.setDisable(true);
        defend.setDisable(true);
        consumablesButton.setDisable(true);

        // Reducir la opacidad
        lightAttack.setOpacity(0.5);
        heavyAttack.setOpacity(0.5);
        specialAttack.setOpacity(0.5);
        defend.setOpacity(0.5);
        consumablesButton.setOpacity(0.5);

        // Remover efectos visuales (como ColorAdjust)
        lightAttack.setEffect(null);
        heavyAttack.setEffect(null);
        specialAttack.setEffect(null);
        defend.setEffect(null);
        consumablesButton.setEffect(null);

        // Remover handlers de hover
        lightAttack.setOnMouseEntered(null);
        lightAttack.setOnMouseExited(null);
        heavyAttack.setOnMouseEntered(null);
        heavyAttack.setOnMouseExited(null);
        specialAttack.setOnMouseEntered(null);
        specialAttack.setOnMouseExited(null);
        defend.setOnMouseEntered(null);
        defend.setOnMouseExited(null);
        consumablesButton.setOnMouseEntered(null);
        consumablesButton.setOnMouseExited(null);
    }

    /**
     * Configura todos los botones de ataque y acción (Defensa, Consumibles)
     * basándose en el estado actual del jugador (principalmente su poder).
     *
     * @param player El objeto Player con los datos actuales.
     * @param onAttackSelected Callback al seleccionar un ataque (devuelve el
     * índice del ataque).
     * @param onDefendSelected Callback al seleccionar la defensa.
     * @param onConsumablesSelected Callback al seleccionar consumibles.
     */
    public void configurarBotonesAtaque(Player player,
            java.util.function.IntConsumer onAttackSelected,
            Runnable onDefendSelected,
            Runnable onConsumablesSelected) {
        if (player == null || player.getWeapon() == null) {
            return;
        }

        // Obtiene la lista de ataques del arma equipada
        ArrayList<Attack> attacks = player.getWeapon().getAttacks();

        // 1. Actualizar labels con el coste y daño de los ataques
        actualizarLabelsAtaques(attacks, player);

        // 2. Configurar los botones de ataque visuales (ImageViews)
        configurarAtaqueImageView(lightAttack, 0, attacks, player, onAttackSelected);
        configurarAtaqueImageView(heavyAttack, 1, attacks, player, onAttackSelected);
        configurarAtaqueImageView(specialAttack, 2, attacks, player, onAttackSelected);

        // 3. Configurar botón defender
        if (defend != null) {
            defend.setOnMouseClicked(e -> onDefendSelected.run());
            defend.setOpacity(1.0);
            defend.setDisable(false);
            defend.setStyle("-fx-cursor: hand;");
            aplicarEfectosHover(defend); // Aplicar hover
        }

        // 4. Configurar botón consumibles
        if (consumablesButton != null) {
            consumablesButton.setOnMouseClicked(e -> onConsumablesSelected.run());
            consumablesButton.setDisable(false);
            consumablesButton.setOpacity(1.0);
            consumablesButton.setStyle("-fx-cursor: hand;");
            aplicarEfectosHover(consumablesButton); // Aplicar hover
        }

        // 5. Habilitar el click también en las etiquetas de texto
        habilitarClickEnLabels(attacks, player, onAttackSelected);
    }

    /**
     * Deshabilita todos los botones de ataque. (Este método estaba vacío en la
     * fuente).
     */
    // Se asume que este método debe llamar a deshabilitarTodo() o similar, 
    // pero se deja como en la fuente original.
    // === MÉTODOS PRIVADOS ===
    /**
     * Actualiza el texto y el estado visual de los Labels de coste y daño.
     */
    private void actualizarLabelsAtaques(ArrayList<Attack> attacks, Player player) {
        // Actualiza el primer ataque
        actualizarLabelAtaque(attack1Cost, attack1Damage,
                attacks.size() > 0 ? attacks.get(0) : null, player);
        // Actualiza el segundo ataque
        actualizarLabelAtaque(attack2Cost, attack2Damage,
                attacks.size() > 1 ? attacks.get(1) : null, player);
        // Actualiza el tercer ataque
        actualizarLabelAtaque(attack3Cost, attack3Damage,
                attacks.size() > 2 ? attacks.get(2) : null, player);
    }

    /**
     * Establece el texto y el estado de un par de etiquetas (costo y daño)
     * basándose en el coste de poder del ataque y el poder actual del jugador.
     */
    private void actualizarLabelAtaque(Label costLabel, Label damageLabel,
            Attack attack, Player player) {
        if (attack == null || costLabel == null || damageLabel == null) {
            return;
        }

        int playerPower = player.getPower();
        boolean puedeUsar = playerPower >= attack.getPowerCost();

        // Establecer texto
        costLabel.setText(String.valueOf(attack.getPowerCost()));
        damageLabel.setText(attack.getMinDamage() + "-" + attack.getMaxDamage());

        // Ajustar opacidad y estado
        costLabel.setOpacity(puedeUsar ? 1.0 : 0.5);
        damageLabel.setOpacity(puedeUsar ? 1.0 : 0.5);

        // Deshabilitar la interacción si no tiene suficiente poder
        costLabel.setDisable(!puedeUsar);
        damageLabel.setDisable(!puedeUsar);

        // Aplicar estilo de cursor de mano
        costLabel.setStyle("-fx-cursor: hand;");
        damageLabel.setStyle("-fx-cursor: hand;");
    }

    /**
     * Configura un ImageView de ataque específico (botón de habilidad).
     * Establece el handler de click, opacidad, estado de deshabilitado y
     * cursor.
     */
    private void configurarAtaqueImageView(ImageView attackView, int index,
            ArrayList<Attack> attacks, Player player,
            java.util.function.IntConsumer onAttackSelected) {
        if (attackView == null) {
            return;
        }

        if (index < attacks.size()) {
            Attack attack = attacks.get(index);
            boolean canUse = player.getPower() >= attack.getPowerCost();

            attackView.setOnMouseClicked(e -> {
                // Solo se ejecuta la acción si el botón NO está deshabilitado
                if (!attackView.isDisabled()) {
                    onAttackSelected.accept(index);
                }
            });
            attackView.setOpacity(canUse ? 1.0 : 0.5);
            attackView.setDisable(!canUse); // Deshabilita si no hay suficiente poder
            attackView.setStyle("-fx-cursor: hand;");

            // Aplica efectos visuales según el estado
            if (canUse) {
                aplicarEfectosHover(attackView);
                attackView.setEffect(null); // Asegura que no tenga efecto deshabilitado
            } else {
                aplicarEfectoDeshabilitado(attackView);
            }
        } else {
            // Si el ataque no existe (fuera de límites), deshabilitar el botón
            attackView.setDisable(true);
            attackView.setOpacity(0.5);
            aplicarEfectoDeshabilitado(attackView);
        }
    }

    /**
     * Asocia el evento de click de las etiquetas de texto (costo/daño) a la
     * selección del ataque.
     */
    private void habilitarClickEnLabels(ArrayList<Attack> attacks, Player player,
            java.util.function.IntConsumer onAttackSelected) {
        // Attack 1
        if (attack1Cost != null && attack1Damage != null) {
            configurarLabelClick(attack1Cost, attack1Damage, 0, attacks, player, onAttackSelected);
        }

        // Attack 2
        if (attack2Cost != null && attack2Damage != null) {
            configurarLabelClick(attack2Cost, attack2Damage, 1, attacks, player, onAttackSelected);
        }

        // Attack 3
        if (attack3Cost != null && attack3Damage != null) {
            configurarLabelClick(attack3Cost, attack3Damage, 2, attacks, player, onAttackSelected);
        }
    }

    /**
     * Establece el handler de click para un par de Labels. Solo permite el
     * click si el ataque está disponible y el jugador tiene el poder.
     */
    private void configurarLabelClick(Label costLabel, Label damageLabel, int index,
            ArrayList<Attack> attacks, Player player,
            java.util.function.IntConsumer onAttackSelected) {
        // Determina si el ataque existe y si el jugador tiene el poder
        boolean habilitado = index < attacks.size()
                && player.getPower() >= attacks.get(index).getPowerCost();

        // Asigna el mismo handler de click a ambas etiquetas
        costLabel.setOnMouseClicked(e -> onAttackSelected.accept(index));
        damageLabel.setOnMouseClicked(e -> onAttackSelected.accept(index));

        // Configura estilos y estado de deshabilitado
        costLabel.setStyle("-fx-cursor: hand;");
        damageLabel.setStyle("-fx-cursor: hand;");
        costLabel.setDisable(!habilitado);
        damageLabel.setDisable(!habilitado);
    }

    /**
     * Método auxiliar para deshabilitar un solo ImageView.
     */
    private void deshabilitarImageView(ImageView imageView) {
        if (imageView != null) {
            imageView.setDisable(true);
            imageView.setOpacity(0.3);
            // Nota: Aquí se podría llamar a aplicarEfectoDeshabilitado para consistencia.
        }
    }

    /**
     * Método auxiliar para deshabilitar varios Labels.
     */
    private void deshabilitarLabels(Label... labels) {
        for (Label label : labels) {
            if (label != null) {
                label.setDisable(true);
                label.setOpacity(0.3);
            }
        }
    }

}
