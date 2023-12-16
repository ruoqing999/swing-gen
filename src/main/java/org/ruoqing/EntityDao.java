package org.ruoqing;

import org.ruoqing.util.JdbcUtil;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class EntityDao {

    final void gen(PrintWriter writer, String tableName, String className) {

    }

    void addPrefix(PrintWriter writer, String tableName, String className) {
        writer.println("    public void add" + className + "(" + className + " " + tableName + ")");
        try (Connection conn = JdbcUtil.getConnection()) {
            String sql = "INSERT INTO pet (name, gender, age, cid) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            //todo
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("" + e.getMessage());
        }

        writer.println("        try (Connection conn = JdbcUtil.getConnection()) {");
    }

    void addSuffix() {

    }

    abstract void addCondiment();

}
