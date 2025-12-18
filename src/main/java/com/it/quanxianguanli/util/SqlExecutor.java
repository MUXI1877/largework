package com.it.quanxianguanli.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlExecutor {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/quanxianguanli?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";
        String sqlFile = "D:\\ideal专业版\\code\\java-homework\\temp_data.sql";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {

            conn.setAutoCommit(false);
            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // 跳过注释和空行
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                sqlBuilder.append(line);
                // 执行完整的SQL语句
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString();
                    stmt.executeUpdate(sql);
                    sqlBuilder.setLength(0);
                }
            }

            conn.commit();
            System.out.println("SQL语句执行成功！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}