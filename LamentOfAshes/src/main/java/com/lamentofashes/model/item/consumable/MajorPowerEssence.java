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
public class MajorPowerEssence extends Consumable{
    public MajorPowerEssence(){
        super("Esencia grande de Poder", "Recupera 50 de Poder", 50, 2);
    }
    
    @Override
    public void apply(Player player){
        player.restorePower(super.getEffect());
    }
}
