/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.Entity.*;

/**
 *
 * @author ASUS
 */
public class BattleManager {
    private Player player;
    private Enemy enemy;
    
    public BattleManager(){
        this.player = new Player("Seb", 100, 20);
        this.enemy = new Enemy("Esqueleto", 80, 10);
    }
    
    public void battle(){
        System.out.println("EMPIEZA LA BATALLA");
        int turn = 1;
        
        while(!player.isDead() && !enemy.isDead()){
            System.out.println("Vida actual de " + player.getName() + ": " + player.getHealth());
            System.out.println("Vida actual de " + enemy.getName() + ": " + enemy.getHealth());
            if (turn % 2 == 1){
                System.out.println("Turno de " + player.getName());
                enemy.takeDamage(player.getBaseDamage());
                System.out.println(player.getName() + " ataca a " + enemy.getName() + " (" + player.getBaseDamage() + " de daño)");
            }else{
                System.out.println("Turno de " + enemy.getName());
                player.takeDamage(enemy.getBaseDamage());
                System.out.println(enemy.getName() + " ataca a " + player.getName() + " (" + enemy.getBaseDamage() + " de daño)");
            }
            turn++;
        }
        System.out.println(player.isDead() ? "Derrota" : "Victoria");
    }
}
