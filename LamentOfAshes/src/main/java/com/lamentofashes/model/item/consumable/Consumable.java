/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.consumable;
import com.lamentofashes.model.entity.Player;
/**
 *
 * @author ASUS
 */
public abstract class Consumable {
    private String name;
    private String description;
    private int effect;
    
    //type 1: health
    //type 2: power
    private int type;
    
    public Consumable(String name, String description, int effect, int type){
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.type = type;
    }
    
    public String getName(){
        return name;
    }
    
    public String getDescription(){
        return description;
    }
    
    public int getType(){
        return type;
    }
    
    public int getEffect(){
        return effect;
    }
    
    public abstract void apply(Player player);
    
    @Override
    public String toString(){
        return name + " - " + description;
    }
}
