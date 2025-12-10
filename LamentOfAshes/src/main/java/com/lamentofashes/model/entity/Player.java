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
    
    private int baseMaxHealth = 100;
    private int baseHealthRegen = 0;
    private int basePowerRegen = 0;
    private double baseReduction = 0.0;

    
    private Weapon weapon;
    private Armor armor;
    private Shield shield;
    
    private ArrayList<Consumable> inventory;
    private int maxInventorySize = 5;
    
    
    public Player(String name){
        super(name, 100, 0, 0.5);
        this.maxPower = 100;
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
    
    public void setShield(Shield shield){
        this.shield = shield;
    }
    
    public void applyEquipablesFirstTime() {
        if (armor != null) {
            int bonusHealth = armor.getStat();
            setMaxHealth(getMaxHealth() + bonusHealth);

            double reduction = armor.getPassive();
            setDefenseReduction(reduction);
        }

        health = maxHealth;

        if (shield != null) {
            healthRegeneration = shield.getStat();
            powerRegeneration = (int) shield.getPassive();
        }
    }
    
    public void refreshStatsFromEquipables() {
        maxHealth = baseMaxHealth;
        healthRegeneration = baseHealthRegen;
        powerRegeneration = basePowerRegen;
        defenseReduction = baseReduction;

        if (armor != null) {
            maxHealth += armor.getStat();
            health += armor.getStat();
            defenseReduction = armor.getPassive();
        }

        if (shield != null) {
            healthRegeneration += shield.getStat();
            powerRegeneration += (int) shield.getPassive();
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
    
    public Shield getShield(){
        return shield;
    }

    public int getMaxPower() {
        return maxPower;
    }
}
