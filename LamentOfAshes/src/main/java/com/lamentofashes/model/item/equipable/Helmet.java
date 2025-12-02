/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;

/**
 *
 * @author ASUS
 */
public class Helmet extends Equipable{
    public Helmet(int stat, double passive){
        super(EquipableType.HELMET, stat, passive);
    }
    
    @Override
    public String toString() {
        return "Casco" + " | Regeneración de vida: " + getStat() + " | Regeneración de poder: " + getPassive();
    }
}
