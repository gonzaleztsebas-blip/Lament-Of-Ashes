/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.entity.enemy;

/**
 *
 * @author ASUS
 */
public class Skeleton extends Enemy{
    public Skeleton(double hpMultiplier, double damageMultiplier, double criticMultiplier){
        super("Esqueleto", 25, 20, 12, 8, 0.1,  hpMultiplier, damageMultiplier, criticMultiplier);
    }
}
