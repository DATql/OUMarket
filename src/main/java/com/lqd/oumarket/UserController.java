/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.lqd.oumarket;

import com.lqd.pojo.Branch;
import com.lqd.pojo.Category;
import com.lqd.pojo.User;
import com.lqd.services.BranchService;
import com.lqd.services.UserService;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lqd.utils.MessageBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Gol
 */
public class UserController implements Initializable {
    UserService u = new UserService();
    BranchService p = new BranchService();
    /**
     * Initializes the controller class.
     */
    @FXML
    TableView<User> tbUsers;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAdress;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private ComboBox cbRole;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox cbBranch;
    @FXML
    private ComboBox cbSex;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField pwfPassWord;
    @FXML
    private PasswordField pwfConfirmPassWord;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadTableColumns();
            loadTableData(null);
            loadInterface();
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.txtSearch.textProperty().addListener(e -> {
            try {
                this.loadTableData(this.txtSearch.getText());
            } catch (SQLException ex) {
                Logger.getLogger(PromotionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void loadTableColumns() {
        TableColumn colName = new TableColumn("Tên");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setPrefWidth(155);

        TableColumn colEmail = new TableColumn("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colEmail.setPrefWidth(200);

        TableColumn colBranch = new TableColumn("Chi nhánh");
        colBranch.setCellValueFactory(new PropertyValueFactory("branchID"));
        colBranch.setPrefWidth(200);

        TableColumn colRole = new TableColumn("Vai trò");
        colRole.setCellValueFactory(new PropertyValueFactory("role"));
        colRole.setPrefWidth(70);

        TableColumn colDel = new TableColumn();
        colDel.setCellFactory(r -> {
            Button btn = new Button("Xóa");

            btn.setOnAction(evt -> {
                Alert a = MessageBox.getBox("Thông báo",
                        "Bạn muốn xóa người dùng này không?",
                        Alert.AlertType.CONFIRMATION);
                a.showAndWait().ifPresent(res -> {
                    if (res == ButtonType.OK) {
                        Button b = (Button) evt.getSource();
                        TableCell cell = (TableCell) b.getParent();
                        User user = (User) cell.getTableRow().getItem();
                        try {
                            boolean btrue = u.deleteUser(user.getId());
                            if (btrue) {
                                MessageBox.getBox("Thông báo", "Xóa người dùng thành công", Alert.AlertType.INFORMATION).show();
                                this.loadTableData(null);
                            } else {
                                MessageBox.getBox("Thông báo", "Xóa người dùng thất bại", Alert.AlertType.WARNING).show();
                            }

                        } catch (SQLException ex) {
                            MessageBox.getBox("Thông báo", ex.getMessage(), Alert.AlertType.WARNING).show();
                            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

            });

            TableCell c = new TableCell<User, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        User user = getTableView().getItems().get(getIndex());
                        String name = user.getName();
                        String role = user.getRole().toLowerCase();
                        if (name != null && !name.isEmpty()  && !"admin".equals(role)) {
                            setGraphic(btn);
                        } else {
                            setGraphic(null);
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            };

            return c;
        });

        TableColumn colUpdate = new TableColumn();
        colUpdate.setCellFactory(r -> {
            Button btn = new Button("Sửa");

            btn.setOnAction(evt -> {
                TableRow<User> row = (TableRow<User>) ((Button) evt.getSource()).getParent().getParent();
                int rowIndex = row.getIndex();
                User user = tbUsers.getItems().get(rowIndex);
                txtName.setText(user.getName());
                txtAdress.setText(user.getAdress());
                txtPhoneNumber.setText(user.getPhoneNumber());
                txtEmail.setText(user.getEmail());
                cbBranch.getSelectionModel().select(user.getBranchID());

                String role = user.getRole().toLowerCase();
                List<String> roles;
                if ("admin".equals(role)) {
                    roles = Arrays.asList("Admin");
                }else {
                    roles = Arrays.asList("Quản lý", "Nhân viên");
                }
                this.cbRole.setItems(FXCollections.observableList(roles));
                cbRole.getSelectionModel().select(user.getRole());

                if (user.getDateOfBirth() != null) {
                    dpDateOfBirth.setValue(user.getDateOfBirth().toLocalDate());
                } else {
                    dpDateOfBirth.setValue(null);
                }

                cbSex.getSelectionModel().select(user.getSex());
                txtUserName.setText(user.getUsername());

                btnAdd.setVisible(false);
                btnSave.setVisible(true);
                btnSave.setOnAction(event -> {
                    user.setName(txtName.getText());
                    user.setAdress(txtAdress.getText());
                    user.setPhoneNumber(txtPhoneNumber.getText());
                    user.setEmail(txtEmail.getText());
                    user.setUsername(txtUserName.getText());
                    user.setRole(cbRole.getSelectionModel().toString());
                    Branch selectedBranch = (Branch) cbBranch.getValue();
                    String branchId = selectedBranch.getId();
                    user.setBranchID(branchId);
                    user.setSex(cbSex.getSelectionModel().toString());
                    System.out.println(user);
                    try {
                        if (u.updateUser(user)) {
                            MessageBox.getBox("Thông báo", "Chỉnh sửa chi nhánh thành công", Alert.AlertType.INFORMATION).show();
                            loadTableData(null);
                            loadInterface();
                        }
                    } catch (SQLException ex) {
                        MessageBox.getBox("Thông báo", "Chỉnh sửa chi nhánh thất bại", Alert.AlertType.ERROR).show();
                        Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            });

            TableCell c = new TableCell<User, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        User user = getTableView().getItems().get(getIndex());
                        String name = user.getName();
                        if (name != null && !name.isEmpty()) {
                            setGraphic(btn);
                        } else {
                            setGraphic(null);
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            };

            return c;
        });
        this.tbUsers.getColumns().addAll(colName, colEmail, colBranch, colRole, colDel, colUpdate);
    }

    private void loadTableData(String kw) throws SQLException {

        List<User> users = u.getUsers(kw);
        this.tbUsers.getItems().clear();
        for (User user : users) {
            // Kiểm tra nếu giá trị của cột "branch" là trống thì gán giá trị mặc định là ""
            if (user.getBranchID() == null || user.getBranchID().isEmpty()) {
                user.setBranchID("");
            }else{
                Branch branch = p.getBranch(user.getBranchID());
                if (branch != null) {
                    user.setBranchID(branch.getName());
                } else {
                    user.setBranchID("");
                }
            }
        }
        this.tbUsers.setItems(FXCollections.observableList(users));
    }
    private void loadInterface() throws SQLException {
        List<Branch> branches = p.getBranchs("");
        this.cbBranch.setItems(FXCollections.observableList(branches));

        List<String> sex = Arrays.asList("Nam", "Nữ", "Khác");
        this.cbSex.setItems(FXCollections.observableList(sex));

        List<String> roles = Arrays.asList("Quản lý", "Nhân viên");
        this.cbRole.setItems(FXCollections.observableList(roles));

        this.txtName.setText(null);
        this.txtAdress.setText(null);
        this.txtPhoneNumber.setText(null);
        this.txtEmail.setText(null);
        this.txtSearch.setText(null);
        this.txtUserName.setText(null);
        this.pwfPassWord.setText(null);
        this.pwfConfirmPassWord.setText(null);
        this.cbSex.getSelectionModel().clearSelection();
        this.cbRole.getSelectionModel().clearSelection();
        this.cbBranch.getSelectionModel().clearSelection();
        this.dpDateOfBirth.setValue(null);
        btnSave.setVisible(false);
        btnAdd.setVisible(true);
    }
    public void CancelUserHandler(ActionEvent event) throws SQLException {
        loadTableData(null);
        loadInterface();
    }
}
