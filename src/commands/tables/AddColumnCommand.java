//TODO do add column tests to make sure it works correctly
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddColumnCommand implements Command {
    private CommandHandler commandHandler;

    public AddColumnCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    @Override
    public void execute(String[] args) {
        if (args.length != 4){
            System.out.println("Invalid number of arguments. See 'help' for more information.");
            return;
        }
        String tableName = args[1];
        String columnName = args[2];
        String columnType = args[3];

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        table.getColumnName().add(columnName);
        table.getDataType().add(columnType);

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();

       // List<String> updatedRows = new ArrayList<>();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(tableFilePath);

            NodeList columnsList = doc.getElementsByTagName("columns");
            if (columnsList.getLength() > 0) {
                Element columnsElement = (Element) columnsList.item(0);

                Element newColumn = doc.createElement("column");
                newColumn.setAttribute("name", columnName);
                newColumn.setAttribute("type", columnType);

                columnsElement.appendChild(newColumn);

            } else {
                System.out.println("Columns not found in table " + tableName);
                return;
            }
            //add new column in all rows
            NodeList rowList = doc.getElementsByTagName("row");
            for (int i = 0; i < rowList.getLength(); i++) {
                Element rowElement = (Element) rowList.item(i);

                Element newColumnValue = doc.createElement(columnName);
                newColumnValue.appendChild(doc.createTextNode(""));

                rowElement.appendChild(newColumnValue);
            }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(tableFilePath));
                transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            System.out.println("Error: " + e.getMessage());
        }

//        try (PrintWriter pw = new PrintWriter(new FileWriter(tableFilePath))) {
//            for (String updatedRow : updatedRows) {
//                pw.println(updatedRow);
//            }
//        } catch (IOException e) {
//            System.out.println("Error writing to file for table " + tableName);
//        }

        System.out.println("Successfully added column " + columnName + " of type " + columnType + " to table " + tableName);
    }
}
