package commands.tables;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DescribeCommand implements Command {
    private CommandHandler commandHandler;

    public DescribeCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
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

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(tableFilePath);

            NodeList columnNodes = doc.getElementsByTagName("column");

            System.out.println("Column Name\tData Type");
            for (int i = 0; i < columnNodes.getLength(); i++) {
                Element columnElement = (Element) columnNodes.item(i);
                String columnName = columnElement.getAttribute("name");
                String columnType = columnElement.getAttribute("type");
                System.out.println(columnName + "\t" + columnType);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Error" + e.getMessage());
        }

    }
}