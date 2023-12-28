package org.ruoqing;


import org.ruoqing.enums.DbTypeEnum;
import org.ruoqing.enums.OperateEnum;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class EntityDao {

    protected void gen(PrintWriter writer, String tableName, String className, Connection con) throws SQLException {
        prefix(writer, tableName, className, OperateEnum.ADD.getCode());
        insertSql(writer, con, tableName);
        suffix(writer);

        del(writer, con, tableName);

        prefix(writer, tableName, className, OperateEnum.UPDATE.getCode());
        updateSql(writer, con, tableName);
        suffix(writer);


    }

    void prefix(PrintWriter writer, String tableName, String className, int code) {

        writer.println("    public void " + OperateEnum.valueOf(code).getDesc() + className + " " + tableName + ") {");
        writer.println("        try (Connection conn = JdbcUtil.getConnection()) {");
    }

    void suffix(PrintWriter writer) {
        writer.println("            statement.executeUpdate();");
        writer.println("        } catch (SQLException e) {");
        writer.println("            throw new RuntimeException(e);");
        writer.println("        }");
        writer.println("    }");
    }


    void del(PrintWriter writer, Connection con, String tableName) throws SQLException {
        ResultSet resultSet = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null);
        String primaryKeyName = null;
        String primaryKeyType = null;
        while (resultSet.next()) {
            boolean isAutoIncrement = Objects.equals(resultSet.getString("IS_AUTOINCREMENT"), "YES");
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("TYPE_NAME");
            if (isAutoIncrement) {
                primaryKeyName = columnName;
                primaryKeyType = columnType;
            }
        }
        writer.println("    public void del(" + DbTypeEnum.valueOf(primaryKeyType).getJavaType() + " " + primaryKeyName + ") {");
        writer.println("        try (Connection conn = JdbcUtil.getConnection()) {");
        writer.println("            String sql = \"delete from " + tableName + " where " + primaryKeyName + " = ?\";");
        writer.println("            PreparedStatement statement = conn.prepareStatement(sql);");
        writer.println("            statement.setInt(1, " + primaryKeyName + ");");
        suffix(writer);
    }

    protected abstract void insertSql(PrintWriter writer, Connection con, String tableName) throws SQLException;

    protected abstract void updateSql(PrintWriter writer, Connection con, String tableName) throws SQLException;

}
