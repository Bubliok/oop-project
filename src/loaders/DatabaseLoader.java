package loaders;

import handlers.TableFileHandlerImpl;
import models.Database;
import models.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class DatabaseLoader {

    public void loadFromFile(Database database, String filePath) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filePath));
            doc.getDocumentElement().normalize();

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
                    database.addTable(table);
                }
            }
        } catch (Exception e) {
            throw new IOException("Error when loading database: " + e.getMessage());
        }
    }
}
