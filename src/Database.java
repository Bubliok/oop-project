import java.util.List;

public class Database {
    private List<Table> tables;

    public Table createTable(String tableName) {
        Table table = new Table(tableName);
        tables.add(table);
        return table;
    }
}
