/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;

/**
 *
 * @author ASUS
 */
public abstract class Equipable {
    private Quality quality;
    private EquipableType type;
    private int stat;
    private double passive;
    
    public Equipable(Quality quality, EquipableType type, int stat, double passive){
        this.quality = quality;
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
    
    public Quality getQuality(){
        return quality;
    }
    
    public String stringQuality(){
        if(quality == Quality.EPIC){
            return "Epico";
        }else if(quality == Quality.RARE){
            return "Raro";
        }else{
            return "Comun";
        }
    }
    
    public void upgrade(int statUpgrade, double passiveUpgrade){
        stat += statUpgrade;
        passive += passiveUpgrade;
    } 
    
}
