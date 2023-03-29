/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.services;

import com.lqd.pojo.Category;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class CategoryService {
    public List<Category> getCategories() throws SQLException {
        List<Category> cates = new ArrayList<>();
        try (Connection conn = jdbcService.getConn()) {
            Statement stm = conn.createStatement();

            ResultSet rs = stm.executeQuery("SELECT * FROM category");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("categoryname");
                cates.add(new Category(id, name));
            }
        }
        
        return cates;
    }
}
