package commands.tables;
import commands.Command;
import handlers.CommandHandler;
import models.Table;
import models.Database;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ImportCommand implements Command {
    private CommandHandler commandHandler;

    public ImportCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid command. Please provide a file path.");
            return;
        }
        String filePath = args[1];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String tableName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            if (commandHandler.getDatabase().getTable(tableName) != null) {
                System.out.println("Error: A table with the name '" + tableName + "' already exists.");
                return;
            }
            String line = reader.readLine();
            if (line == null) {
                System.out.println("Error: The file is empty.");
                return;
            }
            Table table = new Table(tableName);
            Arrays.stream(line.split(", ")).forEach(column -> {
                String[] parts = column.split(" ");
                if (parts.length == 2) {
                    table.addColumn(parts[0], parts[1]);
                }
            });
            commandHandler.getDatabase().addTable(table);
            System.out.println("Successfully imported table '" + tableName + "' from file '" + filePath + "'.");
        } catch (IOException e) {
            System.out.println("Error: Unable to read from file '" + filePath + "'.");
        }
    }
}