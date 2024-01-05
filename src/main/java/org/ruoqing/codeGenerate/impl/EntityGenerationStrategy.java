package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.EntityConfig;
import org.ruoqing.config.PackageConfig;

import java.io.PrintWriter;

public class EntityGenerationStrategy implements CodeGenerationStrategy {

    private final PackageConfig packageConfig;

    private final EntityConfig entityConfig;

    public EntityGenerationStrategy(PackageConfig packageConfig, EntityConfig entityConfig) {
        this.packageConfig = packageConfig;
        this.entityConfig = entityConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        writer.println("package " + packageConfig.getParentPackage() + ";\n");

        if (entityConfig.isLombok()) {
            writer.println("import lombok.Data;");
            writer.println("import lombok.NoArgsConstructor;");
            writer.println("import lombok.AllArgsConstructor;\n");
            writer.println("@Data");
            writer.println("@NoArgsConstructor");
            writer.println("@AllArgsConstructor");
        }
        writer.println("public class " + className + " {");
    }

    @Override
    public void generateConstructor(PrintWriter writer, String... args) {
        String className = args[0];
        String arg1 = args[1];
        String arg2 = args[2];
        String[] split = arg2.split(",");
        writer.println("    public " + className + "() {");
        writer.println("    }\n");

        writer.println("    public " + className + "(" + arg1 + ") {");
        for (String field : split) {
            writer.println("        this." + field + " = " + field + ";");
        }
        writer.println("    }\n");
    }
}
