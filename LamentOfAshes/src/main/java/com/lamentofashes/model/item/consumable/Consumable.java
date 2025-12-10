package com.lamentofashes.model.item.consumable;

import com.lamentofashes.model.entity.Player;

public abstract class Consumable {

    private String name;
    private String description;
    private int effect;

    //type 1: health
    //type 2: power
    private int type;

    public Consumable(String name, String description, int effect, int type) {
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public int getEffect() {
        return effect;
    }

    public abstract void apply(Player player);

    @Override
    public String toString() {
        return name + " - " + description;
    }

    /**
     * Retorna la ruta de la imagen del consumible IMPORTANTE: La ruta debe
     * comenzar con / para buscar desde la raíz del classpath
     */
    public String getImagePath() {
        if (name == null) {
            System.out.println("⚠️ Consumable sin nombre, usando imagen por defecto");
            return "/images/consumables/default.png";
        }

        // Convertir nombre a formato de archivo
        String file = name.toLowerCase()
                .replace(" ", "_")
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u").replace("ñ", "n")
                + ".png";

        String path = "/images/consumables/" + file;
        System.out.println("📁 Ruta de imagen para '" + name + "': " + path);

        return path;
    }
}
