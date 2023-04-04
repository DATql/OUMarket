/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.Product;
import com.lqd.pojo.Promotion;
import com.lqd.services.ProductService;
import com.lqd.services.PromotionService;
import com.lqd.utils.MessageBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class PromotionController implements Initializable {

    static PromotionService p = new PromotionService();
    static ProductService prod = new ProductService();
    @FXML
    private TableView<Promotion> tbPromotions;
    @FXML
    private DatePicker dpFromDate;
    @FXML
    private DatePicker dpToDate;
    @FXML
    private TextField txtNewPrice;
    @FXML
    private ComboBox cbProducts;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDiscard;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadTableColumns();
            loadTableData();
            cbProducts.getSelectionModel().clearSelection();
            cbProducts.setItems(FXCollections.observableArrayList(prod.getProducts(null)));

        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addPromotionHandler(ActionEvent event) throws SQLException {
        Product selectedProduct = (Product) cbProducts.getSelectionModel().getSelectedItem();
        String id = selectedProduct.getId();
        Promotion promotion = new Promotion(
                java.sql.Date.valueOf(this.dpFromDate.getValue()),
                java.sql.Date.valueOf(this.dpToDate.getValue()),
                Float.parseFloat(txtNewPrice.getText()),
                selectedProduct.getId());

        try {
            if (p.addPromotion(promotion)) {
                MessageBox.getBox("Question", "Add question successful", Alert.AlertType.INFORMATION).show();
                loadTableData();
            }
        } catch (SQLException ex) {
            MessageBox.getBox("Question", "Add question failed", Alert.AlertType.ERROR).show();
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void discardSaveHandler(ActionEvent event) {
        btnAdd.setVisible(true);
        btnSave.setVisible(false);
        dpFromDate.setValue(null);
        dpToDate.setValue(null);
        txtNewPrice.setText("");
    }

    public void loadTableColumns() {

        TableColumn colFromDate = new TableColumn("From Date");
        colFromDate.setCellValueFactory(new PropertyValueFactory("fromDate"));

        TableColumn colToDate = new TableColumn("To Date");
        colToDate.setCellValueFactory(new PropertyValueFactory("toDate"));

        TableColumn colNewPrice = new TableColumn("New Price");
        colNewPrice.setCellValueFactory(new PropertyValueFactory("newPrice"));

        TableColumn colProductId = new TableColumn("productID");
        colProductId.setCellValueFactory(new PropertyValueFactory("productID"));
        TableColumn colDel = new TableColumn();
        colDel.setCellFactory(r -> {
            Button btn = new Button("Delete");

            btn.setOnAction(evt -> {
                Alert a = MessageBox.getBox("Question",
                        "Are you sure to delete this promotion?",
                        Alert.AlertType.CONFIRMATION);
                a.showAndWait().ifPresent(res -> {
                    if (res == ButtonType.OK) {
                        Button b = (Button) evt.getSource();
                        TableCell cell = (TableCell) b.getParent();
                        Promotion prod = (Promotion) cell.getTableRow().getItem();
                        try {
                            if ((p.deletePromotion(prod.getId()))) {
                                MessageBox.getBox("Promotion", "Delete successful", Alert.AlertType.INFORMATION).show();
                                this.loadTableData();

                            } else {
                                MessageBox.getBox("Promotion", "Delete failed", Alert.AlertType.WARNING).show();
                            }

                        } catch (SQLException ex) {
                            MessageBox.getBox("Question", ex.getMessage(), Alert.AlertType.WARNING).show();
                            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

            });

            TableCell c = new TableCell();
            c.setGraphic(btn);
            return c;
        });
        TableColumn colUpdate = new TableColumn();
        colUpdate.setCellFactory(r -> {
            Button btn = new Button("Update");

            btn.setOnAction(evt -> {

                TableRow<Product> row = (TableRow<Product>) ((Button) evt.getSource()).getParent().getParent();
                int rowIndex = row.getIndex();
                Promotion promotion = tbPromotions.getItems().get(rowIndex);
                System.out.println(promotion.getId());
                System.out.println(promotion.getFromDate());
                ObservableList<Product> items = cbProducts.getItems();
                for (Product p : items) {
                    if (p.getId().equals(promotion.getProductID())) {
                        cbProducts.setValue(p);
                        break;
                    }
                }
                cbProducts.setItems(items);

                txtNewPrice.setText(Float.toString(promotion.getNewPrice()));

                dpFromDate.setValue(promotion.getFromDate().toLocalDate());
                dpToDate.setValue(promotion.getToDate().toLocalDate());
                btnAdd.setVisible(false);
                btnSave.setVisible(true);
                btnDiscard.setVisible(true);
                btnSave.setOnAction(event -> {
                    Product selectedProduct = (Product) cbProducts.getSelectionModel().getSelectedItem();
                    String id = selectedProduct.getId();
                    promotion.setProductID(id);
                    promotion.setFromDate(java.sql.Date.valueOf(this.dpFromDate.getValue()));
                    promotion.setToDate(java.sql.Date.valueOf(this.dpToDate.getValue()));
                    promotion.setNewPrice(Float.parseFloat(txtNewPrice.getText()));
                    try {

                        if (p.updatePromotion(promotion)) {

                            MessageBox.getBox("Sucessful", "Update Product successful", Alert.AlertType.INFORMATION).show();
                            loadTableData();
                            btnDiscard.setVisible(false);
                            btnAdd.setVisible(true);
                            btnSave.setVisible(false);;
                        }
                    } catch (SQLException ex) {
                        btnDiscard.setVisible(false);
                        btnAdd.setVisible(true);
                        btnSave.setVisible(false);
                        MessageBox.getBox("Fail", "Update Product failed", Alert.AlertType.ERROR).show();
                        Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            });

            TableCell c = new TableCell();
            c.setGraphic(btn);
            return c;
        });
        this.tbPromotions.getColumns().addAll(colFromDate, colToDate, colNewPrice, colProductId, colDel, colUpdate);
    }

    private void loadTableData() throws SQLException {
        List<Promotion> promo = p.getPromotion();

        this.tbPromotions.setItems(FXCollections.observableList(promo));
    }
}
