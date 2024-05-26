package commands.databases;

import commands.Command;
import handlers.CommandHandler;
import handlers.DatabaseHandler;
import models.Database;
import models.Table;

import java.io.*;

public class SaveCommand implements Command {
    private CommandHandler commandHandler;
    private DatabaseHandler databaseHandler;

    public SaveCommand(CommandHandler commandHandler, DatabaseHandler databaseHandler) {
        this.commandHandler = commandHandler;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        File currentFile = commandHandler.getCurrentFile();
        Database database = databaseHandler.getDatabase();
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                //System.out.println(database);
                for (Table table : database.getTables()){
                    //System.out.println(table);
                    writer.write(table.getTableName()+", "+table.getAbsoulutePath());
                    writer.newLine();
                }
                System.out.println("Successfully saved: "+currentFile.getName());
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
