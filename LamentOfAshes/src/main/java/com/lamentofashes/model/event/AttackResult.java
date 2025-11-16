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
    private boolean critical;
    
    public AttackResult(String character, String attackName, String target, String damage, boolean critical){
        super(character, attackName, damage);
        this.target = target;
        this.critical = critical;
    }
    
    @Override
    public String toString(){
        String attack = areaAttack();
        if(critical){
            attack += " causando daño critico";
        }
        
        return attack;
    }
    
    public String areaAttack(){
        if(target.equals("Todos")){
            return getCharacter() + " usa " + getAction() + " causando " + getEffect() + " de daño a todos los enemigos";
        }else{
            return getCharacter() + " usa " + getAction() + " causando " + getEffect() + " de daño a " + target;
        }
    }
    
}
