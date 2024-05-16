package commands;

import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DescribeCommand implements Command {
    private CommandHandler commandHandler;

    public DescribeCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
public void execute(String[] args) {
    if (args.length != 2) {
        System.out.println("Invalid argument.");
        return;
    }

    String tableName = args[1];
    Table table = commandHandler.getDatabase().getTable(tableName);

    if (table == null) {
        System.out.println("Table " + tableName + " does not exist.");
        return;
    }

    TableFileHandlerImpl fileHandler = table.getFileHandler();
    String tableFilePath = fileHandler.getTableFilename();

    try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
        String line;
        if ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            System.out.println("Column Name\tData Type");
            for (String part : parts) {
                String[] nameAndType = part.trim().split(" ",2);
                if (nameAndType.length == 2) {
                    System.out.println(nameAndType[0] + "\t" + nameAndType[1]);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading file for table " + tableName);
    }
}
}