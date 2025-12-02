/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.factorys;
import com.lamentofashes.model.item.equipable.*;
import java.util.Random;
/**
 *
 * @author ASUS
 */
public class EquipableFactory {
    private Random random = new Random();
    
    public Weapon createWeapon() {
        double roll = random.nextDouble();
        int baseStat = 5;
        double basePassive = 0.05;

        int stat = (int)(baseStat * (1 + roll));
        double passive = basePassive * (1 + (1 - roll));
        passive = Math.round(passive * 100) / 100.0;

        return new Weapon(stat, passive);
    }

    public Armor createArmor() {
        double roll = random.nextDouble();
        int baseStat = 25;
        double basePassive = 0.1;

        int stat = (int)(baseStat * (1 + roll));
        double passive = basePassive * (1 + (1 - roll));
        passive = Math.round(passive * 100) / 100.0;
        
        return new Armor(stat, passive);
    }

    public Helmet createHelmet() {
        double roll = random.nextDouble();
        int baseStat = 2;
        double basePassive = 2;

        int stat = (int)(baseStat * (1 + roll));
        double passive = basePassive * (1 + (1 - roll));
        passive = (int)(passive) / 1.0;

        return new Helmet(stat, passive);
    }
    
}
