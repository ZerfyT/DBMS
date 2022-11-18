package com.nipun.dbms;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nipun
 */
public class TableStructure {

    private String tbName;
    private List<TableColumn> tbColumns;

    public TableStructure(String tbName) {
        this.tbName = tbName;
        this.tbColumns = new ArrayList<>();
    }

    public TableStructure(String tbName, List<TableColumn> tbColumns) {
        this.tbName = tbName;
        this.tbColumns = tbColumns;
    }

    public String getTbName() {
        return tbName;
    }

    public List<TableColumn> getTbColumns() {
        return tbColumns;
    }

    public void addNewColumn(TableColumn column) {
        tbColumns.add(column);
    }

    /**
     * Delete a Selected Table Column
     */
    public void deleteColumn(int index) {
        tbColumns.remove(index);
    }
}

/**
 * Inner Class - A Single Table Column
 */
class TableColumn {

    private String columnName;
    private String dataType;
    private String size;
    private boolean isPrimary;
    private boolean isForeign;
    private boolean isNotNull;

    public TableColumn(String columnName, String dataType, boolean isPrimary, boolean isForeign, boolean isNotNull) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isPrimary = isPrimary;
        this.isForeign = isForeign;
        this.isNotNull = isNotNull;
    }

    public TableColumn(String columnName, String dataType, String size, boolean isPrimary, boolean isForeign, boolean isNotNull) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.size = size;
        this.isPrimary = isPrimary;
        this.isForeign = isForeign;
        this.isNotNull = isNotNull;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getSize() {
        return size;
    }

    public boolean isIsPrimary() {
        return isPrimary;
    }

    public boolean isIsForeign() {
        return isForeign;
    }

    public boolean isIsNotNull() {
        return isNotNull;
    }

}
