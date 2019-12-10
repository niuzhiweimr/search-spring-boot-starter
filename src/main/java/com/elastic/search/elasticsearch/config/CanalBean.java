package com.elastic.search.elasticsearch.config;

import java.io.Serializable;
import java.util.Map;

/**
 * Canal接到binlog的封装类
 *
 * @author niuzhiwei
 */
public class CanalBean implements Serializable {

    //数据库
    private String database;
    //表
    private String table;
    //操作时间
    private Long executeTime;
    //操作类型
    //INSERT = 1;UPDATE = 2;DELETE = 3;CREATE = 4;ALTER = 5;ERASE = 6;QUERY = 7;
    //TRUNCATE = 8;RENAME = 9;CINDEX = 10;DINDEX = 11;
    private int eventType;
    //ddl的sql
    private String ddlSql;
    //行数据
    private RowData rowData;

    public static class RowData {
        //操作前字段信息
        private Map<String, ColumnEntry> beforeColumns;
        //操作后字段信息
        private Map<String, ColumnEntry> afterColumns;

        public RowData() {
        }

        public RowData(Map<String, ColumnEntry> beforeColumns, Map<String, ColumnEntry> afterColumns) {
            this.beforeColumns = beforeColumns;
            this.afterColumns = afterColumns;
        }

        public Map<String, ColumnEntry> getBeforeColumns() {
            return beforeColumns;
        }

        public void setBeforeColumns(Map<String, ColumnEntry> beforeColumns) {
            this.beforeColumns = beforeColumns;
        }

        public Map<String, ColumnEntry> getAfterColumns() {
            return afterColumns;
        }

        public void setAfterColumns(Map<String, ColumnEntry> afterColumns) {
            this.afterColumns = afterColumns;
        }

        public static class ColumnEntry {
            //字段名称
            private String name;
            //是否是主键
            private Boolean isKey;
            //是否修改
            private Boolean updated;
            //是否为空
            private Boolean isNull;
            //字段的值
            private String value;

            public ColumnEntry() {
            }

            public ColumnEntry(String name, Boolean isKey, Boolean updated, Boolean isNull, String value) {
                this.name = name;
                this.isKey = isKey;
                this.updated = updated;
                this.isNull = isNull;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Boolean getIsKey() {
                return isKey;
            }

            public void setIsKey(Boolean isKey) {
                this.isKey = isKey;
            }

            public Boolean getUpdated() {
                return updated;
            }

            public void setUpdated(Boolean updated) {
                this.updated = updated;
            }

            public Boolean getIsNull() {
                return isNull;
            }

            public void setIsNull(Boolean isNull) {
                this.isNull = isNull;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public CanalBean() {
    }

    public CanalBean(String database, String table, Long executeTime, int eventType, String ddlSql) {
        this.database = database;
        this.table = table;
        this.executeTime = executeTime;
        this.eventType = eventType;
        this.ddlSql = ddlSql;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getDdlSql() {
        return ddlSql;
    }

    public void setDdlSql(String ddlSql) {
        this.ddlSql = ddlSql;
    }

    public RowData getRowData() {
        return rowData;
    }

    public void setRowData(RowData rowData) {
        this.rowData = rowData;
    }


}
