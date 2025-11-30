/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.logic.round.*;
import com.lamentofashes.model.entity.Player;
/**
 *
 * @author ASUS
 */
public class GameEngine {
    private Player player;
    private int actualRound;
    
    public GameEngine(){
        this.player = new Player("Seb", 125, 100, 15);
        this.actualRound = 1;
    }
    
    public void runGame(){
        boolean isAlive = true;
        while(isAlive){
            RoundManager roundManager = new RoundManager(actualRound, player);
            isAlive = roundManager.playRound();
            actualRound++;
        }
    }
}
