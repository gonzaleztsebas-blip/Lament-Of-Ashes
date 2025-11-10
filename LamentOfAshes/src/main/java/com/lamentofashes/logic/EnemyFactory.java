/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.entity.enemy.*;
import java.util.Random;

/**
 *
 * @author ASUS
 */
public class EnemyFactory {
    private Random random = new Random();
    
    public Enemy generateEnemy(){
        Enemy enemy;
        int index = random.nextInt(3);
        switch (index){
            case 0:
                enemy =  new Skeleton();
                break;
            case 1:
                enemy = new DarkMage();
                break;
            case 2:
                enemy = new Ghost();
                break;
            default:
                enemy = new Skeleton();
                break;
        }
        return enemy;
    }
}
