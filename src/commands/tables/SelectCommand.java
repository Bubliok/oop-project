package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SelectCommand implements Command {
    private CommandHandler commandHandler;
    private DatabaseHandler databaseHandler;
    private List<String> pages;
    private static final int printedRows = 5;
    int currentPage;

    public SelectCommand(CommandHandler commandHandler, DatabaseHandler databaseHandler) {
        this.commandHandler = commandHandler;
        this.databaseHandler = databaseHandler;
        this.pages = new ArrayList<>();
        this.currentPage = 0;
    }

    @Override
    public void execute(String[] args) {
        if(args.length < 4) {
            System.out.println("Invalid arguments. Type 'help' for more command info.");
            return;
        }

        int columnNumber = Integer.parseInt(args[1]) -1;
        String value = args[2];
        String tableName = args[3];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);
        pages = new ArrayList<>();
        currentPage = 0;//reset

        if (table == null){
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        //System.out.println(table);

        List<Column> columns = table.getColumns();
        if(columnNumber < 0 || columnNumber >= columns.size()){
            System.out.println("Invalid column number.");
            return;
        }

        List<Row> rows = table.getRows();
        StringBuilder page = new StringBuilder();
        for (Column column : table.getColumns()){
            //System.out.println("Column name: "+column.getColumnName());
            page.append(column.getColumnName()).append("   ");
        }
        page.append("\n");
        int rowCount = 0;
        for (Row row : rows) {
            Object columnValue = row.getValues().get(columnNumber);
            //System.out.println("Column value: "+columnValue);
            if (columnValue != null && columnValue.toString().equals(value)){
                //System.out.println(row.getValues());
                page.append(row.getValues()).append("\n");
                rowCount++;
            }

            if (rowCount == printedRows) {
                pages.add(page.toString());
                page = new StringBuilder();
                rowCount = 0;
            }
        }
        if (rowCount > 0) {
            pages.add(page.toString());
        }

        if (printCurrentPage()){
            System.out.println("Type 'next', 'previous' to navigate pages or 'exit' to exit.");
        } else {
            return;
        }

        Scanner scanner = new Scanner(System.in);
        String pageCommand;
        while (!(pageCommand = scanner.nextLine()).equals("exit")) {
            if (pageCommand.equals("next")) {
                nextPage();
            } else if (pageCommand.equals("previous")) {
                previousPage();
            } else {
                System.out.println("Invalid command. Use 'next', 'previous' or 'exit'.");
            }
        }
        System.out.println("Exited print dialog.");
    }

    public void nextPage() {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            printCurrentPage();
        } else {
            System.out.println("No other pages.");
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            printCurrentPage();
        } else {
            System.out.println("This is the first page.");
        }
    }

    private boolean printCurrentPage() {
        if (!pages.isEmpty()) {
            System.out.println(pages.get(currentPage));
            return true;
        } else {
            System.out.println("No matching rows found.");
            return false;
        }
    }
}