/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.*;
import com.lqd.services.*;
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
    private Text txtCus;
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
    static CategoryService cateService = new CategoryService();
    static ReceiptDetailService detailService = new ReceiptDetailService();
    private Receipt receipt = new Receipt();
    private String role = "Nhân Viên";
    private String staffName = "dat";

    private User u =LoginController.userLogin;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            receipt.setStaffID(u.getId());
            loadProductTableColumns();
            loadTableProductData(null);
            loadReceiptColumn();
            loadCustomerColumns();
            loadTableCustomerData(null);
        } catch (SQLException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.txtCusSearch.textProperty().addListener(e -> {
            try {
                this.loadTableProductData(this.txtCusSearch.getText());
            } catch (SQLException ex) {
                Logger.getLogger(PromotionController.class.getName()).log(Level.SEVERE, null, ex);
            }

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
        TableColumn colName = new TableColumn("Tên");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(100);

        TableColumn colPhone = new TableColumn("SĐT");
        colPhone.setCellValueFactory(new PropertyValueFactory("phoneNumber"));

        TableColumn colBirthDay = new TableColumn("Ngày sinh");
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

                txtCus.setText(customer.getName());
                java.time.LocalDate localDate = customer.getDateOfBirth().toLocalDate();
                java.time.LocalDate now = java.time.LocalDate.now();

                if (localDate.getMonthValue() == now.getMonthValue() && localDate.getDayOfMonth() == now.getDayOfMonth()) {
                    txtBirthDay.setText("10%");
                    birthday=(float) 0.1;

                } else {
                    txtBirthDay.setText("0%");
                    birthday = 1;
                }
            });

            btn.setStyle("-fx-background-color:  #4e73df; -fx-text-fill: white;");
            TableCell<Customer, Void> c = new TableCell<>() {
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        Customer cus = getTableView().getItems().get(getIndex());
                        setGraphic(cus.getId() != null && !cus.getId().isEmpty() ? btn : null);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return c;
        });

        this.tbCustomers.getColumns().addAll(colName, colPhone, colEmail, colBirthDay, colAdd);
    }

    public void loadProductTableColumns() {
        TableColumn colName = new TableColumn("Tên");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(120);

        TableColumn colUnit = new TableColumn("Đơn vị");
        colUnit.setCellValueFactory(new PropertyValueFactory("unit"));

        TableColumn colPrice = new TableColumn("Giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colPrice.setCellFactory(column -> {
            TableCell<Promotion, Float> cell = new TableCell<Promotion, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String formattedPrice = String.format("%,.0f VNĐ", item);
                        setText(formattedPrice);
                    }
                }
            };
            return cell;
        });

        TableColumn colOrigin = new TableColumn("Xuất Xứ");
        colOrigin.setCellValueFactory(new PropertyValueFactory("origin"));

        TableColumn colCate = new TableColumn("Loại SP");
        colCate.setCellValueFactory(new PropertyValueFactory("categoryID"));
        colCate.setPrefWidth(90);
        TableColumn colAdd = new TableColumn();
        colAdd.setCellFactory(r -> {
            Button btn = new Button("Thêm");

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
            btn.setStyle("-fx-background-color:  #4e73df; -fx-text-fill: white;");
            TableCell<Product, Void> c = new TableCell<>() {
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        Product prod = getTableView().getItems().get(getIndex());
                        setGraphic(prod.getId() != null && !prod.getId().isEmpty() ? btn: null);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return c;

        });

        this.tbProducts.getColumns()
                .addAll(colName, colUnit, colPrice, colOrigin, colCate, colAdd);
    }

    public void loadTableProductData(String kw) throws SQLException {
        try {
            List<Product> prods = prodService.getProducts(kw);
            this.tbProducts.getItems().clear();
            for (Product prod : prods) {
                String categoryID = prod.getCategoryID();
                Category cate = cateService.getCategoryByID(categoryID);
                prod.setCategoryID(cate != null ? cate.getName() : "");
            }
            this.tbProducts.setItems(FXCollections.observableList(prods));
        } catch (SQLException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadTableCustomerData(String kw) {

        try {
            List<Customer> cus = cusService.getCustomersByPhoneNumber(kw);
            this.tbCustomers.getItems().clear();
            this.tbCustomers.setItems(FXCollections.observableList(cus));

        } catch (SQLException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadReceiptColumn() {
        TableColumn colName = new TableColumn("Tên");
        colName.setCellValueFactory(new PropertyValueFactory("name"));


        TableColumn colUnit = new TableColumn("Đơn vị");
        colUnit.setCellValueFactory(new PropertyValueFactory("unit"));
        colUnit.setPrefWidth(60);
        TableColumn colPrice = new TableColumn("Giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colPrice.setCellFactory(column -> {
            TableCell<Promotion, Float> cell = new TableCell<Promotion, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String formattedPrice = String.format("%,.0f VNĐ", item);
                        setText(formattedPrice);
                    }
                }
            };
            return cell;
        });

        TableColumn colNewPrice = new TableColumn("Khuyến mãi");
        colNewPrice.setCellValueFactory(new PropertyValueFactory("newPrice"));
        colNewPrice.setCellFactory(column -> {
            TableCell<Promotion, Float> cell = new TableCell<Promotion, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String formattedPrice = String.format("%,.0f VNĐ", item);
                        setText(formattedPrice);
                    }
                }
            };
            return cell;
        });

        TableColumn colQuantity = new TableColumn("SL");
        colQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        colQuantity.setPrefWidth(50);
        TableColumn colQuantityTxt = new TableColumn("Nhập SL ");

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

            TableCell<ProductPromotion, Void> c = new TableCell<>() {
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        ProductPromotion prod = getTableView().getItems().get(getIndex());
                        setGraphic(prod.getId() != null && !prod.getId().isEmpty() ? txtQuantity: null);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return c;
        });

        TableColumn colDel = new TableColumn();
        colDel.setCellFactory(r -> {
            Button btn = new Button("Xóa");

            btn.setOnAction(evt -> {
                TableCell cell = (TableCell) btn.getParent();
                ProductPromotion prod = (ProductPromotion) cell.getTableRow().getItem();

                deleteReceiptItem(prod);
                List<ProductPromotion> pplist = tbReceipt.getItems();
                pplist.remove(prod);
                tbReceipt.refresh();

            });
            btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            TableCell<ProductPromotion, Void> c = new TableCell<>() {
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        ProductPromotion prod = getTableView().getItems().get(getIndex());
                        setGraphic(prod.getId() != null && !prod.getId().isEmpty() ? btn: null);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return c;
        });
        colDel.setPrefWidth(50);
        this.tbReceipt.getColumns().addAll(colName, colUnit, colPrice, colNewPrice, colQuantity, colQuantityTxt, colDel);
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
