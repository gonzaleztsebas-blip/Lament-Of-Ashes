/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic.round;

import com.lamentofashes.model.item.consumable.Consumable;
import java.util.*;

/**
 *
 * @author ASUS
 */
public class ConsumableMenu {
    private ArrayList<Consumable> options;
    private Scanner scanner;
    
    public ConsumableMenu(ArrayList<Consumable> options){
        this.options = options;
        scanner = new Scanner(System.in);
    }
    
    public Consumable selectConsumable(){
        System.out.println("Selecciona tu recompensa: ");
        for(int i = 0; i < options.size(); i++){
            System.out.println(i+1 + ". " + options.get(i));
        }
        
        int election = -1;
        while(election < 0 || election >= options.size()){
            election = scanner.nextInt() - 1;
            scanner.nextLine();
        }
        
        return options.get(election);
    }
}
