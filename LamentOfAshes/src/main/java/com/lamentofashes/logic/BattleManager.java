/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.Enemy;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class BattleManager {
    private Player player;
    private EnemyFactory enemyFactory;
    private ArrayList<Enemy> enemies;
    
    public BattleManager(){
        this.player = new Player("Seb", 100, 20);
        this.enemyFactory = new EnemyFactory();
        generateEnemies(3);
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
    
    public void playerAttack(int enemyIndex) {
        Enemy target = enemies.get(enemyIndex);
        target.takeDamage(player.getBaseDamage());
        enemies.removeIf(Enemy::isDead);
    }

    public void enemiesTurn() {
        for(int i = 0; i < enemies.size(); i++) {
            player.takeDamage(enemies.get(i).getBaseDamage());
        }
    }

}
