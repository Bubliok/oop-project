package utils;

import models.Column;
import models.Row;
import models.Table;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TableLoader {

    public void loadTable(Table table){
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
                    String columnType = (String) table.getColumns().get(i).getColumnType();
                    Object value = TypeValidator.typeValidator(rowContents[i].trim(), columnType);
                    row.addValue(value);
                }
                table.addRow(row);
                //System.out.println(row);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File " + table.getTablePath() + " was not found.");
        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }
    }
}
