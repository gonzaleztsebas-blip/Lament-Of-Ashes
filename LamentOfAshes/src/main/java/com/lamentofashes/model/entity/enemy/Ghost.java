/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity.enemy;

/**
 *
 * @author ASUS
 */
public class Ghost extends Enemy{
    public Ghost(double hpMultiplier, double damageMultiplier, double criticMultiplier){
        super("Fantasma", 35, 30, 10, 5, 0.1, hpMultiplier, damageMultiplier, criticMultiplier, 0.3);
    }
}
