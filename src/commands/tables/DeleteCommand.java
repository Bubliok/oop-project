package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements Command {
    private CommandHandler commandHandler;

    public DeleteCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 4) {
            System.out.println("Invalid arguments.");
            return;
        }

        String tableName = args[1];
        int searchColumn = Integer.parseInt(args[2]) - 1;
        String searchValue = args[3];

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        List<String> rowsToKeep = new ArrayList<>();
        System.out.println("Search column: " + searchColumn);
        System.out.println("Search value: " + searchValue);
        try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
            String row;
            while ((row = br.readLine()) != null) {
                String[] values = row.split(",");
                if (searchColumn < 0 || searchColumn >= values.length) {
                    System.out.println("Invalid column number. The table " + tableName + " has " + values.length + " columns.");
                    return;
                }
                if (!values[searchColumn].trim().equals(searchValue)) {
                    rowsToKeep.add(row);
                } else {
                    System.out.println("Deleted: " + row);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(tableFilePath))) {
            for (String row : rowsToKeep) {
                pw.println(row);
            }
            System.out.println("Successfully deleted rows from table " + tableName);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}