/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity;

/**
 *
 * @author ASUS
 */
public class Entity {
    private String name;
    private int maxHealth;
    private int health;
    private int damage;
    private double defenseReduction;
    private boolean guarding;
    
    public Entity(String name, int maxHealth, int damage, double defense){
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damage = damage;
        this.defenseReduction = defense;
        this.guarding = false;
    }
    
    public String getName(){
        return name;
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }
    
    public void heal(int amount){
        health += amount;
        if(health > maxHealth){
            health = maxHealth;
        }
    }
    
    public int getHealth(){
        return health;
    }
    
    public int getBaseDamage(){
        return damage;
    }
    
    public double getDefenseReduction(){
        return defenseReduction;
    }
    
    public void guard(){
        guarding = true;
    }
    
    public void stopGuarding(){
        guarding = false;
    }
    
    public boolean isGuarding(){
        return guarding;
    }
    
    public void takeDamage(int damage){
        if(guarding){
            damage = (int)(damage * (1 - defenseReduction));
        }
        
        if (damage < 0){
            damage = 0;
        }
        
        health -= damage;
        if (health < 0){
            health = 0;
        }
    }
    
    public boolean isDead(){
        return health == 0;
    }
}
