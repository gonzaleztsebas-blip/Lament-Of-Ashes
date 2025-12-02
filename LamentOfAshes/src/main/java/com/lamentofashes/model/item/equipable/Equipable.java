/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;

/**
 *
 * @author ASUS
 */
public class Equipable {
    private EquipableType type;
    private int stat;
    private double passive;
    
    public Equipable(EquipableType type, int stat, double passive){
        this.type = type;
        this.stat = stat;
        this.passive = passive;
    }
    
    public EquipableType getType(){
        return type;
    }
    
    public int getStat(){
        return stat;
    }
    
    public double getPassive(){
        return passive;
    }
    
    public void upgrade(int statUpgrade, double passiveUpgrade){
        stat += statUpgrade;
        passive += passiveUpgrade;
    } 
    
}
