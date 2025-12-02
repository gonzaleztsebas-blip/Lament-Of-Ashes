/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.item.equipable;
import java.util.ArrayList;
import com.lamentofashes.model.skills.*;
/**
 *
 * @author ASUS
 */
public class Weapon extends Equipable{
    private ArrayList<Attack> attacks;
    
    public Weapon(int stat, double passive){
        super(EquipableType.WEAPON, stat, passive);
        generateAttacks();
    }
    
    private void generateAttacks(){
        this.attacks = new ArrayList<>();
        attacks.add(new Attack("Ataque moderado", 0, 10, 20, getPassive(), getStat(), AttackType.NORMAL));
        attacks.add(new Attack("Ataque fuerte", 20, 30, 40, getPassive(), getStat(), AttackType.NORMAL));
        attacks.add(new Attack("Ataque especial", 50, 20, 30, getPassive(), getStat(), AttackType.AREA));
    }
    
    public ArrayList<Attack> getAttacks(){
        return attacks;
    }
    
    public Attack getAttack(int index){
        return attacks.get(index);
    }
    
    @Override
    public void upgrade(int statUpgrade, double passiveUpgrade){
        super.upgrade(statUpgrade, passiveUpgrade);
        for(int i = 0; i < attacks.size(); i++){
            Attack a = attacks.get(i);
            
            a.updateDamage(statUpgrade);
            
            double criticChance = a.getCriticChance() + passiveUpgrade;
            if(criticChance > 0.6){
                criticChance = 0.6;
            }
            
            a.setCriticChance(criticChance);
        }
    }
    
    @Override
    public String toString() {
        return "Espada" + " | Daño Extra: " + getStat() + " | Probabilidad de Crítico: " + getPassive();
    }
}
