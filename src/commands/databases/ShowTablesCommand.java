package commands.databases;

import commands.Command;
import handlers.DatabaseHandler;
import models.Database;
import models.Table;

public class ShowTablesCommand implements Command {
    private DatabaseHandler databaseHandler;

    public ShowTablesCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        Database database = databaseHandler.getDatabase();
        System.out.println("Currently loaded tables: ");
        for (Table table : database.getTables()){
            System.out.println(table.getTableName());
        }
    }
}
