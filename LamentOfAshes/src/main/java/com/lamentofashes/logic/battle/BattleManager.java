/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.battle;
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
    private ArrayList<Enemy> enemies;
    private ArrayList<Event> battleResults;
    private EnemyAI ai;
    
    public BattleManager(ArrayList<Enemy> enemies, Player player){
        this.player = player;
        this.enemies = enemies;
        this.battleResults = new ArrayList<>();
        this.ai = new EnemyAI();
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
            return new AttackResult(player.getName(), attack.getName() + " (sin poder)", "-",  "0", false);
        }
        
        Enemy target = enemies.get(enemyIndex);
        
        int damage = attack.use();
        if(attack.getType() == AttackType.AREA){
            specialAttack(damage);
        }else{
            if(ai.decideAction(target) == EnemyAction.DEFEND){
                target.guard();
            }
            target.takeDamage(damage);
                if(target.isDead()){
                    enemies.set(enemyIndex, null);
                }
        }
        player.consumePower(attack.getPowerCost());
        
        AttackResult result = new AttackResult(
            player.getName(),
            attack.getName(),
            attack.getType()==AttackType.AREA?"Todos":target.getName(),
            Integer.toString(damage),
            false);
        battleResults.add(result);
        
        return result;
    }
    
    private void specialAttack(int damage){
        for(int i = 0; i < enemies.size(); i++){
           Enemy e = enemies.get(i);
           if(e == null){
               continue;
           } 
           if(ai.decideAction(e) == EnemyAction.DEFEND){
               e.guard();
           }
           e.takeDamage(damage);
           if(e.isDead()){
               enemies.set(i, null);
           }
           
        }
    }

    public ArrayList<Event> enemiesTurn() {
        ArrayList<Event> enemiesResults = new ArrayList<>();
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if(e == null){
                continue;
            }
            
            if(e.isGuarding()){
                e.stopGuarding();
                DefenseResult result = new DefenseResult(e.getName(), Double.toString(e.getDefenseReduction() * 100));
                enemiesResults.add(result);
                battleResults.add(result);
                continue;
            }
            
            int damage = e.calculateDamage();
            player.takeDamage(damage);
            AttackResult result = new AttackResult(
                e.getName(),
                "Ataque",
                player.getName(),
                Integer.toString(damage),
                damage > e.getBaseDamage());
            enemiesResults.add(result);
            battleResults.add(result);
            
            e.stopGuarding();
        }
        return enemiesResults;
    }

    public ConsumableResult useConsumable(int consumableIndex){
        Consumable c = player.getInventory().get(consumableIndex);
        if(c == null){
            return new ConsumableResult(player.getName(), "no existe el consumible", "0", 1);
        }
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