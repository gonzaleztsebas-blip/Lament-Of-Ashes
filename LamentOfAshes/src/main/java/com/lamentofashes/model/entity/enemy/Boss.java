/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity.enemy;
/**
 *
 * @author ASUS
 */
public class Boss extends Enemy {
    
    public Boss( double hpMultiplier, double damageMultiplier, double criticMultiplier){
        super("Jefe Demonio", 150, 120, 25, 20, 0.3, hpMultiplier, damageMultiplier, criticMultiplier, 0.4);
    }
    
}
