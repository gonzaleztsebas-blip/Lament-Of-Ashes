package com.lamentofashes.controllers.ui;

import com.lamentofashes.controllers.ui.AnimationManager;
import javafx.scene.image.ImageView;
import com.lamentofashes.logic.battle.BattleManager;
import java.util.function.BiConsumer;

public class EnemyClickHandler {
    
    private final BattleManager battleManager;
    private final UIUpdater uiUpdater;
    private final AnimationManager animationManager;
    private final Runnable onBattleEnd;
    
    public EnemyClickHandler(BattleManager battleManager, UIUpdater uiUpdater, 
                            AnimationManager animationManager, Runnable onBattleEnd) {
        this.battleManager = battleManager;
        this.uiUpdater = uiUpdater;
        this.animationManager = animationManager;
        this.onBattleEnd = onBattleEnd;
    }
    
    /**
     * Configurar eventos de ataque en enemigos
     * @param enemyViews Array de ImageView de enemigos
     * @param onEnemySelected Callback cuando se selecciona un enemigo (enemy, index)
     */
    public void configurarEventosAtaque(ImageView[] enemyViews, BiConsumer<ImageView, Integer> onEnemySelected) {
        for (int i = 0; i < enemyViews.length; i++) {
            final int index = i;
            ImageView enemyView = enemyViews[i];
            
            // Guardar índice en el ImageView
            enemyView.setUserData(index);
            
            enemyView.setOnMouseClicked(e -> {
                if (!battleManager.isBattleOver() && index < battleManager.getEnemies().size()) {
                    onEnemySelected.accept(enemyView, index);
                }
            });
            
        }
    }
}