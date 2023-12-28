package org.ruoqing.enums;

import lombok.Getter;

@Getter
public enum OperateEnum {

    ADD(1, "add("),
    DEL(2, "del("),
    UPDATE(3, "update("),
    LIST(4, "list("),
    GET(5, "get("),
    UN_KNOW(100, "un_know")
    ;

    private final int code;
    private final String desc;

    OperateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OperateEnum valueOf(int code) {
        for (OperateEnum value : OperateEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UN_KNOW;
    }

}
