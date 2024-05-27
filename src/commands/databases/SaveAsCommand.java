package commands.databases;

import commands.Command;
import handlers.DatabaseHandler;
import models.Database;
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
    private DatabaseHandler databaseHandler;

    public SaveAsCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments. Provide a valid file path.");
            return;
        }
        String filePath = args[1];
        File file = new File(filePath);
        Database database = databaseHandler.getDatabase();
        File directory = file.getParentFile();

        if (file.exists()){
            System.out.println("File already exists.");
            return;
        }

        if (file.isDirectory()){
            System.out.println("No file name provided.");
            return;
        }

        if (directory == null){
            System.out.println("Invalid file path.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            System.out.println(database);
            for (Table table : database.getTables()) {
                System.out.println(table);
                writer.write(table.getTableName() + ", " + table.getTablePath());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        System.out.println("Successfully saved as: " + filePath);
    }
}



