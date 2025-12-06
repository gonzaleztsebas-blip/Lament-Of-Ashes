/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;

/**
 *
 * @author ASUS
 */
public class Armor extends Equipable{
    public Armor(Quality quality, int stat, double passive){
        super(quality, EquipableType.ARMOR, stat, passive);
    }
    
    @Override
    public void upgrade(int statUpgrade, double passiveUpgrade){        
        if(passiveUpgrade > 0.8){
            passiveUpgrade = 0.8;
        }
        
        super.upgrade(statUpgrade, passiveUpgrade);
    }
    
    @Override
    public String toString() {
        return "Armadura | Rareza: " + stringQuality() + " | Vida Extra: " + getStat() + " | Reducción de daño al defenderse: " + getPassive();
    }
}
