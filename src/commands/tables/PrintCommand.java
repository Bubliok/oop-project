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

public class PrintCommand implements Command {
    private DatabaseHandler databaseHandler;
    private List<String> pages;
    private static final int printedRows = 5;
    int currentPage;

    public PrintCommand(DatabaseHandler databaseHandler) {;
        this.databaseHandler = databaseHandler;
        this.pages = new ArrayList<>();
        this.currentPage = 0;
    }

    @Override
    public void execute(String[] args) {
        if(args.length < 2) {
            System.out.println("Invalid arguments. Please provide a table name.");
            return;
        }
        pages = new ArrayList<>();
        currentPage = 0;//reset
        String tableName = args[1];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);

        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        List<Row> rows = table.getRows();
        StringBuilder page = new StringBuilder();
        for (Column column : table.getColumns()){
            page.append(column.getColumnName()).append("   ");
        }
        page.append("\n");
        int rowCount = 0;
        for (Row row : rows) {
            page.append(row.getValues()).append("\n");
            rowCount++;
            if (rowCount == printedRows) {
                pages.add(page.toString());
                page = new StringBuilder();
                rowCount = 0;
            }
        }
        if (rowCount > 0) {
            pages.add(page.toString());
        }

        printCurrentPage();
        System.out.println("Type 'next', 'previous' to navigate pages or 'exit' to exit.");

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

    private void printCurrentPage() {
        System.out.println(pages.get(currentPage));
    }
}