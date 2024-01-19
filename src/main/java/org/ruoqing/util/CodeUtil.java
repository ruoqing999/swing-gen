package org.ruoqing.util;

import org.ruoqing.enums.GlobalConstants;

import java.io.PrintWriter;
import java.util.StringJoiner;

public class CodeUtil {

    public static void definePackagePath(PrintWriter writer, String parent) {
        writer.println(GlobalConstants.PACKAGE + GlobalConstants.SPACE + parent + GlobalConstants.SEMICOLON +
                GlobalConstants.NEXT_LINE);
    }

    public static void defineImportPath(PrintWriter writer, String path) {
        writer.println(GlobalConstants.IMPORT + GlobalConstants.SPACE + path + GlobalConstants.SEMICOLON);
    }

    public static void defineAnnotation(PrintWriter writer, String annotation) {
        writer.println(GlobalConstants.ANNOTATION + annotation);
    }

    public static void defineClassPrefix(PrintWriter writer, String className) {
        defineSubClassPrefix(writer, className, "");
    }

    public static void defineSubClassPrefix(PrintWriter writer, String className, String parentClassName) {

        StringBuilder res = new StringBuilder(GlobalConstants.PUBLIC)
                .append(GlobalConstants.SPACE)
                .append(GlobalConstants.CLASS)
                .append(GlobalConstants.SPACE)
                .append(className)
                .append(GlobalConstants.SPACE);

        if (!parentClassName.isEmpty()) {
            res.append(GlobalConstants.EXTEND)
                    .append(GlobalConstants.SPACE)
                    .append(parentClassName);
        }
        res.append(GlobalConstants.LEFT)
                .append(GlobalConstants.NEXT_LINE);
        writer.println(res);
    }

    public static void defineConstructor(PrintWriter writer, String levelTab, String modifier, String constructor) {
        defineArgConstructor(writer, levelTab, modifier, constructor, "");
    }

    public static void defineArgConstructor(PrintWriter writer, String levelTab, String modifier, String constructor,
                                            String... arg) {
        StringBuilder res = new StringBuilder(levelTab)
                .append(modifier)
                .append(GlobalConstants.SPACE)
                .append(constructor)
                .append(GlobalConstants.OPEN);

        setArg(res, arg);
        res.append(GlobalConstants.CLOSE)
                .append(GlobalConstants.SPACE)
                .append(GlobalConstants.LEFT);
        writer.println(res);
    }

    public static void defineMethodPrefix(PrintWriter writer, String levelTab, String modifier, String type, String returnVal, String methodName) {
        defineMethodArgPrefix(writer, levelTab, modifier, type, returnVal, methodName, "", "");
    }

    public static void defineMethodArgPrefix(PrintWriter writer, String levelTab, String modifier, String type, String returnVal, String methodName,
                                             String className, String arg) {
        StringBuilder res = new StringBuilder(levelTab)
                .append(modifier);

        if (!type.isEmpty()) {
            res.append(GlobalConstants.SPACE).append(type);
        }
        res.append(GlobalConstants.SPACE)
                .append(returnVal)
                .append(GlobalConstants.SPACE)
                .append(methodName)
                .append(GlobalConstants.OPEN);
        if (!className.isEmpty() && !arg.isEmpty()) {
            res.append(className)
                    .append(GlobalConstants.SPACE)
                    .append(arg);
        }
        res.append(GlobalConstants.CLOSE)
                .append(GlobalConstants.SPACE)
                .append(GlobalConstants.LEFT);
        writer.println(res);
    }

    public static void defineField(PrintWriter writer, String className, String attrName) {
        writer.println("    " + className + GlobalConstants.SPACE + attrName + GlobalConstants.SEMICOLON);
    }

    public static void methodCall(PrintWriter writer, String levelTab, String callName, String methodName,
                                  String... methodArg) {
        methodCallReturn(writer, levelTab, callName, methodName, "", "", methodArg);
    }

    public static void methodCallReturn(PrintWriter writer, String levelTab, String callName, String methodName,
                                        String returnType, String returnName, String... methodArg) {

        StringBuilder res = new StringBuilder(levelTab);
        //返回值
        if (!returnType.isEmpty() && !returnName.isEmpty()) {
            res.append(returnType)
                    .append(GlobalConstants.SPACE)
                    .append(returnName)
                    .append(GlobalConstants.SPACE)
                    .append(GlobalConstants.EQUAL)
                    .append(GlobalConstants.SPACE);
        }
        if (!callName.isEmpty()) {
            res.append(callName)
                    .append(GlobalConstants.DOT);
        }
        res.append(methodName)
                .append(GlobalConstants.OPEN);

        setArg(res, methodArg);
        res.append(GlobalConstants.CLOSE)
                .append(GlobalConstants.SEMICOLON);
        writer.println(res);
    }

    public static void newObj(PrintWriter writer, String levelTab, String objName, String... arg) {
        newArgObjReturn(writer, levelTab, "", "", objName, arg);
    }

    public static void newArgObjReturn(PrintWriter writer, String levelTab, String returnType, String returnName,
                                       String objName, String... arg) {
        StringBuilder res = new StringBuilder(levelTab);
        if (!returnType.isEmpty() && !returnName.isEmpty()) {
            res.append(returnType)
                    .append(GlobalConstants.SPACE)
                    .append(returnName)
                    .append(GlobalConstants.SPACE)
                    .append(GlobalConstants.EQUAL)
                    .append(GlobalConstants.SPACE);
        }
        res.append(GlobalConstants.NEW)
                .append(GlobalConstants.SPACE)
                .append(objName)
                .append(GlobalConstants.OPEN);

        setArg(res, arg);
        res.append(GlobalConstants.CLOSE)
                .append(GlobalConstants.SEMICOLON);
        writer.println(res);
    }

    public static void ifStatement(PrintWriter writer, String levelTab, String boolVal) {
        String res = levelTab +
                GlobalConstants.IF +
                GlobalConstants.SPACE +
                GlobalConstants.OPEN +
                boolVal +
                GlobalConstants.CLOSE +
                GlobalConstants.SPACE +
                GlobalConstants.LEFT;
        writer.println(res);
    }

    public static void forStatementPrefix(PrintWriter writer, String levelTab, String collectionName, String className) {
        String res = levelTab + GlobalConstants.FOR + GlobalConstants.SPACE + GlobalConstants.OPEN + className +
                GlobalConstants.SPACE + className.toLowerCase() + GlobalConstants.SPACE + GlobalConstants.COLON +
                GlobalConstants.SPACE + collectionName + GlobalConstants.CLOSE + GlobalConstants.SPACE + GlobalConstants.LEFT;
        writer.println(res);
    }

    private static void setArg(StringBuilder res, String[] arg) {
        StringJoiner args = new StringJoiner(", ");
        if (arg != null) {
            for (String s : arg) {
                args.add(s);
            }
            res.append(args);
        }
    }
}
