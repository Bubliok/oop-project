//TODO fix the select command
//TODO maybe remove colunm number to column name parsing
package commands;

import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SelectCommand implements Command {
    private CommandHandler commandHandler;
    private static final int printedRows = 8;
    private List<String> pages;
    private int currentPage;

    public SelectCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.pages = new ArrayList<>();
        this.currentPage = 0;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 4) {
            System.out.println("Invalid number of arguments. See 'help' for more information.");
            return;
        }
//        int value = Integer.parseInt(args[2]);
        String value = args[2];
        String tableName = args[3];

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        try (BufferedReader br = new BufferedReader(new FileReader(tableFilePath))) {
            String firstRow = br.readLine();

            if (firstRow == null) {
                System.out.println("Table " + tableName + " is empty.");
                return;
            }
            String[] columnNames = firstRow.split(",");
            int columnNumber = Integer.parseInt(args[1]) - 1;
            if (columnNumber < 0 || columnNumber >= columnNames.length) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnNames.length + " columns.");
                return;
            }
            System.out.println("Selected column: " + columnNames[columnNumber]);
            System.out.println("Selected value: " + value);
            // br.readLine();

            String row;
            StringBuilder page = new StringBuilder();
            int rowCount = 0;
            while ((row = br.readLine()) != null) {
                String[] values = row.trim().split(",");
                if (values[columnNumber].trim().equals(value)) {
                    page.append(row).append("\n");
                    rowCount++;
                    if (rowCount == printedRows) {
                        pages.add(page.toString());
                        page = new StringBuilder();
                        rowCount = 0;
                    }
                    //System.out.println(values[columnNumber].trim());
                }
            }
            if (rowCount > 0) {
                pages.add(page.toString());
            }
            if (pages.isEmpty()) {
                System.out.println("No matching rows found.");
                return;
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
            System.out.println("Exited select dialog.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid column number.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void nextPage(){
        if (currentPage < pages.size() - 1){
            currentPage++;
            printCurrentPage();
        } else {
            System.out.println("No other pages.");
        }
    }
    public void previousPage(){
        if (currentPage > 0){
            currentPage--;
            printCurrentPage();
        } else {
            System.out.println("This is the first page.");
        }
    }
    public void printCurrentPage(){
            System.out.println(pages.get(currentPage));
    }
}
