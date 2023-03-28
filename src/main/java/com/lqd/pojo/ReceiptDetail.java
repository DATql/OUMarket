/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.pojo;

/**
 *
 * @author Gol
 */
public class ReceiptDetail {
    private int id;
    private int quantity;
    private int productID;
    private int receiptID;

    public ReceiptDetail(int id, int quantity, int productID, int receiptID) {
        this.id = id;
        this.quantity = quantity;
        this.productID = productID;
        this.receiptID = receiptID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }
    
}
