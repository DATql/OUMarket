/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.Category;
import com.lqd.pojo.Product;
import com.lqd.services.CategoryService;
import com.lqd.services.ProductService;
import com.lqd.utils.MessageBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class ProductController implements Initializable {

    static CategoryService s = new CategoryService();
    static ProductService p = new ProductService();

    @FXML
    TableView<Product> tbProducts;
    @FXML
    private ComboBox cbCategories;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtUnit;
    @FXML
    private TextField txtOrigin;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtSearch;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<Category> cates = s.getCategories();
            this.cbCategories.setItems(FXCollections.observableList(cates));

        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addProductHandler(ActionEvent event) throws SQLException {
        Category selectedCategory = (Category) cbCategories.getValue();
        int categoryId = selectedCategory.getId();
        Product prod = new Product(
                this.txtName.getText(),
                this.txtUnit.getText(),
                Float.parseFloat(this.txtPrice.getText()),
                Integer.parseInt(this.txtQuantity.getText()),
                this.txtOrigin.getText(),
                categoryId
        );
        try {
            if (p.addProduct(prod)) {
                MessageBox.getBox("Question", "Add question successful", Alert.AlertType.INFORMATION).show();
            }
        } catch (SQLException ex) {
            MessageBox.getBox("Question", "Add question failed", Alert.AlertType.ERROR).show();
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
