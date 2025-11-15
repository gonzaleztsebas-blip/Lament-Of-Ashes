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
public class BigHealPotion extends Consumable{
    
    public BigHealPotion(){
        super("Poción grande de vida", "Recupera 50 HP", 50, 1);
    }
    
    @Override
    public void apply(Player player){
        player.heal(super.getEffect());
    }
}
