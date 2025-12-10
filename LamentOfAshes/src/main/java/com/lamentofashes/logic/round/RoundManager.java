package com.lamentofashes.logic.round;

import com.lamentofashes.logic.battle.*;
import com.lamentofashes.logic.factorys.*;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;

/**
 * Maneja una ronda completa del juego
 * Ciclo: Ronda 1, 2, 3 (normales) → Ronda 4 (BOSS) → Ronda 5, 6, 7 (normales) → Ronda 8 (BOSS)...
 */
public class RoundManager {
    
    private Player player;
    private int roundNumber; // Número global de ronda (1, 2, 3, 4, 5, 6, 7, 8...)
    private ArrayList<WaveManager> waves;
    private int currentWaveIndex;
    private WaveManager currentWave;
    
    private int enemiesDefeatedThisRound = 0;
    private int bossesDefeatedThisRound = 0;
    private int damageDealtThisRound = 0;
    private int consumablesUsedThisRound = 0;
    
    public RoundManager(int roundNumber, Player player) {
        this.roundNumber = roundNumber;
        this.player = player;
        this.waves = new ArrayList<>();
        this.currentWaveIndex = 0;
        
        System.out.println("=== INICIANDO RONDA " + roundNumber + " ===");
        initializeRound();
    }
    
    /**
     * Determina si esta ronda es de boss
     * Boss aparece cada 4 rondas: 4, 8, 12, 16...
     */
    public boolean isBossRound() {
        return roundNumber % 4 == 0;
    }
    
    /**
     * Inicializa la ronda según sea normal o boss
     */
    private void initializeRound() {
        double hpMultiplier = 1.0 + ((roundNumber - 1) * 0.12);
        double damageMultiplier = 1.0 + ((roundNumber - 1) * 0.08);
        double criticMultiplier = 1.0 + ((roundNumber - 1) * 0.03);
        
        System.out.println("=== INICIALIZANDO RONDA " + roundNumber + " ===");
        System.out.println("isBossRound: " + isBossRound());
        
        if (isBossRound()) {
            System.out.println("¡RONDA DE BOSS!");
            // Para boss, solo 1 wave con el boss
            WaveManager bossWave = new WaveManager(
                0, // 0 indica boss en WaveManager
                player, 
                hpMultiplier, 
                damageMultiplier, 
                criticMultiplier
            );
            waves.add(bossWave);
        } else {
            // Para rondas normales: calcular posición en el ciclo (1, 2, o 3)
            int positionInCycle = ((roundNumber - 1) % 4) + 1; // 1, 2, o 3
            WaveManager wave = new WaveManager(
                positionInCycle, // 1=2 enemigos, 2=3 enemigos, 3=4 enemigos
                player,
                hpMultiplier,
                damageMultiplier,
                criticMultiplier
            );
            waves.add(wave);
        }
        
        currentWave = waves.get(0);
    }
    
    /**
     * Ejecuta la ronda completa (usado en modo consola)
     */
    public boolean playRound() {
        // Este método es para el modo consola original
        // En JavaFX, BattleSceneController maneja el flujo
        return true;
    }
    
    /**
     * Obtiene el BattleManager de la wave actual
     */
    public BattleManager getCurrentBattleManager() {
        if (currentWave == null) {
            throw new IllegalStateException("No hay wave actual");
        }
        return currentWave.getBattleManager();
    }
    
    /**
     * Obtiene los enemigos vivos de la wave actual
     */
    public ArrayList<Enemy> getCurrentEnemies() {
        if (currentWave == null) {
            return new ArrayList<>();
        }
        return currentWave.getAliveEnemies();
    }
    
    /**
     * Verifica si la ronda ha terminado (todos los enemigos muertos o jugador muerto)
     */
    public boolean isRoundOver() {
        if (currentWave == null) {
            return true;
        }
        
        BattleManager battle = currentWave.getBattleManager();
        return battle.isBattleOver();
    }
    
    /**
     * Genera recompensas de consumibles al terminar la wave
     */
    public ArrayList<Consumable> generateRewards() {
        if (currentWave == null) {
            return new ArrayList<>();
        }
        return currentWave.generateWaveRewards();
    }
    
    /**
     * Finaliza la ronda y recopila estadísticas
     */
    public void finalizeRound() {
        if (currentWave != null) {
            BattleManager battle = currentWave.getBattleManager();
            
            enemiesDefeatedThisRound = battle.getEnemiesKilled();
            damageDealtThisRound = battle.getTotalDamageDealt();
            consumablesUsedThisRound = battle.getConsumablesUsed();
            
            if (isBossRound()) {
                bossesDefeatedThisRound = 1;
            }
        }
    }
    
    // Getters para estadísticas
    public int getEnemiesDefeatedThisRound() {
        return enemiesDefeatedThisRound;
    }
    
    public int getBossesDefeatedThisRound() {
        return bossesDefeatedThisRound;
    }
    
    public int getDamageDealtThisRound() {
        return damageDealtThisRound;
    }
    
    public int getConsumablesUsedThisRound() {
        return consumablesUsedThisRound;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
}