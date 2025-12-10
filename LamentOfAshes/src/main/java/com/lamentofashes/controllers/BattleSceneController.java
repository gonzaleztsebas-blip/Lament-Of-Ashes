package com.lamentofashes.controllers;

import com.lamentofashes.controllers.ui.GameTerminal;
import com.lamentofashes.controllers.ui.BattleUIManager;
import com.lamentofashes.controllers.ui.SelectionDialogHelper;
import com.lamentofashes.controllers.ui.ConsumableDialogHelper;
import com.lamentofashes.controllers.ui.AnimationManager;
import com.lamentofashes.controllers.ui.BattleTurnManager;
import com.lamentofashes.App;
import com.lamentofashes.controllers.ui.BattleAttackManager;
import com.lamentofashes.controllers.ui.ButtonSoundManager;
import com.lamentofashes.logic.GameStateManager;
import com.lamentofashes.logic.battle.BattleManager;
import com.lamentofashes.logic.round.RoundManager;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.Enemy;
import com.lamentofashes.model.event.AttackResult;
import com.lamentofashes.model.event.DefenseResult;
import com.lamentofashes.model.skills.Attack;

import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Controlador principal para la escena de batalla. Coordina la UI, la lógica
 * del juego (a través de BattleManager) y las animaciones.
 */
public class BattleSceneController {

    // === 1. Componentes FXML de la UI ===
    @FXML
    private Label gameTerminal;        // Área de mensajes (terminal)
    @FXML
    private HBox enemyContainer;        // Contenedor de sprites de enemigos
    @FXML
    private ImageView heroe;           // Sprite del héroe
    @FXML
    private ProgressBar healthBar;     // Barra de vida del héroe
    @FXML
    private ProgressBar powerBar;      // Barra de poder (maná) del héroe
    @FXML
    private ImageView lightAttack;     // Botón de Ataque 1
    @FXML
    private ImageView heavyAttack;     // Botón de Ataque 2
    @FXML
    private ImageView specialAttack;   // Botón de Ataque 3 (Área)
    @FXML
    private ImageView defend;          // Botón de Defensa
    @FXML
    private ImageView consumablesButton;// Botón de Consumibles
    @FXML
    private Label attack1Cost, attack1Damage; // Info de Ataque 1
    @FXML
    private Label attack2Cost, attack2Damage; // Info de Ataque 2
    @FXML
    private Label attack3Cost, attack3Damage; // Info de Ataque 3
    @FXML
    private Label healthLabel, powerLabel;    // Texto de HP/Mana

    // === 2. Managers y Dependencias ===
    private GameTerminal terminal;
    private BattleManager battleManager;     // Lógica de batalla del juego
    private GameStateManager gameState;       // Estado global del juego
    private AnimationManager animationManager;// Gestión de animaciones
    private BattleUIManager uiManager;       // Gestión de la interfaz de batalla (barras, sprites)
    private BattleAttackManager attackManager; // Gestión de los botones de ataque
    private BattleTurnManager turnManager;   // Gestión de la secuencia de turnos

    // === 3. Estado de Selección del Jugador ===
    private Font pixelFont;
    private int selectedAttackIndex = -1;
    private Enemy selectedEnemy = null;
    private Attack selectedAttack = null;
    private boolean isProcessingAction = false; // Bloquea la UI durante animaciones/turnos

    /**
     * Método de inicialización llamado por JavaFX al cargar la escena.
     */
    public void initialize() {
        gameState = GameStateManager.getInstance();
        RoundManager roundManager = gameState.getCurrentRoundManager();

        if (roundManager == null) {
            // Manejo de error si no hay un manager de ronda activo
            return;
        }

        battleManager = roundManager.getCurrentBattleManager();

        if (battleManager == null) {
            // Manejo de error si no hay un manager de batalla activo
            return;
        }

        cargarFuente();
        inicializarManagers();
        configurarInterfaz();
        aplicarEfectosSonido();

        // Configuración inicial del terminal, sin callbacks de fin de typing
        terminal.setTypingCallbacks(null, null);
    }

    // === 4. Métodos de Configuración Inicial ===
    /**
     * Aplica efectos de sonido a los botones de acción del jugador.
     */
    private void aplicarEfectosSonido() {
        ButtonSoundManager.applyToNode(lightAttack);
        ButtonSoundManager.applyToNode(heavyAttack);
        ButtonSoundManager.applyToNode(specialAttack);
        ButtonSoundManager.applyToNode(defend);
        ButtonSoundManager.applyToNode(consumablesButton);
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
            // Si falla la carga, deja la fuente por defecto
            pixelFont = null;
        }
    }

    /**
     * Inicializa todas las clases manager con sus dependencias FXML.
     */
    private void inicializarManagers() {
        terminal = new GameTerminal(gameTerminal);
        animationManager = new AnimationManager();
        uiManager = new BattleUIManager(
                healthBar, powerBar, healthLabel, powerLabel,
                heroe, enemyContainer, pixelFont
        );
        attackManager = new BattleAttackManager(
                lightAttack, heavyAttack, specialAttack, defend, consumablesButton,
                attack1Cost, attack1Damage, attack2Cost, attack2Damage,
                attack3Cost, attack3Damage
        );
        // BattleTurnManager necesita BattleManager, Terminal, AnimationManager y UIManager
        turnManager = new BattleTurnManager(
                battleManager, terminal, animationManager, uiManager
        );
    }

    /**
     * Configura los elementos interactivos de la UI.
     */
    private void configurarInterfaz() {
        // Carga el sprite del héroe y genera los contenedores de enemigos en la UI
        uiManager.cargarSpriteHero();
        uiManager.generarEnemigosUI(
                battleManager.getAliveEnemies(),
                // Callback que se ejecuta al hacer clic en un enemigo
                enemy -> onEnemyClicked(enemy)
        );

        Player player = battleManager.getPlayer();
        // Configura los handlers de clic para los botones de ataque, defensa y consumibles
        attackManager.configurarBotonesAtaque(
                player,
                index -> onAttackSelected(index),
                () -> onDefendSelected(),
                () -> abrirVentanaConsumibles()
        );

        // Muestra el estado inicial del héroe y el mensaje de inicio de ronda
        uiManager.actualizarBarrasHero(player);
        mostrarMensajeInicio();
    }

    // === 5. Manejadores de Eventos de la UI (Player Actions) ===
    /**
     * Manejador al seleccionar un ataque.
     *
     * @param attackIndex El índice del ataque seleccionado (0, 1, o 2).
     */
    private void onAttackSelected(int attackIndex) {
        if (isProcessingAction) {
            return; // Ignorar si ya se está ejecutando una acción
        }

        Player player = battleManager.getPlayer();
        ArrayList<Attack> attacks = player.getWeapon().getAttacks();

        if (attackIndex < 0 || attackIndex >= attacks.size()) {
            return; // Índice de ataque inválido
        }

        selectedAttack = attacks.get(attackIndex);
        selectedAttackIndex = attackIndex;

        // Comprobar coste de maná
        if (player.getPower() < selectedAttack.getPowerCost()) {
            terminal.printError("No tienes suficiente mana!");
            resetearSeleccion();
            return;
        }

        terminal.print("> " + selectedAttack.getName() + " seleccionado");

        // Si ya había un enemigo seleccionado, ejecuta el ataque inmediatamente
        if (selectedEnemy != null) {
            ejecutarAtaque();
        } else {
            // Si no, espera la selección del objetivo
            terminal.print("Elige un objetivo...");
        }
    }

    /**
     * Manejador al hacer clic en un enemigo para seleccionarlo como objetivo.
     *
     * @param enemy El objeto Enemy clicado.
     */
    private void onEnemyClicked(Enemy enemy) {
        if (isProcessingAction) {
            return; // Ignorar si ya se está ejecutando una acción
        }

        if (enemy == null || enemy.isDead()) {
            terminal.printError("Enemigo no válido!");
            return;
        }

        if (!battleManager.getAliveEnemies().contains(enemy)) {
            terminal.printError("El enemigo ya no está en batalla!");
            return;
        }

        selectedEnemy = enemy;
        terminal.print("> Objetivo: " + enemy.getName());

        // Si ya había un ataque seleccionado, ejecuta el ataque inmediatamente
        if (selectedAttackIndex != -1) {
            ejecutarAtaque();
        } else {
            // Si no, espera la selección del ataque
            terminal.print("Elige un ataque...");
        }
    }

    /**
     * Manejador al seleccionar la acción de Defender.
     */
    private void onDefendSelected() {
        if (isProcessingAction) {
            return;
        }

        Player player = battleManager.getPlayer();
        if (player == null) {
            return;
        }

        // 1. Iniciar acción y bloquear UI
        isProcessingAction = true;
        deshabilitarTodo();

        // 2. Aplicar lógica de defensa/regeneración
        player.regeneratePower();
        uiManager.actualizarBarrasHero(player);
        DefenseResult result = battleManager.guard();

        // 3. Mostrar mensajes y animaciones
        terminal.printTyping(player.getName() + " se prepara para defender!");
        animationManager.animarDefensa(uiManager.getHeroSprite());
        terminal.printSystem(result.toString());

        // 4. Esperar y ejecutar el turno enemigo
        PauseTransition defenseDelay = new PauseTransition(Duration.millis(500));
        defenseDelay.setOnFinished(e -> {
            if (!battleManager.isBattleOver()) {
                turnManager.ejecutarTurnoEnemigo(() -> {
                    // Después de que el turno enemigo y sus animaciones terminen:
                    PauseTransition finalDelay = new PauseTransition(Duration.millis(300));
                    finalDelay.setOnFinished(ev -> {
                        isProcessingAction = false;
                        if (battleManager.isBattleOver()) {
                            finalizarBatalla();
                        } else {
                            habilitarTodo();
                        }
                    });
                    finalDelay.play();
                });
            } else {
                isProcessingAction = false;
                finalizarBatalla();
            }
        });
        defenseDelay.play();
    }

    // === 6. Lógica de Ejecución de Ataque ===
    /**
     * Inicia la secuencia de ataque (animación del jugador y llamada a
     * procesarAtaque).
     */
    private void ejecutarAtaque() {
        Player player = battleManager.getPlayer();

        if (isProcessingAction || selectedAttackIndex == -1 || selectedEnemy == null) {
            return;
        }

        if (player.getPower() < selectedAttack.getPowerCost()) {
            terminal.printError("No tienes suficiente mana!");
            resetearSeleccion();
            return;
        }

        int enemyIndex = obtenerIndiceEnemigo(selectedEnemy);

        if (enemyIndex == -1) {
            terminal.printError("Enemigo no encontrado!");
            resetearSeleccion();
            return;
        }

        // Bloquear UI e iniciar turno del jugador
        isProcessingAction = true;
        deshabilitarTodo();

        // Aplicar coste/regeneración de maná antes de la animación
        player.regeneratePower();
        uiManager.actualizarBarrasHero(player);

        // Animación de ataque del jugador
        animationManager.animarAtaqueJugador(uiManager.getHeroSprite(), () -> {
            // Callback: Ejecutar la lógica del ataque una vez que la animación visual termine
            procesarAtaque(enemyIndex);
        });
    }

    /**
     * Procesa la lógica de daño en BattleManager y maneja la respuesta.
     *
     * @param enemyIndex El índice del enemigo objetivo.
     */
    private void procesarAtaque(int enemyIndex) {
        // Ejecutar la lógica central de daño
        AttackResult result = battleManager.playerAttack(selectedAttackIndex, enemyIndex);

        ArrayList<Enemy> enemies = battleManager.getEnemies();
        if (enemyIndex >= enemies.size()) {
            resetearSeleccion();
            isProcessingAction = false;
            habilitarTodo();
            return;
        }

        Enemy target = enemies.get(enemyIndex);
        boolean enemyDied = target.isDead();
        boolean isAreaAttack = (selectedAttackIndex == 2); // Ataque Especial = Ataque de Área

        // Mostrar mensajes iniciales
        terminal.printSeparator();
        terminal.printTyping(result.getCharacter() + " usa " + result.getAttackName() + "!");

        if (isAreaAttack) {
            // Lógica para Ataque de Área: animar daño en todos los objetivos
            terminal.printTyping("¡Ataque de área!");
            animarAtaqueArea(() -> {
                mostrarResultadosAtaque(result, target, enemyDied);
            });
        } else {
            // Lógica para Ataque de Objetivo Único
            terminal.printTyping("Causa " + result.getEffect() + " de daño a " + result.getTarget() + "!");
            mostrarResultadosAtaque(result, target, enemyDied);
        }
    }

    /**
     * Muestra las animaciones de daño y actualizaciones de UI después del
     * ataque.
     */
    private void mostrarResultadosAtaque(AttackResult result, Enemy target, boolean enemyDied) {
        // Mostrar el resultado de daño
        terminal.printTyping("Causa " + result.getEffect() + " de daño!");

        BattleUIManager.EnemyUIElement uiElement = uiManager.getEnemyUIElement(selectedEnemy);
        if (uiElement != null) {
            if (enemyDied) {
                // Animación de Muerte y eliminación del sprite
                terminal.printTyping(target.getName() + " ha sido derrotado!");
                animationManager.animarMuerte(uiElement.container, () -> {
                    uiManager.eliminarEnemigoUI(selectedEnemy, null);
                });
                selectedEnemy = null;
            } else {
                // Animación de daño y actualización de barra de vida
                animationManager.animarDamage(uiElement.sprite);
                terminal.printTyping(target.getName() + " - HP: "
                        + target.getHealth() + "/" + target.getMaxHealth());
                uiManager.actualizarEnemigoUI(target);
            }
        }

        // Actualizar estados de enemigos que hayan muerto por daño colateral de área
        ArrayList<Enemy> enemies = battleManager.getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy.isDead() && enemy != target) {
                BattleUIManager.EnemyUIElement deadElement = uiManager.getEnemyUIElement(enemy);
                if (deadElement != null) {
                    terminal.printTyping(enemy.getName() + " ha sido derrotado!");
                    animationManager.animarMuerte(deadElement.container, () -> {
                        uiManager.eliminarEnemigoUI(enemy, null);
                    });
                }
            }
        }

        uiManager.actualizarBarrasHero(battleManager.getPlayer());

        // Continuar la batalla con un delay antes del turno enemigo
        PauseTransition delay = new PauseTransition(Duration.millis(enemyDied ? 1500 : 1000));
        delay.setOnFinished(e -> {
            if (battleManager.isBattleOver()) {
                resetearSeleccion();
                isProcessingAction = false;
                finalizarBatalla();
            } else {
                // Iniciar el turno enemigo
                turnManager.ejecutarTurnoEnemigo(() -> {
                    isProcessingAction = false;
                    if (battleManager.isBattleOver()) {
                        finalizarBatalla();
                    } else {
                        resetearSeleccion();
                        habilitarTodo(); // Habilitar controles para el siguiente turno del jugador
                    }
                });
            }
        });
        delay.play();
    }

    /**
     * Realiza la animación de daño en todos los enemigos vivos.
     *
     * @param onComplete Callback al finalizar la animación.
     */
    private void animarAtaqueArea(Runnable onComplete) {
        ArrayList<Enemy> aliveEnemies = battleManager.getAliveEnemies();

        // Animar daño y actualizar UI para todos los enemigos (sin esperar)
        for (Enemy enemy : aliveEnemies) {
            BattleUIManager.EnemyUIElement uiElement = uiManager.getEnemyUIElement(enemy);
            if (uiElement != null) {
                animationManager.animarDamage(uiElement.sprite);
                uiManager.actualizarEnemigoUI(enemy);
            }
        }

        // Esperar a que la animación de daño simultánea termine
        PauseTransition delay = new PauseTransition(Duration.millis(600));
        delay.setOnFinished(e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
        delay.play();
    }

    // === 7. Utilidades de Control de Flujo y UI ===
    /**
     * Busca el índice del objeto Enemy dentro de la lista de enemigos del
     * BattleManager.
     *
     * @param enemy El objeto Enemy.
     * @return El índice del enemigo o -1 si no se encuentra.
     */
    private int obtenerIndiceEnemigo(Enemy enemy) {
        ArrayList<Enemy> enemies = battleManager.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i) == enemy) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Abre la ventana modal para usar consumibles.
     */
    private void abrirVentanaConsumibles() {
        if (isProcessingAction) {
            return;
        }

        ConsumableDialogHelper.abrirVentanaConsumibles(
                battleManager, terminal, pixelFont,
                () -> uiManager.actualizarBarrasHero(battleManager.getPlayer()), // Callback: Actualizar barras de héroe
                () -> resetearSeleccion(), // Callback: Resetear selección
                () -> {
                    // Callback: Reconfigurar botones de ataque (si fuera necesario)
                    Player player = battleManager.getPlayer();
                    attackManager.configurarBotonesAtaque(
                            player,
                            index -> onAttackSelected(index),
                            () -> onDefendSelected(),
                            () -> abrirVentanaConsumibles()
                    );
                },
                () -> finalizarBatalla(), // Callback: Finalizar batalla
                animationManager,
                uiManager,
                turnManager
        );
    }

    /**
     * Muestra el mensaje de inicio de ronda (Ronda N o Ronda de Jefe).
     */
    private void mostrarMensajeInicio() {
        terminal.printSeparator();
        boolean isBoss = gameState.isBossRound();
        if (isBoss) {
            terminal.printTyping("RONDA DE JEFE!");
        } else {
            terminal.printTyping("Ronda " + gameState.getCurrentRound());
        }
        terminal.print("Enemigos: " + battleManager.getAliveEnemies().size());
        terminal.printSeparator();
    }

    /**
     * Maneja la transición final de la batalla (Victoria o Game Over).
     */
    private void finalizarBatalla() {
        Player player = battleManager.getPlayer();
        terminal.printSeparator();

        if (player.isDead()) {
            // Game Over
            terminal.printTyping("Has sido derrotado...");
            terminal.printError("GAME OVER");
            gameState.endGame();

            PauseTransition delay = new PauseTransition(Duration.millis(2000));
            delay.setOnFinished(e -> {
                try {
                    App.setRoot("GameOverScene");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            delay.play();
            return;
        }

        // Victoria
        boolean isBoss = gameState.isBossRound();
        terminal.printTyping(isBoss ? "JEFE DERROTADO!" : "VICTORIA!");
        terminal.printSeparator();

        PauseTransition delay = new PauseTransition(Duration.millis(2000));
        delay.setOnFinished(e -> {
            // Determinar la siguiente fase (Selección de Consumibles o Mejoras)
            GameStateManager.GamePhase nextPhase = gameState.determineNextPhase();

            switch (nextPhase) {
                case CONSUMABLE_SELECTION:
                    SelectionDialogHelper.abrirSeleccionConsumibles(gameState, player);
                    break;

                case UPGRADE_SELECTION:
                    SelectionDialogHelper.abrirSeleccionMejoras(gameState, player);
                    break;

                default:
                    // En caso de que se necesite avanzar directamente a la siguiente batalla
                    gameState.advanceRound();
                    try {
                        App.setRoot("BattleScene");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        });
        delay.play();
    }

    /**
     * Deshabilita todos los controles de acción del jugador.
     */
    private void deshabilitarTodo() {
        attackManager.deshabilitarTodo();
        uiManager.setEnemigosHabilitados(false);
    }

    /**
     * Habilita todos los controles de acción del jugador.
     */
    private void habilitarTodo() {
        Player player = battleManager.getPlayer();
        attackManager.configurarBotonesAtaque(
                player,
                index -> onAttackSelected(index),
                () -> onDefendSelected(),
                () -> abrirVentanaConsumibles()
        );
        uiManager.setEnemigosHabilitados(true);
    }

    /**
     * Restablece las variables de selección de ataque y enemigo.
     */
    private void resetearSeleccion() {
        selectedAttackIndex = -1;
        selectedEnemy = null;
        selectedAttack = null;
    }
}
