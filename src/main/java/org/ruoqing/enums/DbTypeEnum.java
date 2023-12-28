package org.ruoqing.enums;

import lombok.Getter;

@Getter
public enum DbTypeEnum {

    INT("INT", "Integer", "setInt("),
    VARCHAR("VARCHAR", "String", "setString("),
    DOUBLE("DOUBLE", "Double", "setDouble("),
    ;

    private final String dbType;
    private final String javaType;
    private final String dbMethod;

    DbTypeEnum(String dbType, String javaType, String dbMethod) {
        this.dbType = dbType;
        this.javaType = javaType;
        this.dbMethod = dbMethod;
    }

}
