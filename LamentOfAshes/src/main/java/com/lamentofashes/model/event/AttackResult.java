/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model.event;

/**
 *
 * @author ASUS
 */
public class AttackResult extends Event {
    private String target;
    private boolean critical;

    public AttackResult(String character, String attackName, String target, String damage, boolean critical) {
        super(character, attackName, damage);
        this.target = target;
        this.critical = critical;
    }

    @Override
    public String toString() {
        String attack = areaAttackMessage();

        if (critical) {
            attack += " causando daño crítico";
        }

        return attack;
    }

    // --- Corrige el NPE ---
    private boolean isAreaAttack() {
        return target != null && (target.equalsIgnoreCase("Todos") || target.equalsIgnoreCase("ALL"));
    }

    private String areaAttackMessage() {
        if (isAreaAttack()) {
            return getCharacter() + " usa " + getAction() +
                   " causando " + getEffect() + " de daño a todos los enemigos";
        } else {
            if (target == null || target.isEmpty()) {
                return getCharacter() + " usa " + getAction() +
                       " causando " + getEffect() + " de daño";
            }
            return getCharacter() + " usa " + getAction() +
                   " causando " + getEffect() + " de daño";
        }
    }

    public boolean isCritical() {
        return critical;
    }

    public String getTarget() {
        return target;
    }

    public String getAttackName() {
        return getAction();
    }
}


