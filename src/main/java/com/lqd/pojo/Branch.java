/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.pojo;

import java.util.UUID;

/**
 *
 * @author Gol
 */
public class Branch {
    private String id;
    private String name;
    private String adress;
    private String phoneNumber;
     {
        id = UUID.randomUUID().toString();
    }
    public Branch(String id, String name, String adress, String phoneNumber) {
        this.id=id;
        this.name = name;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    
}
