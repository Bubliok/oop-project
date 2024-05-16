package commands;

import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrintCommand implements Command {
    private CommandHandler commandHandler;
    private static final int printedRows = 8;
    private List<String> pages;
    private int currentPage;

    public PrintCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.pages = new ArrayList<>();
        this.currentPage = 0;
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
            String row;
            StringBuilder page = new StringBuilder();
            int rowCount = 0;
            while ((row = br.readLine()) != null) {
                page.append(row).append("\n");
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
        } catch (IOException e) {
            System.out.println("Error reading file for table " + tableName);
        }
        printCurrentPage();
        System.out.println("Type 'next', 'previous' to navigate pages or 'exit' to exit.");
        //next, previous, exit
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
