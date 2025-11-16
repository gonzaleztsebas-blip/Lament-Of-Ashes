/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.battle;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.skills.Attack;
import com.lamentofashes.model.event.AttackResult;
import com.lamentofashes.model.event.ConsumableResult;
import com.lamentofashes.model.item.consumable.*;
import java.util.Scanner;
import java.util.ArrayList;
/**
 *
 * @author ASUS
 */
public class ConsoleBattle {
    private BattleManager battleManager;
    private Scanner scanner = new Scanner(System.in);
    
    public ConsoleBattle(int enemiesNumber) {
        this.battleManager = new BattleManager(enemiesNumber);
    }
    
    
    public void startBattle(){
        Player player;
        ArrayList<Enemy> enemies;
        while(!battleManager.isBattleOver()){
            player = battleManager.getPlayer();
            enemies = battleManager.getEnemies();
            
            System.out.println("\nVida del jugador: " + player.getHealth() + " | Poder: " + player.getPower());
            printEnemies(enemies);
            
            int option = -1;
            while(option < 1 || option > 2){
                System.out.println("Que quieres hacer?");
                System.out.println("1. atacar");
                System.out.println("2. usar consumible");
                option = scanner.nextInt();
                scanner.nextLine();

                switch(option){
                    case 1:
                        attack(player, enemies);
                        break;
                    case 2:
                        useConsumable(player);
                        break;
                }
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
    
    private void printEnemies(ArrayList<Enemy> enemies){
        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            if(e == null){
                continue;
            }
            System.out.println((i+1) + ": " + e.getName() + " - HP: " + e.getHealth() + " - Dano: " + e.getBaseDamage());
        }
    }
    
    private void attack(Player player, ArrayList<Enemy> enemies){
        int enemyChoice = -1;
        while(enemyChoice < 0 || enemyChoice > enemies.size()){
            System.out.println("A quien deseas atacar?");
            System.out.println("Enemigos:");
            printEnemies(enemies);
            System.out.println("Elige a quien atacar: ");
            enemyChoice = scanner.nextInt() - 1;
            scanner.nextLine();
            if(enemyChoice < 0 || enemyChoice > enemies.size()){
                System.out.println("Enemigo no valido");
            }else if(enemies.get(enemyChoice) == null){
                System.out.println("Enemigo muerto");
                enemyChoice = -1;
            }
        }
            
        AttackResult result = new AttackResult ("", "", "", "0", false);
        while(result.getEffect().equals("0")){
            System.out.println("Elige el ataque que vas a usar");
            for(int i = 0; i < player.getAttacks().size(); i++){
                Attack a = player.getAttacks().get(i);
                System.out.println((i+1) +". " + a.toString());
            }
            int attackChoice = scanner.nextInt() - 1;
            scanner.nextLine();
                
            if(attackChoice < 0 || attackChoice > player.getAttacks().size()){
            System.out.println("Ataque no valido");
                continue;
            }
                
            result = battleManager.playerAttack(attackChoice, enemyChoice);
            System.out.println(result);       
        }
    }
    
    private void useConsumable(Player player){
        ConsumableResult result = new ConsumableResult("", "", "0", 1);
        while(result.getEffect().equals("0")){
            System.out.println("Elige el consumible: ");
            for(int i = 0; i < player.getInventory().size(); i++){
                Consumable c = player.getInventory().get(i);
                if(c == null){
                    continue;
                }
                System.out.println((i + 1) + ". " + c);
            }
            int choice = scanner.nextInt() - 1;
            scanner.nextLine();
            if(choice < 0 || choice > player.getInventory().size()){
                System.out.println("Objeto no valido");
                continue;
            }
            result = battleManager.useConsumable(choice);
        }
        
        
        System.out.println(result);
    }
}
