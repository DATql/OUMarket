/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
*/
package com.lqd.oumarket;


import com.lqd.pojo.Branch;
import com.lqd.services.BranchService;
import com.lqd.utils.MessageBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
public class BranchController implements Initializable {
   static BranchService p = new BranchService();
   /**
    * Initializes the controller class.
    */
   @FXML
   TableView<Branch> tbBranchs;


   @FXML
   private TextField txtName;
   @FXML
   private TextField txtAdress;
   @FXML
   private TextField txtSearch;
   @FXML
   private Button btnAdd;
   @FXML
   private Button btnSave;
   @Override
   public void initialize(URL url, ResourceBundle rb) {
       try {
           loadTableColumns();
           loadTableData(null);
           loadInterface();
       } catch (SQLException ex) {
           Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
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
       TableColumn colName = new TableColumn("Tên chi nhánh");
       colName.setCellValueFactory(new PropertyValueFactory("name"));
       colName.setPrefWidth(200);


       TableColumn colAdress = new TableColumn("Địa chỉ");
       colAdress.setCellValueFactory(new PropertyValueFactory("adress"));
       colAdress.setPrefWidth(250);
      
//        TableColumn colManager = new TableColumn("Quản lý");
//        colAdress.setCellValueFactory(new PropertyValueFactory("manager"));
//        colAdress.setPrefWidth(200);
      
       TableColumn colDel = new TableColumn();
       colDel.setCellFactory(r -> {
           Button btn = new Button("Xóa");


           btn.setOnAction(evt -> {
               Alert a = MessageBox.getBox("Thông báo",
                       "Bạn muốn xóa chi nhánh này không?",
                       Alert.AlertType.CONFIRMATION);
               a.showAndWait().ifPresent(res -> {
                   if (res == ButtonType.OK) {
                       Button b = (Button) evt.getSource();
                       TableCell cell = (TableCell) b.getParent();
                       Branch branch = (Branch) cell.getTableRow().getItem();
                       try {
                           boolean btrue=p.deleteBranch(branch.getId());
                           if (btrue) {
                               MessageBox.getBox("Thông báo", "Xóa chi nhánh thành công", Alert.AlertType.INFORMATION).show();
                               this.loadTableData(null);
                           } else {
                               MessageBox.getBox("Thông báo", "Xóa chi nhánh thất bại", Alert.AlertType.WARNING).show();
                           }


                       } catch (SQLException ex) {
                           MessageBox.getBox("Thông báo", ex.getMessage(), Alert.AlertType.WARNING).show();
                           Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
                       }


                   }
               });


           });


           TableCell c = new TableCell<Branch, Void>() {
               @Override
               protected void updateItem(Void item, boolean empty) {
                   super.updateItem(item, empty);
                   if (!empty) {
                       Branch branch = getTableView().getItems().get(getIndex());
                       String name = branch.getName();
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


       TableColumn colUpdate = new TableColumn();
       colUpdate.setCellFactory(r -> {
           Button btn = new Button("Sửa");
          
           btn.setOnAction(evt -> {
               TableRow<Branch> row = (TableRow<Branch>) ((Button) evt.getSource()).getParent().getParent();
               int rowIndex = row.getIndex();
               Branch branch = tbBranchs.getItems().get(rowIndex);
               txtName.setText(branch.getName());
               txtAdress.setText(branch.getAdress());
              
               btnAdd.setVisible(false);
               btnSave.setVisible(true);
               btnSave.setOnAction(event -> {
                   if(txtName.getText().isEmpty() || txtAdress.getText().isEmpty()){
                       MessageBox.getBox("Thông báo", "Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING).show();
                   }
                   else{
                       branch.setName(txtName.getText());
                       branch.setAdress(txtAdress.getText());
                       try {
                           if (p.updateBranch(branch)) {


                               MessageBox.getBox("Thông báo", "Chỉnh sửa chi nhánh thành công", Alert.AlertType.INFORMATION).show();
                               loadTableData(null);
                               loadInterface();
                           }
                       } catch (SQLException ex) {
                           MessageBox.getBox("Thông báo", "Chỉnh sửa chi nhánh thất bại", Alert.AlertType.ERROR).show();
                           Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   }
               });
           });


           TableCell c = new TableCell<Branch, Void>() {
               @Override
               protected void updateItem(Void item, boolean empty) {
                   super.updateItem(item, empty);
                   if (!empty) {
                       Branch branch = getTableView().getItems().get(getIndex());
                       String name = branch.getName();
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
       this.tbBranchs.getColumns().addAll(colName, colAdress,  colDel, colUpdate);
   }


   private void loadTableData(String kw) throws SQLException {


       List<Branch> branch = p.getBranchs(kw);


       this.tbBranchs.getItems().clear();
       this.tbBranchs.setItems(FXCollections.observableList(branch));
   }
   private void loadInterface(){
       this.txtName.setText(null);
       this.txtAdress.setText(null);
       btnSave.setVisible(false);
       btnAdd.setVisible(true);
   }


   public void addBranchHandler(ActionEvent event) throws SQLException {
       if (txtName.getText() == null || txtName.getText().isEmpty() || txtAdress.getText() == null || txtAdress.getText().isEmpty()) {
           MessageBox.getBox("Thông báo", "Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING).show();
       } else {
           Branch branch = new Branch(
                   txtName.getText(),
                   txtAdress.getText()
           );
           try {
               if (p.addBranch(branch)) {
                   MessageBox.getBox("Thông báo", "Thêm chi nhánh mới thành công", Alert.AlertType.INFORMATION).show();
                   loadTableData(null);
                   loadInterface();
               }
           } catch (SQLException ex) {
               MessageBox.getBox("Thông báo", "Thêm chi nhánh mới thất bại", Alert.AlertType.ERROR).show();
               Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
   }
   public void CancelBranchHandler(ActionEvent event) throws SQLException {
       loadTableData(null);
       loadInterface();
   }
  
}



