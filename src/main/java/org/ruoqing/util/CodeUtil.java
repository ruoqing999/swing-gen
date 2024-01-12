package org.ruoqing.util;

import org.ruoqing.enums.GlobalConstants;

import java.io.PrintWriter;

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
        writer.println(GlobalConstants.PUBLIC + GlobalConstants.SPACE + GlobalConstants.CLASS + GlobalConstants.SPACE +
                className + GlobalConstants.SPACE + GlobalConstants.LEFT + GlobalConstants.NEXT_LINE);
    }

    public static void defineSubClassPrefix(PrintWriter writer, String className, String parentClassName) {
        writer.println(GlobalConstants.PUBLIC + GlobalConstants.SPACE + GlobalConstants.CLASS + GlobalConstants.SPACE +
                className + GlobalConstants.SPACE + GlobalConstants.EXTEND + GlobalConstants.SPACE + parentClassName +
                GlobalConstants.LEFT + GlobalConstants.NEXT_LINE);
    }

    public static void defineConstructor(PrintWriter writer, String constructor) {
        writer.println("    " + GlobalConstants.PUBLIC + GlobalConstants.SPACE + constructor + GlobalConstants.OPEN +
                GlobalConstants.CLOSE + GlobalConstants.SPACE + GlobalConstants.LEFT);
    }

    public static void defineMethodPrefix(PrintWriter writer, String modifier, String returnVal, String methodName) {
        writer.println("    " + modifier + GlobalConstants.SPACE + returnVal + GlobalConstants.SPACE +
                methodName + GlobalConstants.OPEN + GlobalConstants.CLOSE + GlobalConstants.SPACE + GlobalConstants.LEFT);
    }

    public static void defineField(PrintWriter writer, Class<?> clazz, String attrName, String className) {
        writer.println("    " + clazz.getSimpleName() + GlobalConstants.SPACE + attrName + GlobalConstants.SEMICOLON);
    }

}
