/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.pojo;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Gol
 */
public class Receipt {
    private String id;
    private Date createdDate;
    private float total;
    private String staffID;
    private String customerID;
     {
        id = UUID.randomUUID().toString();
    }
    public Receipt(Date createdDate, float total, String staffID, String customerID) {

        this.createdDate = createdDate;
        this.total = total;
        this.staffID = staffID;
        this.customerID = customerID;
    }
       public Receipt(String id, Date createdDate, float total, String staffID, String customerID) {
        this.id = id;
        this.createdDate = createdDate;
        this.total = total;
        this.staffID = staffID;
        this.customerID = customerID;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }


    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    
}
