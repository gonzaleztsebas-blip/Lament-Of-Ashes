/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.factorys;
import com.lamentofashes.model.entity.enemy.*;
import java.util.Random;

/**
 *
 * @author ASUS
 */
public class BossFactory {
    private Random random = new Random();
    
    public Boss generateBpss(){
        return new Boss();
    }
}
