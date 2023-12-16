package org.ruoqing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {

    public static final String URL = "jdbc:mysql://192.168.56.10:3306/flower?useUnicode=true&characterEncoding=utf8";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static String getJavaType(String dbType) {
        return switch (dbType) {
            case "INT" -> "Integer";
            case "VARCHAR" -> "String";
            case "DOUBLE" -> "Double";
            // 添加其他数据库类型到Java类型的映射
            default -> "Object";
        };
    }

    public static String toCamelCase(String columnName) {
        if (!columnName.contains("_")) {
            return columnName;
        }

        String[] split = columnName.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                sb.append(split[i]);
            } else {
                sb.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1));
            }
        }
        return sb.toString();
    }

}
