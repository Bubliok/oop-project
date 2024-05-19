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

public class CountCommand implements Command {
    private CommandHandler commandHandler;

    public CountCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 4) {
            System.out.println("Invalid arguments.");
            return;
        }

        String tableName = args[1];
        int searchColumn;
        String searchValue = args[3];

        try {
            searchColumn = Integer.parseInt(args[2]) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid column number. Column numbers should be integers.");
            return;
        }

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        System.out.println("Search column: " + args[2]);
        System.out.println("Search value: " + searchValue);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tableFilePath);
            doc.getDocumentElement().normalize();

            NodeList columnList = doc.getElementsByTagName("column");
            if (searchColumn < 0 || searchColumn >= columnList.getLength()) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnList.getLength() + " columns.");
                return;
            }
            String columnTagName = columnList.item(searchColumn).getAttributes().getNamedItem("name").getNodeValue();
            //System.out.println("Searching for rows with column " + columnTagName + " = " + searchValue);

            NodeList rowList = doc.getElementsByTagName("row");
            int rowCount = 0;
            for (int i = 0; i < rowList.getLength(); i++) {
                Node rowNode = rowList.item(i);
                if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element rowElement = (Element) rowNode;
                    NodeList columnNodes = rowElement.getElementsByTagName(columnTagName);
                    if (columnNodes.getLength() > 0) {
                        Node columnNode = columnNodes.item(0);
//                        System.out.println("Found row with " + columnTagName + " = " + columnNode.getTextContent().trim());
                        if (columnNode.getTextContent().trim().equals(searchValue)) {
//                            System.out.println(" row for deletion: " + rowElement.getTextContent());
                            rowCount++;
                        }
                    }
                }
            }
            System.out.println("Number of rows with " + columnTagName + " = " + searchValue + ": " + rowCount);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
