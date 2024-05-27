package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Database;
import models.Row;
import models.Table;

import java.util.List;

public class CountCommand implements Command {
    private DatabaseHandler databaseHandler;

    public CountCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 4){
            System.out.println("Invalid arguments. See 'help' for more information.");
            return;
        }
        String tableName = args[1];
        int searchColumn = Integer.parseInt(args[2]) -1;
        String searchValue = args[3];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);

        if (table == null){
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        int rowCount = 0;
        List<Row> rows = table.getRows();

        for (Row row : rows){
            Object columnValue = row.getValues().get(searchColumn);
            if (columnValue.toString().equals(searchValue)){
                rowCount++;
            }
        }
        System.out.println("Row count containing " + searchValue + ": " + rowCount);
    }
}
