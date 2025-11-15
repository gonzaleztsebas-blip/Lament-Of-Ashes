/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.event;

/**
 *
 * @author ASUS
 */
public class AttackResult extends Event{
    private String target;
    
    public AttackResult(String character, String attackName, String target, String damage){
        super(character, attackName, damage);
        this.target = target;
    }
    
    @Override
    public String toString(){
        if(target.equals("Todos")){
            return getCharacter() + " usa " + getAction() + " causando " + getEffect() + " de dano a todos los enemigos";
        }else{
            return getCharacter() + " usa " + getAction() + " causando " + getEffect() + " de dano a " + target;
        }
    }
    
}
