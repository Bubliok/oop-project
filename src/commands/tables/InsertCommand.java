package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Row;
import models.Table;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsertCommand implements Command {
    private CommandHandler commandHandler;

    public InsertCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments.");
            return;
        }

        String tableName = args[1];
        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
            String firstRow = br.readLine();
            String[] columnNames = firstRow.split(",");

            if (args.length - 2 != columnNames.length) {
                System.out.println("Invalid number of values. Expected " + columnNames.length + " but got " + (args.length - 2));
                return;
            }

            List<Object> values = new ArrayList<>();
            for (int i = 2; i < 5; i++) {
                values.add(args[i]);
            }

            try {
                table.addRow(values);
                System.out.println("Successfully inserted row into table " + tableName);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid number of values: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error inserting row: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file for table " + tableName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
