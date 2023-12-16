package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.SwingConfig;

import java.io.PrintWriter;

public class ManageGenerationStrategy implements CodeGenerationStrategy {

    private PackageConfig packageConfig;

    private SwingConfig swingConfig;

    private String jMenuBarAttr;
    private String jMenuAttr;
    private String jTableAttr;
    private String tableModel;
    private String jMenuItemAdd;
    private String jMenuItemDelete;
    private String jMenuItemUpdate;
    private String jMenuItemView;
    private String jMenuItemSelectReturn;

    public ManageGenerationStrategy(PackageConfig packageConfig, SwingConfig swingConfig) {
        this.packageConfig = packageConfig;
        this.swingConfig = swingConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        writer.println("package " + packageConfig.getParentPackage() + ";");
        writer.println("\nimport javax.swing.*;");
        writer.println("import javax.swing.table.DefaultTableModel;");
        writer.println("import java.awt.*;");
        writer.println("\npublic class " + className + "Manage extends JFrame {");
        jMenuBarAttr = "jMenuBar" + className;
        jMenuAttr = "jMenu" + className;
        jTableAttr = "jTable" + className;
        tableModel = "tableModel";
        jMenuItemAdd = "jMenuItemAdd" + className;
        jMenuItemDelete = "jMenuItemDelete" + className;
        jMenuItemUpdate = "jMenuItemUpdate" + className;
        jMenuItemView = "jMenuItemSelect" + className;
        jMenuItemSelectReturn = "jMenuItemSelectReturn";
        writer.println("    JMenuBar " + jMenuBarAttr + ";");
        writer.println("    JMenu " + jMenuAttr + ";");
        writer.println("    JMenuItem " + jMenuItemAdd + ", " + jMenuItemDelete + ", " + jMenuItemUpdate + ", " + jMenuItemView + ", " + jMenuItemSelectReturn + ";");
        writer.println("    JTable " + jTableAttr + ";");
        writer.println("    DefaultTableModel " + tableModel + ";");
    }

    @Override
    public void generateConstructor(PrintWriter writer, String className) {
        var title = this.swingConfig.getTitle();
        var add = ".add(";
        var end = ");";
        writer.println("    public " + className + "Manage() {");
        writer.println("        setTitle(\"" + title + "管理系统" + "\");");
        writer.println("        setBounds(100, 100, 500, 500);");
        writer.println("        " + jMenuBarAttr + " = new JMenuBar();");
        writer.println("        " + jMenuAttr + " = new JMenu(\"" + title + "\");");
        writer.println("        " + jMenuItemAdd + " = new JMenuItem(\"添加" + title + "信息\");");
        writer.println("        " + jMenuItemDelete + " = new JMenuItem(\"删除" + title + "信息\");");
        writer.println("        " + jMenuItemUpdate + " = new JMenuItem(\"修改" + title + "信息\");");
        writer.println("        " + jMenuItemView + " = new JMenuItem(\"查询所有" + title + "信息\");");
        writer.println("        " + jMenuItemSelectReturn + " = new JMenuItem(\"返回\");");
        writer.println("        " + jMenuAttr + add + jMenuItemAdd + end);
        writer.println("        " + jMenuAttr + add + jMenuItemDelete + end);
        writer.println("        " + jMenuAttr + add + jMenuItemUpdate + end);
        writer.println("        " + jMenuAttr + add + jMenuItemView + end);
        writer.println("        " + jMenuAttr + add + jMenuItemSelectReturn + end);
        writer.println("        setJMenuBar(" + jMenuBarAttr + ");");
        writer.println("        setVisible(true);");
        writer.println("    }");
    }



}
