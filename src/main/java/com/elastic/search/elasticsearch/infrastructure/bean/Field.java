package com.elastic.search.elasticsearch.infrastructure.bean;


import com.elastic.search.elasticsearch.infrastructure.enums.FieldTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 字段类型变更实体
 */
public class Field implements Serializable {
    private static final long serialVersionUID = 4819211937495431355L;
    /**
     * 原字段名
     */
    private String originName;
    /**
     * 新字段名
     */
    private String currentName;
    /**
     * 字段类型
     */
    private FieldTypeEnum fieldType;

    public Boolean isValid() {
        return null != this.fieldType && !StringUtils.isBlank(this.originName) && !StringUtils.isBlank(this.currentName) ? true : false;
    }

    public Field() {
    }

    public String getOriginName() {
        return this.originName;
    }

    public String getCurrentName() {
        return this.currentName;
    }

    public FieldTypeEnum getFieldType() {
        return this.fieldType;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setFieldType(FieldTypeEnum fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Field)) {
            return false;
        }
        Field field = (Field) o;
        return Objects.equals(originName, field.originName) &&
                Objects.equals(currentName, field.currentName) &&
                fieldType == field.fieldType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originName, currentName, fieldType);
    }

    @Override
    public String toString() {
        return "Field(originName=" + this.getOriginName() + ", currentName=" + this.getCurrentName() + ", fieldType=" + this.getFieldType() + ")";
    }
}
