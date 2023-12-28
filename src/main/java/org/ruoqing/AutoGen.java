package org.ruoqing;

import org.ruoqing.codeGenerate.impl.DaoGenerationStrategy;
import org.ruoqing.codeGenerate.impl.EntityGenerationStrategy;
import org.ruoqing.codeGenerate.impl.ManageGenerationStrategy;
import org.ruoqing.config.JdbcConfig;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.EntityConfig;
import org.ruoqing.config.SwingConfig;
import org.ruoqing.util.JdbcUtil;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

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
        var className = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        var outputPath = packageConfig.getPath() + packageConfig.getParentPath();
        genEntity(outputPath, tableName, className);
        genManage(outputPath, className);
        genDao(outputPath, tableName, className);
    }

    private void genEntity(String outputPath, String tableName, String className) {
        try (Connection connection = JdbcUtil.getConnection();
             PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + ".java"))) {
            ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, null);

            var entityGenerationStrategy = new EntityGenerationStrategy(packageConfig, entityConfig);
            entityGenerationStrategy.generatePackageAndImport(writer, className);


            // 输出字段定义
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String columnType = resultSet.getString("TYPE_NAME");
                writer.println("    private " + JdbcUtil.getJavaType(columnType) + " " + JdbcUtil.toCamelCase(columnName) + ";");
            }

            resultSet.beforeFirst(); // 将结果集指针重置到起始位置
            if (!entityConfig.isLombok()) {
                entityGenerationStrategy.generateGetterSetter(resultSet, writer);
            }
            entityGenerationStrategy.generateClassEnd(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void genManage(String outputPath, String className) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath + className + "Manage.java"))) {
            var manageGenerationStrategy = new ManageGenerationStrategy(packageConfig, swingConfig);
            manageGenerationStrategy.generatePackageAndImport(writer, className);
            manageGenerationStrategy.generateConstructor(writer, className);

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

    private void genMain() {

    }

}
