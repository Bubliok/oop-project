package models;

import handlers.TableFileHandlerImpl;

import java.io.IOException;
import java.util.*;

public class Table {

    private String tableName;
    private TableFileHandlerImpl fileHandler;
    private List<String> columnName;
    private List<String> dataType;
    private HashMap<String, String> columnTypes;
    private List<Row> row;

    public Table(String tableName, TableFileHandlerImpl tableFileHandler) throws IOException {
        this.tableName = tableName;
        this.columnName = new ArrayList<>();
        this.dataType = new ArrayList<>();
        this.row = new ArrayList<>();
        this.columnTypes = new HashMap<>();
        this.fileHandler = tableFileHandler;
    }

    public void setFileHandler(TableFileHandlerImpl fileHandler) {
        this.fileHandler = fileHandler;
    }

    public String getTableFilename() {
        return fileHandler.getTableFilename();
    }

    public List<Row> getRows() {
        return row;
    }

    public void addColumn(String column, String type) {
        if (!type.equals("int") && !type.equals("string") && !type.equals("float")) {
            throw new IllegalArgumentException("Invalid data type.");
        }
        columnName.add(column);
        dataType.add(type);
        for(Row row : row) {
            row.addValue(null);
        }
        System.out.println("Added column "+ column +" of type " + type +" successfully.");
    }

    public HashMap<String, String> getColumnType() {
        return columnTypes;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public List<String> getDataType() {
        return dataType;
    }
    public List<String> getColumnName() {
        return columnName;
    }
    public String getTableName() {
        return tableName;
    }
    public TableFileHandlerImpl getFileHandler() {
        return this.fileHandler;
    }
}

