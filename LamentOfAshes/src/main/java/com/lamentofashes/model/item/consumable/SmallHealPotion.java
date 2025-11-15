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
public class SmallHealPotion extends Consumable{
    
    public SmallHealPotion(){
        super("Poción pequeña de vida", "Recupera 20 HP", 20, 1);
    }
    
    @Override
    public void apply(Player player){
        player.heal(super.getEffect());
    }

}
