/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.event.*;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.skills.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class BattleManager {
    private Player player;
    private EnemyFactory enemyFactory;
    private ArrayList<Enemy> enemies;
    private ArrayList<Event> battleResults;
    
    public BattleManager(){
        this.player = new Player("Seb", 100, 100, 20);
        this.enemyFactory = new EnemyFactory();
        generateEnemies(3);
        this.battleResults = new ArrayList<>();
        generateConsumables();
    }
    
    private void generateEnemies(int count){
        enemies = new ArrayList<>();
        for (int i = 0; i < count; i++){
            enemies.add(enemyFactory.generateEnemy());
        }
    }
    
    private void generateConsumables(){
        player.addConsumable(new SmallHealPotion());
        player.addConsumable(new BigHealPotion());
        player.addConsumable(new MinorPowerEssence());
        player.addConsumable(new MajorPowerEssence());
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
    
    public boolean isBattleOver(){
        boolean allEnemiesDead = true;
        for(int i = 0; i < enemies.size() && allEnemiesDead; i++){
            if(!(enemies.get(i) == null)){
                allEnemiesDead = false;
            }
        }
        return allEnemiesDead || player.isDead();
    }
    
    public AttackResult playerAttack(int attackIndex, int enemyIndex) { 
        Attack attack = player.getAttacks().get(attackIndex);
        if (player.getPower() < attack.getPowerCost()) {
            return new AttackResult(player.getName(), attack.getName() + " (sin poder)", "-",  "0");
        }
        
        Enemy target = enemies.get(enemyIndex);
        
        int damage = attack.use();
        if(attack.getType() == 2){
            specialAttack(damage);
        }else{
            target.takeDamage(damage);
                if(target.isDead()){
                    enemies.set(enemyIndex, null);
                }
        }
        player.consumePower(attack.getPowerCost());
        
        AttackResult result = new AttackResult(
            player.getName(),
            attack.getName(),
            attack.getType()==2?"Todos":target.getName(),
            Integer.toString(damage));
        battleResults.add(result);
        
        return result;
    }
    
    private void specialAttack(int damage){
        for(int i = 0; i < enemies.size(); i++){
           Enemy e = enemies.get(i);
           if(e == null){
               continue;
           } 
           e.takeDamage(damage);
           if(e.isDead()){
               enemies.set(i, null);
           }
           
        }
    }

    public ArrayList<AttackResult> enemiesTurn() {
        ArrayList<AttackResult> enemiesResults = new ArrayList<>();
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if(e == null){
                continue;
            }
            player.takeDamage(e.getBaseDamage());
            AttackResult result = new AttackResult(
                e.getName(),
                "Ataque",
                player.getName(),
                Integer.toString(e.getBaseDamage()));
            enemiesResults.add(result);
            battleResults.add(result);
        }
        return enemiesResults;
    }

    public ConsumableResult useConsumable(int consumableIndex){
        Consumable c = player.getInventory().get(consumableIndex);
        ConsumableResult result = new ConsumableResult(
            player.getName(),
            c.getName(),
            Integer.toString(c.getEffect()),
            c.getType()
        );
        battleResults.add(result);
        player.useConsumable(consumableIndex);
        return result;
    }
    
    public ArrayList<Event> getBattleResults(){
        return battleResults;
    }
    
    public Event getAttackResults(int index){
        return battleResults.get(index);
    }
    
    public void clearBattleResults(){
        battleResults.clear();
    }
    
}
