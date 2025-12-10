package com.lamentofashes.logic.round;

import com.lamentofashes.logic.battle.*;
import com.lamentofashes.logic.factorys.*;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;

/**
 * Maneja una wave (oleada) de enemigos waveType: 0 = BOSS, 1-3 = rondas
 * normales con diferente cantidad de enemigos
 */
public class WaveManager {

    private Player player;
    private ArrayList<Enemy> enemies;
    private BattleManager battleManager;
    private EnemyFactory enemyFactory;
    private ConsumableFactory consumableFactory;
    private BossFactory bossFactory;

    /**
     * @param waveType 0=BOSS, 1=2 enemigos, 2=3 enemigos, 3=4 enemigos
     */
    public WaveManager(int waveType, Player player, double hpMultiplier, double damageMultiplier, double criticMultiplier) {
        this.player = player;
        this.enemyFactory = new EnemyFactory();
        this.consumableFactory = new ConsumableFactory();
        this.bossFactory = new BossFactory();

        System.out.println("Creando WaveManager con waveType: " + waveType);
        startWave(waveType, hpMultiplier, damageMultiplier, criticMultiplier);

        // IMPORTANTE: Inicializar BattleManager DESPUÉS de generar enemigos
        this.battleManager = new BattleManager(enemies, player);

        System.out.println("WaveManager creado con " + enemies.size() + " enemigos");
    }

    private void startWave(int waveType, double hpMultiplier, double damageMultiplier, double criticMultiplier) {
        int enemyCount = numberOfEnemies(waveType);

        if (enemyCount == 0) {
            // Es boss
            generateBossFight(hpMultiplier, damageMultiplier, criticMultiplier);
        } else {
            // Es ronda normal
            generateEnemies(enemyCount, hpMultiplier, damageMultiplier, criticMultiplier);
        }
    }

    /**
     * Determina cuántos enemigos según el tipo de wave
     *
     * @param waveType 0=BOSS(0 normales), 1=2 enemigos, 2=3 enemigos, 3=4
     * enemigos
     */
    private int numberOfEnemies(int waveType) {
        switch (waveType) {
            case 0:
                return 0; // BOSS - se maneja aparte
            case 1:
                return 2; // Primera ronda del ciclo
            case 2:
                return 3; // Segunda ronda del ciclo
            case 3:
                return 4; // Tercera ronda del ciclo
            default:
                System.err.println("WaveType inválido: " + waveType);
                return 2; // Default fallback
        }
    }

    private void generateBossFight(double hpMultiplier, double damageMultiplier, double criticMultiplier) {
        enemies = new ArrayList<>();
        Enemy boss = bossFactory.generateBoss(hpMultiplier, damageMultiplier, criticMultiplier);
        enemies.add(boss);
        System.out.println(">>> BOSS GENERADO: " + boss.getName() + " (HP: " + boss.getMaxHealth()+ ")");
    }

    private void generateEnemies(int count, double hpMultiplier, double damageMultiplier, double criticMultiplier) {
        enemies = new ArrayList<>();
        System.out.println(">>> Generando " + count + " enemigos normales...");

        for (int i = 0; i < count; i++) {
            Enemy enemy = enemyFactory.generateEnemy(hpMultiplier, damageMultiplier, criticMultiplier);
            enemies.add(enemy);
            System.out.println("  - Enemigo " + (i + 1) + ": " + enemy.getName() + " (HP: " + enemy.getMaxHealth() + ")");
        }
    }

    public BattleManager getBattleManager() {
        return battleManager;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Retorna solo los enemigos que están vivos (sin nulls ni muertos)
     */
    public ArrayList<Enemy> getAliveEnemies() {
        ArrayList<Enemy> alive = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e != null && !e.isDead()) {
                alive.add(e);
            }
        }
        return alive;
    }

    public ArrayList<Consumable> generateWaveRewards() {
        ArrayList<Consumable> rewards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            rewards.add(consumableFactory.generateConsumable());
        }
        return rewards;
    }
}
