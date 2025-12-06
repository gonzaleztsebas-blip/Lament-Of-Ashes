/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;

import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class NameMenu {
    public String askName(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Recuerdas tu nombre? ");
        System.out.print(">");
        return scanner.nextLine();
    }
}
