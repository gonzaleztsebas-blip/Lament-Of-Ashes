/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.event;

/**
 *
 * @author ASUS
 */
public class Event {
    private String character;
    private String action;
    private String effect;
    
    public Event (String character, String action, String effect){
        this.character = character;
        this.action = action;
        this.effect = effect;
    }
    
    public String getCharacter(){
        return character;
    }
    
    public String getAction(){
        return action;
    }
    
    public String getEffect(){
        return effect;
    }
}
