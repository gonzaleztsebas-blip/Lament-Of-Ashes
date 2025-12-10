package com.lamentofashes.model.item.equipable;

/**
 *
 * @author ASUS
 */
public abstract class Equipable {

    private Quality quality;
    private EquipableType type;
    private int stat;
    private double passive;

    public Equipable(Quality quality, EquipableType type, int stat, double passive) {
        this.quality = quality;
        this.type = type;
        this.stat = stat;
        this.passive = passive;
    }

    public EquipableType getType() {
        return type;
    }

    public int getStat() {
        return stat;
    }

    public double getPassive() {
        return passive;
    }

    public Quality getQuality() {
        return quality;
    }

    public String stringQuality() {
        if (quality == Quality.EPIC) {
            return "Epico";
        } else if (quality == Quality.RARE) {
            return "Raro";
        } else {
            return "Comun";
        }
    }

    public void upgrade(int statUpgrade, double passiveUpgrade) {
        stat += statUpgrade;
        passive += passiveUpgrade;
    }

    /**
     * Obtiene la ruta de la imagen del equipable según su tipo y calidad
     *
     * @return Ruta de la imagen
     */
    public String getImagePath() {
        String basePath = "/images/";
        String qualityPrefix = "";

        // Prefijo según calidad
        switch (quality) {
            case COMMON:
                qualityPrefix = "common";
                break;
            case RARE:
                qualityPrefix = "rare";
                break;
            case EPIC:
                qualityPrefix = "epic";
                break;
        }

        // Ruta según tipo
        switch (type) {
            case WEAPON:
                return basePath + "weapons/" + qualityPrefix + ".png";
            case ARMOR:
                return basePath + "armors/" + qualityPrefix + ".png";
            case SHIELD:
                return basePath + "shields/" + qualityPrefix + ".png";
            default:
                return basePath + "default.png";
        }
    }
}
