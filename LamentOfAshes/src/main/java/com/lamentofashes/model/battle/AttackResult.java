/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.battle;

/**
 *
 * @author ASUS
 */
public class AttackResult {
    private String character;
    private String attackName;
    private String target;
    private int damage;
    
    public AttackResult(String character, String attackName, String target, int damage){
        this.character = character;
        this.attackName = attackName;
        this.target = target;
        this.damage = damage;
    }
    
    public int getDamage(){
        return damage;
    }
    
    @Override
    public String toString(){
        if(target.equals("Todos")){
            return character + " usa " + attackName + " causando " + damage + " de dano a todos los enemigos";
        }else{
            return character + " usa " + attackName + " causando " + damage + " de dano a " + target;
        }
    }
    
}
