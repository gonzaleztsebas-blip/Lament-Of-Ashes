/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lamentofashes.logic;
import com.lamentofashes.model.GameStats;
import java.io.*;
import java.util.*;
/**
 *
 * @author ASUS
 */
public class ScoreManager {
    private static final String SCORES_FILE = "highscores.csv";
    private static final int MAX_SCORES = 10;
    
    public void saveScore(GameStats stats) {
        List<GameStats> scores = loadScores();
        scores.add(stats);
        
        scores.sort((a, b) -> Integer.compare(b.calculateScore(), a.calculateScore()));
        
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }
        
        writeScores(scores);
    }
    
    public List<GameStats> loadScores() {
        List<GameStats> scores = new ArrayList<>();
        File file = new File(SCORES_FILE);

        if (!file.exists()) {
            return scores;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    scores.add(GameStats.fromCSV(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar scores: " + e.getMessage());
        }

        return scores;
    }
    
    private void writeScores(List<GameStats> scores) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SCORES_FILE))) {
            pw.println(GameStats.getCSVHeader());

            for (GameStats stats : scores) {
                pw.println(stats.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar scores: " + e.getMessage());
        }
    }
    
    public String getHighScoresDisplay() {
    List<GameStats> scores = loadScores();
    StringBuilder sb = new StringBuilder();
    sb.append("                    RANKING DE PARTIDAS                    \n");
    
    if (scores.isEmpty()) {
        sb.append("          No hay puntuaciones registradas aún              \n");
    } else {
        for (int i = 0; i < scores.size(); i++) {
            GameStats s = scores.get(i);
            sb.append(String.format("║ %2d. %-10s | R:%2d E:%3d J:%2d D:%6d C:%2d T:%3d:%02d S:%7d ║\n",
                i + 1, 
                s.getPlayerName(), 
                s.getRoundReached(),
                s.getEnemiesDefeated(),
                s.getBossesDefeated(),
                s.getDamageDealt(),
                s.getConsumablesUsed(),
                s.getPlayTime() / 60,
                s.getPlayTime() % 60,
                s.calculateScore()
            ));
        }
    }
    
    return sb.toString();
}
}
