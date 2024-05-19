package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

        this.pages = new ArrayList<>();//resetvane na stranicite
        this.currentPage = 0;

        int columnNumber;
        try {
            columnNumber = Integer.parseInt(args[1]) -1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid column number.");
            return;
        }

        String value = args[2];
        String tableName = args[3];

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(tableFilePath);
            doc.getDocumentElement().normalize();

            NodeList columnList = doc.getElementsByTagName("column");
            if (columnNumber < 0 || columnNumber >= columnList.getLength()) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnList.getLength() + " columns.");
                return;
            }
            //String columnName = columnList.item(columnNumber).getTextContent();
            String columnTagName = columnList.item(columnNumber).getAttributes().getNamedItem("name").getNodeValue();
            //tursene po ime na kolonata
            NodeList rowList = doc.getElementsByTagName("row");

            System.out.println("Selected column: " + columnTagName);
            System.out.println("Selected value: " + value);

            StringBuilder page = new StringBuilder();
            int rowCount = 0;
            for (int i = 0; i < rowList.getLength(); i++) {
                Node rowNode = rowList.item(i);
                if (rowNode.getNodeType() == Node.ELEMENT_NODE) { //pr dali e element
                    Element rowElement = (Element) rowNode;
                    NodeList columnNodes = rowElement.getElementsByTagName(columnTagName);//kolonite v reda
                    if (columnNodes.getLength() > 0) {
                        Node columnNode = columnNodes.item(0);
                        if (columnNode != null && columnNode.getTextContent().trim().equals(value)) {
                            NodeList allColumns = rowElement.getChildNodes();
                            for (int j = 0; j < allColumns.getLength(); j++) {
                                Node colNode = allColumns.item(j);
                                if (colNode != null && colNode.getNodeType() == Node.ELEMENT_NODE) {
                                    page.append(colNode.getTextContent());

                                    if (j < rowList.getLength() - 1) {
                                        page.append(", ");
                                    }
                                   // System.out.println(colNode.getTextContent());
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
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Error: " + e.getMessage());
        }

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
        System.out.println("Exited select dialog.");
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

    public void printCurrentPage() {
        System.out.println(pages.get(currentPage));
    }
}
