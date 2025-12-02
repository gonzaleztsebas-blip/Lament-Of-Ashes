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
    public Armor(int stat, double passive){
        super(EquipableType.ARMOR, stat, passive);
    }
    
    @Override
    public String toString() {
        return "Armadura" + " | Vida Extra: " + getStat() + " | Reducción de daño al defenderse: " + getPassive();
    }
}
