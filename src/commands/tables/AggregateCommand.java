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

public class AggregateCommand implements Command {
    private CommandHandler commandHandler;

    public AggregateCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 6) {
            System.out.println("Invalid arguments.");
            return;
        }
        String tableName = args[1];
        int searchColumn;
        int targetColumn;
        try {
            searchColumn = Integer.parseInt(args[2]) - 1;
            targetColumn = Integer.parseInt(args[4]) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid column number. Column numbers should be integers.");
            return;
        }
        String operation = args[5];
        String searchValue = args[3];

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        System.out.println("Search column: " + args[2]);
        System.out.println("Target column: " + args[4]);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tableFilePath);
            doc.getDocumentElement().normalize();

            NodeList columnList = doc.getElementsByTagName("column");
            if (searchColumn < 0 || searchColumn >= columnList.getLength() || targetColumn < 0 || targetColumn >= columnList.getLength()) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnList.getLength() + " columns.");
                return;
            }
            String searchColumnName = columnList.item(searchColumn).getAttributes().getNamedItem("name").getNodeValue();
            String targetColumnName = columnList.item(targetColumn).getAttributes().getNamedItem("name").getNodeValue();

            NodeList rows = doc.getElementsByTagName("row");
            List<Double> results = new ArrayList<>();
            // double result = 0;
            //double prodResult = 1;
            for (int i = 0; i < rows.getLength(); i++) {
                Node rowNode = rows.item(i);
                if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element rowElement = (Element) rowNode;
                    NodeList searchColumnNodes = rowElement.getElementsByTagName(searchColumnName);//vzima vs elementi ot searchColumn
                    if (searchColumnNodes.getLength() > 0) { //pr dali ima elemetni v kolonata
                        Node searchColumnNode = searchColumnNodes.item(0);
                        if (searchColumnNode != null && searchColumnNode.getTextContent().trim().equals(searchValue)) {
                            try {
                                double targetValue = Double.parseDouble(rowElement.getElementsByTagName(targetColumnName).item(0).getTextContent());
                                results.add(targetValue);
                                System.out.println("Target value: " + targetValue);
                            } catch (NumberFormatException e) {
                                System.out.println("Target value is not numeric.");
                                return;
                            }
                        }
                    }
                }
            }
            double result = 0;
            switch (operation) {
                case "sum":
                    for (double res : results) {
                        result += res;
                    }
                    break;
                case "product":
                    result = 1;
                    for (double res : results) {
                        result *= res;
                    }
                    break;
                case "maximum":
                    for (double res : results) {
                        result = Math.max(result, res);
                    }
                    break;
                case "minimum":
                    result = Double.POSITIVE_INFINITY;
                    for (double res : results) {
                        result = Math.min(result, res);
                    }
                    break;
                default:
                    System.out.println("Invalid operation. The operation should be one of the following: sum, product, maximum, minimum.");
                    return;
            }

                 System.out.println("The result of the " + operation + " operation is: " + result);

//            if ((operation.equals("minimum") && result == Double.MAX_VALUE) || (operation.equals("maximum") && result == Double.MIN_VALUE)) {
//                System.out.println("No matching rows found for the given search criteria.");
//            } else {
//                System.out.println("The result of the " + operation + " operation is: " + result);
//            }
        } catch(ParserConfigurationException | IOException | SAXException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
