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

public class InsertCommand implements Command {
    private DatabaseHandler databaseHandler;

    public InsertCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String tableName = args[1];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);
        TableWriter writer = new TableWriter();

        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        if (args.length != table.getColumns().size() + 2) {
            System.out.println("Invalid number of arguments. Expected " + (table.getColumns().size() + 2) + " but got " + args.length + ".");
            return;
        }
        List<Row> rows = table.getRows();
        Row newRow = new Row();
        boolean flag = false;
        for (int i = 2; i < args.length; i++) {
            Column targetColumn = table.getColumns().get(i-2);
            String targetValue = args[i];
            //System.out.println(args[i]);
            try {
               Object validatedValue = TypeValidator.typeValidator(targetValue, targetColumn.getColumnType());
                newRow.addValue(validatedValue);
                flag = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid data type for " + targetValue + " Expected type: " + targetColumn.getColumnType());
                return;
            }
        }
            rows.add(newRow);
        try {
            writer.writeTable(table, rows, table.getTablePath());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        if (flag) {
            System.out.println("Row inserted succesfully.");
        } else {
            System.out.println("Search value not found.");
        }
    }
}

