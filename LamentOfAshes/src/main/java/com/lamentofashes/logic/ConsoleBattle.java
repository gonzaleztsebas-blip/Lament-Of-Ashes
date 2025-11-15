/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.skills.Attack;
import com.lamentofashes.model.battle.AttackResult;
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
            
            System.out.println("\nVida del jugador: " + player.getHealth() + " | Poder: " + player.getPower());
            
            int enemyChoice = -1;
            while(enemyChoice < 0 || enemyChoice > enemies.size()){
                System.out.println("Enemigos:");
                for(int i = 0; i < enemies.size(); i++){
                    Enemy e = enemies.get(i);
                    if(e == null){
                        continue;
                    }
                    System.out.println((i+1) + ": " + e.getName() + " - HP: " + e.getHealth() + " - Dano: " + e.getBaseDamage());
                }
                System.out.println("Elige a quien atacar: ");
                enemyChoice = scanner.nextInt();
                scanner.nextLine();
                if(enemyChoice < 0 || enemyChoice > enemies.size()){
                    System.out.println("Enemigo no valido");
                }
            }
            
            AttackResult result = new AttackResult ("", "", "", 0);
            while(result.getDamage() == 0){
                System.out.println("Elige el ataque que vas a usar");
                for(int i = 0; i < player.getAttacks().size(); i++){
                    Attack a = player.getAttacks().get(i);
                    System.out.println((i+1) +". " + a.toString());
                }
                int attackChoice = scanner.nextInt();
                scanner.nextLine();
                
                if(attackChoice < 0 || attackChoice > player.getAttacks().size()){
                    System.out.println("Ataque no valido");
                    continue;
                }
                
                result = battleManager.playerAttack(attackChoice-1, enemyChoice-1);
                System.out.println(result);
                
            }

            ArrayList<AttackResult> enemiesResult = battleManager.enemiesTurn();
            for(int i=0; i < enemiesResult.size(); i++){
            System.out.println(enemiesResult.get(i));
            }
            player.regenetarePower();
        }
        if(battleManager.getPlayer().isDead()){
            System.out.println("Derrota");
        } else {
            System.out.println("Victoria");
        }
        
        
        System.out.println("Resumen de la batalla: ");
        for(int i=0; i < battleManager.getBattleResults().size(); i++){
            System.out.println(battleManager.getAttackResults(i));
        }
    }
}
