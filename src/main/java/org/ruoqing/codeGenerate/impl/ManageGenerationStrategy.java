package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.SwingConfig;
import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.CodeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.PrintWriter;
import java.util.Arrays;

public class ManageGenerationStrategy implements CodeGenerationStrategy {

    private final PackageConfig packageConfig;

    private final SwingConfig swingConfig;

    private final String jMenuBar = "jMenuBar";
    private final String jMenu = "jMenu";
    private final String jTable = "jTable";
    private final String tableModel = "tableModel";
    private final String jMenuItemAdd = "jMenuItemAdd";
    private final String jMenuItemDelete = "jMenuItemDelete";
    private final String jMenuItemUpdate = "jMenuItemUpdate";
    private final String jMenuItemView = "jMenuItemView";
    private final String jMenuItemSelectReturn = "jMenuItemSelectReturn";

    public ManageGenerationStrategy(PackageConfig packageConfig, SwingConfig swingConfig) {
        this.packageConfig = packageConfig;
        this.swingConfig = swingConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        CodeUtil.definePackagePath(writer, packageConfig.getParentPackage());
        var imports = Arrays.asList("javax.swing.*", "javax.swing.table.DefaultTableModel",
                "java.awt.*");
        for (String anImport : imports) {
            CodeUtil.defineImportPath(writer, anImport);
        }
        writer.println();
        CodeUtil.defineSubClassPrefix(writer, className + GlobalConstants.MANAGE, GlobalConstants.J_FRAME);
        CodeUtil.defineField(writer, JMenuBar.class, jMenuBar, className);
        CodeUtil.defineField(writer, JMenu.class, jMenu, className);
        CodeUtil.defineField(writer, JMenuItem.class, jMenuItemAdd + ", " + jMenuItemDelete + ", " +
                jMenuItemUpdate + ", " + jMenuItemView + ", " + jMenuItemSelectReturn, className);
        CodeUtil.defineField(writer, JTable.class, jTable, className);
        CodeUtil.defineField(writer, DefaultTableModel.class, tableModel, className);
    }

    @Override
    public void generateConstructor(PrintWriter writer, String... args) {
        String className = args[0];
        var title = this.swingConfig.getTitle();
        CodeUtil.defineConstructor(writer, className + GlobalConstants.MANAGE);
        writer.println("        setTitle(\"" + title + GlobalConstants.MANAGE_NAME + "\");");
        writer.println("        setBounds(100, 100, 500, 500);");
        newObject(writer, jMenuBar, JMenuBar.class, "");
        newObject(writer, jMenu, JMenu.class, "\"" + title + "\"");
        newObject(writer, jMenuItemAdd, JMenuItem.class, "\"添加" + title + "信息\"");
        newObject(writer, jMenuItemDelete, JMenuItem.class, "\"删除" + title + "信息\"");
        newObject(writer, jMenuItemUpdate, JMenuItem.class, "\"修改" + title + "信息\"");
        newObject(writer, jMenuItemView, JMenuItem.class, "\"查询所有" + title + "信息\"");
        newObject(writer, jMenuItemSelectReturn, JMenuItem.class, "\"返回\"");
        var menuItems = Arrays.asList(jMenuItemAdd, jMenuItemDelete, jMenuItemUpdate, jMenuItemView, jMenuItemSelectReturn);
        for (String menuItem : menuItems) {
            menuAdd(writer, jMenu, menuItem);
        }
        menuAdd(writer, jMenuBar, jMenu);
        writer.println("        setJMenuBar(" + jMenuBar + ");");
        writer.println("        setVisible(true);");
        writer.println("        setListener();");
//        writer.println("        String[] columnNames = { " + add + " };");
        writer.println(GlobalConstants.FIRST_LEVEL_END + GlobalConstants.NEXT_LINE);
    }

    private void newObject(PrintWriter writer, String attrName, Class<?> clazz, String val) {
        writer.println("        " + attrName + GlobalConstants.SPACE + GlobalConstants.EQUAL + GlobalConstants.SPACE +
                GlobalConstants.NEW + GlobalConstants.SPACE + clazz.getSimpleName() + GlobalConstants.OPEN + val +
                GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
    }

    private void menuAdd(PrintWriter writer, String from, String item) {
        writer.println("        " + from + GlobalConstants.DOT + GlobalConstants.ADD + GlobalConstants.OPEN + item +
                GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
    }

    @Override
    public void generateAddListener(PrintWriter writer, String className) {

        var addListener = ".addActionListener(e -> {";
        var addListenerSuffix = "});";
        var Dao = className + GlobalConstants.DAO;
        var dao = className.substring(0, 1).toLowerCase() + className.substring(1) + GlobalConstants.DAO;
        CodeUtil.defineMethodPrefix(writer, GlobalConstants.PUBLIC, GlobalConstants.VOID, "setListener");
        writer.println("        " + Dao + " " + dao + " = new " + Dao + "();\n");

        writer.println("        " + jMenuItemAdd + addListener);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemDelete + addListener);
        writer.println("            del(" + dao + ");");
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
    public void genMethod(PrintWriter writer, String className, String manageClassName) {

        add(writer, className);
        del(writer, className);

        loadList();

    }

    private void add(PrintWriter writer, String className) {

    }

    private void del(PrintWriter writer, String className) {
        var Dao = className + GlobalConstants.DAO;
        var dao = className.substring(0, 1).toLowerCase() + className.substring(1) + GlobalConstants.DAO;
        writer.println("    private void del(" + Dao + " " + dao + ") {");
        writer.println("        int selectedRow = " + jTable + ".getSelectedRow();");
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
