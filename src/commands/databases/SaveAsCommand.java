package commands.databases;
import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsCommand implements Command {
    private CommandHandler commandHandler;

    public SaveAsCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        try {
            String filePath = args[1];
            File file = new File(filePath);
            File directory = file.getParentFile();

            if (args.length < 2) {
                System.out.println("Invalid file path.");
                return;
            }

            if (file.isDirectory()) {
                System.out.println("No file name provided.");
                return;
            }

            if (file.exists()) {
                System.out.println("File already exists.");
                return;
            }

            if (directory == null) {
                System.out.println("Invalid file path.");
                return;
            }

//            if (!directory.exists()) {
//                directory.mkdirs();
//            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Table table : commandHandler.getDatabase().getTables()) {
                    String tableName = table.getTableName();
                    TableFileHandler fileHandler = new TableFileHandlerImpl(tableName);
                    String tableFilePath = fileHandler.getFilePath();
                    writer.write(tableFilePath + "," + table.getTableName());
                    writer.newLine();
                }
                System.out.println("Successfully saved as " + filePath);
            } catch (IOException e) {
                System.out.println("Error saving file: " + e.getMessage());
                return;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid file path.");
        }
    }
}