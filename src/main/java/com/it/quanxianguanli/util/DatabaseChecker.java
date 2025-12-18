package com.it.quanxianguanli.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseChecker {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/quanxianguanli?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";
        
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            
            // Check sys_role table structure
            System.out.println("Checking sys_role table structure:");
            ResultSet rs1 = stmt.executeQuery("DESCRIBE sys_role");
            while (rs1.next()) {
                System.out.println(rs1.getString(1) + " - " + rs1.getString(2) + " - " + rs1.getString(3));
            }
            
            System.out.println("\nChecking sys_module table structure:");
            // Check sys_module table structure
            ResultSet rs2 = stmt.executeQuery("DESCRIBE sys_module");
            while (rs2.next()) {
                System.out.println(rs2.getString(1) + " - " + rs2.getString(2) + " - " + rs2.getString(3));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}