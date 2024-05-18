package models;

import handlers.TableFileHandlerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class Database {
    private Map<String, Table> tables = new HashMap<>();
    private String databaseName;
    private String filePath;

    public Database(){};
    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new HashMap<>();
    }

public void loadFromFile(String filePath) throws IOException {
    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(filePath));

        NodeList tableNodes = doc.getElementsByTagName("table");
        for (int i = 0; i < tableNodes.getLength(); i++) {
            Node tableNode = tableNodes.item(i);
            if (tableNode.getNodeType() == Node.ELEMENT_NODE) {
                Element tableElement = (Element) tableNode;
                String tableName = tableElement.getElementsByTagName("name").item(0).getTextContent();
                String tablePath = tableElement.getElementsByTagName("path").item(0).getTextContent();

                Table table = new Table (tableName);
                TableFileHandlerImpl fileHandler = new TableFileHandlerImpl(tableName);
                table.setFileHandler(fileHandler);
                this.addTable(table);
            }
        }
    } catch (Exception e) {
        throw new IOException("Error when loading database: " + e.getMessage());
    }
}
    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Table> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                String tableFilePath = entry.getValue().getTableFilename();
                writer.write(tableName + "," + tableFilePath);
                writer.newLine();
            }
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Collection<Table> getTables() {
        return tables.values();
    }
    public void addTable(Table table) {
        tables.put(table.getTableName(), table);
    }
    public Table getTable(String tableName) {
        return tables.get(tableName);
    }
}
