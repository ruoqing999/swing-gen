package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.SwingConfig;
import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.CodeUtil;

import java.io.PrintWriter;

public class MainGenerationStrategy implements CodeGenerationStrategy {

    private final SwingConfig swingConfig;

    private final PackageConfig packageConfig;

    public MainGenerationStrategy(SwingConfig swingConfig, PackageConfig packageConfig) {
        this.swingConfig = swingConfig;
        this.packageConfig = packageConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        CodeUtil.definePackagePath(writer, packageConfig.getParentPackage());
        CodeUtil.defineClassPrefix(writer, className);
    }

    @Override
    public void genMethod(PrintWriter writer, String... args) {
        String className = args[0];
        CodeUtil.defineMethodArgPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PUBLIC, GlobalConstants.STATIC,
                GlobalConstants.VOID, "main", String[].class.getSimpleName(), "arg");
        if (swingConfig.isNeedLogin()) {
            CodeUtil.newObj(writer, GlobalConstants.SECOND_TAB, "Login");
        } else {
            CodeUtil.newObj(writer, GlobalConstants.SECOND_TAB, className);
        }
        writer.println(GlobalConstants.FIRST_TAB + GlobalConstants.RIGHT);
    }
}
