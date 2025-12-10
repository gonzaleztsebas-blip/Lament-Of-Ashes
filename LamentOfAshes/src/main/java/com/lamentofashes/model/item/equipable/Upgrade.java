package com.lamentofashes.model.item.equipable;

import com.lamentofashes.model.entity.Player;

public class Upgrade {

    private EquipableType type;
    private int statUpgrade;
    private double passiveUpgrade;

    public Upgrade(EquipableType type, int statUpgrade, double passiveUpgrade) {
        this.type = type;
        this.statUpgrade = statUpgrade;
        this.passiveUpgrade = passiveUpgrade;
    }

    public EquipableType getType() {
        return type;
    }

    public int getStatUpgrade() {
        return statUpgrade;
    }

    public double getPassiveUpgrade() {
        return passiveUpgrade;
    }

    /**
     * Devuelve un texto con la mejora que se aplicará al equipamiento actual.
     */
    public String toString(Player player) {
        StringBuilder sb = new StringBuilder();
        Equipable equipable;

        switch (type) {

            case WEAPON:
                equipable = player.getWeapon();
                sb.append("Arma | Daño Extra: ")
                  .append(equipable.getStat()).append(" + ").append(statUpgrade)
                  .append(" | Probabilidad de Crítico: ").append(equipable.getPassive()).append(" + ");

                double critIncrease = Math.min(passiveUpgrade, 0.6 - equipable.getPassive());
                sb.append(critIncrease);
                break;

            case ARMOR:
                equipable = player.getArmor();
                sb.append("Armadura | Vida Extra: ")
                  .append(equipable.getStat()).append(" + ").append(statUpgrade)
                  .append(" | Reducción de daño al defenderse: ").append(equipable.getPassive()).append(" + ");

                double reductionIncrease = Math.min(passiveUpgrade, 0.8 - equipable.getPassive());
                sb.append(reductionIncrease);
                break;

            case SHIELD:
                equipable = player.getShield();
                sb.append("Escudo | Regeneración de vida: ")
                  .append(equipable.getStat()).append(" + ");

                int hpRegenIncrease = Math.min(statUpgrade, 50 - equipable.getStat());
                sb.append(hpRegenIncrease);

                sb.append(" | Regeneración de Poder: ")
                  .append(equipable.getPassive()).append(" + ");

                double powerRegenIncrease = Math.min(passiveUpgrade, 30 - equipable.getPassive());
                sb.append(powerRegenIncrease);
                break;

            default:
                sb.append("");
                break;
        }

        return sb.toString();
    }
}
