package org.ruoqing.codeGenerate;

import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.CodeUtil;
import org.ruoqing.util.JdbcUtil;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface CodeGenerationStrategy {

    void generatePackageAndImport(PrintWriter writer, String className);

    default void genVariable(PrintWriter writer) {
    }

    default void generateConstructor(PrintWriter writer, String... args) {
    }

    default void generateAddListener(PrintWriter writer, String className) {
    }

    default void genMethod(PrintWriter writer, String... args) {
    }

    default void generateGetterSetter(ResultSet resultSet, PrintWriter writer) throws SQLException {
        while (resultSet.next()) {
            var columnName = resultSet.getString(GlobalConstants.COLUMN_NAME);
            columnName = JdbcUtil.toCamelCase(columnName);
            var capitalizedColumnName = JdbcUtil.toCapitalized(columnName);
            var columnType = resultSet.getString(GlobalConstants.TYPE_NAME);

            // Getter
            writer.println("    public " + JdbcUtil.getJavaType(columnType) + " get" + capitalizedColumnName + "() {");
            writer.println("        return " + columnName + ";");
            writer.println("    }");

            // Setter
            writer.println("    public void set" + capitalizedColumnName + "(" + JdbcUtil.getJavaType(columnType) + " " + columnName + ") {");
            writer.println("        this." + columnName + " = " + columnName + ";");
            writer.println("    }");
        }
    }

    default void generateClassEnd(PrintWriter writer) {
        writer.println(GlobalConstants.END);
    }

}
