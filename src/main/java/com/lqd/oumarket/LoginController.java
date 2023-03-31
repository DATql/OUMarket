package com.lqd.oumarket;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginController {
    
    @FXML private Button loginButton;
    @FXML private Button closeButton;
    
    @FXML
    private void switchToMainUI() throws IOException {
        App.setRoot("MainUI");
        
    }
}
