/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;

import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.item.equipable.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class EquipableMenu {
    private Scanner scanner = new Scanner(System.in);

    public int askChoice(ArrayList<Equipable> options, EquipableType type){
        if(type == EquipableType.WEAPON){
            System.out.println("Elige tu Arma: ");
        }else if(type == EquipableType.ARMOR){
            System.out.println("Elige tu Armadura: ");
        }else if(type == EquipableType.SHIELD){
            System.out.println("Elige tu Escudo: ");
        }
        
        int choice = -1;
        while(choice < 1 || choice > options.size()){
            for(int i = 0; i < options.size(); i++){
                System.out.println((i + 1) + ". " + options.get(i));
            }
            choice = scanner.nextInt();
        }
        
        return choice - 1;
    }
    
    public int askUpgrade(Upgrade[] options, Player player){
        System.out.println("Elige tu recompensa: ");
        int choice = -1;
        while(choice < 1 || choice > options.length){
            for(int i = 0; i < options.length; i++){
                System.out.println((i + 1) + ". " + options[i].toString(player));
            }
            choice = scanner.nextInt();
        }
        
        return choice - 1;
    }
    
}
