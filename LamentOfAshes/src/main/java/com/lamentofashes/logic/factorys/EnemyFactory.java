/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.factorys;
import com.lamentofashes.model.entity.enemy.*;
import java.util.Random;

/**
 *
 * @author ASUS
 */
public class EnemyFactory {
    private Random random = new Random();
    
    public Enemy generateEnemy(double hpMultiplier, double damageMultiplier, double criticMultiplier){
        Enemy enemy;
        int index = random.nextInt(3);
        switch (index){
            case 0:
                enemy =  new Skeleton(hpMultiplier, damageMultiplier, criticMultiplier);
                break;
            case 1:
                enemy = new DarkMage(hpMultiplier, damageMultiplier, criticMultiplier);
                break;
            case 2:
                enemy = new Ghost(hpMultiplier, damageMultiplier, criticMultiplier);
                break;
            default:
                enemy = new Skeleton(hpMultiplier, damageMultiplier, criticMultiplier);
                break;
        }
        return enemy;
    }
}
