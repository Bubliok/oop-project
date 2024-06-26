package utils;

import models.Column;
import models.Database;
import models.Row;
import models.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TableLoader {
    private Database database;

    public TableLoader(Database database) {
        this.database = database;
    }

    public boolean loadTable(Table table){
        try (BufferedReader br = new BufferedReader(new FileReader(table.getTablePath()))){
            String line = br.readLine();
            if(line != null){
                String[] columnContents = (line.split(","));
                for(String columnContent : columnContents) {
                    String[] parts = columnContent.split(":");
                    String columnName = parts[0].trim();
                    String columnType = parts[1].trim();
                    Column column = new Column(columnName, columnType);
                    table.addColumn(column);
                    //System.out.println(columnName + " " + columnType);
                }
            }
            while((line = br.readLine()) != null){
                String[] rowContents = line.split(",");
                Row row = new Row();
                for (int i = 0; i < rowContents.length; i++) {
                    String columnType = table.getColumns().get(i).getColumnType();
                    try {
                        Object value = TypeValidator.typeValidator(rowContents[i].trim(), columnType);
                        row.addValue(value);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid type in table file: " + table.getTableName() + "; " + e.getMessage());
                        return false;
                    }
                }
                table.addRow(row);
            }
            database.addTable(table);
            return true;
          //System.out.println(table);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
