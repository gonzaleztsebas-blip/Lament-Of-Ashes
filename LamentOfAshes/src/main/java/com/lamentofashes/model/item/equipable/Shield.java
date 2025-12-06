/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;

/**
 *
 * @author ASUS
 */
public class Shield extends Equipable{
    public Shield(Quality quality, int stat, double passive){
        super(quality, EquipableType.SHIELD, stat, passive);
    }
        
    @Override
    public void upgrade(int statUpgrade, double passiveUpgrade){
        if(statUpgrade > 50){
            statUpgrade = 50;
        }
        
        if(passiveUpgrade > 45){
            passiveUpgrade = 45;
        }
        
        super.upgrade(statUpgrade, passiveUpgrade);
    }
    
    @Override
    public String toString() {
        return "Escudo | Rareza: " + stringQuality() +  " | Regeneración de vida: " + getStat() + " | Regeneración de poder: " + getPassive();
    }
}
