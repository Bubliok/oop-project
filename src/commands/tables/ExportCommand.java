package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import models.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExportCommand implements Command {
    private CommandHandler commandHandler;

    public ExportCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid arguments.");
            return;
        }

        String tableName = args[1];
        String exportPath = args[2];
        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        File file = new File(exportPath);
        if (file.exists()) {
            System.out.println("File already exists.");
            return;
        }

        try {
            Files.copy(Paths.get(table.getTableFilename()), Paths.get(exportPath));
            System.out.println("Successfully exported table " + tableName + " to " + exportPath);

        } catch (IOException e) {
            System.out.println("Error exporting table: " + e.getMessage());
        }
    }
}