/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.services;

import com.lqd.pojo.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gol
 */
public class CustomerService {
    public boolean addCustomer(Customer b) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            PreparedStatement stm1 = conn.prepareStatement("Insert into customer(id,name,dateofbirth,sex,phonenumber,email)Values(?,?,?,?,?,?)");
            stm1.setString(1, b.getId());
            stm1.setString(2, b.getName());
            stm1.setDate(3, b.getDateOfBirth());
            stm1.setString(4,b.getSex());
            stm1.setString(5, b.getPhoneNumber());
            stm1.setString(6,b.getEmail());
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

    public List<Customer> getCustomers(String kw) throws SQLException {
        List<Customer> results = new ArrayList<>();
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from customer";
            if (kw != null && !kw.isEmpty()) {
                sql += "where name like concat('%',?,'%')";
            }

            PreparedStatement stm = conn.prepareCall(sql);

            if (kw != null && !kw.isEmpty()) {
                stm.setString(1, kw);
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Customer c = new Customer(rs.getString("id"),
                        rs.getString("name"),
                        rs.getDate("dateofbirth"),
                        rs.getString("sex"),
                        rs.getString("phonenumber"),
                        rs.getString("email"));
                results.add(c);
            }
        }

        return results;
    }

    public Customer getCustomer(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from customer where name=?";

            PreparedStatement stm = conn.prepareCall(sql);

            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
              Customer c = new Customer(rs.getString("id"),
                        rs.getString("name"),
                        rs.getDate("dateofbirth"),
                        rs.getString("sex"),
                        rs.getString("phonenumber"),
                        rs.getString("email"));
            return c;
        }
    }

    public boolean updateCustomer(Customer c) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            String sql = "Update customer set name=?,dateofbirth=?,sex=?,phonenumber=?,email=? where id=?  ";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, c.getName());
            stm.setDate(2, c.getDateOfBirth());
            stm.setString(3,c.getSex());
            stm.setString(4, c.getPhoneNumber());
            stm.setString(5,c.getEmail());
                        stm.setString(6, c.getId());
    
            stm.executeUpdate();

            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }

    public boolean deleteCustomer(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "DELETE FROM customer WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            return stm.executeUpdate() > 0;
        }
    }
}
