package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;

import java.io.PrintWriter;

public class DaoGenerationStrategy implements CodeGenerationStrategy {

    private PackageConfig packageConfig;

    public DaoGenerationStrategy(PackageConfig packageConfig) {
        this.packageConfig = packageConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        writer.println("package " + packageConfig.getParentPackage() + ";\n");
        writer.println("import java.sql.*;");
        writer.println("import java.util.ArrayList;");
        writer.println("import java.util.List;\n");
        writer.println("public class " + className + "Dao {");
    }

    @Override
    public void generateConstructor(PrintWriter writer, String className) {

    }


}
