package commands.databases;
import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandler;
import handlers.TableFileHandlerImpl;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsCommand implements Command {
    private CommandHandler commandHandler;

    public SaveAsCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid file path.");
            return;
        }

        String filePath = args[1];
        File file = new File(filePath);
        File directory = file.getParentFile();

        if (file.isDirectory()) {
            System.out.println("No file name provided.");
            return;
        }

        if (file.exists()) {
            System.out.println("File already exists.");
            return;
        }

        if (directory == null) {
            System.out.println("Invalid file path.");
            return;
        }

//            if (!directory.exists()) {
//                directory.mkdirs();
//            }

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element databaseElement = doc.createElement("database");
            //databaseElement.setAttribute("name", commandHandler.getDatabase().getDatabaseName());
            doc.appendChild(databaseElement);

            for (Table table : commandHandler.getDatabase().getTables()) {
                Element tableElement = doc.createElement("table");

                String tableName = table.getTableName();
                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(tableName));
                tableElement.appendChild(nameElement);

                Element pathElement = doc.createElement("path");
                pathElement.appendChild(doc.createTextNode(table.getFileHandler().getFilePath()));
                tableElement.appendChild(pathElement);
                // tableElement.appendChild(tableElement);
                databaseElement.appendChild(tableElement);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);

            }
            System.out.println("Successfully saved as " + filePath);

        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}





