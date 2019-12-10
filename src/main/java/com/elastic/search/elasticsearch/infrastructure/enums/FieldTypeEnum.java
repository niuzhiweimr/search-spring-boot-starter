package com.elastic.search.elasticsearch.infrastructure.enums;

/**
 * @author niuzhiwei
 */

public enum FieldTypeEnum {

    /**
     * 字段类型
     */
    STRING("string"),
    INTEGER("integer"),
    LONG("long"),
    SHORT("short"),
    BYTE("byte"),
    DOUBLE("double"),
    BIG_DECIMAL("double"),
    FLOAT("float");

    String type;

    private FieldTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
