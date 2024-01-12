package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.EntityConfig;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.util.CodeUtil;

import java.io.PrintWriter;
import java.util.Arrays;

public class EntityGenerationStrategy implements CodeGenerationStrategy {

    private final PackageConfig packageConfig;

    private final EntityConfig entityConfig;

    public EntityGenerationStrategy(PackageConfig packageConfig, EntityConfig entityConfig) {
        this.packageConfig = packageConfig;
        this.entityConfig = entityConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        CodeUtil.definePackagePath(writer, packageConfig.getParentPackage());
//        writer.println("import java.math.BigDecimal;");
        if (entityConfig.isLombok()) {
            var imports = Arrays.asList("lombok.Data", "lombok.NoArgsConstructor", "lombok.AllArgsConstructor");
            for (String s : imports) {
                CodeUtil.defineImportPath(writer, s);
            }

            var annotations = Arrays.asList("Data", "NoArgsConstructor", "AllArgsConstructor");
            for (String annotation : annotations) {
                CodeUtil.defineAnnotation(writer, annotation);
            }
        }
        CodeUtil.defineClassPrefix(writer, className);
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
