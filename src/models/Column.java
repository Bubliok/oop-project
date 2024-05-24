package models;

public class Column {
    private String columnName;
    private Object columnType;

    public Column(String columnName, Object columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getColumnType() {
        return columnType;
    }

    public void setColumnType(Object columnType) {
        this.columnType = columnType;
    }
}
