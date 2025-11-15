/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.event;

/**
 *
 * @author ASUS
 */
public class ConsumableResult extends Event{
    private int type;
    
    public ConsumableResult(String character, String action, String effect, int type){
        super(character, action, effect);
        this.type = type;
    }
    
    @Override
    public String toString(){
        switch (type) {
            case 1:
                return getCharacter() + " usa " + getAction() + " regenerando " + getEffect() + " HP";
            case 2:
                return getCharacter() + " usa " + getAction() + " regenerando " + getEffect() + " de poder";
            default:
                return "";
        }
    }
}
