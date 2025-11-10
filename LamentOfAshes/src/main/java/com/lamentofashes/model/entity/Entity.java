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
    
    public Entity(String name, int maxHealth, int damage){
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damage = damage;                
    }
    
    public String getName(){
        return name;
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }
    
    public int getHealth(){
        return health;
    }
    
    public int getBaseDamage(){
        return damage;
    }
    
    public void takeDamage(int damage){
        health -= damage;
        if (health < 0){
            health = 0;
        }
    }
    
    public boolean isDead(){
        return health == 0;
    }
}
