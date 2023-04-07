package com.lqd.oumarket;

import com.lqd.pojo.User;
import com.lqd.services.UserService;
import com.lqd.utils.MessageBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.lqd.utils.HashPassword;
import javafx.scene.Node;

public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    HashPassword hash = new HashPassword();

    public void switchToMainUI(ActionEvent event) throws IOException {
        String username = this.txtUsername.getText();
        String password = hash.hashPassword(this.txtPassword.getText());

        UserService userService = new UserService();
        try {
            User u = userService.checkUser(username, password);
            if(u!=null)
            {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();     
       FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainUI.fxml"));
       Parent root = fxmlLoader.load();
       
       MainUIController controller = fxmlLoader.getController();
           
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            loginStage.close();
            }
            else
                       MessageBox.getBox("Thông báo", "Mật khẩu hoặc tài khoản không đúng", Alert.AlertType.ERROR).show();
        } catch (SQLException ex) {
                 

            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
