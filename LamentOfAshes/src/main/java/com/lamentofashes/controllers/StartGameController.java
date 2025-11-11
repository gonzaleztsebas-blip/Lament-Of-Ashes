package com.lamentofashes.controllers;

import com.lamentofashes.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class StartGameController {

    @FXML
    private void switchToBattleScene() throws IOException {
        App.setRoot("BattleScene");
    }
    
    @FXML
    private void switchToTitleScene() throws IOException {
        App.setRoot("TitleScene");
    }
    
    @FXML
    private void exitGame() throws IOException {
        System.exit(0);
    }
}
