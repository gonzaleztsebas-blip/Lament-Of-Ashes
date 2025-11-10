/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.entity.*;
import com.lamentofashes.model.entity.enemy.*;
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
            
            System.out.println("Vida del jugador: " + player.getHealth());
            for(int i = 0; i < enemies.size(); i++){
                Enemy e = enemies.get(i);
                System.out.println((i+1) + ": " + e.getName() + " - HP: " + e.getHealth());
            }
            
            System.out.println("Elige a quien atacar: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            battleManager.playerAttack(choice-1);
            battleManager.enemiesTurn();
            
            System.out.println();
        }
        if(battleManager.getPlayer().isDead()){
            System.out.println("Derrota");
        } else {
            System.out.println("Victoria");
        }
    }
}
