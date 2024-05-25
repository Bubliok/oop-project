package handlers;

import commands.Command;
import commands.HelpCommand;
import commands.databases.*;
import commands.tables.DescribeCommand;
import commands.tables.ExportCommand;
import commands.tables.ImportCommand;
import commands.tables.PrintCommand;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private Map<String, Command> commands;
    private File currentFile;
    private DatabaseHandler databaseHandler;

    public CommandHandler() {
        databaseHandler = new DatabaseHandler();
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this, databaseHandler));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this, databaseHandler));
        commands.put("saveas", new SaveAsCommand(databaseHandler));
        commands.put("help", new HelpCommand());

        commands.put("import", new ImportCommand(databaseHandler));
        commands.put("showtables", new ShowTablesCommand(databaseHandler));
        commands.put("describe", new DescribeCommand(this, databaseHandler));
        commands.put("print", new PrintCommand(databaseHandler));
        commands.put("export", new ExportCommand(databaseHandler));
    }

    public void handleCommand(String command) {
        if(currentFile == null && !command.startsWith("open") && !command.startsWith("help")) {
            System.out.println("No file is currently open.");
            return;
        }

        String[] parts = command.split(" ");
        if (commands.containsKey(parts[0])) {
            commands.get(parts[0]).execute(parts);
        } else {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

}
