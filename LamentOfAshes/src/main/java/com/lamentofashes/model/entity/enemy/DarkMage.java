/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity.enemy;

/**
 *
 * @author ASUS
 */
public class DarkMage extends Enemy{
    public DarkMage( double hpMultiplier, double damageMultiplier, double criticMultiplier){
        super("Mago Oscuro" , 20, 15, 15, 12, 0.1,  hpMultiplier,  damageMultiplier, criticMultiplier, 0.3);
    }
}
