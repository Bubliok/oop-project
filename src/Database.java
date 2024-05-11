import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Table> tables;
    private String databaseName;

    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new ArrayList<>();
    }

    public Table createTable(String tableName) throws IOException {
        Table table = new Table(tableName);
        tables.add(table);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseName + ".txt", true))){
            writer.write(tableName + ", " + table.getTableFilename() + "\n");
        }
        return table;
    }
}
