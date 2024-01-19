package org.ruoqing.codeGenerate.impl;

import org.ruoqing.codeGenerate.CodeGenerationStrategy;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.SwingConfig;
import org.ruoqing.enums.GlobalConstants;
import org.ruoqing.util.CodeUtil;

import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;

public class LoginGenerationStrategy implements CodeGenerationStrategy {

    private static final int LABEL_WIDTH = 50;
    private static final int LABEL_HEIGHT = 20;
    private static final int FIELD_X = 110;
    private static final int FIELD_WIDTH = 120;
    private static final int FIELD_HEIGHT = 20;
    private static final String USERNAME_LABEL = "usernameLabel";
    private static final String PASSWORD_LABEL = "passwordLabel";
    private static final String USERNAME_FIELD = "usernameField";
    private static final String PASSWORD_FIELD = "passwordField";
    private static final String LOGIN_BUTTON = "loginButton";
    private static final String REGISTER_BUTTON = "registerButton";
    private final PackageConfig packageConfig;
    private final SwingConfig swingConfig;

    public LoginGenerationStrategy(PackageConfig packageConfig, SwingConfig swingConfig) {
        this.packageConfig = packageConfig;
        this.swingConfig = swingConfig;
    }

    @Override
    public void generatePackageAndImport(PrintWriter writer, String className) {
        CodeUtil.definePackagePath(writer, packageConfig.getParentPackage());
        var imports = Arrays.asList("javax.swing.*", "java.awt.*");
        for (String anImport : imports) {
            CodeUtil.defineImportPath(writer, anImport);
        }
        CodeUtil.defineSubClassPrefix(writer, className, GlobalConstants.J_FRAME);
    }

    @Override
    public void genVariable(PrintWriter writer) {
        addAttr(writer, GlobalConstants.J_LABEL, USERNAME_LABEL, "\"用户名:\"");
        addAttr(writer, GlobalConstants.J_LABEL, PASSWORD_LABEL, "\"密码:\"");
        addAttr(writer, GlobalConstants.J_TEXT_FIELD, USERNAME_FIELD, "");
        addAttr(writer, GlobalConstants.J_PASSWORD_FIELD, PASSWORD_FIELD, "");
        addAttr(writer, GlobalConstants.J_BUTTON, LOGIN_BUTTON, "\"登录\"");
        addAttr(writer, GlobalConstants.J_BUTTON, REGISTER_BUTTON, "\"注册\"\n");
    }

    @Override
    public void generateConstructor(PrintWriter writer, String... args) {
        String className = args[0];
        String title = swingConfig.getTitle() + GlobalConstants.MANAGE_NAME;
        writer.println("    public " + className + "() {");
        writer.println("        setTitle(\"欢迎使用" + title + "\");");
        writer.println("        setBounds(600, 200, 300, 220);");
        writer.println("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);");
        writer.println("        setLayout(new BorderLayout());");
        writer.println("        init();");
        writer.println("        setListener();");
        writer.println("        setLocationRelativeTo(null);");
        writer.println("        setVisible(true);");
        writer.println("    }\n");
    }

    @Override
    public void genMethod(PrintWriter writer, String... args) {
        String manageClassName = args[1];
        genInit(writer);
        genSetListener(writer, manageClassName);
    }

    private void genInit(PrintWriter writer) {
        var title = swingConfig.getTitle() + GlobalConstants.MANAGE_NAME;
        var titlePanel = "titlePanel";
        var fieldPanel = "fieldPanel";
        var buttonPanel = "buttonPanel";
        CodeUtil.defineMethodPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PRIVATE, "", GlobalConstants.VOID, "init");

        createPanel(writer, titlePanel, new FlowLayout());
        createPanel(writer, fieldPanel, null);
        createPanel(writer, buttonPanel, new FlowLayout());

        writer.println("        " + titlePanel + GlobalConstants.DOT + GlobalConstants.ADD + GlobalConstants.OPEN + "new JLabel(\"" + title + "\"));");
        add(writer, titlePanel, "BorderLayout.NORTH");

        addComponentWithBounds(writer, fieldPanel, USERNAME_LABEL, LABEL_WIDTH, LABEL_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
        addComponentWithBounds(writer, fieldPanel, PASSWORD_LABEL, LABEL_WIDTH, LABEL_HEIGHT + 40, LABEL_WIDTH, LABEL_HEIGHT);
        addComponentWithBounds(writer, fieldPanel, USERNAME_FIELD, FIELD_X, FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
        addComponentWithBounds(writer, fieldPanel, PASSWORD_FIELD, FIELD_X, FIELD_HEIGHT + 40, FIELD_WIDTH, FIELD_HEIGHT);
        add(writer, fieldPanel, "BorderLayout.CENTER");

        addComponent(writer, buttonPanel, LOGIN_BUTTON);
        addComponent(writer, buttonPanel, REGISTER_BUTTON);
        add(writer, buttonPanel, "BorderLayout.SOUTH");
        writer.println(GlobalConstants.FIRST_LEVEL_END);
    }

    private void genSetListener(PrintWriter writer, String manageClassName) {
        var addListener = ".addActionListener(e -> {";
        var addListenerSuffix = "});";
        CodeUtil.defineMethodPrefix(writer, GlobalConstants.FIRST_TAB, GlobalConstants.PRIVATE, "", GlobalConstants.VOID, "setListener");
        writer.println("        " + LOGIN_BUTTON + addListener);
        writer.println("            String username = " + USERNAME_FIELD + ".getText();");
        writer.println("            char[] passwordCharArray = " + PASSWORD_FIELD + ".getPassword();");
        writer.println("            String password = new String(passwordCharArray);");
        writer.println("            if (username.equals(\"admin\") && \"admin\".equals(password)) {");
        writer.println("                dispose();");
        writer.println("                new " + manageClassName + "Manage();");
        writer.println("            } else {");
        writer.println("                JOptionPane.showMessageDialog(null, \"账号或密码错误\", \"登录失败\", JOptionPane.ERROR_MESSAGE);");
        writer.println("            }");
        writer.println("        " + addListenerSuffix);
        writer.println("    }");
    }

    private void addAttr(PrintWriter writer, String className, String label, String val) {
        writer.println(String.format("    private final %s %s = new %s(%s);", className, label, className, val));
    }

    private void createPanel(PrintWriter writer, String name, LayoutManager layout) {
        writer.println("        JPanel " + name + " = new JPanel();");
        if (layout == null) {
            writer.println("        " + name + ".setLayout(null);");
        } else {
            writer.println("        " + name + ".setLayout(new " + layout.getClass().getSimpleName() + "());");
        }
    }


    private void addComponentWithBounds(PrintWriter writer, String panel, String component, int x, int y, int width, int height) {
        writer.println("        " + component + ".setBounds(" + x + ", " + y + ", " + width + ", " + height + ");");
        addComponent(writer, panel, component);
    }

    private void addComponent(PrintWriter writer, String panel, String component) {
        writer.println("        " + panel + GlobalConstants.DOT + GlobalConstants.ADD + GlobalConstants.OPEN + component + GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
    }

    private void add(PrintWriter writer, String panel, String layout) {
        writer.println("        " + GlobalConstants.ADD + GlobalConstants.OPEN + panel + GlobalConstants.COMMA + GlobalConstants.SPACE + layout + GlobalConstants.CLOSE + GlobalConstants.SEMICOLON);
    }
}
