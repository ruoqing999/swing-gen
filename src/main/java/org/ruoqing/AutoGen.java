package org.ruoqing;

import org.ruoqing.codeGenerate.impl.*;
import org.ruoqing.config.JdbcConfig;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.EntityConfig;
import org.ruoqing.config.SwingConfig;
import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.JdbcUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.StringJoiner;

public class AutoGen {

    private EntityConfig entityConfig;

    private PackageConfig packageConfig;

    private JdbcConfig jdbcConfig;

    private SwingConfig swingConfig;

    public AutoGen strategy(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
        return this;
    }

    public AutoGen packageInfo(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
        return this;
    }

    public AutoGen jdbcInfo(JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
        return this;
    }

    public AutoGen swingInfo(SwingConfig swingConfig) {
        this.swingConfig = swingConfig;
        return this;
    }

    public void gen() {
        var tableName = entityConfig.getTableName();
        var camelName = JdbcUtil.toCamelCase(tableName);
        var className = camelName.substring(0, 1).toUpperCase() + camelName.substring(1);
        var outputPath = packageConfig.getPath() + packageConfig.getParentPath();
        genEntity(outputPath, tableName, className);
        genManage(outputPath, tableName, className);
        genDao(outputPath, tableName, className);
        if (swingConfig.isNeedLogin()) {
            genLogin(outputPath, className);
        }
        genMain(outputPath, className + "Manage");
    }

    private void genEntity(String outputPath, String tableName, String className) {
        try (Connection connection = JdbcUtil.getConnection();
             PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + ".java"))) {
            ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, null);

            StringJoiner arg1 = new StringJoiner(", ");
            StringJoiner arg2 = new StringJoiner(",");
            StringJoiner fieldDefinition = new StringJoiner("\n");
            while (resultSet.next()) {
                String columnName = resultSet.getString(GlobalConstants.COLUMN_NAME);
                String columnType = resultSet.getString(GlobalConstants.TYPE_NAME);
                arg1.add(JdbcUtil.getJavaType(columnType) + GlobalConstants.SPACE + JdbcUtil.toCamelCase(columnName));
                arg2.add(JdbcUtil.toCamelCase(columnName));
                fieldDefinition.add("    " + GlobalConstants.PRIVATE + GlobalConstants.SPACE + JdbcUtil.getJavaType(columnType) +
                        GlobalConstants.SPACE + JdbcUtil.toCamelCase(columnName) + GlobalConstants.SEMICOLON);
            }

            var entityGenerationStrategy = new EntityGenerationStrategy(packageConfig, entityConfig);
            entityGenerationStrategy.generatePackageAndImport(writer, className);
            writer.println(fieldDefinition);
            resultSet.beforeFirst(); // 将结果集指针重置到起始位置
            if (!entityConfig.isLombok()) {
                entityGenerationStrategy.generateConstructor(writer, className, arg1.toString(), arg2.toString());
                entityGenerationStrategy.generateGetterSetter(resultSet, writer);
            }
            entityGenerationStrategy.generateClassEnd(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void genManage(String outputPath, String tableName, String className) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + "Manage.java"));
             Connection connection = JdbcUtil.getConnection()) {
            ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, null);

            var stringJoiner = new StringJoiner(",");
            var fieldJoiner = new StringJoiner(", ");
            while (resultSet.next()) {
                var columnName = resultSet.getString(GlobalConstants.COLUMN_NAME);
                var columnType = resultSet.getString(GlobalConstants.TYPE_NAME);
                String camelCase = JdbcUtil.toCamelCase(columnName);
                stringJoiner.add(camelCase + "-" + columnType);
                camelCase = "\"" + camelCase + "\"";
                fieldJoiner.add(camelCase);
            }
            var manageGenerationStrategy = new ManageGenerationStrategy(packageConfig, swingConfig);
            manageGenerationStrategy.generatePackageAndImport(writer, className);
            manageGenerationStrategy.generateConstructor(writer, className, fieldJoiner.toString());
            manageGenerationStrategy.generateAddListener(writer, className);
            manageGenerationStrategy.genMethod(writer, className, stringJoiner.toString());
            manageGenerationStrategy.generateClassEnd(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void genDao(String outputPath, String tableName, String className) {
        try (Connection con = JdbcUtil.getConnection();
             PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + "Dao.java"))) {
            var daoGenerationStrategy = new DaoGenerationStrategy(packageConfig);
            daoGenerationStrategy.generatePackageAndImport(writer, className);
            daoGenerationStrategy.gen(writer, tableName, className, con);
            daoGenerationStrategy.generateClassEnd(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void genMain(String outputPath, String manageClassName) {
        var className = "Main";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + ".java"))) {
            var mainGenerationStrategy = new MainGenerationStrategy(swingConfig, packageConfig);
            mainGenerationStrategy.generatePackageAndImport(writer, className);
            mainGenerationStrategy.genMethod(writer, manageClassName);
            mainGenerationStrategy.generateClassEnd(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void genLogin(String outputPath, String manageClassName) {
        var className = "Login";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + ".java"))) {
            var loginGenerationStrategy = new LoginGenerationStrategy(packageConfig, swingConfig);
            loginGenerationStrategy.generatePackageAndImport(writer, className);
            loginGenerationStrategy.genVariable(writer);
            loginGenerationStrategy.generateConstructor(writer, className);
            loginGenerationStrategy.genMethod(writer, className, manageClassName);
            loginGenerationStrategy.generateClassEnd(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
