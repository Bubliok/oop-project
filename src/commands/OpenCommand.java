package commands;
import handlers.CommandHandler;
import models.Table;

import java.io.*;
import java.util.Arrays;

public class OpenCommand implements Command {
    private CommandHandler commandHandler;

    public OpenCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid command. Please provide a file path.");
            return;
        }

        String filePath = args[1];
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tableName = reader.readLine();
            Table table = new Table(tableName);
            String columnLine = reader.readLine();
            if (columnLine != null) {
                String[] columns = columnLine.split(",");
                for (String column : columns) {
                    String[] parts = column.split(" ");
                    if (parts.length == 2) {
                        table.addColumn(parts[0], parts[1]);
                    }
                }
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                table.addRow(Arrays.asList(values));
            }
            commandHandler.setCurrentTable(table);
            commandHandler.setCurrentFile(file);
            System.out.println("Successfully opened " + filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}