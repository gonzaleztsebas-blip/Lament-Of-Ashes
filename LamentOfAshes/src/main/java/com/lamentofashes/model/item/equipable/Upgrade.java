/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;

import com.lamentofashes.model.entity.Player;

/**
 *
 * @author ASUS
 */
public class Upgrade {
    private EquipableType type;
    private int statUpgrade;
    private double passiveUpgrade;
    
    public Upgrade(EquipableType type, int statUpgrade, double passiveUpgrade){
        this.type = type;
        this.statUpgrade = statUpgrade;
        this.passiveUpgrade = passiveUpgrade;
    }
    
    public EquipableType getType(){
        return type;
    }
    
    public int getStatUpgrade(){
        return statUpgrade;
    }
    
    public double getPassiveUpgrade(){
        return passiveUpgrade;
    }
    
    public String toString(Player player){
        String string = "";
        Equipable equipable;
        switch (type) {
            case WEAPON:
                equipable = player.getWeapon();
                string += "Arma | Daño Extra: " + equipable.getStat() + " + " + statUpgrade + " | Probabilidad de Crítico: " + equipable.getPassive() + " + " ;
                if(equipable.getPassive() + passiveUpgrade > 0.6){
                    string += (0.6 - equipable.getPassive());
                }else{
                    string +=  passiveUpgrade;
                }
                break;
            case ARMOR:
                equipable = player.getArmor();
                string += "Armadura | Vida Extra: " + equipable.getStat() + " + " + statUpgrade + " | Reducción de daño al defenderse: " + equipable.getPassive() + " + ";
                if(equipable.getPassive() + passiveUpgrade > 0.8){
                    string += (0.8 - equipable.getPassive());
                }else{
                    string +=  passiveUpgrade;
                }
                break;
            case SHIELD:
                equipable = player.getShield();
                string += "Escudo | Regeneración de vida: " + equipable.getStat() + " + ";
                if(equipable.getStat() + statUpgrade > 50){
                    string += (50 - equipable.getStat());
                }else{
                    string +=  statUpgrade;
                }
                string += " | Regeneración de Poder: " + equipable.getPassive() + " + ";
                if(equipable.getPassive() + passiveUpgrade > 30){
                    string += (30 - equipable.getPassive());
                }else{
                    string +=  passiveUpgrade;
                }
                break;
            default:
                string += "";
        }
        return string;
    }
}
