package commands.databases;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class SaveCommand implements Command {
    private CommandHandler commandHandler;

    public SaveCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        File currentFile = commandHandler.getCurrentFile();
        if (currentFile != null) {
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
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(currentFile);
                transformer.transform(source, result);

                System.out.println("Successfully saved " + currentFile.getName());
            } catch (ParserConfigurationException | TransformerException e) {
                System.out.println("Error saving file: " + e.getMessage());
            }
        }
    }
}
