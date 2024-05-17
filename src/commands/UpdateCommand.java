//TODO add null support and type checking, fix space on new values
package commands;

import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateCommand implements Command {
    private CommandHandler commandHandler;

    public UpdateCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 6) {
            System.out.println("Invalid number of arguments. See 'help' for more information.");
            return;
        }

        String tableName = args[1];
        int searchColumn = Integer.parseInt(args[2]) -1;
        String searchValue = args[3];
        int targetColumn = Integer.parseInt(args[4]) -1;
        String targetValue = args[5].equals("NULL") ? null : args[5];

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();
        List<String> updatedRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
            String firstRow = br.readLine();


            if (firstRow == null) {
                System.out.println("Table " + tableName + " is empty.");
                return;
            }

            updatedRows.add(firstRow); // Add the first row without making any changes
            String[] columnNames = firstRow.split(",");
            if (searchColumn < 0 || searchColumn >= columnNames.length) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnNames.length + " columns.");
                return;
            }
            System.out.println("Search column: " + columnNames[searchColumn]);
            System.out.println("Search value: " + searchValue);
            if (targetColumn < 0 || targetColumn >= columnNames.length) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnNames.length + " columns.");
                return;
            }
            System.out.println("Target column: " + columnNames[targetColumn]);
            System.out.println("Target value: " + targetValue);

            String row;
            while ((row = br.readLine()) != null) {
                String[] values = row.trim().split(",");
                if (values[searchColumn].trim().equals(searchValue)) {
                    if ("NULL".equals(targetValue)) {
                        values[targetColumn] = "";
                    } else {
                        values[targetColumn] = targetValue;
                    }
                    System.out.println(Arrays.toString(values));
                    // Print the updated row
                    //System.out.println("Updated row: " + String.join(",", values));
                }
                updatedRows.add(String.join(",", values));
                //updatedRows.add(String.join(",", values));
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileHandler.getTableFilename()))) {
            for (String updatedRow : updatedRows) {
                pw.println(updatedRow);
            }
        }
        catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
//        try (PrintWriter pw = new PrintWriter(new FileWriter(fileHandler.getTableFilename()))) {
//            for (String updatedRow : updatedRows) {
//                pw.println(updatedRow);
//            }
//        } catch (IOException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
    }
}
