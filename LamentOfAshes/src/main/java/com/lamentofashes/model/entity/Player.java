/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity;
import com.lamentofashes.model.skills.*;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class Player extends Entity{
    private ArrayList<Attack> attacks;
    private int power;
    private int powerRegeneration;
    private int maxPower;
    
    
    public Player(String name, int maxHealth, int maxPower, int powerRegeneration){
        super(name, maxHealth, 0);
        this.powerRegeneration = powerRegeneration;
        this.power = 0;
        this.maxPower = maxPower;
        generateAttacks();
    }
    
    public void generateAttacks(){
        this.attacks = new ArrayList<>();
        attacks.add(new Attack("Ataque moderado", 0, 10, 20, 1));
        attacks.add(new Attack("Ataque fuerte", 20, 30, 40, 1));
        attacks.add(new Attack("Ataque especial", 50, 20, 30, 2));
    }
    
    public void consumePower(int powerConsumed){
        power -= powerConsumed;
    }
    
    public void regenetarePower(){
        power += powerRegeneration;
    }
    
    public int getPower(){
        return power;
    }
    
    public ArrayList<Attack> getAttacks(){
        return attacks;
    }
    
    public Attack getAttack(int index){
        return attacks.get(index);
    }
}
