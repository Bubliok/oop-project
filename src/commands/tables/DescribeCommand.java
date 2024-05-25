package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Table;

public class DescribeCommand implements Command {
    private CommandHandler commandHandler;
    private DatabaseHandler databaseHandler;

    public DescribeCommand(CommandHandler commandHandler, DatabaseHandler databaseHandler) {
        this.commandHandler = commandHandler;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if(args.length < 2){
            System.out.println("Invalid arguments. Please provide a valid table name.");
            return;
        }
        String tableName = args[1];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);

        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        System.out.println("Column name\t\t\tColumn Type");
        for (Column column : table.getColumns()){
            System.out.println(column.getColumnName() + " \t\t" + column.getColumnType());
        }
    }
}
