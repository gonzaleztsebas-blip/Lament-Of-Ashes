/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.factorys;
import java.util.Random;
import com.lamentofashes.model.item.consumable.*;
/**
 *
 * @author ASUS
 */
public class ConsumableFactory {
    private Random random = new Random();
    
    public Consumable generateConsumable(){
        double choice = random.nextDouble();
        
        /* 35% probabilidad pocion de vida pequeña
         * 35% probabilidad pocion menor de poder
         * 15% probabilidad pocion grande de vida
         * 15% probabilidad pocion mayor de poder
         */
        if(choice <= 0.35){
            return new SmallHealPotion();
        }else if(choice <= 0.7){
            return new MinorPowerEssence();
        }else if (choice <= 0.85){
            return new BigHealPotion();
        }else{
            return new MajorPowerEssence();
        }
    }
}
