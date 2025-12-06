/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.round;
import com.lamentofashes.logic.battle.*;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;
/**
 *
 * @author ASUS
 */
public class RoundManager {
    private int round;
    private int actualWave;
    private Player player;
    private WaveManager waveManager;
    
    private int enemiesDefeatedThisRound = 0;
    private int bossesDefeatedThisRound = 0;
    private int damageDealtThisRound = 0;
    private int consumablesUsedThisRound = 0;
    
    double hpMultiplier;
    double damageMultiplier;
    double criticMultiplier;

    
    public RoundManager(int round, Player player){
        this.round = round;
        this.player = player;
        this.actualWave = 1;
    }
    
    private void calculateMultipliers(){
        boolean isBoss = (actualWave == 4);
        
        if(isBoss){
            hpMultiplier = 1 + (0.1 * (round - 1));
            damageMultiplier = 1 + (0.2 * (round - 1));
            criticMultiplier = 1;
        } else {
            hpMultiplier = 1 + (0.15 * (round - 1));
            damageMultiplier = 1 + (0.1 * (round -1));
            criticMultiplier = 1 + (0.05 * (round - 1));
        }
    }
    
    public BattleManager createWave(){
        calculateMultipliers();
        
        waveManager = new WaveManager(actualWave, player, hpMultiplier, damageMultiplier, criticMultiplier);
        ArrayList<Enemy> enemies = waveManager.getEnemies();
        
        BattleManager battleManager = new BattleManager(enemies, player);
        return battleManager;
    }
   
    public boolean playRound(){
        while(actualWave <= 4){
            BattleManager battleManager = createWave();
            ConsoleBattle consoleBattle = new ConsoleBattle(battleManager);
            
            player = battleManager.getPlayer();
            boolean result = consoleBattle.startBattle();
            
            damageDealtThisRound += battleManager.getTotalDamageDealt();
            consumablesUsedThisRound += battleManager.getConsumablesUsed();
            
            if(actualWave == 4) {
                if(result) {
                    bossesDefeatedThisRound++;
                }
            } else {
                enemiesDefeatedThisRound += battleManager.getEnemiesKilled();
            }
            
            if(!result){
                return false;
            }
     
            if(actualWave < 4){
               ArrayList<Consumable> consumables = waveManager.generateWaveRewards();
               ConsumableMenu consumableMenu = new ConsumableMenu(consumables);
               Consumable selectedConsumable = consumableMenu.selectConsumable();
               player.addConsumable(selectedConsumable);
            }
            
            actualWave++;
        }
        
        return true;
    }
    
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
}
