//TODO data type checks for this type validator needs to be implemented to read the strings form the file and interpret it as the correct type
package commands.tables;

import commands.Command;
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
        String targetValue = args[5].equals("NULL") ? "" : args[5].trim();

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();
        List<String> updatedRows = new ArrayList<>();
        boolean flag = false;
        try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
            String firstRow = br.readLine();

            if (firstRow == null) {
                System.out.println("Table " + tableName + " is empty.");
                return;
            }
            updatedRows.add(firstRow);

            String[] columnNames = firstRow.split(",");
            if (searchColumn < 0 || searchColumn >= columnNames.length) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnNames.length + " columns.");
                return;
            }
            if (targetColumn < 0 || targetColumn >= columnNames.length) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnNames.length + " columns.");
                return;
            }

            System.out.println("Search column: " + columnNames[searchColumn]);
            System.out.println("Search value: " + searchValue);
            System.out.println("Target column: " + columnNames[targetColumn]);
            System.out.println("Target value: " + targetValue);

            String row;
            while ((row = br.readLine()) != null) {
//                String[] values = row.trim().split(",");
                String[] values = row.split("\\s*,\\s*", -1);
                try {
                    if (values[searchColumn].trim().equals(searchValue)) {
                        values[targetColumn] = targetValue;
                        System.out.println(Arrays.toString(values));
                        flag = true;
                    }

                }catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                //System.out.println("Updated row: " + String.join(",", values));
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                updatedRows.add(String.join(", ", values));

            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        if(!flag){
            System.out.println("Search value not found.");
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileHandler.getTableFilename()))) {
            for (String updatedRow : updatedRows) {
                pw.println(updatedRow);
            }
        }
        catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
