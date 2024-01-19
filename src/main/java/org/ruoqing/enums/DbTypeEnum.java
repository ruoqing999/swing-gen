package org.ruoqing.enums;

import lombok.Getter;

@Getter
public enum DbTypeEnum {

    INT("INT", "Integer", "setInt(", "getInt(", "Integer.valueOf("),
    VARCHAR("VARCHAR", "String", "setString(", "getString(", ""),
    DOUBLE("DOUBLE", "Double", "setDouble(", "getDouble(", "DOUBLE.valueOf("),
    DECIMAL("DECIMAL", "BigDecimal", "setBigDecimal(", "getBigDecimal(", "BigDecimal.valueOf("),
    ;

    private final String dbType;
    private final String javaType;
    private final String dbSetMethod;
    private final String dbGetMethod;
    private final String parseType;

    DbTypeEnum(String dbType, String javaType, String dbSetMethod, String dbGetMethod, String parseType) {
        this.dbType = dbType;
        this.javaType = javaType;
        this.dbSetMethod = dbSetMethod;
        this.dbGetMethod = dbGetMethod;
        this.parseType = parseType;
    }

}
