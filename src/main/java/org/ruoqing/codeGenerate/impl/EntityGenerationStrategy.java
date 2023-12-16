package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.EntityConfig;
import org.ruoqing.config.PackageConfig;

import java.io.PrintWriter;

public class EntityGenerationStrategy implements CodeGenerationStrategy {

    private PackageConfig packageConfig;

    private EntityConfig entityConfig;

    public EntityGenerationStrategy(PackageConfig packageConfig, EntityConfig entityConfig) {
        this.packageConfig = packageConfig;
        this.entityConfig = entityConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        writer.println("package " + packageConfig.getParentPackage() + ";\n");

        if (entityConfig.isLombok()) {
            writer.println("import lombok.Data;\n");
            writer.println("@Data");
        }
        writer.println("public class " + className + " {");
    }


    @Override
    public void generateConstructor(PrintWriter writer, String className) {

    }


}
