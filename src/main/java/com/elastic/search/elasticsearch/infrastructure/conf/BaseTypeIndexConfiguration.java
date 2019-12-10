package com.elastic.search.elasticsearch.infrastructure.conf;


import com.elastic.search.elasticsearch.infrastructure.constants.TypeIndexConstants;

import java.util.Objects;

/**
 * 索引配置类
 *
 * @author niuzhiwei
 */
public abstract class BaseTypeIndexConfiguration {

    private String systemName = TypeIndexConstants.SYSTEM_NAME;
    private String indexName;
    private String typeName;


    protected BaseTypeIndexConfiguration() {
    }

    protected BaseTypeIndexConfiguration(String indexName, String typeName) {
        this.indexName = indexName;
        this.typeName = typeName;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public String getIndexName() {
        return this.indexName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseTypeIndexConfiguration)) {
            return false;
        } else {
            BaseTypeIndexConfiguration other = (BaseTypeIndexConfiguration) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47:
                {
                    Object thisSystemname = this.getSystemName();
                    Object otherSystemname = other.getSystemName();
                    if (thisSystemname == null) {
                        if (otherSystemname == null) {
                            break label47;
                        }
                    } else if (thisSystemname.equals(otherSystemname)) {
                        break label47;
                    }

                    return false;
                }

                Object thisIndexname = this.getIndexName();
                Object otherIndexname = other.getIndexName();
                if (thisIndexname == null) {
                    if (otherIndexname != null) {
                        return false;
                    }
                } else if (!thisIndexname.equals(otherIndexname)) {
                    return false;
                }

                Object this$typeName = this.getTypeName();
                Object other$typeName = other.getTypeName();
                if (this$typeName == null) {
                    if (other$typeName != null) {
                        return false;
                    }
                } else if (!this$typeName.equals(other$typeName)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseTypeIndexConfiguration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemName, indexName, typeName);
    }

    @Override
    public String toString() {
        return "TypeIndexConfiguration(systemName=" + this.getSystemName() + ", indexName=" + this.getIndexName() + ", typeName=" + this.getTypeName() + ")";
    }
}

