/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;
import com.lqd.pojo.Category;
import com.lqd.pojo.Product;
import com.lqd.services.CategoryService;
import com.lqd.services.ProductService;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class ProductController implements Initializable {
    @FXML private ComboBox cbCategories;
    @FXML private TextField txtName;
    @FXML private TextField txtUnit;
    @FXML private TextField txtOrigin;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtPrice;
    @FXML private TextField txtSearch;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      CategoryService s = new CategoryService();
        try {
            List<Category> cates = s.getCategories();
            this.cbCategories.setItems(FXCollections.observableList(cates));
            
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public void addProductHandler(ActionEvent event) throws SQLException{
        Category selectedCategory = (Category) cbCategories.getValue();
                int categoryId = selectedCategory.getId();
        Product prod = new Product(UUID.randomUUID().toString(),
                this.txtName.getText(),
                this.txtUnit.getText(),
                Float.parseFloat(this.txtPrice.getText())
                ,Integer.parseInt(this.txtQuantity.getText()),
                this.txtOrigin.getText(),
                categoryId
                );
        ProductService p = new ProductService();
        p.addProduct(prod);

    }
    
}
