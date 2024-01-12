package org.ruoqing.util;

import org.ruoqing.enums.GlobalConstants;

import java.io.PrintWriter;

public class CodeUtil {

    public static void defineMethodPrefix(PrintWriter writer, String modifier, String returnVal, String methodName) {
        if (GlobalConstants.PUBLIC.equals(modifier)) {
            definePublicMethodPrefix(writer, returnVal, methodName);
        }

        if (GlobalConstants.PRIVATE.equals(modifier)) {
            definePrivateMethodPrefix(writer, returnVal, methodName);
        }
    }

    public static void defineClassPrefix(PrintWriter writer, String className) {
        writer.println(GlobalConstants.PUBLIC + GlobalConstants.SPACE + GlobalConstants.CLASS + GlobalConstants.SPACE +
                className + GlobalConstants.SPACE + GlobalConstants.LEFT);
    }

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

    private static void definePublicMethodPrefix(PrintWriter writer, String returnVal, String methodName) {
        writer.println("    " + GlobalConstants.PUBLIC + GlobalConstants.SPACE + returnVal + GlobalConstants.SPACE +
                methodName + GlobalConstants.OPEN + GlobalConstants.CLOSE + GlobalConstants.SPACE + GlobalConstants.LEFT);
    }

    private static void definePrivateMethodPrefix(PrintWriter writer, String returnVal, String methodName) {
        writer.println("    " + GlobalConstants.PRIVATE + GlobalConstants.SPACE + returnVal + GlobalConstants.SPACE +
                methodName + GlobalConstants.OPEN + GlobalConstants.CLOSE + GlobalConstants.SPACE + GlobalConstants.LEFT);
    }

}
