package loaders;

import models.Table;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TableLoader {
    public Table importTable(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String tableName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Error: The file is empty.");
            }
            Table table = new Table(tableName);
            Arrays.stream(line.split(", ")).forEach(column -> {
                String[] parts = column.split(" ");
                if (parts.length == 2) {
                    table.addColumn(parts[0], parts[1]);
                }
            });
            return table;
        }
    }
}