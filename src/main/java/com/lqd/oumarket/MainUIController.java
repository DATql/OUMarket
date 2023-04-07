/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.Branch;
import com.lqd.pojo.User;
import com.lqd.services.BranchService;
import com.lqd.services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class MainUIController implements Initializable {

    @FXML
    private VBox vbxUI;
    @FXML
    private Button btnSale;
    @FXML
    private Button btnUser;
    @FXML
    private Button btnProd;
    @FXML
    private Button btnBra;
    @FXML
    private Button btnCus;
    @FXML
    private Button btnPro;
    @FXML
    private Button btnOut;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnProd.setOnAction(evt -> {
            loadFxml("Product", vbxUI);
        });
        btnSale.setOnAction(evt -> {
            loadFxml("Sale", vbxUI);
        });
        btnUser.setOnAction(evt -> {
            try {
                loadFxml("User", vbxUI);
                UserController uC = new UserController();
                uC.resetUI(LoginController.userLogin);

            } catch (SQLException ex) {
                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnBra.setOnAction(evt -> {
            loadFxml("Branch", vbxUI);
        });

        btnPro.setOnAction(evt -> {
            loadFxml("Promotion", vbxUI);
        });
    }

    public void loadFxml(String fxmlFile, VBox UI) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile + ".fxml"));
            Node productNode = loader.load();
            UI.getChildren().clear();
            UI.getChildren().add(productNode);
        } catch (IOException ex) {
            Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadMainUI(User u) {
        if (u.getRole().toLowerCase().equals("admin")) {
            btnSale.setVisible(false);
        }
        else   if (u.getRole().toLowerCase().equals("quản lý")) {
            btnBra.setVisible(false);
            btnPro.setVisible(false);
            btnProd.setVisible(false);

        } else {
            btnBra.setVisible(false);
            btnPro.setVisible(false);
            btnProd.setVisible(false);
            btnUser.setVisible(false);
        }

    }
}



