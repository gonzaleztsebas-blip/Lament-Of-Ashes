package com.lamentofashes.logic;

import com.lamentofashes.App;
import com.lamentofashes.logic.factorys.UpgradeFactory;
import com.lamentofashes.logic.round.RoundManager;
import com.lamentofashes.model.GameStats;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.equipable.Armor;
import com.lamentofashes.model.item.equipable.Equipable;
import com.lamentofashes.model.item.equipable.EquipableType;
import com.lamentofashes.model.item.equipable.Shield;
import com.lamentofashes.model.item.equipable.Upgrade;
import com.lamentofashes.model.item.equipable.Weapon;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;

/**
 * Coordina el estado general del juego (ronda, fase) y la navegación entre escenas.
 * Mantiene la referencia a las lógicas centrales (GameEngine, RoundManager).
 */
public class GameStateManager {

    private static GameStateManager instance;

    private GameEngine gameEngine = new GameEngine();
    private ScoreManager scoreManager;
    private RoundManager currentRoundManager;

    private GamePhase currentPhase;
    private int equipmentStep; // 0=weapon, 1=armor, 2=shield

    private String playerName;
    private Player player; // Referencia directa al player para fácil acceso

    /**
     * Define las fases principales del flujo del juego.
     */
    public enum GamePhase {
        MAIN_MENU,
        ASK_NAME,
        EQUIPMENT_SELECTION,
        BATTLE,
        CONSUMABLE_SELECTION,
        UPGRADE_SELECTION,
        GAME_OVER
    }

    public GameStateManager() {
        currentPhase = GamePhase.MAIN_MENU;
        // Inicializar el ScoreManager (independiente del juego actual)
        this.scoreManager = new ScoreManager();
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    // --- Métodos de Inicialización y Estado ---

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Inicializa el objeto Player si aún no existe, usando el nombre guardado.
     */
    public void initializePlayerIfNeeded() {
        if (player == null) {
            player = new Player(playerName != null ? playerName : "Héroe");
        }
    }

    /**
     * Inicia un nuevo juego, reseteando GameEngine y estableciendo la fase inicial.
     * @param playerName Nombre del jugador.
     */
    public void startNewGame(String playerName) {
        this.gameEngine = new GameEngine();
        this.gameEngine.player = new Player(playerName);
        this.gameEngine.actualRound = 1;
        this.gameEngine.gameStats = new GameStats(playerName);
        this.gameEngine.startTime = System.currentTimeMillis();

        this.player = this.gameEngine.player;

        equipmentStep = 0;
        currentPhase = GamePhase.EQUIPMENT_SELECTION;
        // La escena debe ser cargada por el controlador llamante (AskNameController)
    }

    /**
     * Termina el juego cuando el jugador muere.
     * Finaliza la ronda, calcula el puntaje y transiciona a la pantalla Game Over.
     */
    public void endGame() {
        // 1. Finalizar RoundManager para transferir stats de la ronda de la derrota
        if (currentRoundManager != null) {
            advanceRound();
        }

        // 2. Guardar estadísticas finales
        if (gameEngine != null && gameEngine.gameStats != null) {
            // La ronda alcanzada es la ÚLTIMA completada. 
            // Como 'actualRound' fue incrementada en advanceRound, se resta 1.
            gameEngine.gameStats.setRoundReached(gameEngine.actualRound - 1);

            // Calcular y guardar el tiempo de juego
            long endTime = System.currentTimeMillis();
            int totalSeconds = (int) ((endTime - gameEngine.startTime) / 1000);
            gameEngine.gameStats.setPlayTime(totalSeconds);

            // Guardar la partida
            getScoreManager().saveScore(gameEngine.gameStats);
        }

        // 3. Transicionar a la pantalla de Game Over
        setCurrentPhase(GamePhase.GAME_OVER);
    }

    /**
     * Reinicia el estado del juego, manteniendo el ScoreManager.
     */
    public void reset() {
        this.gameEngine = null;
        this.currentRoundManager = null;
        this.player = null;
        this.playerName = null;
        this.currentPhase = GamePhase.MAIN_MENU;
        this.equipmentStep = 0;
    }

    // --- Gestión de Equipamiento ---

    /**
     * Obtiene opciones de equipamiento para la selección inicial.
     */
    public ArrayList<Equipable> getEquipmentOptions(EquipableType type) {
        return gameEngine.generateEquipments(type);
    }

    /**
     * Aplica el equipamiento seleccionado y avanza el paso de selección.
     */
    public void selectEquipment(Equipable equipment, EquipableType type) {
        switch (type) {
            case WEAPON:
                gameEngine.player.setWeapon((Weapon) equipment);
                break;
            case ARMOR:
                gameEngine.player.setArmor((Armor) equipment);
                break;
            case SHIELD:
                gameEngine.player.setShield((Shield) equipment);
                break;
        }
        equipmentStep++;
    }

    /**
     * Verifica si el proceso de selección inicial de equipamiento (Arma, Armadura, Escudo) ha terminado.
     */
    public boolean isEquipmentSelectionComplete() {
        return equipmentStep >= 3;
    }

    /**
     * Finaliza la selección de equipamiento e inicializa las estadísticas del jugador.
     */
    public void finalizeEquipment() {
        // 1. Aplicar las estadísticas base de los equipos iniciales
        gameEngine.player.applyEquipablesFirstTime();

        // 2. Cambiar fase (la transición de escena debe ser manejada por el controlador)
        this.currentPhase = GamePhase.BATTLE;
    }

    /**
     * Obtiene el tipo de equipamiento actual a seleccionar (WEAPON, ARMOR, SHIELD).
     */
    public EquipableType getCurrentEquipmentType() {
        switch (equipmentStep) {
            case 0:
                return EquipableType.WEAPON;
            case 1:
                return EquipableType.ARMOR;
            case 2:
                return EquipableType.SHIELD;
            default:
                return null;
        }
    }

    // --- Gestión de Rondas y Batalla ---

    /**
     * Crea el RoundManager para la batalla actual, inicializando enemigos.
     */
    public RoundManager createCurrentRound() {
        currentRoundManager = new RoundManager(gameEngine.actualRound, gameEngine.player);
        return currentRoundManager;
    }

    /**
     * Verifica si la ronda actual es una ronda de jefe (cada 4 rondas).
     */
    public boolean isBossRound() {
        int cycle = (gameEngine.actualRound - 1) % 4;
        return cycle == 3;
    }

    /**
     * Obtiene la cantidad de enemigos para la ronda actual basándose en el ciclo.
     * Ciclo: 2, 3, 4, 1 (Boss)
     */
    public int getEnemyCountForRound() {
        int cycle = (gameEngine.actualRound - 1) % 4; // 0, 1, 2, 3

        switch (cycle) {
            case 0:
                return 2; // Ronda 1, 5, 9,...
            case 1:
                return 3; // Ronda 2, 6, 10,...
            case 2:
                return 4; // Ronda 3, 7, 11,...
            case 3:
                return 1; // Ronda 4, 8, 12,... (Boss)
            default:
                return 2;
        }
    }

    /**
     * Avanza a la siguiente ronda: transfiere las estadísticas de la ronda terminada
     * a GameStats e incrementa el contador de ronda.
     */
    public void advanceRound() {
        if (currentRoundManager != null) {
            currentRoundManager.finalizeRound();

            // Transferir estadísticas de la ronda a GameStats
            if (gameEngine != null && gameEngine.gameStats != null) {
                GameStats stats = gameEngine.gameStats;
                stats.addEnemiesDefeated(currentRoundManager.getEnemiesDefeatedThisRound());
                stats.addBossesDefeated(currentRoundManager.getBossesDefeatedThisRound());
                stats.addDamage(currentRoundManager.getDamageDealtThisRound());
                stats.addConsumablesUsed(currentRoundManager.getConsumablesUsedThisRound());
            }
        }

        gameEngine.actualRound++;
    }

    /**
     * Determina la fase que sigue después de ganar una batalla, basándose en si fue una ronda de jefe.
     * Ciclo de transición: BATTLE -> CONSUMABLE / BATTLE -> UPGRADE
     */
    public GamePhase determineNextPhase() {
        // Después de cada batalla normal (no boss), toca consumible
        if (!isBossRound()) {
            return GamePhase.CONSUMABLE_SELECTION;
        }

        // Después de boss, toca upgrade
        return GamePhase.UPGRADE_SELECTION;
    }

    // --- Gestión de Upgrades y Consumibles ---

    /**
     * Aplica el upgrade seleccionado al equipamiento del jugador y refresca las estadísticas.
     */
    public void applyUpgrade(Upgrade upgrade) {
        switch (upgrade.getType()) {
            case WEAPON:
                gameEngine.player.getWeapon().upgrade(upgrade.getStatUpgrade(), upgrade.getPassiveUpgrade());
                break;
            case ARMOR:
                gameEngine.player.getArmor().upgrade(upgrade.getStatUpgrade(), upgrade.getPassiveUpgrade());
                break;
            case SHIELD:
                gameEngine.player.getShield().upgrade(upgrade.getStatUpgrade(), upgrade.getPassiveUpgrade());
                break;
        }
        gameEngine.player.refreshStatsFromEquipables();
        // Tras el upgrade, la siguiente fase (transición) es BATTLE
    }

    /**
     * Genera opciones de upgrade para la ronda actual.
     */
    public Upgrade[] generateUpgradeOptions() {
        UpgradeFactory upgradeFactory = new UpgradeFactory(gameEngine.actualRound, gameEngine.player);
        return upgradeFactory.generateUpgrades();
    }

    /**
     * Finaliza la selección de consumible y regresa a la batalla (misma ronda).
     */
    public void finishConsumableSelection() {
        setCurrentPhase(GamePhase.BATTLE);
    }

    // --- Getters ---

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public RoundManager getCurrentRoundManager() {
        return currentRoundManager;
    }

    public RoundManager getRoundManager() {
        return currentRoundManager;
    }

    public Player getPlayer() {
        // Asume que gameEngine ya fue inicializado por startNewGame()
        return gameEngine.player;
    }

    public int getCurrentRound() {
        return gameEngine.actualRound;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public GameStats getGameStats() {
        return gameEngine.gameStats;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    // --- Gestión de Transición de Escenas (Fases) ---

    private boolean isChangingPhase = false;

    /**
     * Establece la fase actual y dispara la carga de la escena correspondiente
     * si no es una fase modal (ej: selección de equipo/upgrade/consumible).
     * @param phase La nueva fase del juego.
     */
    public void setCurrentPhase(GamePhase phase) {
        // Evitar llamadas recursivas o múltiples en el mismo ciclo de cambio
        if (isChangingPhase || this.currentPhase == phase) {
            return;
        }

        isChangingPhase = true;
        this.currentPhase = phase;

        // Si la fase es un diálogo modal o selección inicial, el controlador debe abrir la ventana.
        if (phase == GamePhase.CONSUMABLE_SELECTION
                || phase == GamePhase.UPGRADE_SELECTION
                || phase == GamePhase.EQUIPMENT_SELECTION) {
            isChangingPhase = false;
            return;
        }

        // Carga la escena principal correspondiente
        Platform.runLater(() -> {
            try {
                switch (phase) {
                    case BATTLE:
                        App.getStage().getScene().setRoot(App.loadFXML("BattleScene"));
                        break;
                    case GAME_OVER:
                        App.getStage().getScene().setRoot(App.loadFXML("GameOverScene"));
                        break;
                    // Aquí se manejarían MAIN_MENU y ASK_NAME si se llama a este método para ellos
                    default:
                        break;
                }
            } catch (IOException ex) {
                // Manejo silencioso de la excepción de carga de FXML
            } finally {
                isChangingPhase = false;
            }
        });
    }
}