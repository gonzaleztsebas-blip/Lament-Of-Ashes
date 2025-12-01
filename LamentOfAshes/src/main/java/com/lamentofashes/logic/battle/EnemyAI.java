/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.battle;
import com.lamentofashes.model.entity.enemy.Enemy;
import java.util.Random;
/**
 *
 * @author ASUS
 */
public class EnemyAI {
    private Random random;
    
    public EnemyAI(){
        random = new Random();
    }
    
    public EnemyAction decideAction(Enemy e){
        double healthRatio = e.getHealth()/e.getMaxHealth();
        double probability;
        
        if(healthRatio <= 0.15){
            probability = random.nextDouble();
            if(probability <= 0.9){
                return EnemyAction.DEFEND;
            }else{
                return EnemyAction.ATTACK;
            }
        }else if(healthRatio <= 0.5){
            probability = random.nextDouble();
            if(probability <= 0.5){
                return EnemyAction.DEFEND;
            }else{
                return EnemyAction.ATTACK;
            }
        }else{
            return EnemyAction.ATTACK;
        }
    }
}
