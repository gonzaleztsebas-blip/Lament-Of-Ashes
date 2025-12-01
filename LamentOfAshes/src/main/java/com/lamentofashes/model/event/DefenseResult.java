/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.event;

/**
 *
 * @author ASUS
 */
public class DefenseResult extends Event{    
    
    public DefenseResult(String character, String reduction){
        super(character, "defiende", reduction);
    }
    
    @Override
    public String toString(){
        return getCharacter() + " se defiende reduciendo " + getEffect() + "% de daño";
    }
}
