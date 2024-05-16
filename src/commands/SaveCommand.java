package commands;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import handlers.TableFileHandler;

public class SaveCommand implements Command {
    private CommandHandler commandHandler;

    public SaveCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }


    @Override
    public void execute(String[] args) {
        File currentFile = commandHandler.getCurrentFile();
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                for (Table table : commandHandler.getDatabase().getTables()) {
                    String tableName = table.getTableName();
                    TableFileHandler fileHandler = new TableFileHandlerImpl(tableName);
                    String filePath = fileHandler.getFilePath();
                    writer.write(filePath + "," + table.getTableName());
                    writer.newLine();
                }
                System.out.println("Successfully saved " + currentFile.getName());
            } catch (IOException e) {
                System.out.println("Error saving file: " + e.getMessage());
                return;
            }
        }
    }
}

