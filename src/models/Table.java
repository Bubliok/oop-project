package models;

import handlers.TableFileHandlerImpl;
import models.Row;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;

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
        loadDataTypeFromFile();
    }

    public String getTableFilename() {
        return fileHandler.getTableFilename();
    }

    public List<Row> getRows() {
        return row;
    }

    public void addRow(List<Object> values) throws IOException {
        if (values.size() != columnName.size()) {
            throw new IllegalArgumentException("Invalid number of values.");
        }
        Row newRow = new Row();
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
        System.out.println("Added column "+ column +" of type " + type +" successfully.");
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
    public String getTableName() {
        return tableName;
    }
    public void loadFromFile(String tableFilePath) {
    }
    public void loadDataTypeFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(tableName + ".xml"))) {
            String line;
            if ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    String[] columnAndType = part.split(" ");
                    if (columnAndType.length == 2) {
                        columnName.add(columnAndType[0]);
                        dataType.add(columnAndType[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file for table " + tableName);
        }
    }
    public TableFileHandlerImpl getFileHandler() {
        return this.fileHandler;
    }
//    public String getFilePath() {
//        return fileHandler.getFilePath();
//    }
//    use this instead when creating new tables
//    TableFileHandler fileHandler = new TableFileHandlerImpl(tableName);
//    Table table = new Table(tableName, fileHandler);
}

