package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;

public class ImportCommand implements Command {
    private CommandHandler commandHandler;

    public ImportCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments.");
            return;
        }

        String filePath = args[1];
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        String fileName = file.getName();
        String tableName = fileName.substring(0, fileName.lastIndexOf('.'));

        if (commandHandler.getDatabase().getTable(tableName) != null) {
            System.out.println("Table " + tableName + " already exists.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null) {
                System.out.println("Error: The file is empty.");
                return;
            }
            TableFileHandlerImpl fileHandler = new TableFileHandlerImpl(tableName);
            Table newTable = new Table(tableName, fileHandler);
            Arrays.stream(line.split(", ")).forEach(column -> {
                String[] parts = column.split(" ");
                if (parts.length == 2) {
                    newTable.addColumn(parts[0], parts[1]);
                }
            });
            commandHandler.getDatabase().addTable(newTable);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(commandHandler.getCurrentFile());

            Element tableElement = doc.createElement("table");
            doc.getDocumentElement().appendChild(tableElement);

            Element nameElement = doc.createElement("name");
            nameElement.appendChild(doc.createTextNode(newTable.getTableName()));
            tableElement.appendChild(nameElement);

            Element pathElement = doc.createElement("path");
            pathElement.appendChild(doc.createTextNode(file.getAbsolutePath()));
            tableElement.appendChild(pathElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(commandHandler.getCurrentFile());
            transformer.transform(source, result);

            System.out.println("Successfully added " + tableName);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}