package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.SwingConfig;
import org.ruoqing.enums.DbTypeEnum;
import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.CodeUtil;
import org.ruoqing.util.JdbcUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringJoiner;

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
                "java.awt.*", "java.util.List");
        for (String anImport : imports) {
            CodeUtil.defineImportPath(writer, anImport);
        }
        writer.println();
        CodeUtil.defineSubClassPrefix(writer, className + GlobalConstants.MANAGE, GlobalConstants.J_FRAME);
        CodeUtil.defineField(writer, JMenuBar.class.getSimpleName(), jMenuBar);
        CodeUtil.defineField(writer, JMenu.class.getSimpleName(), jMenu);
        CodeUtil.defineField(writer, JMenuItem.class.getSimpleName(), jMenuItemAdd + ", " + jMenuItemDelete + ", " +
                jMenuItemUpdate + ", " + jMenuItemView + ", " + jMenuItemSelectReturn);
        CodeUtil.defineField(writer, JTable.class.getSimpleName(), jTable);
        CodeUtil.defineField(writer, DefaultTableModel.class.getSimpleName(), tableModel);
        CodeUtil.defineField(writer, className + GlobalConstants.DAO, className.toLowerCase() + GlobalConstants.DAO);
    }

    @Override
    public void generateConstructor(PrintWriter writer, String... args) {
        String className = args[0];
        String fields = args[1];
        var title = this.swingConfig.getTitle();
        CodeUtil.defineConstructor(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PUBLIC, className + GlobalConstants.MANAGE);
        writer.println("        setTitle(\"" + title + GlobalConstants.MANAGE_NAME + "\");");
        writer.println("        setBounds(100, 100, 500, 500);");
        String classNameDao = className.toLowerCase() + GlobalConstants.DAO;
        newObject(writer, classNameDao, className + GlobalConstants.DAO, "");
        newObject(writer, jMenuBar, JMenuBar.class.getSimpleName(), "");
        newObject(writer, jMenu, JMenu.class.getSimpleName(), "\"" + title + "\"");
        newObject(writer, jMenuItemAdd, JMenuItem.class.getSimpleName(), "\"添加" + title + "信息\"");
        newObject(writer, jMenuItemDelete, JMenuItem.class.getSimpleName(), "\"删除" + title + "信息\"");
        newObject(writer, jMenuItemUpdate, JMenuItem.class.getSimpleName(), "\"修改" + title + "信息\"");
        newObject(writer, jMenuItemView, JMenuItem.class.getSimpleName(), "\"查询所有" + title + "信息\"");
        newObject(writer, jMenuItemSelectReturn, JMenuItem.class.getSimpleName(), "\"返回\"");
        var menuItems = Arrays.asList(jMenuItemAdd, jMenuItemDelete, jMenuItemUpdate, jMenuItemView, jMenuItemSelectReturn);
        for (String menuItem : menuItems) {
            menuAdd(writer, jMenu, menuItem);
        }
        menuAdd(writer, jMenuBar, jMenu);
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, "", "setJMenuBar", jMenuBar);
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, "", "setListener");
        writer.println("        String[] columnNames " + GlobalConstants.EQUAL + GlobalConstants.SPACE +
                GlobalConstants.LEFT + fields + GlobalConstants.RIGHT + GlobalConstants.SEMICOLON);
        newObject(writer, tableModel, DefaultTableModel.class.getSimpleName(), "null, columnNames");
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, "", "loadList", classNameDao);
        newObject(writer, jTable, JTable.class.getSimpleName(), tableModel);
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, "", GlobalConstants.ADD, "new JScrollPane(jTable)", "BorderLayout.CENTER");
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, "", "setVisible", Boolean.TRUE.toString());
        writer.println(GlobalConstants.FIRST_LEVEL_END + GlobalConstants.NEXT_LINE);
    }

    @Override
    public void generateAddListener(PrintWriter writer, String className) {

        var addListener = ".addActionListener(e -> {";
        var addListenerSuffix = GlobalConstants.RIGHT + GlobalConstants.CLOSE + GlobalConstants.SEMICOLON;
        var dao = className.toLowerCase() + GlobalConstants.DAO;
        CodeUtil.defineMethodPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PUBLIC, "", GlobalConstants.VOID, "setListener");

        writer.println("        " + jMenuItemAdd + addListener);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "add", dao);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "loadList",
                className.toLowerCase() + GlobalConstants.DAO);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemDelete + addListener);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "del", dao);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemUpdate + addListener);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "update", dao);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "loadList",
                className.toLowerCase() + GlobalConstants.DAO);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemView + addListener);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "loadList",
                className.toLowerCase() + GlobalConstants.DAO);
        writer.println("        " + addListenerSuffix + "\n");

        writer.println("        " + jMenuItemSelectReturn + addListener);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, "", "loadList",
                className.toLowerCase() + GlobalConstants.DAO);
        writer.println("        " + addListenerSuffix + "\n");
        writer.println("    }\n");
    }

    @Override
    public void genMethod(PrintWriter writer, String... args) {
        String className = args[0];
        String fields = args[1];
        add(writer, className, fields);
        writer.println();
        del(writer, className);
        writer.println();
        update(writer, className, fields);
        loadList(writer, className, fields);
    }

    private void textField(PrintWriter writer, String field) {
        field = field.split("-")[0];

        writer.println(GlobalConstants.SECOND_TAB + JTextField.class.getSimpleName() + GlobalConstants.SPACE +
                field + "Field" + GlobalConstants.SPACE + GlobalConstants.EQUAL + GlobalConstants.SPACE +
                GlobalConstants.NEW + GlobalConstants.SPACE + JTextField.class.getSimpleName() + GlobalConstants.OPEN +
                GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);

        String arg = GlobalConstants.NEW + GlobalConstants.SPACE + JLabel.class.getSimpleName() +
                GlobalConstants.OPEN + "\"" + field + GlobalConstants.COLON + "\"" + GlobalConstants.CLOSE;
        String lowerPanel = JPanel.class.getSimpleName().substring(1).toLowerCase();
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, lowerPanel, GlobalConstants.ADD, arg);
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, lowerPanel, GlobalConstants.ADD, field + "Field");
    }

    private void add(PrintWriter writer, String className, String fields) {

        var Dao = className + GlobalConstants.DAO;
        String lowerCase = className.toLowerCase();
        var dao = lowerCase + GlobalConstants.DAO;
        CodeUtil.defineMethodArgPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PRIVATE, "", GlobalConstants.VOID, "add", Dao, dao);
        String[] split = fields.split(",");
        genPanel(writer, split);
        String JPanel = JPanel.class.getSimpleName();
        String lowerPanel = JPanel.substring(1).toLowerCase();

        StringJoiner constructorArg = new StringJoiner(", ");
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                continue;
            }

            String s = split[i];
            textField(writer, s);
            constructorArg.add(getText(s));
        }

        CodeUtil.methodCallReturn(writer, GlobalConstants.SECOND_TAB, JOptionPane.class.getSimpleName(),
                "showConfirmDialog", int.class.getSimpleName(), "result", "null",
                lowerPanel, "\"add\"", "JOptionPane.OK_CANCEL_OPTION");
        CodeUtil.ifStatement(writer, GlobalConstants.SECOND_TAB, "result == JOptionPane.OK_OPTION");

        CodeUtil.newArgObjReturn(writer, GlobalConstants.THIRD_TAB, className, lowerCase, className,
                "null", constructorArg.toString());
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, dao, GlobalConstants.ADD, lowerCase);
        writer.println(GlobalConstants.SECOND_LEVEL_END);
        writer.println(GlobalConstants.FIRST_LEVEL_END);
    }

    private String getText(String s) {
        var split = s.split("-");
        var fieldName = split[0];
        var fieldType = split[1];

        var res = fieldName + "Field.getText()";
        if (!fieldType.equals(DbTypeEnum.VARCHAR.getDbType())) {
            String parseType = DbTypeEnum.valueOf(fieldType).getParseType();
            return parseType + res + GlobalConstants.CLOSE;
        }
        return res;
    }

    private void del(PrintWriter writer, String className) {
        var dao = className.toLowerCase() + GlobalConstants.DAO;
        selectRow(writer, className, "del", dao);
        CodeUtil.methodCallReturn(writer, GlobalConstants.SECOND_TAB, "(String) " + tableModel,
                "getValueAt", String.class.getSimpleName(), "code", "selectedRow", "0");
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, dao, "del", "Integer.valueOf(code)");
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, tableModel, "removeRow", "selectedRow");
        writer.println(GlobalConstants.FIRST_LEVEL_END);
    }

    private void update(PrintWriter writer, String className, String fields) {
        String lowerCase = className.toLowerCase();
        var dao = lowerCase + GlobalConstants.DAO;
        selectRow(writer, className, "update", dao);

        String[] split = fields.split(",");
        genPanel(writer, split);
        String JPanel = JPanel.class.getSimpleName();
        String lowerPanel = JPanel.substring(1).toLowerCase();
        StringJoiner constructorArg = new StringJoiner(", ");
        for (int i = 0; i < split.length; i++) {

            String fieldName = split[i].split("-")[0];
            constructorArg.add(getText(split[i]));
            String callName = tableModel + GlobalConstants.DOT + "getValueAt" + GlobalConstants.OPEN +
                    "selectedRow" + GlobalConstants.COMMA + GlobalConstants.SPACE + i + GlobalConstants.CLOSE;
            CodeUtil.methodCallReturn(writer, GlobalConstants.SECOND_TAB, callName, "toString",
                    String.class.getSimpleName(), fieldName);

            writer.println(GlobalConstants.SECOND_TAB + JTextField.class.getSimpleName() + GlobalConstants.SPACE +
                    fieldName + "Field" + GlobalConstants.SPACE + GlobalConstants.EQUAL + GlobalConstants.SPACE +
                    GlobalConstants.NEW + GlobalConstants.SPACE + JTextField.class.getSimpleName() + GlobalConstants.OPEN +
                    fieldName + GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
            String arg = GlobalConstants.NEW + GlobalConstants.SPACE + JLabel.class.getSimpleName() +
                    GlobalConstants.OPEN + "\"" + fieldName + GlobalConstants.COLON + "\"" + GlobalConstants.CLOSE;
            CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, lowerPanel, GlobalConstants.ADD, arg);
            CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, lowerPanel, GlobalConstants.ADD, fieldName + "Field");
        }
        CodeUtil.methodCallReturn(writer, GlobalConstants.SECOND_TAB, JOptionPane.class.getSimpleName(),
                "showConfirmDialog", int.class.getSimpleName(), "result", "null",
                lowerPanel, "\"update\"", "JOptionPane.OK_CANCEL_OPTION");
        CodeUtil.ifStatement(writer, GlobalConstants.SECOND_TAB, "result == JOptionPane.OK_OPTION");

        CodeUtil.newArgObjReturn(writer, GlobalConstants.THIRD_TAB, className, lowerCase, className, constructorArg.toString());
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, dao, GlobalConstants.UPDATE, lowerCase);
        writer.println(GlobalConstants.SECOND_LEVEL_END);
        writer.println(GlobalConstants.FIRST_LEVEL_END);

    }

    private static void genPanel(PrintWriter writer, String[] split) {
        int length = split.length;
        int row = length / 2;
        if (length % 2 != 0) {
            row++;
        }
        String JPanel = JPanel.class.getSimpleName();
        String lowerPanel = JPanel.substring(1).toLowerCase();
        String girdLayout = GridLayout.class.getSimpleName();
        writer.println(GlobalConstants.SECOND_TAB + JPanel + GlobalConstants.SPACE +
                lowerPanel + GlobalConstants.SPACE + GlobalConstants.EQUAL +
                GlobalConstants.SPACE + GlobalConstants.NEW + GlobalConstants.SPACE + JPanel + GlobalConstants.OPEN +
                GlobalConstants.NEW + GlobalConstants.SPACE + girdLayout + GlobalConstants.OPEN + row +
                GlobalConstants.COMMA + GlobalConstants.SPACE + 2 + GlobalConstants.CLOSE + GlobalConstants.CLOSE +
                GlobalConstants.SEMICOLON);
    }

    private void selectRow(PrintWriter writer, String className, String methodName, String dao) {
        var Dao = className + GlobalConstants.DAO;
        CodeUtil.defineMethodArgPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PRIVATE, "", GlobalConstants.VOID,
                methodName, Dao, dao);
        CodeUtil.methodCallReturn(writer, GlobalConstants.SECOND_TAB, jTable, "getSelectedRow",
                int.class.getSimpleName(), "selectedRow");
        CodeUtil.ifStatement(writer, GlobalConstants.SECOND_TAB, "selectedRow == -1");
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, JOptionPane.class.getSimpleName(),
                "showMessageDialog", GlobalConstants.THIS, " \"请选中要操作的数据行\" ");
        writer.println(GlobalConstants.THIRD_TAB + GlobalConstants.RETURN + GlobalConstants.SEMICOLON);
        writer.println(GlobalConstants.SECOND_LEVEL_END);
    }

    private void loadList(PrintWriter writer, String className, String fields) {
        String lowerCase = className.toLowerCase();
        var dao = lowerCase + GlobalConstants.DAO;
        CodeUtil.defineMethodArgPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PRIVATE, "", GlobalConstants.VOID,
                "loadList", className + GlobalConstants.DAO, dao);
        CodeUtil.methodCall(writer, GlobalConstants.SECOND_TAB, tableModel, "setRowCount", "0");
        CodeUtil.methodCallReturn(writer, GlobalConstants.SECOND_TAB, dao, "list",
                "List<" + className + ">", "list");
        CodeUtil.forStatementPrefix(writer, GlobalConstants.SECOND_TAB, "list", className);

        StringJoiner stringJoiner = new StringJoiner(", ");
        String[] split = fields.split(",");
        for (String s : split) {
            String[] split1 = s.split("-");
            var fieldName = split1[0];
            var fieldType = split1[1];
            var capitalizedColumnName = JdbcUtil.toCapitalized(JdbcUtil.toCamelCase(fieldName));
            var item = lowerCase + ".get" + capitalizedColumnName + GlobalConstants.OPEN + GlobalConstants.CLOSE;
            if (!fieldType.equals(DbTypeEnum.VARCHAR.getDbType())) {
                item = item + GlobalConstants.DOT + "toString()";
            }
            stringJoiner.add(item);
        }

        writer.println(GlobalConstants.THIRD_TAB + "String[] data " + GlobalConstants.EQUAL + GlobalConstants.SPACE +
                GlobalConstants.LEFT + stringJoiner + GlobalConstants.RIGHT + GlobalConstants.SEMICOLON);
        CodeUtil.methodCall(writer, GlobalConstants.THIRD_TAB, tableModel, "addRow", "data");
        writer.println(GlobalConstants.SECOND_TAB + GlobalConstants.RIGHT);
        writer.println(GlobalConstants.FIRST_TAB + GlobalConstants.RIGHT);

    }

    private void newObject(PrintWriter writer, String attrName, String className, String val) {
        writer.println("        " + attrName + GlobalConstants.SPACE + GlobalConstants.EQUAL + GlobalConstants.SPACE +
                GlobalConstants.NEW + GlobalConstants.SPACE + className + GlobalConstants.OPEN + val +
                GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
    }

    private void menuAdd(PrintWriter writer, String from, String item) {
        writer.println("        " + from + GlobalConstants.DOT + GlobalConstants.ADD + GlobalConstants.OPEN + item +
                GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
    }
}
