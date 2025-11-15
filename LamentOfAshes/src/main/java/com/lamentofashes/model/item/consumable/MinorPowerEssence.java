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
public class MinorPowerEssence extends Consumable{
    public MinorPowerEssence(){
        super("Esencia pequeña de Poder", "Recupera 20 de Poder", 20, 2);
    }
    
    @Override
    public void apply(Player player){
        player.restorePower(super.getEffect());
    }
}
