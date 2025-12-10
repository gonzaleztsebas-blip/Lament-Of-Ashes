package com.lamentofashes.logic;

import com.lamentofashes.model.GameStats;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestiona la carga, guardado y visualización de los puntajes máximos del
 * juego. Los puntajes se guardan en un archivo CSV.
 */
public class ScoreManager {

    // Usar la ruta absoluta del HOME del usuario o una ruta más robusta en un juego real
    private static final String SCORES_FILE = "highscores.csv";
    private static final int MAX_SCORES = 10;

    // Métodos públicos para la lógica del juego
    // -------------------------------------------------------------------------
    /**
     * Guarda un nuevo puntaje, lo ordena, y mantiene solo el top MAX_SCORES.
     *
     * @param stats La instancia de GameStats a guardar.
     */
    public void saveScore(GameStats stats) {
        List<GameStats> scores = loadScores();
        scores.add(stats);

        // 1. Ordenar descendente por el puntaje calculado
        scores.sort(Comparator.comparingInt(GameStats::calculateScore).reversed());

        // 2. Limitar al top MAX_SCORES
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        writeScores(scores);
    }

    /**
     * Carga todos los puntajes del archivo CSV.
     *
     * @return Una lista de GameStats ordenada por puntaje (la ordenación se
     * hace implícitamente al guardar, pero la recargamos por seguridad).
     */
    public List<GameStats> loadScores() {
        File file = new File(SCORES_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        // Usamos try-with-resources y NIO para lectura más segura (StandardCharsets)
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            // Omitir la primera línea (header)
            br.readLine();

            // Usar streams para leer, filtrar líneas vacías y mapear a GameStats
            return br.lines()
                    .filter(line -> line != null && !line.trim().isEmpty())
                    .map(GameStats::fromCSV) // Asume que GameStats.fromCSV maneja errores de formato
                    .sorted(Comparator.comparingInt(GameStats::calculateScore).reversed()) // Recargar ordenado
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println("✗ Error al cargar scores desde " + SCORES_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("✗ Error de formato en los datos de score: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una lista de puntajes, manteniendo el nombre original para
     * mantener consistencia con los métodos auxiliares.
     *
     * @return Lista de GameStats.
     */
    public List<GameStats> getHighScores() {
        return loadScores();
    }

    // Métodos auxiliares y utilidades
    // -------------------------------------------------------------------------
    /**
     * Devuelve una cadena formateada para mostrar el ranking de puntuaciones.
     * Mejora el formato y usa Unicode para bordes si es necesario (ASCII en
     * este caso).
     *
     * @return La cadena formateada para el display.
     */
    public String getHighScoresDisplay() {
        List<GameStats> scores = getHighScores();
        StringBuilder sb = new StringBuilder();

        String headerLine = "═════════════════════════════════════════════════════════════════════════\n";
        String border = "═"; // Carácter de borde simplificado

        sb.append(headerLine);
        sb.append(String.format("║ %-69s ║\n", "RANKING DE PARTIDAS"));
        sb.append(headerLine);

        // Encabezados de columna
        sb.append(String.format("║ %-3s %-10s | %-2s %-3s %-2s %-6s %-2s | %-5s | %-7s ║\n",
                "Nº", "JUGADOR", "R", "E", "J", "D", "C", "TIEMPO", "SCORE"));
        sb.append(headerLine);

        if (scores.isEmpty()) {
            sb.append(String.format("║ %-69s ║\n", "No hay puntuaciones registradas aún"));
        } else {
            for (int i = 0; i < scores.size(); i++) {
                GameStats s = scores.get(i);

                // Formato de tiempo: Minutos:Segundos
                String timeDisplay = String.format("%3d:%02d", s.getPlayTime() / 60, s.getPlayTime() % 60);

                // Formato de línea de puntaje
                sb.append(String.format("║ %2d. %-10s | R:%2d E:%3d J:%2d D:%6d C:%2d | T:%5s | S:%7d ║\n",
                        i + 1,
                        // Limitar el nombre del jugador a 10 caracteres para mantener el formato
                        s.getPlayerName().length() > 10 ? s.getPlayerName().substring(0, 10) : s.getPlayerName(),
                        s.getRoundReached(),
                        s.getEnemiesDefeated(),
                        s.getBossesDefeated(),
                        s.getDamageDealt(),
                        s.getConsumablesUsed(),
                        timeDisplay,
                        s.calculateScore()
                ));
            }
        }
        sb.append(headerLine);
        return sb.toString();
    }

    /**
     * Elimina todos los puntajes guardados creando un archivo vacío con solo el
     * header.
     */
    public void clearAllScores() {
        try {
            File file = new File(SCORES_FILE);
            // Usar writeScores con una lista vacía es más limpio que reescribir la lógica
            writeScores(Collections.emptyList());
            System.out.println("✓ Todos los puntajes han sido eliminados.");
        } catch (Exception e) {
            System.err.println("✗ Error al limpiar puntajes: " + e.getMessage());
        }
    }

    /**
     * Verifica si existen puntajes guardados.
     */
    public boolean hasScores() {
        return !getHighScores().isEmpty();
    }

    /**
     * Obtiene el número total de puntajes registrados.
     */
    public int getTotalScores() {
        return getHighScores().size();
    }

    /**
     * Obtiene el puntaje más alto.
     */
    public GameStats getTopScore() {
        List<GameStats> scores = getHighScores();
        return scores.isEmpty() ? null : scores.get(0);
    }

    // -------------------------------------------------------------------------
    /**
     * Escribe la lista de puntajes al archivo CSV.
     *
     * @param scores La lista de GameStats a escribir.
     */
    private void writeScores(List<GameStats> scores) {
        // Usamos BufferedWriter para un mejor rendimiento en escritura de archivos
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORES_FILE, StandardCharsets.UTF_8))) {
            bw.write(GameStats.getCSVHeader());
            bw.newLine();

            for (GameStats stats : scores) {
                bw.write(stats.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("✗ Error al guardar scores: " + e.getMessage());
        }
    }
}
