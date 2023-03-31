/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.services;

import com.lqd.pojo.User;
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
public class UserService {

    public boolean addEmployee(User e) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            PreparedStatement stm1 = conn.prepareStatement("Insert into employee(id,name,dateofbirth,sex,phonenumber,role,username,password,branchID)Values(?,?,?,?,?,?,?,?,?)");
            stm1.setString(1, e.getId());
            stm1.setString(2, e.getName());
            stm1.setDate(3, e.getDateOfBirth());
            stm1.setString(4, e.getSex());
            stm1.setString(5, e.getPhoneNumber());
            stm1.setString(6, e.getRole());
            stm1.setString(7, e.getUsername());
            stm1.setString(8, e.getPassword());
            stm1.setString(9, e.getBranchID());

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

    public List<User> getEmployees(String kw) throws SQLException {
        List<User> results = new ArrayList<>();
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from employee";
            if (kw != null && !kw.isEmpty()) {
                sql += "where name like concat('%',?,'%')";
            }

            PreparedStatement stm = conn.prepareCall(sql);

            if (kw != null && !kw.isEmpty()) {
                stm.setString(1, kw);
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                User c = new User(rs.getString("id"),
                        rs.getString("name"),
                        rs.getDate("dateofbirth"),
                        rs.getString("sex"),
                        rs.getString("adress"),
                        rs.getString("phonenumber"),
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("branchID"));
                results.add(c);
            }
        }

        return results;
    }

    public User getEmployee(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from employee where name=?";

            PreparedStatement stm = conn.prepareCall(sql);

            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
            User c = new User(rs.getString("id"),
                        rs.getString("name"),
                        rs.getDate("dateofbirth"),
                        rs.getString("sex"),
                        rs.getString("adress"),
                        rs.getString("phonenumber"),
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("branchID"));
            return c;
        }
    }

    public boolean updateEmployee(User c) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            String sql = "Update Employee set name=?,dateofbirth=?,sex=?,phonenumber=?,email=? where id=?  ";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, c.getName());
            stm.setDate(2, c.getDateOfBirth());
            stm.setString(3, c.getSex());
            stm.setString(4, c.getPhoneNumber());
            stm.setString(5, c.getEmail());
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

    public boolean deleteEmployee(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "DELETE FROM Employee WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            return stm.executeUpdate() > 0;
        }
    }
}
