package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Row;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InsertCommand implements Command {
    private CommandHandler commandHandler;

    public InsertCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments.");
            return;
        }

        String tableName = args[1];
        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(tableFilePath));

            Node TableNode = doc.getElementsByTagName("table").item(0);

            NodeList columnList = ((Element) TableNode).getElementsByTagName("column");
            List<String> columnNames = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();
            for (int i = 0; i < columnList.getLength(); i++) {
                Node columnNode = columnList.item(i);
                if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element columnElement = (Element) columnNode;
                    columnNames.add(columnElement.getAttribute("name"));
                    columnTypes.add(columnElement.getAttribute("type"));
                }
            }
            Element rowElement = doc.createElement("row");
            for (int i = 2; i < args.length; i++) {
                String columnType = columnTypes.get(i - 2);
                String value = args[i];
                try {
                    if (columnType.equals("int")) {
                        Integer.parseInt(value);
                    } else if (columnType.equals("float")) {
                        Float.parseFloat(value);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data type for column " + columnNames.get(i - 2));
                    return;
                }
                Element columnElement = doc.createElement(columnNames.get(i - 2));
                columnElement.appendChild(doc.createTextNode(value));
                rowElement.appendChild(columnElement);
                //rowElement.appendChild(doc.createTextNode("\n"));
            }

            Node rowsNode = ((Element) TableNode).getElementsByTagName("rows").item(0);
            rowsNode.appendChild(rowElement);
            rowsNode.appendChild(doc.createTextNode("\n"));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(tableFilePath));
            transformer.transform(source, result);

            System.out.println("Successfully inserted row into table " + tableName);
        } catch (Exception e) {
            System.out.println("Error inserting row: " + e.getMessage());
        }
    }
}