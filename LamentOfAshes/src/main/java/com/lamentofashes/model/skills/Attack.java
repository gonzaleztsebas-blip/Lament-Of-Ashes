/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.skills;
import java.util.Random;
/**
 *
 * @author ASUS
 */
public class Attack {
    private Random random;
    private String name;
    private int powerCost;
    private int minDamage;
    private int maxDamage;
    
    //type 1: normal attack
    //type 2: area attack
    private int type;
    
    public Attack(String name, int powerCost, int minDamage, int maxDamage, int type){
        this.random = new Random();
        this.name = name;
        this.powerCost = powerCost;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.type = type;
    }
    
    public int use(){
        int finalDamage = random.nextInt(maxDamage - minDamage + 1) + minDamage;
        return finalDamage;
    }
    
    public String getName(){
        return name;
    }
    
    public int getPowerCost(){
        return powerCost;
    }
    
    public int getType(){
        return type;
    }
    
    @Override
    public String toString() {
        return name + " (Dano: " + minDamage + "-" + maxDamage + ", Costo de poder: " + powerCost + ")";
    }
}
