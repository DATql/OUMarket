/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.Customer;
import com.lqd.pojo.Product;
import com.lqd.pojo.ProductPromotion;
import com.lqd.pojo.Receipt;
import com.lqd.pojo.ReceiptDetail;
import com.lqd.services.CustomerService;
import com.lqd.services.ProductService;
import com.lqd.services.PromotionService;
import com.lqd.services.ReceiptDetailService;
import com.lqd.services.ReceiptService;
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
import java.util.Date;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class SaleController implements Initializable {

    @FXML
    private Button btnSubmit;
    @FXML
    private TableView tbCustomers;
    @FXML
    private TableView tbProducts;
    @FXML
    private TextField txtCusSearch;
    @FXML
    private TextField txtProdSearch;
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
    private float birthday = (float) 0.1;
    private List<ProductPromotion> pplist;
    static PromotionService proService = new PromotionService();
    static ProductService prodService = new ProductService();
    static CustomerService cusService = new CustomerService();
    static ReceiptService repService = new ReceiptService();
    static ReceiptDetailService detailService = new ReceiptDetailService();
    private Receipt receipt = new Receipt();
    private String role = "Nhân Viên";
    private String staffName = "dat";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            receipt.setStaffID("64acd540-7c61-4dad-9e8f-6efba1343652");
            loadProductTableColumns();
            loadTableProductData(null);
            loadReceiptColumn();
            loadCustomerColumns();
            loadTableCustomerData(null);
        } catch (SQLException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.txtCusSearch.textProperty().addListener(e -> {

            this.loadTableCustomerData(this.txtCusSearch.getText());

        });
        this.txtProdSearch.textProperty().addListener(e -> {
            try {
                this.loadTableProductData(this.txtProdSearch.getText());
            } catch (SQLException ex) {
                Logger.getLogger(PromotionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void loadCustomerColumns() {
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(120);

        TableColumn colPhone = new TableColumn("Phone");
        colPhone.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        TableColumn colSex = new TableColumn("Sex");
        colSex.setCellValueFactory(new PropertyValueFactory("sex"));

        TableColumn colBirthDay = new TableColumn("dateOfBirth");
        colBirthDay.setCellValueFactory(new PropertyValueFactory("dateOfBirth"));

        TableColumn colEmail = new TableColumn("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colEmail.setPrefWidth(60);

        TableColumn colAdd = new TableColumn();
        colAdd.setCellFactory(r -> {
            Button btn = new Button("Chọn");

            btn.setOnAction(evt -> {
                TableCell cell = (TableCell) btn.getParent();
                Customer customer = (Customer) cell.getTableRow().getItem();
                receipt.setCustomerID(customer.getId());

                java.time.LocalDate localDate = customer.getDateOfBirth().toLocalDate();
                java.time.LocalDate now = java.time.LocalDate.now();
                int compareResult = localDate.compareTo(now);

                if (compareResult == 0) {
                    txtBirthDay.setText("Giảm 10%");
                    birthday = (float) 0.1;
                } else {
                    txtBirthDay.setText("Không có giảm giá");
                    birthday = 1;
                }
            });

            TableCell c = new TableCell();
            c.setGraphic(btn);
            return c;
        }
        );

        this.tbCustomers.getColumns()
                .addAll(colName, colPhone, colEmail, colBirthDay, colSex, colAdd);
    }

    public void loadProductTableColumns() {
        TableColumn colName = new TableColumn("Name");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(120);

        TableColumn colUnit = new TableColumn("Unit");
        colUnit.setCellValueFactory(new PropertyValueFactory("unit"));

        TableColumn colPrice = new TableColumn("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));

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
                        ProductPromotion pp = new ProductPromotion(prod.getId(), prod.getPrice(), prod.getUnit(), (proService.getNewPrice(prod.getId()) == null ? prod.getPrice() : proService.getNewPrice(prod.getId())), prod.getName());
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

                        ProductPromotion pp = new ProductPromotion(prod.getId(), prod.getPrice(), prod.getUnit(), (proService.getNewPrice(prod.getId()) == null ? prod.getPrice() : proService.getNewPrice(prod.getId())), prod.getName());
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
                .addAll(colName, colUnit, colPrice, colOrigin, colCate, colAdd);
    }

    public void loadTableProductData(String kw) throws SQLException {

        List<Product> ques = prodService.getProducts(kw);

        this.tbProducts.getItems().clear();
        this.tbProducts.setItems(FXCollections.observableList(ques));
    }

    public void loadTableCustomerData(String kw) {

        try {
            List<Customer> cus = cusService.getCustomers(kw);
            this.tbCustomers.getItems().clear();
            this.tbCustomers.setItems(FXCollections.observableList(cus));

        } catch (SQLException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadReceiptColumn() {
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

                        if (!txtQuantity.getText().isEmpty()) {
                            try {
                                if (prod.getUnit().equalsIgnoreCase("kg")) {
                                    prod.setQuantity(Float.parseFloat(txtQuantity.getText()));
                                } else {
                                    prod.setQuantity(Integer.parseInt(txtQuantity.getText()));
                                }
                                Label dummyLabel = new Label();
                                Pane root = (Pane) cell.getScene().getRoot();
                                root.getChildren().add(dummyLabel);
                                dummyLabel.requestFocus();
                                tbReceipt.refresh();
                            } catch (NumberFormatException ex) {
                                MessageBox.getBox("Thông báo", "Sai định lượng sản phẩm!!", Alert.AlertType.ERROR).show();
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
            temp += ppList.get(i).getPrice() * ppList.get(i).getQuantity();

            promo += (ppList.get(i).getPrice() - ppList.get(i).getNewPrice()) * ppList.get(i).getQuantity();
            total = temp - promo;

        }

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

    public void submitReceiptHandler() throws SQLException {
        receipt.setReceipt(birthday, total, promo, total);
        if (pplist == null) {
            MessageBox.getBox("Error", "Receipt Product Detail Is Emty !!!", Alert.AlertType.INFORMATION).show();
            return;
        }
        try {

            if (repService.addReceipt(receipt)) {
                for (ProductPromotion pp : pplist) {
                    detailService.addReceiptDetail(pp, receipt.getId());
                }
                MessageBox.getBox("Successful", "Add receipt successful", Alert.AlertType.INFORMATION).show();
            }
        } catch (SQLException ex) {
            MessageBox.getBox("Failed", "Add receipt failed", Alert.AlertType.ERROR).show();
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
