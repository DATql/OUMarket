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
    public String role;
    public String staffName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (role.toLowerCase().equals("admin")) {
            btnSale.setDisable(true);

        }
        if (role.toLowerCase().equals("quản lý")) {
            btnBra.setDisable(true);
            btnProd.setDisable(true);
            btnPro.setDisable(true);
        }
        else
              btnBra.setDisable(true);
            btnProd.setDisable(true);
            btnPro.setDisable(true);
            btnUser.setDisable(true);

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
                UserService userService = new UserService();
                User user =userService.getUser(staffName);
                BranchService branchService = new BranchService();
                Branch branch = branchService.getBranch(user.getBranchID());
                uC.setComboBox(branch, user);
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

    public void loadMainUI(String role) {
        if (!role.toLowerCase().equals("admin")) {
            if (role.toLowerCase().equals("Quản lý")) {

            } else {
                btnBra.setDisable(false);
                btnCus.setVisible(false);
                btnPro.setVisible(false);
                btnProd.setVisible(false);
                btnUser.setVisible(false);
                ;
            }

        } else {
            btnSale.setVisible(false);
        }

    }

 
}
