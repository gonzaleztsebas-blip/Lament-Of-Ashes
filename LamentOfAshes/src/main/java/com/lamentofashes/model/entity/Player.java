/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity;
import com.lamentofashes.model.skills.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class Player extends Entity{
    private int powerRegeneration;
    private int maxPower;
    private int power;
    
    private ArrayList<Attack> attacks;

    private ArrayList<Consumable> inventory;
    private int maxInventorySize = 5;
    
    
    public Player(String name, int maxHealth, int maxPower, int powerRegeneration){
        super(name, maxHealth, 0);
        this.powerRegeneration = powerRegeneration;
        this.power = 0;
        this.maxPower = maxPower;
        generateAttacks();
        this.inventory = createEmptyInventory(maxInventorySize);
    }
    
    public void generateAttacks(){
        this.attacks = new ArrayList<>();
        attacks.add(new Attack("Ataque moderado", 0, 10, 20, 1));
        attacks.add(new Attack("Ataque fuerte", 20, 30, 40, 1));
        attacks.add(new Attack("Ataque especial", 50, 20, 30, 2));
    }
    
    private ArrayList<Consumable> createEmptyInventory(int size) {
        ArrayList<Consumable> inv = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            inv.add(null);
        }
        return inv;
    }
    
    public void consumePower(int powerConsumed){
        power -= powerConsumed;
    }
    
    public void regenetarePower(){
        power += powerRegeneration;
        if(power > maxPower){
            power = maxPower;
        }
    }
    
    public void restorePower(int amount){
        power += amount;
        if(power > maxPower){
            power = maxPower;
        }
    }
    
    public void addConsumable(Consumable c){
        for(int i = 0; i < inventory.size(); i++){
            if(inventory.get(i) == null){
                inventory.set(i, c);
                return;
            }
        }
    }
    
    public void useConsumable(int index){
        Consumable consumable = inventory.get(index);
        consumable.apply(this);
        inventory.remove(consumable);
    }
    
    public ArrayList<Consumable> getInventory(){
        return inventory;
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
