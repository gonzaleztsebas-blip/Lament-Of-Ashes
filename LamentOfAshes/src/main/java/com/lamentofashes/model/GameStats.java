/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.model;

/**
 *
 * @author ASUS
 */
public class GameStats {
    private String playerName;
    private int roundReached;
    private int enemiesDefeated;
    private int bossesDefeated;
    private int damageDealt;
    private int consumablesUsed;
    private int playTime;
    
    public GameStats(String playerName) {
        this.playerName = playerName;
        this.roundReached = 1;
        this.enemiesDefeated = 0;
        this.bossesDefeated = 0;
        this.damageDealt = 0;
        this.consumablesUsed = 0;
        this.playTime = 0;
    }
    
    public static GameStats fromCSV(String line) {
        String[] parts = line.split(",");

        GameStats stats = new GameStats(parts[0]);
        stats.roundReached = Integer.parseInt(parts[1]);
        stats.enemiesDefeated = Integer.parseInt(parts[2]);
        stats.bossesDefeated = Integer.parseInt(parts[3]);
        stats.damageDealt = Integer.parseInt(parts[4]);
        stats.consumablesUsed = Integer.parseInt(parts[5]);
        stats.playTime = Integer.parseInt(parts[6]);

        return stats;
    }
    
    public int calculateScore(){
        int score = 0;
        score += roundReached * 5000;
        score += enemiesDefeated * 100;
        score += bossesDefeated * 500;
        score += damageDealt;
        score -= consumablesUsed * 50;
        return score;
    }
    
    public static String getCSVHeader() {
        return "playerName,roundReached,enemiesDefeated,bossesDefeated,damageDealt,consumablesUsed,playTime,score";
    }
    
    public String toCSV(){
        return String.format("%s,%d,%d,%d,%d,%d,%d,%d",
            playerName,
            roundReached,
            enemiesDefeated,
            bossesDefeated,
            damageDealt,
            consumablesUsed,
            playTime,
            calculateScore()
        );
    }
    
    public void incrementRound() { 
        roundReached++; 
    }

    public void addEnemiesDefeated(int enemies) { 
        enemiesDefeated += enemies; 
    }

    public void addBossesDefeated(int bosses) { 
        bossesDefeated += bosses; 
    }

    public void addDamage(int damage) { 
        damageDealt += damage; 
    }

    public void addConsumablesUsed(int consumable) { 
        consumablesUsed += consumable; 
    }

    public void setPlayTime(int seconds) { 
        playTime = seconds; 
    }
    
    public String getPlayerName() { 
        return playerName; 
    }

    public int getRoundReached() { 
        return roundReached; 
    }

    public int getEnemiesDefeated() { 
        return enemiesDefeated; 
    }

    public int getBossesDefeated() { 
        return bossesDefeated; 
    }

    public int getDamageDealt() { 
        return damageDealt; 
    }

    public int getConsumablesUsed() { 
        return consumablesUsed; 
    }

    public int getPlayTime() { 
        return playTime; 
    }
    
    @Override
    public String toString() {
        return String.format("%-10s | Round: %2d | Enemigos: %3d | Jefes: %2d | Daño: %6d | Consumibles: %2d | Score: %7d",
            playerName, 
            roundReached, 
            enemiesDefeated, 
            bossesDefeated, 
            damageDealt,
            consumablesUsed,
            calculateScore()
        );
    }
}
