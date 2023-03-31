/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.services;

import com.lqd.pojo.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import com.lqd.utils.StringStorage;

/**
 *
 * @author Gol
 */
public class ProductService {

    public boolean addProduct(Product p) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            PreparedStatement stm1 = conn.prepareStatement("Insert into product(id,name,unit,price,quantity,origin,categoryID)Values(?,?,?,?,?,?,?)");
            stm1.setString(1, p.getId());
            stm1.setString(2, p.getName());
            stm1.setString(3, p.getUnit());
            stm1.setFloat(4, p.getPrice());
            stm1.setInt(5, p.getQuantity());
            stm1.setString(6, p.getOrigin());
            stm1.setInt(7, p.getCategoryID());
            stm1.executeUpdate();
            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }

    }

    public List<Product> getProducts(String kw) throws SQLException {
        List<Product> results = new ArrayList<>();
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from product";
            if (kw != null && !kw.isEmpty()) {
                sql += "where name like concat('%',?,'%')";
            }

            PreparedStatement stm = conn.prepareCall(sql);

            if (kw != null && !kw.isEmpty()) {
                stm.setString(1, kw);
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Product p = new Product(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getFloat("price"),
                        rs.getInt("quantity"), rs.getString("origin"),
                        rs.getInt("categoryID"));
                results.add(p);
            }
        }

        return results;
    }

    public Product getProduct(StringStorage name) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {

            String sql = "Select * from product where name=?";

            PreparedStatement stm = conn.prepareCall(sql);

            stm.setString(1, name.getStr());

            ResultSet rs = stm.executeQuery();
            Product p = new Product(rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("unit"),
                    rs.getFloat("price"),
                    rs.getInt("quantity"), rs.getString("origin"),
                    rs.getInt("categoryID"));

            return p;
        }
    }

    public boolean updateProduct(Product p) throws SQLException {
                    System.out.println("đã chạy vào hàm");

        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            String sql = "Update product set name=?,unit=?,price=?,quantity=?,origin=?,categoryID=? where id=?  ";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getName());
            stm.setString(2, p.getUnit());
            stm.setFloat(3, p.getPrice());
            stm.setInt(4, p.getQuantity());
            stm.setString(5, p.getOrigin());
            stm.setInt(6, p.getCategoryID());
            stm.setString(7, p.getId());
            stm.executeUpdate();
            System.out.println(stm);
            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }

    public boolean deleteProduct(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "DELETE FROM product WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            return stm.executeUpdate() > 0;
        }
    }
}
