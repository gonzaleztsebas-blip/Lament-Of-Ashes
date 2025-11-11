/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.battle.*;
import com.lamentofashes.model.skills.Attack;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class BattleManager {
    private Player player;
    private EnemyFactory enemyFactory;
    private ArrayList<Enemy> enemies;
    private ArrayList<AttackResult> battleResults;
    
    public BattleManager(){
        this.player = new Player("Seb", 100, 100, 20);
        this.enemyFactory = new EnemyFactory();
        generateEnemies(3);
        this.battleResults = new ArrayList<>();
    }
    
    private void generateEnemies(int count){
        enemies = new ArrayList<>();
        for (int i = 0; i < count; i++){
            enemies.add(enemyFactory.generateEnemy());
        }
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
    
    public boolean isBattleOver(){
        return player.isDead() || enemies.isEmpty();
    }
    
    public AttackResult playerAttack(int attackIndex, int enemyIndex) { 
        Attack attack = player.getAttacks().get(attackIndex);
        if (player.getPower() < attack.getPowerCost()) {
            return new AttackResult(player.getName(), "-", attack.getName() + " (sin poder)", 0);
        }
        
        Enemy target = enemies.get(enemyIndex);
        int damage = attack.use();
        if(attack.getType() == 2){
            specialAttack(damage);
        }else{
            target.takeDamage(damage);
                if(target.isDead()){
                    enemies.remove(target);
                }
        }
        player.consumePower(attack.getPowerCost());
        
        AttackResult result = new AttackResult(
            player.getName(),
            attack.getName(),
            attack.getType()==2?"Todos":target.getName(),
            damage);
        battleResults.add(result);
        
        return result;
    }
    
    private void specialAttack(int damage){
        for(int i = 0; i < enemies.size(); i++){
           Enemy e = enemies.get(i);
           e.takeDamage(damage);
           
        }
        enemies.removeIf(Enemy::isDead);
    }

    public ArrayList<AttackResult> enemiesTurn() {
        ArrayList<AttackResult> enemiesResults = new ArrayList<>();
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            player.takeDamage(e.getBaseDamage());
            AttackResult result = new AttackResult(
                e.getName(),
                "Ataque",
                player.getName(),
                e.getBaseDamage());
            enemiesResults.add(result);
            battleResults.add(result);
        }
        return enemiesResults;
    }

    public ArrayList<AttackResult> getBattleResults(){
        return battleResults;
    }
    
    public AttackResult getAttackResults(int index){
        return battleResults.get(index);
    }
    
    public void clearBattleResults(){
        battleResults.clear();
    }
    
}
