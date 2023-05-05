import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The class ModuleManager maintains knowledge of all modules and is used to
 * create, save, load and perform module queries that a module itself cannot
 * provide.
 * 
 * @Author Patrick Macpherson
 * @Version 27 December 2022
 */
public class ModuleManager {

    // instance fields
    private HashMap<String, Module> modules; // A list of all created modules
    private File userDataDirectory; // A directory to store user generated data
    private File moduleXMLFile; // The path of the XML file that stores module information

    /**
     * Constructs a new ModuleManager object with a HashMap to hold modules, and
     * initializes the userDataDirectory and moduleXMLFile file locations.
     */
    public ModuleManager() {
        modules = new HashMap<>();
        String userHomeDirectory = System.getProperty("user.home");
        userDataDirectory = new File(userHomeDirectory + AppConfiguration.getProperty("userDataDirectory"));
        moduleXMLFile = new File(userDataDirectory, AppConfiguration.getProperty("moduleXMLFile"));
    }

    /**
     * The method getModuleNames will return all module names.
     * 
     * @return A HashSet, composed of all module names.
     */
    public HashSet<String> getModuleNames() {
        HashSet<String> moduleNames = new HashSet<>();
        for (Module module : modules.values()) {
            moduleNames.add(module.getName());
        }

        return moduleNames;
    }

    /**
     * The method getModuleCodes will return all module codes.
     * 
     * @return A HashSet, composed of all module codes.
     */
    public HashSet<String> getModuleCodes() {
        HashSet<String> moduleCodes = new HashSet<>();
        for (Module module : modules.values()) {
            moduleCodes.add(module.getCode());
        }

        return moduleCodes;
    }

    /**
     * getModuleTitles will return a list of all module titles.
     * 
     * @return An ArrayList of String, holding all the module titles.
     */
    public ArrayList<String> getModuleTitles() {
        // Create a new ArrayList to store the module titles
        ArrayList<String> moduleTitles = new ArrayList<>();

        // Iterate over the list of modules
        for (Module module : modules.values()) {
            // Add the title of the current module to the moduleTitles list
            moduleTitles.add(module.getTitle());
        }

        // Return the list of module titles
        return moduleTitles;
    }

    /**
     * Parses a module title and returns a module code.
     * 
     * @param aModuleTitle A String, representing the module title
     * @return A String, representing the module code.
     */
    public String parseTitleToCode(String aModuleTitle) {
        // Split the string
        String[] titleSplit = aModuleTitle.split(" ");

        // Return the module code, will always be the first element
        return titleSplit[0];
    }

    /**
     * The method buildNewModule builds a new module and saves the module
     * information to an XML file.
     * 
     * @param moduleData A HashMap that holds the data necessary for creating a new
     *                   module.
     * @return A Module, representing the new module.
     */
    public Module buildNewModule(HashMap<String, String> moduleData) {
        // Unpack data.
        String moduleCode = moduleData.get("moduleCode");
        String moduleName = moduleData.get("moduleName");
        String noteTemplatePath = moduleData.get("noteTemplatePath");
        String tmaTemplatePath = moduleData.get("tmaTemplatePath");

        // Create new module and add to list of modules.
        Module module = new Module(moduleCode, moduleName, noteTemplatePath, tmaTemplatePath, 1, 1);
        modules.put(moduleCode, module);

        saveNewModuleToXML(module);
        return module;
    }

    /**
     * Saves a field of a module to an XML file.
     * 
     * @param aModuleCode A String, representing the code of the module.
     * @param field       A String, representing the field to be saved.
     * @throws IOException if an I/O error occurs.
     */
    public void saveModuleFieldToXML(String aModuleCode, String field) {
        // Find the module with the given code
        Module module = modules.get(field);

        // Get the name of the module
        String moduleName = module.getName();

        // Parse the XML file
        Document document = parseXML();

        // Find the module element in the XML file
        NodeList moduleNodes = document.getElementsByTagName("module");
        Element moduleElement = null;
        boolean match = false;
        int i = 0;
        while (!match && i < moduleNodes.getLength()) {
            Node node = moduleNodes.item(i);
            moduleElement = (Element) node;
            if (moduleElement.getAttribute("name").equals(moduleName)) {
                match = true;
            } else {
                i++;
            }
        }

        // Get the value of the field to be saved
        String value = "";
        switch (field) {
            case "noteTemplatePath":
                value = module.getNoteTemplatePath();
                break;
            case "tmaTemplatePath":
                value = module.gettmaTemplatePath();
                break;
            case "TMACount":
                value = Integer.toString(module.getTMACount());
                break;
            case "EMACount":
                value = Integer.toString(module.getEMACount());
                break;
        }

        // Update the value of the field in the XML file
        NodeList nodelist = moduleElement.getElementsByTagName(field);
        Node node = nodelist.item(0);
        Element element = (Element) node;
        element.setAttribute("value", value);

        // Save the updated DOM to the XML file
        saveDOMToXML(document);
    }

    /**
     * Loads modules from an XML file and returns a HashMap of module objects.
     * 
     * @return a HashMap of Module objects containing module data extracted from an
     *         XML file, or an empty HashMap if the XML file does not exist or
     *         contains no module data
     */
    public HashMap<String, Module> loadModules() {
        if (moduleXMLFile.exists()) {
            Document document = parseXML();

            // Get a list of all modules and iterate over each
            NodeList moduleNodes = document.getElementsByTagName("module");
            for (int i = 0; i < moduleNodes.getLength(); i++) {
                // Get a module
                Node moduleNode = moduleNodes.item(i);
                Element moduleElement = (Element) moduleNode;

                // Array to hold the module data
                String[] moduleData = new String[6];

                // Extract module information by the following tag names
                String[] tagNames = { "code", "noteTemplatePath", "tmaTemplatePath", "TMACount", "EMACount" };
                for (int j = 0; j < tagNames.length; j++) {
                    NodeList nodelist = moduleElement.getElementsByTagName(tagNames[j]);
                    Node node = nodelist.item(0);
                    Element element = (Element) node;
                    moduleData[j] = element.getAttribute("value");
                }
                // Get the name of the module
                moduleData[moduleData.length - 1] = moduleElement.getAttribute("name");

                // Create new module and add to list of modules.
                Module module = new Module(moduleData[0], moduleData[moduleData.length - 1], moduleData[1],
                        moduleData[2], Integer.parseInt(moduleData[3]), Integer.parseInt(moduleData[4]));
                modules.put(moduleData[0], module);
            }
        }
        return modules;
    }

    /**
     * The method saveModuleToXML saves the given module information to an XML file.
     * Is used soley on the creation of a new module.
     * 
     * @param moduleCode       A String, reprsenting the code of a module.
     * @param moduleName       A String, reprsenting the name of a module.
     * @param noteTemplatePath A String, reprsenting the path to the users notes
     *                         template document for the module.
     * @param tmaTemplatePath  A String, reprsenting the path to the users TMA
     *                         template document for the module.
     */
    private void saveNewModuleToXML(Module aModule) {
        // Create the users data directory if it does not exists
        if (!userDataDirectory.exists()) {
            userDataDirectory.mkdir();
        }

        // Create a builder to build a DOM
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Create new XML file if it does not exists
        if (!moduleXMLFile.exists()) {
            createNewXMLFile(builder);
        }

        // Build the DOM
        Document document = null;
        try {
            document = builder.parse(moduleXMLFile);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        // Create the elements to hold the module data and set there values
        Element module = document.createElement("module");
        module.setAttribute("name", aModule.getName());

        Element code = document.createElement("code");
        code.setAttribute("value", aModule.getCode());

        Element noteTemplatePathElement = document.createElement("noteTemplatePath");
        noteTemplatePathElement.setAttribute("value", aModule.getNoteTemplatePath());

        Element tmaTemplatePathElement = document.createElement("tmaTemplatePath");
        tmaTemplatePathElement.setAttribute("value", aModule.gettmaTemplatePath());

        Element TMACount = document.createElement("TMACount");
        TMACount.setAttribute("value", String.valueOf(aModule.getTMACount()));

        Element EMACount = document.createElement("EMACount");
        EMACount.setAttribute("value", String.valueOf(aModule.getEMACount()));

        // Get root element, that holds all module elements
        Element root = document.getDocumentElement();
        // Place new elements into the DOM
        root.appendChild(module);
        module.appendChild(code);
        module.appendChild(noteTemplatePathElement);
        module.appendChild(tmaTemplatePathElement);
        module.appendChild(TMACount);
        module.appendChild(EMACount);

        // write DOM to XML file
        saveDOMToXML(document);
    }

    /**
     * The method createNewXMLFile will create a blank XML file with a root element
     * of tagName "elements".
     * 
     * @param builder A DocumentBuilder object, that will be used to build the DOM.
     */
    private void createNewXMLFile(DocumentBuilder builder) {
        // Create a new DOM with root element
        Document document = builder.newDocument();
        Element root = document.createElement("modules");
        document.appendChild(root);

        // write DOM to XML file
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(moduleXMLFile));
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the moduleXMLFile XML file and creates a DOM (Document Object Model).
     * 
     * @return A Document, the DOM representation of the XML file.
     */
    private Document parseXML() {
        // Create a builder to build a DOM
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Read in the XML file and create a DOM
        Document document = null;
        try {
            document = builder.parse(moduleXMLFile);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return document;
    }

    /**
     * Saves a DOM (Document Object Model) to an XML file.
     * 
     * @param document A Document object, reprsenting the DOM to be saved.
     */
    private void saveDOMToXML(Document document) {
        // TODO: to avoid the additional whitespace when saving xml we may have to
        // create a stylesheet or find other method???
        try {
            // Create a transformer for saving the DOM
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // Set the transformer to indent the output
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Save the DOM to the XML file
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(moduleXMLFile);
            transformer.transform(source, result);
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            e.printStackTrace();
        }
    }

}
