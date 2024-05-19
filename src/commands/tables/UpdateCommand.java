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
import validators.TypeValidator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class UpdateCommand implements Command {
    private CommandHandler commandHandler;

    public UpdateCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 6) {
            System.out.println("Invalid number of arguments. See 'help' for more information.");
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
        String searchValue = args[3].equals("NULL") ? "" : args[3].trim();
        String targetValue = args[5].equals("NULL") ? "" : args[5].trim();

        Table table = commandHandler.getDatabase().getTable(tableName);
        if (table == null) {
            System.out.println("Table not found.");
            return;
        }

        TableFileHandlerImpl fileHandler = table.getFileHandler();
        String tableFilePath = fileHandler.getTableFilename();


        boolean flag = false;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tableFilePath);

            NodeList columnList = doc.getElementsByTagName("column");
            if (searchColumn < 0 || searchColumn >= columnList.getLength() || targetColumn < 0 || targetColumn >= columnList.getLength()) {
                System.out.println("Invalid column number. The table " + tableName + " has " + columnList.getLength() + " columns.");
                return;
            }
            String searchColumnName = columnList.item(searchColumn).getAttributes().getNamedItem("name").getNodeValue();
            String targetColumnName = columnList.item(targetColumn).getAttributes().getNamedItem("name").getNodeValue();
            String targetColumnType = columnList.item(targetColumn).getAttributes().getNamedItem("type").getNodeValue();

            TypeValidator typeValidator = new TypeValidator();
            if (!"".equals(targetValue)) {
                if (!typeValidator.isValueOfType(targetValue, targetColumnType)) {
                    System.out.println("Invalid target value. The column " + targetColumnName + " expects a " + targetColumnType + ".");
                    return;
                }
            }

            System.out.println("Search column: " + searchColumnName);
            System.out.println("Search value: " + args[3].trim());
            System.out.println("Target column: " + targetColumnName);
            System.out.println("Target value: " +  args[5].trim());

//            StringBuilder updatedRow = new StringBuilder();
//            for (int i = 0; i < columnList.getLength(); i++) {
//                Node columnNode = columnList.item(i);
//                if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element columnElement = (Element) columnNode;
//                    updatedRow.append(columnElement.getAttribute("name")).append(" ");
//                } // printira imenata na kolonite
//            }
            //updatedRow.append("\n");
            NodeList rowList = doc.getElementsByTagName("row");
            for (int i = 0; i < rowList.getLength(); i++) {
                Node rowNode = rowList.item(i);
                if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element rowElement = (Element) rowNode;
                    NodeList searchColumnNodes = rowElement.getElementsByTagName(searchColumnName);//vzima vs elementi ot searchColumn
                    if (searchColumnNodes.getLength() > 0) { //pr dali ima elemetni v kolonata
                        Node searchColumnNode = searchColumnNodes.item(0);
                        if (searchColumnNode != null && searchColumnNode.getTextContent().trim().equals(searchValue)) {
                            //flag = true;
                            //NodeList allColumns = rowElement.getChildNodes();
                            //if (allColumns.getLength() > 0) {
                            NodeList targetColumnNodes = rowElement.getElementsByTagName(targetColumnName);//vzima vs elementi ot targetColumn
                            if (targetColumnNodes.getLength() > 0) { // pr dali ima elementi v kolonata
                                Node targetColumnNode = targetColumnNodes.item(0);
                                if (targetColumnNode != null) {
                                    targetColumnNode.setTextContent(targetValue);
                                    flag = true;

                                   // updatedRow.append(targetColumnNodes.getTextContent()).append(", ");
                                }
                            }
                        }
                    }
                }
            }

            if(!flag){
                System.out.println("\nSearch value not found.");
                return;
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(tableFilePath);
            transformer.transform(source, result);

            System.out.println("\nSuccessfully updated table rows.");

        }
            catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
                System.out.println("Error: " + e.getMessage());
        }
//    } catch (IOException e) {
//            System.out.println("Error: " + e.getMessage());
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException(e);
//        } catch (SAXException e) {
//            throw new RuntimeException(e);
//        }
//        if(!flag){
//            System.out.println("Search value not found.");
//        }
//        try (PrintWriter pw = new PrintWriter(new FileWriter(fileHandler.getTableFilename()))) {
//            for (String updatedRow : updatedRows) {
//                pw.println(updatedRow);
//            }
//        }

    }
}
