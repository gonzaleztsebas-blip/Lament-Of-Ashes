package com.lamentofashes.logic.battle;

import com.lamentofashes.model.event.*;
import com.lamentofashes.model.entity.Player;
import com.lamentofashes.model.entity.enemy.*;
import com.lamentofashes.model.skills.*;
import com.lamentofashes.model.item.consumable.*;
import java.util.ArrayList;

public class BattleManager {

    private Player player;
    private ArrayList<Enemy> enemies;
    private EnemyAI ai;

    private int totalDamageDealt = 0;
    private int enemiesKilled = 0;
    private int consumablesUsed = 0;

    public BattleManager(ArrayList<Enemy> enemies, Player player) {
        this.player = player;
        this.enemies = enemies;
        this.ai = new EnemyAI();
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    
    // Método para obtener enemigos vivos sin nulls
    public ArrayList<Enemy> getAliveEnemies() {
        ArrayList<Enemy> alive = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e != null && !e.isDead()) {
                alive.add(e);
            }
        }
        return alive;
    }

    public boolean isBattleOver() {
        boolean allEnemiesDead = true;

        for (Enemy e : enemies) {
            if (e != null && !e.isDead()) {
                allEnemiesDead = false;
                break;
            }
        }

        return allEnemiesDead || player.isDead();
    }

    public AttackResult playerAttack(int attackIndex, int enemyIndex) {
        Attack attack = player.getWeapon().getAttack(attackIndex);
        if (player.getPower() < attack.getPowerCost()) {
            return new AttackResult(player.getName(), attack.getName() + " (sin poder)", "-", "0", false);
        }

        // VERIFICAR que el índice es válido
        if (enemyIndex < 0 || enemyIndex >= enemies.size()) {
            throw new IndexOutOfBoundsException("Índice de enemigo inválido: " + enemyIndex);
        }

        Enemy target = enemies.get(enemyIndex);
        
        // VERIFICAR que el enemigo no sea null antes de usarlo
        if (target == null) {
            throw new IllegalStateException("Enemigo en índice " + enemyIndex + " es null");
        }
        
        if (target.isDead()) {
            throw new IllegalStateException("Intentando atacar un enemigo muerto");
        }

        int damage = attack.use();
        totalDamageDealt += damage;

        if (attack.getType() == AttackType.AREA) {
            specialAttack(damage);
        } else {
            if (ai.decideAction(target) == EnemyAction.DEFEND) {
                target.guard();
            }
            target.takeDamage(damage);
            if (target.isDead()) {
                enemiesKilled++;
                // NO remover aquí, mantener el índice consistente
            }
        }

        player.consumePower(attack.getPowerCost());

        return new AttackResult(
                player.getName(),
                attack.getName(),
                attack.getType() == AttackType.AREA ? "Todos" : target.getName(),
                Integer.toString(damage),
                damage > attack.getMaxDamage());
    }
    
    private void specialAttack(int damage) {
        for (Enemy e : enemies) {
            if (e == null || e.isDead()) {
                continue;
            }
            
            if (ai.decideAction(e) == EnemyAction.DEFEND) {
                e.guard();
            }
            
            e.takeDamage(damage);
            
            if (e.isDead()) {
                enemiesKilled++;
            }
        }
    }

    public ArrayList<Event> enemiesTurn() {
        ArrayList<Event> enemiesResults = new ArrayList<>();

        for (Enemy e : enemies) {
            if (e == null || e.isDead()) {
                continue;
            }

            // Si estaba en modo defensa → dejar de defender
            if (e.isGuarding()) {
                e.stopGuarding();
                DefenseResult result = new DefenseResult(
                        e.getName(),
                        Double.toString(e.getDefenseReduction() * 100)
                );
                enemiesResults.add(result);
                continue;
            }

            // Ataque normal
            int damage = e.calculateDamage();

            // Aplicar daño correctamente considerando defensa del jugador
            player.takeDamage(
                    player.isGuarding() ? (int) (damage * 0.5) : damage
            );

            AttackResult result = new AttackResult(
                    e.getName(),
                    "Ataque",
                    player.getName(),
                    Integer.toString(damage),
                    false
            );

            enemiesResults.add(result);
        }

        // Si el jugador estaba en defensa, se desactiva al final del turno enemigo
        if (player.isGuarding()) {
            player.stopGuarding();
        }

        return enemiesResults;
    }

    public ConsumableResult useConsumable(int consumableIndex) {
        Consumable c = player.getInventory().get(consumableIndex);
        if (c == null) {
            return null;
        }
        ConsumableResult result = new ConsumableResult(
                player.getName(),
                c.getName(),
                Integer.toString(c.getEffect()),
                c.getType()
        );
        player.useConsumable(consumableIndex);
        consumablesUsed++;
        return result;
    }

    public DefenseResult guard() {
        player.guard();
        return new DefenseResult(player.getName(), Double.toString(player.getDefenseReduction() * 100));
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public int getConsumablesUsed() {
        return consumablesUsed;
    }
}