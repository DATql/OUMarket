/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.services;

import com.lqd.pojo.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Gol
 */
public class ProductService {
    public void addProduct(Product p) throws SQLException{
        try(Connection conn = jdbcService.getConn()){
            conn.setAutoCommit(false);
            PreparedStatement stm1=conn.prepareStatement("Insert into product(id,name,unit,price,quantity,origin,categoryID)Values(?,?,?,?,?,?,?)");
            stm1.setString(1,p.getId());
            stm1.setString(2, p.getName());
            stm1.setString(3, p.getUnit());
            stm1.setFloat(4,p.getPrice());
            stm1.setInt(5,p.getQuantity());
            stm1.setString(6, p.getOrigin());
            stm1.setInt(7,p.getCategoryID());
            stm1.executeUpdate();
            conn.commit();
        }   
    }
    
}
