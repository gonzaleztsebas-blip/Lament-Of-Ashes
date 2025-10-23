/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lament.of.ashes.Data_Layer.Characters;

/**
 *
 * @author ASUS
 */
public class character {
    private int health;
    private int damage;
    
    public int getHealth(){
        return health;
    }
    
    public int getDamage(){
        return damage;
    }
    
    public void takeDamage(int damage){
        health -= damage;
    }
}
