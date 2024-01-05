package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.SwingConfig;

import java.io.PrintWriter;

public class ManageGenerationStrategy implements CodeGenerationStrategy {

    private final PackageConfig packageConfig;

    private final SwingConfig swingConfig;

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
        writer.println("    DefaultTableModel " + tableModel + ";\n");
    }

    @Override
    public void generateConstructor(PrintWriter writer, String... args) {
        String className = args[0];
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
        writer.println("        " + jMenuBarAttr + add + jMenuAttr + end);
        writer.println("        setJMenuBar(" + jMenuBarAttr + ");");
        writer.println("        setVisible(true);");
//        writer.println("        String[] columnNames = { " + add + " };");
        writer.println("        setListener();");
        writer.println("    }\n");
    }

    @Override
    public void generateAddListener(PrintWriter writer, String className) {
        var addListener = ".addActionListener(e -> {";
        var addListenerSuffix = "});";
        var Dao = className + "Dao";
        var dao = className.substring(0, 1).toLowerCase() + className.substring(1) + "Dao ";
        writer.println("    public void setListener() {\n");
        writer.println("        " + Dao + " " + dao + "= new " + Dao + "();\n");

        writer.println("        " + jMenuItemAdd + addListener);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemDelete + addListener);
        writer.println("            del(stuDao);");
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemUpdate + addListener);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemView + addListener);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemSelectReturn + addListener);
        writer.println("            tableModel.setRowCount(0);");
//        writer.println("            loadList();");
        writer.println("        " + addListenerSuffix + "\n");
        writer.println("    }\n");
    }

    @Override
    public void genCrud(PrintWriter writer, String className) {

        add(writer, className);
        del(writer, className);

        loadList();

    }

    private void add(PrintWriter writer, String className) {

    }

    private void del(PrintWriter writer, String className) {
        var Dao = className + "Dao";
        var dao = className.substring(0, 1).toLowerCase() + className.substring(1) + "Dao";
        writer.println("    private void del(" + Dao + " " + dao + ") {");
        writer.println("        int selectedRow = " + jTableAttr + ".getSelectedRow();");
        writer.println("        if (selectedRow == -1) {");
        writer.println("            JOptionPane.showMessageDialog(this, \"请选中要操作的数据行\");");
        writer.println("            return;");
        writer.println("        }");
        writer.println("        String code = (String) " + tableModel + ".getValueAt(selectedRow, 0);");
        writer.println("        " + dao + ".del(Integer.valueOf(code));");
        writer.println("    }");
    }

    private void loadList() {

    }
}
