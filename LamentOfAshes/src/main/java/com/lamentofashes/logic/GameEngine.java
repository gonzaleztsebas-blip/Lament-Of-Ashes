/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.logic.factorys.EquipableFactory;
import com.lamentofashes.logic.round.*;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.equipable.*;
import java.util.ArrayList;
/**
 *
 * @author ASUS
 */
public class GameEngine {
    private Player player;
    private int actualRound;
    
    public GameEngine(){
        this.player = new Player("Seb", 100, 100);
        this.actualRound = 1;
    }
    
    public void initialEquipSelection() {
        EquipableMenu ui = new EquipableMenu();
        ArrayList<Equipable> options;
        
        options = generateEquipments(EquipableType.WEAPON);
        int index = ui.askChoice(options, EquipableType.WEAPON);
        player.setWeapon((Weapon) options.get(index));
        
        options = generateEquipments(EquipableType.ARMOR);
        index = ui.askChoice(options, EquipableType.ARMOR);
        player.setArmor((Armor) options.get(index));

        
        options = generateEquipments(EquipableType.HELMET);
        index = ui.askChoice(options, EquipableType.HELMET);
        player.setHelmet((Helmet) options.get(index));

        player.updateStatsFromEquipables();
    }
    
    public ArrayList<Equipable> generateEquipments(EquipableType type){
        EquipableFactory equipableFactory = new EquipableFactory();
        ArrayList<Equipable> options = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            if(type == EquipableType.WEAPON){
                options.add(equipableFactory.createWeapon());
            }else if(type == EquipableType.ARMOR){
                options.add(equipableFactory.createArmor());
            }else if(type == EquipableType.HELMET){
                options.add(equipableFactory.createHelmet());
            }
        }
        return options;
    }
    
    public void runGame(){
        initialEquipSelection();
        
        boolean isAlive = true;
        while(isAlive){
            RoundManager roundManager = new RoundManager(actualRound, player);
            isAlive = roundManager.playRound();
            actualRound++;
        }
    }
}
