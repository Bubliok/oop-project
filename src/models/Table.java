package models;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tableName;
    private String tablePath;
    private List<Column> columns;
    private List<Row> rows;

    public Table(String tableName, String tablePath) {
        this.tableName = tableName;
        this.tablePath = tablePath;
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    public void addRow(Row row){
        this.rows.add(row);
    }
    //add a new column object to the columns list
    public void addColumn(Column column){
        this.columns.add(column);
    }
    //remove a column object from the columns list
    public void removeColumn(Column column){
        this.columns.remove(column);
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTablePath() {
        return tablePath;
    }
    public String getAbsoulutePath(){
        return Paths.get("").toAbsolutePath().toString()+"/"+getTablePath();
    }

    public void setTablePath(String tablePath) {
        this.tablePath = tablePath;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}