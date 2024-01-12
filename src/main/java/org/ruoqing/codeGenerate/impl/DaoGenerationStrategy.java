package org.ruoqing.codeGenerate.impl;

import org.ruoqing.enums.DbTypeEnum;
import org.ruoqing.EntityDao;
import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.CodeUtil;
import org.ruoqing.util.JdbcUtil;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class DaoGenerationStrategy extends EntityDao implements CodeGenerationStrategy {

    private final PackageConfig packageConfig;

    public DaoGenerationStrategy(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        CodeUtil.definePackagePath(writer, packageConfig.getParentPackage());
        var imports = Arrays.asList("org.ruoqing.util.JdbcUtil", "java.sql.*", "java.util.ArrayList", "java.util.List");
        for (String anImport : imports) {
            CodeUtil.defineImportPath(writer, anImport);
        }
        CodeUtil.defineClassPrefix(writer, className + GlobalConstants.DAO);
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
            String columnName = resultSet.getString(GlobalConstants.COLUMN_NAME);
            String capitalizedColumnName = JdbcUtil.toCapitalized(JdbcUtil.toCamelCase(columnName));
            String columnType = resultSet.getString(GlobalConstants.TYPE_NAME);
            columnStr.add(columnName);
            paramStr.add("?");
            setStr.add("            statement." + DbTypeEnum.valueOf(columnType).getDbSetMethod() + count + ", " + JdbcUtil.toCamelCase(tableName) + ".get" + capitalizedColumnName + "());");
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
            String columnName = resultSet.getString(GlobalConstants.COLUMN_NAME);
            String columnType = resultSet.getString(GlobalConstants.TYPE_NAME);
            String capitalizedColumnName = JdbcUtil.toCapitalized(JdbcUtil.toCamelCase(columnName));
            if (isAutoIncrement) {
                pKey = columnName;
                pKeyType = columnType;
                pKeyName = capitalizedColumnName;
                continue;
            }
            count++;
            columnStr.add(columnName);
            setStr.add("            statement." + DbTypeEnum.valueOf(columnType).getDbSetMethod() + count + ", " + JdbcUtil.toCamelCase(tableName) + ".get" + capitalizedColumnName + "());");
        }
        writer.println("            String sql = \" update " + tableName + " set " + columnStr + " = ? where " + pKey + " = ?\";");
        writer.println("            PreparedStatement statement = conn.prepareStatement(sql);");
        writer.println(setStr);
        writer.println("            statement." + DbTypeEnum.valueOf(pKeyType).getDbSetMethod() + ++count + ", " + JdbcUtil.toCamelCase(tableName) + ".get" + pKeyName + "());");
    }

    @Override
    protected void querySql(PrintWriter writer, Connection con, String tableName) throws SQLException {
        ResultSet resultSet = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null);
        StringJoiner getStr = new StringJoiner("\n");
        StringJoiner fields = new StringJoiner(", ");
        while (resultSet.next()) {
            String columnName = resultSet.getString(GlobalConstants.COLUMN_NAME);
            String columnType = resultSet.getString(GlobalConstants.TYPE_NAME);
            String camelName = JdbcUtil.toCamelCase(columnName);
            getStr.add("                " + JdbcUtil.getJavaType(columnType) + " " + camelName + " = resultSet." + DbTypeEnum.valueOf(columnType).getDbGetMethod() + "\"" + columnName + "\");");
            fields.add(camelName);
        }
        writer.println("            String sql = \"select * from " + tableName + "\";");
        writer.println("            PreparedStatement statement = conn.prepareStatement(sql);");
        writer.println("            ResultSet resultSet = statement.executeQuery();");
        writer.println("            while (resultSet.next()) {");
        writer.println(getStr);
        var camelName = JdbcUtil.toCamelCase(tableName);
        camelName = camelName.substring(0, 1).toUpperCase() + camelName.substring(1);
        writer.println("                list.add(new " + camelName + "(" + fields + ")" + ");");
        writer.println("            }");
    }

}
