/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.Product;
import com.lqd.pojo.ProductPromotion;
import com.lqd.services.ProductService;
import com.lqd.services.PromotionService;
import com.lqd.utils.MessageBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class SaleController implements Initializable {

    @FXML
    private TableView tbProducts;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView tbReceipt;
    @FXML
    private TextField txtReceive;
    @FXML
    private Text txtTemp;
    @FXML
    private Text txtPromo;
    @FXML
    private Text txtBirthDay;
    @FXML
    private Text txtChanges;
    @FXML
    private Text txtTotal;
    private float temp;
    private float promo;
    private float total;
    private List<ProductPromotion> pplist;
    static PromotionService p = new PromotionService();
    static ProductService prod = new ProductService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadProductTableColumns();
            loadTableData(null);
            loadReceiptColumn();
        } catch (SQLException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.txtSearch.textProperty().addListener(e -> {
            try {
                this.loadTableData(this.txtSearch.getText());
            } catch (SQLException ex) {
                Logger.getLogger(PromotionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void loadProductTableColumns() {
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(120);

        TableColumn colUnit = new TableColumn("Unit");
        colUnit.setCellValueFactory(new PropertyValueFactory("unit"));

        TableColumn colPrice = new TableColumn("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));

        TableColumn colQuantity = new TableColumn("Quantity");
        colQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));

        TableColumn colOrigin = new TableColumn("Origin");
        colOrigin.setCellValueFactory(new PropertyValueFactory("origin"));

        TableColumn colCate = new TableColumn("CategoryID");
        colCate.setCellValueFactory(new PropertyValueFactory("categoryID"));
        colCate.setPrefWidth(40);
        TableColumn colAdd = new TableColumn();
        colAdd.setCellFactory(r -> {
            Button btn = new Button("Add");

            btn.setOnAction(evt -> {
                TableCell cell = (TableCell) btn.getParent();
                Product prod = (Product) cell.getTableRow().getItem();
                if (tbReceipt.getItems().isEmpty()) {
                    try {
                        ProductPromotion pp = new ProductPromotion(prod.getId(), prod.getPrice(), prod.getUnit(), (p.getNewPrice(prod.getId()) == null ? prod.getPrice() : p.getNewPrice(prod.getId())), prod.getName());
                        pplist = new ArrayList<>();
                        pplist.add(pp);
                        setReceipt(pplist);

                        this.tbReceipt.setItems(FXCollections.observableList(pplist));
                    } catch (SQLException ex) {
                        Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    List<ProductPromotion> pplist = tbReceipt.getItems();
                    for (ProductPromotion pp : pplist) {
                        if (pp.getId().equals(prod.getId())) {
                            return;
                        }
                    }
                    try {

                        ProductPromotion pp = new ProductPromotion(prod.getId(), prod.getPrice(), prod.getUnit(), (p.getNewPrice(prod.getId()) == null ? prod.getPrice() : p.getNewPrice(prod.getId())), prod.getName());
                        tbReceipt.getItems().add(pp);
                        tbReceipt.refresh();

                        setReceipt(pplist);
                    } catch (SQLException ex) {
                        Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            TableCell c = new TableCell();
            c.setGraphic(btn);
            return c;
        }
        );

        this.tbProducts.getColumns()
                .addAll(colName, colUnit, colPrice, colQuantity, colOrigin, colCate, colAdd);
    }

    private void loadTableData(String kw) throws SQLException {

        List<Product> ques = prod.getProducts(kw);

        this.tbProducts.getItems().clear();
        this.tbProducts.setItems(FXCollections.observableList(ques));
    }

    private void loadReceiptColumn() {
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(100);

        TableColumn colUnit = new TableColumn("Unit");
        colUnit.setCellValueFactory(new PropertyValueFactory("unit"));
        colUnit.setPrefWidth(40);
        TableColumn colPrice = new TableColumn("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));

        TableColumn colNewPrice = new TableColumn("New Price");
        colNewPrice.setCellValueFactory(new PropertyValueFactory("newPrice"));

        TableColumn colQuantity = new TableColumn("Quantity");
        colQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        colQuantity.setPrefWidth(60);
        TableColumn colQuantityTxt = new TableColumn("Count ");
        colQuantityTxt.setPrefWidth(50);
        colQuantityTxt.setCellFactory(r -> {
            TextField txtQuantity = new TextField();
            txtQuantity.setText("");
            txtQuantity.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    // TextField đang được focus
                    TableCell cell = (TableCell) txtQuantity.getParent();
                    ProductPromotion prod = (ProductPromotion) cell.getTableRow().getItem();
                    if (cell != null) {
                        int rowIndex = cell.getIndex();
                        System.out.println(rowIndex);

                        if (!txtQuantity.getText().isEmpty()) {
                            try {
                                prod.setQuantity(Integer.parseInt(txtQuantity.getText()));
                                Label dummyLabel = new Label();
                                Pane root = (Pane) cell.getScene().getRoot();
                                root.getChildren().add(dummyLabel);
                                dummyLabel.requestFocus();
                                tbReceipt.refresh();
                            } catch (NumberFormatException ex) {
                                MessageBox.getBox("Error", "Not Integer Number !!", Alert.AlertType.ERROR).show();
                                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                    setReceipt(pplist);

                }
            });

            TableCell c = new TableCell();
            c.setGraphic(txtQuantity);
            return c;
        });

        TableColumn colAdd = new TableColumn();
        colAdd.setCellFactory(r -> {
            Button btn = new Button("Delete");

            btn.setOnAction(evt -> {
                TableCell cell = (TableCell) btn.getParent();
                ProductPromotion prod = (ProductPromotion) cell.getTableRow().getItem();
                System.out.println(prod);
                deleteReceiptItem(prod);
                List<ProductPromotion> pplist = tbReceipt.getItems();
                pplist.remove(prod);
                tbReceipt.refresh();

            });

            TableCell c = new TableCell();
            c.setGraphic(btn);
            return c;
        });
        this.tbReceipt.getColumns().addAll(colName, colUnit, colPrice, colNewPrice, colQuantity, colQuantityTxt, colAdd);
    }

    public void setReceipt(List<ProductPromotion> ppList) {
        temp = 0;
        promo = 0;
        total = 0;
        for (int i = 0; i < ppList.size(); i++) {
            System.out.print(" " + ppList.get(i));
        }
        System.out.println(ppList);

        for (int i = 0; i < ppList.size(); i++) {
            temp += ppList.get(i).getPrice() * ppList.get(i).getQuantity();
            System.out.print(" " + ppList.get(i).getPrice());
            promo += (ppList.get(i).getPrice() - ppList.get(i).getNewPrice()) * ppList.get(i).getQuantity();
            total = temp - promo;

        }
        System.out.println();
        System.out.println(temp);
        txtTotal.setText(String.valueOf(total));
        txtTemp.setText(String.valueOf(temp));
        txtPromo.setText("- " + promo);
        if (txtReceive.getText().isEmpty()) {
            txtChanges.setText("Chưa nhập số tiền");
        } else {
            txtChanges.setText(String.valueOf(Float.parseFloat(txtReceive.getText()) - total));
        }
    }

    public void deleteReceiptItem(ProductPromotion pp) {
        temp -= pp.getPrice() * pp.getQuantity();
        txtTemp.setText(String.valueOf(temp));
        promo -= (pp.getPrice() - pp.getNewPrice()) * pp.getQuantity();
        txtPromo.setText(String.valueOf(promo));
        total -= pp.getNewPrice();
        txtTotal.setText(String.valueOf(total));
        if (txtReceive.getText().isEmpty()) {
            txtChanges.setText("Chưa nhập số tiền");
        } else {
            txtChanges.setText(String.valueOf(Float.parseFloat(txtReceive.getText()) - total));
        }

    }

}
