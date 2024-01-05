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
        suffix(writer, OperateEnum.ADD.getCode());

        del(writer, con, tableName);

        prefix(writer, tableName, className, OperateEnum.UPDATE.getCode());
        updateSql(writer, con, tableName);
        suffix(writer, OperateEnum.UPDATE.getCode());

        prefix(writer, tableName, className, OperateEnum.QUERY.getCode());
        querySql(writer, con, tableName);
        suffix(writer, OperateEnum.QUERY.getCode());

    }

    void prefix(PrintWriter writer, String tableName, String className, int code) {
        if (OperateEnum.QUERY.getCode() == code) {
            writer.println("    public List<" + className + "> " + OperateEnum.valueOf(code).getDesc() + ") {");
            writer.println("        List<" + className + "> list = new ArrayList<>();");
        } else {
            writer.println("    public void " + OperateEnum.valueOf(code).getDesc() + className + " " + tableName + ") {");
        }
        writer.println("        try (Connection conn = JdbcUtil.getConnection()) {");
    }

    void suffix(PrintWriter writer, int code) {
        if (OperateEnum.QUERY.getCode() != code) {
            writer.println("            statement.executeUpdate();");
        }
        writer.println("        } catch (SQLException e) {");
        writer.println("            throw new RuntimeException(e);");
        writer.println("        }");
        if (OperateEnum.QUERY.getCode() == code) {
            writer.println("        return list;");
        }
        writer.println("    }\n");
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
        suffix(writer, OperateEnum.DEL.getCode());
    }

    protected abstract void insertSql(PrintWriter writer, Connection con, String tableName) throws SQLException;

    protected abstract void updateSql(PrintWriter writer, Connection con, String tableName) throws SQLException;

    protected abstract void querySql(PrintWriter writer, Connection con, String tableName) throws SQLException;

}
