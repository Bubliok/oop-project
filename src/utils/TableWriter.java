package utils;

import models.Column;
import models.Row;
import models.Table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TableWriter {

    public void writeTable(Table table, List<Row> rows, String filePath) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int columnCount = table.getColumns().size();
            for (int i = 0; i < columnCount; i++) {
                Column column = table.getColumns().get(i);
                writer.write(column.getColumnName() + ":" + column.getColumnType());
                if (i < columnCount - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();
            for (Row row : rows) {
                writer.write(row.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

