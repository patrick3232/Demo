import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The class PageManager is used to create the pages the application.
 * It may also be extended to provide any other management of the pages.
 * 
 * Author: Patrick Macpherson
 * Version: 19 December 2022
 */
public class PageManager {
    // Class fields
    private static final String PAGES_XML_PATH = Main.CURRENT_WORKING_DIRECTORY
            + AppConfiguration.getProperty("pagesXMLFile");

    // Instance fields
    private Document document; // An XML DOM holding the data that makes up each page
    private HashMap<String, Page> pages; // Collection for holding the pages

    /**
     * Constructs a new PageManager object with null document and pages.
     */
    public PageManager() {
        document = null;
        pages = new HashMap<>();
    }

    /**
     * Creates all the pages of the application.
     */
    public HashMap<String, Page> createPages() {
        // Read and parse the pages
        ParseXML();

        // Get a list of all the pages
        NodeList pageNodes = document.getElementsByTagName("page");

        // Iterate over each page node
        for (int i = 0; i < pageNodes.getLength(); i++) {
            // Initialise data for each iteration
            Page page = null;
            String pageName = "";
            String pageOutput = "";
            ArrayList<String> pageChoices = new ArrayList<>();
            String pageType = "";

            Node pageNode = pageNodes.item(i); // Select a node from the list
            if (pageNode.getNodeType() == Node.ELEMENT_NODE) {
                Element pageElement = (Element) pageNode; // typecast the Node to an Element
                pageName = pageElement.getAttribute("name");

                // Get the output that the page will display
                NodeList output = pageElement.getElementsByTagName("output");
                Node outputNode = output.item(0); // There is one question per a page
                Element outputElement = (Element) outputNode;
                pageOutput = outputElement.getAttribute("value");

                // Get the choices that the page will offer
                NodeList choices = pageElement.getElementsByTagName("choice");
                for (int j = 0; j < choices.getLength(); j++) {
                    Node choiceNode = choices.item(j);
                    if (choiceNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element choiceElement = (Element) choiceNode;
                        String choice = choiceElement.getAttribute("value");
                        pageChoices.add(choice);
                    }
                }

                // Get the type of the page
                NodeList type = pageElement.getElementsByTagName("type");
                Node typeNode = type.item(0); // there is one input type per page
                Element TypeElement = (Element) typeNode;
                pageType = TypeElement.getAttribute("value");
            }

            // Create the page and add it into a list of pages
            switch (pageType) {
                case "choice":
                    page = new PageChoice(pageName, pageOutput, pageChoices);
                    break;
                case "filePath":
                    page = new PageFilePath(pageName, pageOutput);
                    break;
                case "info":
                    page = new PageInfo(pageName, pageOutput);
                    break;
                case "text":
                    page = new PageText(pageName, pageOutput);
                    break;
                default:
                    System.out.println("Error has occurred");
                    break;
            }
            pages.put(page.getName(), page);
        }
        return pages;
    }

    /**
     * A helper method that reads and parses the "Pages.xml" file and creates
     * an XML DOM.
     */
    private void ParseXML() {
        // Get the document builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newNSInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Get document
            document = builder.parse(PAGES_XML_PATH);

            // Normalize the xml structue, in essence cleans the data the parser pulled in
            // and ensures its of correct format
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method getNumberOfPagesFor will find the number of pages that are
     * associated with a given task.
     * 
     * @param aTask A String, representing the task that we would like to find the
     *              number of associated pages for.
     * @return An int, representing the number of pages associated with the given
     *         task.
     */
    public int getNumberOfPagesForTask(String aTask) {
        int count = 0;
        for (Page page : pages.values()) {
            if (page.getName().startsWith(aTask)) {
                count++;
            }
        }

        return count;
    }
}
