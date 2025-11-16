/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity.enemy;
import com.lamentofashes.model.entity.Entity;
import java.util.Random;

/**
 *
 * @author ASUS
 */
public class Enemy extends Entity{
    private Random random = new Random();
    private double criticChance;
    
    
    public Enemy(String name, int maxHealth, int minHealth, int maxDamage, int minDamage, double criticChance){
        super(name,                 
              new Random().nextInt(maxHealth - minHealth + 1) + minHealth, 
              new Random().nextInt(maxDamage - minDamage + 1) + minDamage
        );
        this.criticChance = criticChance;
    }

    public int calculateDamage(){
        double probability = random.nextDouble();
        if(probability < criticChance){
            return (getBaseDamage() * 2);
        }
        return getBaseDamage();
    }
    
}