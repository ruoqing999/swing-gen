package org.ruoqing.enums;

import lombok.Getter;

@Getter
public enum DbTypeEnum {

    INT("INT", "Integer", "setInt(", "getInt("),
    VARCHAR("VARCHAR", "String", "setString(", "getString("),
    DOUBLE("DOUBLE", "Double", "setDouble(", "getDouble("),
    DECIMAL("DECIMAL", "BigDecimal", "setBigDecimal(", "getBigDecimal("),
    ;

    private final String dbType;
    private final String javaType;
    private final String dbSetMethod;
    private final String dbGetMethod;

    DbTypeEnum(String dbType, String javaType, String dbSetMethod, String dbGetMethod) {
        this.dbType = dbType;
        this.javaType = javaType;
        this.dbSetMethod = dbSetMethod;
        this.dbGetMethod = dbGetMethod;
    }

}
