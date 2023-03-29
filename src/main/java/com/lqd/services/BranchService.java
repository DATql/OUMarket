/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lqd.services;

import com.lqd.pojo.Branch;
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
public class BranchService {

    public boolean addBranch(Branch b) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            PreparedStatement stm1 = conn.prepareStatement("Insert into branch(id,name,adress,phonenumber)Values(?,?,?,?)");
            stm1.setString(1, b.getId());
            stm1.setString(2, b.getName());
            stm1.setString(3, b.getAdress());
            stm1.setString(4, b.getPhoneNumber());
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

    public List<Branch> getBranches(String kw) throws SQLException {
        List<Branch> results = new ArrayList<>();
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from branch";
            if (kw != null && !kw.isEmpty()) {
                sql += "where name like concat('%',?,'%')";
            }

            PreparedStatement stm = conn.prepareCall(sql);

            if (kw != null && !kw.isEmpty()) {
                stm.setString(1, kw);
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Branch p = new Branch(rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("adress"),
                        rs.getString("phonenumber"));
                results.add(p);
            }
        }

        return results;
    }

    public Branch getBranch(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "Select * from branch where name=?";

            PreparedStatement stm = conn.prepareCall(sql);

            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
            Branch b = new Branch(rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("adress"),
                    rs.getString("origin"));
            return b;
        }
    }

    public boolean updateBranch(Branch p) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            conn.setAutoCommit(false);
            String sql = "Update branch set name=?,adress=?,phonenumber=? where id=?  ";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getName());
            stm.setString(2, p.getAdress());
            stm.setString(3, p.getPhoneNumber());
            stm.setString(4, p.getId());
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

    public boolean deleteBranch(String id) throws SQLException {
        try (Connection conn = jdbcService.getConn()) {
            String sql = "DELETE FROM branch WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            return stm.executeUpdate() > 0;
        }
    }
}
