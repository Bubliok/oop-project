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
import java.util.ArrayList;
import java.util.List;

public class InnerJoinCommand implements Command {
    private CommandHandler commandHandler;

    public InnerJoinCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 5) {
            System.out.println("Invalid arguments.");
            return;
        }
        String table1Name = args[1];
        int searchColumn1;
        String table2Name = args[3];
        int searchColumn2;
        try {
            searchColumn1 = Integer.parseInt(args[2]) - 1;
            searchColumn2 = Integer.parseInt(args[4]) - 1;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid column number. Column numbers should be integers.");
            return;
        }

        Table table1 = commandHandler.getDatabase().getTable(table1Name);
        Table table2 = commandHandler.getDatabase().getTable(table2Name);

        if (table1 == null || table2 == null) {
            System.out.println("One or both tables not found.");
            return;
        }
        System.out.println("Table 1: " + table1Name);
        System.out.println("Table 2: " + table2Name);

        TableFileHandlerImpl fileHandler1 = table1.getFileHandler();
        String table1FilePath = fileHandler1.getTableFilename();
        TableFileHandlerImpl fileHandler2 = table2.getFileHandler();
        String table2FilePath = fileHandler2.getTableFilename();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc1 = dBuilder.parse(table1FilePath);
            Document doc2 = dBuilder.parse(table2FilePath);
            doc1.getDocumentElement().normalize();
            doc2.getDocumentElement().normalize();


            dBuilder = dbFactory.newDocumentBuilder();

            assert dBuilder != null;
            Document newDoc = dBuilder.newDocument();
            Element rootElement = newDoc.createElement("table");
            newDoc.appendChild(rootElement);

            Element columnsElement = newDoc.createElement("columns");
            rootElement.appendChild(columnsElement);

            Element rowsElement = newDoc.createElement("rows");
            rootElement.appendChild(rowsElement);

            NodeList columnList1 = doc1.getElementsByTagName("column");
            NodeList columnList2 = doc2.getElementsByTagName("column");

            if (searchColumn1 < 0 || searchColumn1 >= columnList1.getLength() || searchColumn2 < 0 || searchColumn2 >= columnList2.getLength()) {
                System.out.println("Invalid column number.");
                return;
            }

            String columnTagName1 = columnList1.item(searchColumn1).getAttributes().getNamedItem("name").getNodeValue();
            String columnTagName2 = columnList2.item(searchColumn2).getAttributes().getNamedItem("name").getNodeValue();

            NodeList rowList1 = doc1.getElementsByTagName("row");
            NodeList rowList2 = doc2.getElementsByTagName("row");

//            for (int i = 0; i < columnList1.getLength(); i++) {
//                Node columnNode1 = columnList1.item(i);
//                if (columnNode1.getNodeType() == Node.ELEMENT_NODE) {
//                    Element columnElement1 = (Element) columnNode1;
//                    String columnName = columnElement1.getAttributes().getNamedItem("name").getNodeValue();
//                    String columnType = columnElement1.getAttributes().getNamedItem("type").getNodeValue();
//
//                    Element columnElement = newDoc.createElement("column");
//                    columnElement.setAttribute("name", columnName);
//                    columnElement.setAttribute("type", columnType);
//                    columnsElement.appendChild(columnElement);
//                }
//            }
            for (int i = 0; i < columnList1.getLength(); i++) {
                Node columnNode = columnList1.item(i);
                if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element columnElement = (Element) columnNode;
                    String columnName = columnElement.getAttribute("name");
                    String columnType = columnElement.getAttribute("type");
                    Element newColumnElement = newDoc.createElement("column");
                    newColumnElement.setAttribute("name", columnName);
                    newColumnElement.setAttribute("type", columnType);
                    columnsElement.appendChild(newColumnElement);
                }
            }

            for (int i = 0; i < columnList2.getLength(); i++) {
                if (i != searchColumn2) {
                    Node columnNode = columnList2.item(i);
                    if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element columnElement = (Element) columnNode;
                        String columnName = columnElement.getAttribute("name");
                        String columnType = columnElement.getAttribute("type");
                        Element newColumnElement = newDoc.createElement("column");
                        newColumnElement.setAttribute("name", columnName);
                        newColumnElement.setAttribute("type", columnType);
                        columnsElement.appendChild(newColumnElement);
                    }
                }
            }

            for (int i = 0; i < rowList1.getLength(); i++) {
                Node rowNode1 = rowList1.item(i);
                if (rowNode1.getNodeType() == Node.ELEMENT_NODE) {
                    Element rowElement1 = (Element) rowNode1;
                    NodeList columnNodes1 = rowElement1.getElementsByTagName(columnTagName1);
                    if (columnNodes1.getLength() > 0) {
                        String value1 = columnNodes1.item(0).getTextContent();

                        List<Element> matchingRows = new ArrayList<>();
                        for (int j = 0; j < rowList2.getLength(); j++) {
                            Node rowNode2 = rowList2.item(j);
                            if (rowNode2.getNodeType() == Node.ELEMENT_NODE) {
                                Element rowElement2 = (Element) rowNode2;
                                NodeList columnNodes2 = rowElement2.getElementsByTagName(columnTagName2);
                                if (columnNodes2.getLength() > 0) {
                                    String value2 = columnNodes2.item(0).getTextContent();
                                    if (value1.equals(value2)) {
                                        matchingRows.add(rowElement2);
                                    }
                                }
                            }
                        }

                        for (Element matchingRow : matchingRows) {
                            Element newRowElement = newDoc.createElement("row");
                            rowsElement.appendChild(newRowElement);

                            for (int k = 0; k < columnList1.getLength(); k++) {
                                Node columnNode = columnList1.item(k);
                                if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element columnElement = (Element) columnNode;
                                    String columnName = columnElement.getAttribute("name");
                                    Element newColumnElement = newDoc.createElement(columnName);
                                    newColumnElement.setTextContent(rowElement1.getElementsByTagName(columnName).item(0).getTextContent());
                                    newRowElement.appendChild(newColumnElement);
                                }
                            }

                            for (int k = 0; k < columnList2.getLength(); k++) {
                                if (k != searchColumn2) {
                                    Node columnNode = columnList2.item(k);
                                    if (columnNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element columnElement = (Element) columnNode;
                                        String columnName = columnElement.getAttribute("name");
                                        Element newColumnElement = newDoc.createElement(columnName);
                                        newColumnElement.setTextContent(matchingRow.getElementsByTagName(columnName).item(0).getTextContent());
                                        newRowElement.appendChild(newColumnElement);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(new File("innerjoin.xml"));
            transformer.transform(source, result);

            System.out.println("Inner join saved to innerjoin.xml.");

        } catch (IOException | SAXException | TransformerException | ParserConfigurationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

