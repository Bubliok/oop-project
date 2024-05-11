package models;

import handlers.TableFileHandlerImpl;
import models.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private String tableName;
    private TableFileHandlerImpl fileHandler;
    private List<String> columnName;
    private List<String> dataType;
    private List<Row> row;

    public Table(String tableName) throws IOException {
        this.tableName = tableName;
        this.columnName = new ArrayList<>();
        this.dataType = new ArrayList<>();
        this.row = new ArrayList<>();
        this.fileHandler = new TableFileHandlerImpl(tableName);
    }

    public String getTableFilename() {
        return fileHandler.getTableFilename();
    }

    public void addRow(List<Object> values) throws IOException {
        if (values.size() != columnName.size()) {
            throw new IllegalArgumentException("Invalid number of values.");
        }
        Row newRow = new Row(values);
        row.add(newRow);
        fileHandler.writeRow(values);
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
        System.out.println("Column "+ column +" of type " + type +" successfully.");
    }


    public void printRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= row.size()) {
            throw new IllegalArgumentException("Invalid row index.");
        }
        Row targetRow = row.get(rowIndex);
        for (int i = 0; i < columnName.size(); i++) {
            System.out.println(columnName.get(i) + ": " + targetRow.getValues().get(i));
        }
    }

    public List<String> getDataType() {
        return dataType;
    }

    public List<String> getColumnName() {
        return columnName;
    }
}

