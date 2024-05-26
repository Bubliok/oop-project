package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;
import utils.TableWriter;
import utils.TypeValidator;

import java.io.IOException;
import java.util.List;

public class AddColumnCommand implements Command {
    private DatabaseHandler databaseHandler;

    public AddColumnCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            System.out.println("Invalid arguments. See 'help' for more information.");
            return;
        }

        String tableName = args[1];
        String columnName = args[2];
        String columnType = args[3];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);
        TableWriter writer = new TableWriter();

        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        try {
            TypeValidator.typeValidator("1", columnType);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (table.hasColumn(columnName)) {
            System.out.println("Column " + columnName + " already exists.");
            return;
        }

        Column newColumn = new Column(columnName, columnType);
        table.addColumn(newColumn);

        List<Row> rows = table.getRows();
        for (Row row : rows) {
            row.addValue(null);
        }

        try {
            writer.writeTable(table, rows, table.getTablePath());
            System.out.println("Column added successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());;
        }
    }
}
