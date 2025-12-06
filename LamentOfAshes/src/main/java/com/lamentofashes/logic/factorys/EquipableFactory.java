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


    private Quality rollQuality() {
        double q = random.nextDouble();
        if (q < 0.75) return Quality.COMMON;
        if (q < 0.95) return Quality.RARE;
        return Quality.EPIC;
    }

    private double randomVariance() {
        return 0.9 + (random.nextDouble() * 0.2); // 0.9 a 1.1
    }

    public Weapon createWeapon() {
        Quality quality = rollQuality();

        int baseStat = 5;
        double basePassive = 0.05;

        double statMult;
        switch (quality) {
            case COMMON:
                statMult = 1.0;
                break;
            case RARE:
                statMult = 1.25;
                break;
            case EPIC:
                statMult = 1.5;
                break;
            default:
                statMult = 1.0;
        };

        double passiveMult;
        switch (quality) {
            case COMMON:
                passiveMult = 1.0;
                break;
            case RARE:
                passiveMult = 1.3;
                break;
            case EPIC:
                 passiveMult = 1.7;
                 break;
            default:
                passiveMult = 1.0;
        };

        int stat = (int) (baseStat * statMult * randomVariance());
        double passive = basePassive * passiveMult * randomVariance();
        passive = Math.round(passive * 100) / 100.0;

        return new Weapon(quality,stat, passive);
    }

    public Armor createArmor() {
        Quality quality = rollQuality();

        int baseStat = 25;
        double basePassive = 0.1;

        double statMult;
        switch (quality) {
            case COMMON:
                statMult = 1.0;
                break;
            case RARE:
                statMult = 1.2;
                break;
            case EPIC:
                statMult = 1.45;
                break;
            default:
                statMult = 1.0;
        };

        double passiveMult;
        switch (quality) {
            case COMMON:
                passiveMult = 1.0;
                break;
            case RARE:
                passiveMult = 1.25;
                break;
            case EPIC:
                 passiveMult = 1.5;
                 break;
            default:
                passiveMult = 1.0;
        };

        int stat = (int) (baseStat * statMult * randomVariance());
        double passive = basePassive * passiveMult * randomVariance();
        passive = Math.round(passive * 100) / 100.0;

        return new Armor(quality, stat, passive);
    }

    public Shield createShield() {
        Quality quality = rollQuality();

        int baseStat = 2;
        double basePassive = 10;

        double statMult;
        switch (quality) {
            case COMMON:
                statMult = 1.0;
                break;
            case RARE:
                statMult = 1.35;
                break;
            case EPIC:
                statMult = 1.7;
                break;
            default:
                statMult = 1.0;
        };

        double passiveMult;
        switch (quality) {
            case COMMON:
                passiveMult = 1.0;
                break;
            case RARE:
                passiveMult = 1.25;
                break;
            case EPIC:
                 passiveMult = 1.5;
                 break;
            default:
                passiveMult = 1.0;
        };

        int stat = (int) (baseStat * statMult * randomVariance());
        double passive = basePassive * passiveMult * randomVariance();
        passive = (int) passive;   // entero, como debe ser

        return new Shield(quality, stat, passive);
    }
}
