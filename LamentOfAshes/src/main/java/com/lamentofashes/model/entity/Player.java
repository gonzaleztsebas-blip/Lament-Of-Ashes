/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity;
import com.lamentofashes.model.item.equipable.*;
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
    private int healthRegeneration;
    
    private Weapon weapon;
    private Armor armor;
    private Helmet helmet;
    
    private ArrayList<Consumable> inventory;
    private int maxInventorySize = 5;
    
    
    public Player(String name, int maxHealth, int maxPower){
        super(name, maxHealth, 0, 0.5);
        this.powerRegeneration = 15;
        this.power = 0;
        this.healthRegeneration = 0;
        this.maxPower = maxPower;
        this.inventory = createEmptyInventory(maxInventorySize);
    }
    
    private ArrayList<Consumable> createEmptyInventory(int size) {
        ArrayList<Consumable> inv = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            inv.add(null);
        }
        return inv;
    }
    
    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }
    
    public void setArmor(Armor armor){
        this.armor = armor;
    }
    
    public void setHelmet(Helmet helmet){
        this.helmet = helmet;
    }
    
    public void updateStatsFromEquipables() {
        if (armor != null) {
            int bonusHealth = armor.getStat();
            setMaxHealth(getMaxHealth() + bonusHealth);

            double reduction = armor.getPassive();
            if (reduction > 0.8) {
                reduction = 0.8;
            }
            setDefenseReduction(reduction);
        }
        
        health = maxHealth;
        
        if (helmet != null) {
            int regenLife = healthRegeneration += helmet.getStat();
            if (regenLife > 50) {
                regenLife = 50;
            }
            healthRegeneration = regenLife;

            int regenPower = powerRegeneration += (int)(helmet.getPassive());
            if (regenPower > 30) {
                regenPower = 30;
            }
            powerRegeneration = regenPower;
        }
    }
    
    public void consumePower(int powerConsumed){
        power -= powerConsumed;
    }
    
    public void regenerateHealth(){
        health += healthRegeneration;
        if(health > maxHealth){
            health = maxHealth;
        }
    }
    
    public void regeneratePower(){
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
    
    public Weapon getWeapon(){
        return weapon;
    }
    
    public Armor getArmor(){
        return armor;
    }
    
    public Helmet getHelmet(){
        return helmet;
    }
}
