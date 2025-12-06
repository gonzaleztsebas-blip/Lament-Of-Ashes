/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.factorys;

import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.equipable.*;
import java.util.Random;

/**
 *
 * @author ASUS
 */
public class UpgradeFactory {
    private int round;
    private Random random;
    private EquipableType[] types;
    private Player player;

    public UpgradeFactory(int round, Player player){
        this.round = round;
        this.player = player;
        random = new Random();
        types = new EquipableType[]{EquipableType.WEAPON, EquipableType.ARMOR, EquipableType.SHIELD};
    }

    public Upgrade[] generateUpgrades(){
        Upgrade[] upgrades = new Upgrade[2];
        int number;

        for(int i = 0; i < 2; i++){
            number = random.nextInt(3);

            while(i == 1 && upgrades[i-1].getType() == types[number]){
                number = random.nextInt(3);
            }

            Upgrade upgrade;
            switch(number){
                case 0: 
                    upgrade = generateWeaponUpgrade(); 
                    break;
                case 1: 
                    upgrade = generateArmorUpgrade();  
                    break;
                case 2: 
                    upgrade = generateShieldUpgrade(); 
                    break;
                default: throw new IllegalStateException("Número fuera de rango: " + number);
            }

            upgrades[i] = upgrade;
        }

        return upgrades;
    }

    private double getMult(Quality quality){
        switch(quality){
            case COMMON: 
                return 1.0;
            case RARE:   
                return 1.15;
            case EPIC:   
                return 1.3;
            default:     
                return 1.0;
        }
    }

    private Upgrade generateWeaponUpgrade(){
        Quality q = player.getWeapon().getQuality();
        double mult = getMult(q);

        int statUpgrade = (int)((3 + round) * mult);
        double passiveUpgrade = (0.02 + (round * 0.002)) * mult;

        passiveUpgrade = roundDoubles(passiveUpgrade);

        return new Upgrade(EquipableType.WEAPON, statUpgrade, passiveUpgrade);
    }

    private Upgrade generateArmorUpgrade(){
        Quality q = player.getArmor().getQuality();
        double mult = getMult(q);

        int statUpgrade = (int)((5 + (round * 2)) * mult);
        double passiveUpgrade = (0.02 + (round * 0.005)) * mult;

        passiveUpgrade = roundDoubles(passiveUpgrade);

        return new Upgrade(EquipableType.ARMOR, statUpgrade, passiveUpgrade);
    }

    private Upgrade generateShieldUpgrade(){
        Quality q = player.getShield().getQuality();
        double mult = getMult(q);

        int statUpgrade = (int)((1 + (round * 0.5)) * mult);
        double passiveUpgrade = (0.5 + (round * 0.2)) * mult;

        passiveUpgrade = roundDoubles(passiveUpgrade);

        return new Upgrade(EquipableType.SHIELD, statUpgrade, passiveUpgrade);
    }

    private double roundDoubles(double num){
        return (int)(num * 100.0) / 100.0;
    }
}

