/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.skills.Attack;
import java.util.Scanner;
import java.util.ArrayList;
/**
 *
 * @author ASUS
 */
public class ConsoleBattle {
    private BattleManager battleManager;
    private Scanner scanner = new Scanner(System.in);
    
    public ConsoleBattle() {
        this.battleManager = new BattleManager();
    }
    
    
    public void startBattle(){
        Player player;
        ArrayList<Enemy> enemies;
        while(!battleManager.isBattleOver()){
            player = battleManager.getPlayer();
            enemies = battleManager.getEnemies();
            boolean cost = false;
            
            System.out.println("\nVida del jugador: " + player.getHealth() + " | Poder: " + player.getPower());
            
            int enemyChoice = -1;
            while(enemyChoice < 0 || enemyChoice > enemies.size()){
                System.out.println("Enemigos:");
                for(int i = 0; i < enemies.size(); i++){
                    Enemy e = enemies.get(i);
                    System.out.println((i+1) + ": " + e.getName() + " - HP: " + e.getHealth());
                }
                System.out.println("Elige a quien atacar: ");
                enemyChoice = scanner.nextInt();
                scanner.nextLine();
                if(enemyChoice < 0 || enemyChoice > enemies.size()){
                    System.out.println("Enemigo no valido");
                }
            }
            
            int attackChoice = -1;
            while(attackChoice < 0 || attackChoice > player.getAttacks().size()){
                System.out.println("Elige el ataque que vas a usar");
                for(int i = 0; i < player.getAttacks().size(); i++){
                    Attack a = player.getAttacks().get(i);
                    System.out.println((i+1) +". " + a.toString());
                }
                attackChoice = scanner.nextInt();
                scanner.nextLine();
                
                if(attackChoice < 0 || attackChoice > player.getAttacks().size()){
                    System.out.println("Ataque no valido");
                    continue;
                }
                
                
                if(!cost){
                   cost = battleManager.playerAttack(attackChoice-1, enemyChoice-1);
                   if(!cost){
                       System.out.println("No hay suficiente poder para usar " + player.getAttack(attackChoice-1).getName());
                       attackChoice = -1;
                   }
                }
            }

            battleManager.enemiesTurn();
            player.regenetarePower();
        }
        if(battleManager.getPlayer().isDead()){
            System.out.println("Derrota");
        } else {
            System.out.println("Victoria");
        }
    }
}
