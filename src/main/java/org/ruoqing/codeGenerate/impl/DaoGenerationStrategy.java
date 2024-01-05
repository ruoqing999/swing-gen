package org.ruoqing.codeGenerate.impl;

import org.ruoqing.enums.DbTypeEnum;
import org.ruoqing.EntityDao;
import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.util.JdbcUtil;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.StringJoiner;

public class DaoGenerationStrategy extends EntityDao implements CodeGenerationStrategy {

    private final PackageConfig packageConfig;

    public DaoGenerationStrategy(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        writer.println("package " + packageConfig.getParentPackage() + ";\n");
        writer.println("import org.ruoqing.util.JdbcUtil;\n");
        writer.println("import java.sql.*;");
        writer.println("import java.util.ArrayList;");
        writer.println("import java.util.List;\n");
        writer.println("public class " + className + "Dao {\n");
    }


    protected void insertSql(PrintWriter writer, Connection con, String tableName) throws SQLException {
        ResultSet resultSet = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null);
        StringJoiner columnStr = new StringJoiner(", ");
        StringJoiner paramStr = new StringJoiner(", ");
        StringJoiner setStr = new StringJoiner("\n");
        int count = 0;
        while (resultSet.next()) {

            boolean isAutoIncrement = Objects.equals(resultSet.getString("IS_AUTOINCREMENT"), "YES");
            if (isAutoIncrement) {
                continue;
            }

            count++;
            String columnName = resultSet.getString("COLUMN_NAME");
            String capitalizedColumnName = JdbcUtil.toCapitalized(JdbcUtil.toCamelCase(columnName));
            String columnType = resultSet.getString("TYPE_NAME");
            columnStr.add(columnName);
            paramStr.add("?");
            setStr.add("            statement." + DbTypeEnum.valueOf(columnType).getDbSetMethod() + count + ", " + tableName + ".get" + capitalizedColumnName + "());");
        }
        writer.println("            String sql = \" insert into " + tableName + " ( " + columnStr + " ) values ( " + paramStr + " )\";");
        writer.println("            PreparedStatement statement = conn.prepareStatement(sql);");
        writer.println(setStr);
    }

    @Override
    protected void updateSql(PrintWriter writer, Connection con, String tableName) throws SQLException {
        ResultSet resultSet = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null);
        StringJoiner columnStr = new StringJoiner("= ?, ");
        StringJoiner setStr = new StringJoiner("\n");
        String pKey = "";
        String pKeyType = "";
        String pKeyName = "";
        int count = 0;
        while (resultSet.next()) {
            boolean isAutoIncrement = Objects.equals(resultSet.getString("IS_AUTOINCREMENT"), "YES");
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("TYPE_NAME");
            String capitalizedColumnName = JdbcUtil.toCapitalized(JdbcUtil.toCamelCase(columnName));
            if (isAutoIncrement) {
                pKey = columnName;
                pKeyType = columnType;
                pKeyName = capitalizedColumnName;
                continue;
            }
            count++;
            columnStr.add(columnName);
            setStr.add("            statement." + DbTypeEnum.valueOf(columnType).getDbSetMethod() + count + ", " + tableName + ".get" + capitalizedColumnName + "());");
        }
        writer.println("            String sql = \" update " + tableName + " set " + columnStr + " = ? where " + pKey + " = ?\";");
        writer.println("            PreparedStatement statement = conn.prepareStatement(sql);");
        writer.println(setStr);
        writer.println("            statement." + DbTypeEnum.valueOf(pKeyType).getDbSetMethod() + ++count + ", " + tableName + ".get" + pKeyName + "());");
    }

    @Override
    protected void querySql(PrintWriter writer, Connection con, String tableName) throws SQLException {
        ResultSet resultSet = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null);
        StringJoiner getStr = new StringJoiner("\n");
        StringJoiner fields = new StringJoiner(", ");
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("TYPE_NAME");
            getStr.add("                " + JdbcUtil.getJavaType(columnType) + " " + columnName + " = resultSet." + DbTypeEnum.valueOf(columnType).getDbGetMethod() + "\"" + columnName + "\");");
            fields.add(columnName);
        }
        writer.println("            String sql = \"select * from " + tableName + "\";");
        writer.println("            PreparedStatement statement = conn.prepareStatement(sql);");
        writer.println("            ResultSet resultSet = statement.executeQuery();");
        writer.println("            while (resultSet.next()) {");
        writer.println(getStr);
        writer.println("                list.add(new " + JdbcUtil.toCapitalized(tableName) + "(" + fields + ")" + ");");
        writer.println("            }");
    }

}
