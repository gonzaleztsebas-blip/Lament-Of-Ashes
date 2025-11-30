/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.round;
import com.lamentofashes.logic.battle.*;
import com.lamentofashes.logic.factorys.*;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;
/**
 *
 * @author ASUS
 */
public class WaveManager {
    private Player player;
    private ArrayList<Enemy> enemies;
    private BattleManager battleManager;
    private EnemyFactory enemyFactory;
    private ConsumableFactory consumableFactory;
    private BossFactory bossFactory;
    
    public WaveManager(int wave, Player player, double hpMultiplier, double damageMultiplier, double criticMultiplier){
        this.player = player;
        this.enemyFactory = new EnemyFactory();
        this.consumableFactory = new ConsumableFactory();
        this.bossFactory = new BossFactory();
        startWave(wave, hpMultiplier, damageMultiplier, criticMultiplier);
    }
    
    public void startWave(int wave, double hpMultiplier, double damageMultiplier, double criticMultiplier){
        if(numberOfEnemies(wave) == 0){
            generateBossFight(hpMultiplier, damageMultiplier, criticMultiplier);
        }else{
            generateEnemies(numberOfEnemies(wave), hpMultiplier, damageMultiplier, criticMultiplier);
        }
    }
    
    private int numberOfEnemies(int wave){
        switch (wave){
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 0;
            default:
                return -1;
        }
    }
    
    private void generateBossFight(double hpMultiplier, double damageMultiplier, double criticMultiplier){
        enemies = new ArrayList<>();
        enemies.add(bossFactory.generateBoss(hpMultiplier, damageMultiplier, criticMultiplier));
    }
    
    private void generateEnemies(int count, double hpMultiplier, double damageMultiplier, double criticMultiplier){
        enemies = new ArrayList<>();
        for (int i = 0; i < count; i++){
            enemies.add(enemyFactory.generateEnemy(hpMultiplier, damageMultiplier, criticMultiplier));
        }
    }
    
    public Player getPlayer(){
        return battleManager.getPlayer();
    }
    
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
    
    public ArrayList<Consumable> generateWaveRewards(){
        ArrayList<Consumable> rewards = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            rewards.add(consumableFactory.generateConsumable());
        }
        return rewards;
    }
}
