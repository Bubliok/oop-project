//TODO do add column tests to make sure it works correctly
package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import models.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddColumnCommand implements Command {
    private CommandHandler commandHandler;

    public AddColumnCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    @Override
    public void execute(String[] args) {
        if (args.length != 4){
            System.out.println("Invalid number of arguments. See 'help' for more information.");
            return;
        }
        String tableName = args[1];
        String columnName = args[2];
        String columnType = args[3];
        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        table.getColumnName().add(columnName);
        table.getDataType().add(columnType);

        String tableFilePath = table.getFileHandler().getTableFilename();
        List<String> updatedRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
            String firstRow = br.readLine();
            if (firstRow == null){
                updatedRows.add(columnName + " " + columnType);
            } else {
                updatedRows.add(firstRow + ", " + columnName + " " + columnType);
            }
            String row;
            while ((row = br.readLine()) != null) {
                updatedRows.add(row + ",");
            }
        } catch (IOException e) {
            System.out.println("Error reading file for table " + tableName);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(tableFilePath))) {
            for (String updatedRow : updatedRows) {
                pw.println(updatedRow);
            }
        } catch (IOException e) {
            System.out.println("Error writing to file for table " + tableName);
        }

        System.out.println("Successfully added column " + columnName + " of type " + columnType + " to table " + tableName);
    }
}
