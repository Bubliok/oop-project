package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;
import utils.TableWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements Command {
    private DatabaseHandler databaseHandler;

    public DeleteCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            System.out.println("Invalid arguments. See 'help' for more information.");
            return;
        }

        String tableName = args[1];
        int searchColumn = Integer.parseInt(args[2]) - 1;
        String value = args[3];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);
        TableWriter writer = new TableWriter();

        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

//        try {
//            TypeValidator.typeValidator("1", columnType);
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//            return;
//        }

//        if (table.hasColumn(columnName)) {
//            System.out.println("Column " + columnName + " already exists.");
//            return;
//        }
        List<Row> rowsToKeep = new ArrayList<>();
        List<Column> columns = table.getColumns();
        List<Row> rows = table.getRows();

        boolean flag = false;
        for (Row row : rows) {
            Object columnValue = row.getValues().get(searchColumn);
            //System.out.println(columnValue);
            if (columnValue.toString().equals(value)) {
                flag = true;
            } else {
                rowsToKeep.add(row);
            }
        }
        table.setRows(rowsToKeep);

        try {
            writer.writeTable(table, rowsToKeep, table.getTablePath());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (!flag){
            System.out.println("Search value not found.");
        } else {
            System.out.println("Rows deleted successfully.");
        }
    }
}
