package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
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

    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(tableFilePath));

        doc.getDocumentElement().normalize();

        NodeList columnList = doc.getElementsByTagName("column");
        NodeList rowList = doc.getElementsByTagName("row");

        StringBuilder page = new StringBuilder();
        int rowCount = 0;

        for (int i = 0; i < columnList.getLength(); i++) {
            Node columnNode = columnList.item(i);
            if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                Element columnElement = (Element) columnNode;
                page.append(columnElement.getAttribute("name")).append(" ");
            }
        }
        page.append("\n");

        for (int i = 0; i < rowList.getLength(); i++) {
            Node rowNode = rowList.item(i);
            if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                Element rowElement = (Element) rowNode;
                for (int j = 0; j < columnList.getLength(); j++) {
                    Node columnNode = columnList.item(j);
                    if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element columnElement = (Element) columnNode;
                        String columnName = columnElement.getAttribute("name");
                        String cellValue = rowElement.getElementsByTagName(columnName).item(0).getTextContent();
                        page.append(cellValue).append(" ");
                    }
                }
                page.append("\n");
                rowCount++;
                if (rowCount == printedRows) {
                    pages.add(page.toString());
                    page = new StringBuilder();
                    rowCount = 0;
                }
            }
        }
        if (rowCount > 0) {
            pages.add(page.toString());
        }
    } catch (Exception e) {
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
