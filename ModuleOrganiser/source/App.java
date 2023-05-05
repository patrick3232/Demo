import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * The class App handles the core execution flow of the module organizer
 * application.
 * 
 * @Author Patrick Macpherson
 * @Version 17 December 2022
 */
public class App {
    // Class fields
    private static final Scanner SCANNER = new Scanner(System.in);

    // Instance fields
    private boolean running;
    private PageManager pageManager; // Handles actions regarding pages
    private ModuleManager moduleManager; // Handles actions regarding modules
    private HashMap<String, Page> pages; // Collection for holding the pages
    private HashMap<String, Module> modules; // Collection for holding the modules

    /**
     * Constructs an instance of the App class, initializing its fields and objects.
     * The running flag is set to true by default. A new PageManager object is
     * created and assigned to the pageManager field. A new ModuleManager object is
     * created and assigned to the moduleManager field. The createPages() method of
     * the PageManager is called to create pages, which are then assigned to the
     * pages field. The loadModules() method of the ModuleManager is called to load
     * modules, which are then assigned to the modules field.
     */
    public App() {
        running = true;
        pageManager = new PageManager();
        moduleManager = new ModuleManager();
        pages = pageManager.createPages();
        modules = moduleManager.loadModules();
    }

    public void mainMenu() {
        while (running) {
            int choice = Integer.parseInt(pages.get("mainMenu").getUserInput());
            switch (choice) {
                case 1:
                    newModuleStructure();
                    break;
                case 2:
                    newPartStructure();
                    break;
                case 3:
                    newEMATMAStructure("TMA");
                    break;
                case 4:
                    newEMATMAStructure("EMA");
                    break;
                case 5:
                    optionsMenu();
                    break;
            }
        }
    }

    /**
     * The method newModuleStructure will display the pages that collect module
     * information
     * and then create a new module directory structure
     */
    private void newModuleStructure() {
        // Page information
        String task = "createModule";
        int pageCount = pageManager.getNumberOfPagesForTask(task);

        // Collected user input
        String[] keySet = { "moduleCode", "moduleName", "noteTemplatePath", "tmaTemplatePath" };
        HashMap<String, String> userInput = new HashMap<>();
        int collectedInput = 0;

        // Collect information about the module from the user by presenting a set of
        // pages
        for (int i = 0; i < pageCount; i++) {
            String pageName = task + i;
            Page page = pages.get(pageName);
            

            if (page instanceof PageChoice) {
                // The page offers the user a yes or no choice. Selecting no will mean we skip
                // the next page of data collection
                int choice = Integer.parseInt(page.getUserInput());
                switch (choice) {
                    case 1:
                        // User selected yes we continue to next page
                        break;
                    case 2:
                        // User selected no, submit empty data and skip next page
                        userInput.put(keySet[collectedInput], "");
                        collectedInput++;
                        i++;
                        break;
                }
            } else {
                // The page will collect text data from the user
                String input = page.getUserInput();

                // Check that the module code entered does not already exist
                HashSet<String> savedModuleCodes = moduleManager.getModuleCodes();
                if (pageName.equals("createModule0") && savedModuleCodes.contains(input)) {
                    // TODO: should probably be a new type of page, type="information"
                    System.out.println("\nIt seems the module code " + "\"" + input + "\"" + " already exists");
                    System.out.print("Returning to main menu. Press enter to continue...");
                    SCANNER.nextLine();
                    return;
                }

                // Place collected data into a Hashmap and increment the keySet index
                userInput.put(keySet[collectedInput], input);
                collectedInput++;
            }
        }

        // Hand the collected data to the module manager so the module structure can be
        // created
        Module module = moduleManager.buildNewModule(userInput);

        // Open the modules directory
        File moduleDirectory = module.getModuleDirectory();
        openDirectory(moduleDirectory);

        // TODO: should probably be a new type of page, type="information"
        // Give feedback that the module has been created
        System.out.println("\nThe module " + "\"" + module.getTitle() + "\"" + " was created successfully!");
        System.out.print("Returning to main menu. Press enter to continue...");
        SCANNER.nextLine();
        
        return;
    }

    private void newPartStructure() {
        // Get a list of all module titles and hand them to the page to be displayed to
        // the user
        ArrayList<String> moduleTitles = moduleManager.getModuleTitles();

        // Return to main menu if there are no saved modules
        if (moduleTitles.size() == 0) {
            System.out.println("\nNo modules found. Please create a new module structure.");
            System.out.print("Returning to main menu. Press enter to continue...");
            SCANNER.nextLine();
            return;

        }

        // Specify the task so we can retrive the relevant pages
        String task = "createPart";

        // Using the users input find the module they selected
        PageChoice page = (PageChoice) pages.get(task + 0);
        page.setChoices(moduleTitles);
        int choice = Integer.parseInt(page.getUserInput());
        String selectedModuleTitle = moduleTitles.get(choice - 1);

        // Get the full part title including Block, Part, Chapter, Title
        String partTitle = pages.get(task + 1).getUserInput();
        
        // Parse the part title into the names of directories
        DirectoryParser directoryParser = new DirectoryParser();
        ArrayList<String> directories = directoryParser.parse(partTitle);

        // Get the module code for the selected module
        String moduleCode = moduleManager.parseTitleToCode(selectedModuleTitle);
        Module module = modules.get(moduleCode);
        // Get the notes directory for the module
        File notesDirectory = module.getNotesDirectory();

        // Create the directory structure using the modules notes directory and the
        // previously parsed directories as the sub directories
        // TODO: throw custom exception if directory already exist and returne to main
        // menu with message
        File partDirectory = DirectoryCreator.createNestedDirectories(notesDirectory, directories);

        // Copy the notes template document for the module into the newly created
        // directory structure
        if (module.isNoteTemplatePath()) {
            String path = module.getNoteTemplatePath().toString();
            String fileExtension = findFileExtension(path);
            copyAndRename(path, partDirectory.toString(), partDirectory.getName() + fileExtension);
        }

        // Open the new part directory
        openDirectory(partDirectory);

        // TODO: should probably be a new type of page, type="information"
        // Give feedback that the module has been created
        String outputTitle = directories.get(directories.size() - 1);
        System.out.println("\n" + outputTitle + " was created successfully!");
        System.out.print("Returning to main menu. Press enter to continue...");
        SCANNER.nextLine();
        return;
    }

    /**
     * Returns the file extension of the given source file
     * 
     * @param sourceFile A String, representing a file name or path.
     * @return A String, representing the file extension of the given file. (e.g.
     *         ".docx")
     */
    private String findFileExtension(String sourceFile) {
        int sourceExtensionPosition = sourceFile.lastIndexOf(".");
        return sourceFile.substring(sourceExtensionPosition);
    }

    private void newEMATMAStructure(String task) {
        // Let user select a module
        String moduleCode = moduleSelectPage(task);
        if (moduleCode == null) {
            return;
        }

        // Create the new directory name
        Module module = modules.get(moduleCode);
        int currentAssignment = task.equals("TMA") ? module.getTMACount() : module.getEMACount();
        String childDirectoryName = String.valueOf(currentAssignment).length() == 1 ? task + " 0" + currentAssignment
                : task + " " + currentAssignment;

        // Create a root directory that will be the new EMA/TMA directory
        File masterDirectory = task.equals("TMA") ? module.getTMADirectory() : module.getEMADirectory();
        File rootDirectory = new File(masterDirectory, childDirectoryName);

        // get subdirectories for the task and convert to ArrayList
        String[] tempSubDirectories = AppConfiguration.getProperty(task + "SubDirectories").split(" ");
        ArrayList<String> subDirectories = new ArrayList<>(Arrays.asList(tempSubDirectories));

        // create the structure
        DirectoryCreator.createRootAndSubDirectories(rootDirectory, subDirectories);

        // Copy TMA/EMA template document into new directory.
        if (module.isTMATemplatePath()) {
            Path sourceFile = Paths.get(module.gettmaTemplatePath());
            String newFileName = parseEMATMAFileName(sourceFile.getFileName().toString(),
                    currentAssignment, task);
            copyAndRename(sourceFile.toString(), rootDirectory.toString(), newFileName);
        }

        // Increment and save the updated assignment count to XML
        if (task.equals("TMA")) {
            module.incrementTMACount();
            moduleManager.saveModuleFieldToXML(moduleCode, "TMACount");
        } else {
            module.incrementEMACount();
            moduleManager.saveModuleFieldToXML(moduleCode, "EMACount");
        }

        // Open the new part directory
        openDirectory(rootDirectory);

        // TODO: should probably be a new type of page, type="information"
        // Give feedback that the module has been created
        String outputTitle = childDirectoryName;
        System.out.println("\n" + outputTitle + " was created successfully!");
        System.out.print("Returning to main menu. Press enter to continue...");
        SCANNER.nextLine();
        return;

    }

    /**
     * Copies a source file to a destination directory and renames the copied file.
     * 
     * @param sourceFile           A String, representing the path of the source
     *                             file to be copied.
     * @param destinationDirectory A String, representing the path of the
     *                             destination directory.
     * @param newFileName          A String, representing the name of the copied
     *                             file.
     */
    private void copyAndRename(String sourceFile, String destinationDirectory, String newFileName) {
        // Create a copy destination
        Path destination = Path.of(destinationDirectory, newFileName);

        // Copy the source file to the copy destination
        try {
            Files.copy(Path.of(sourceFile), destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renames an existing EMA/TMA filename to reflect the current assignment
     * number.
     * 
     * @param fileName         A String, representing the original filename.
     * @param assignmentNumber An int, representing the current assignment number.
     * @param task             A String, representing the assignment type.
     * @return A String, representing the renamed filename.
     */
    private String parseEMATMAFileName(String fileName, int assignmentNumber, String task) {
        String replacement = String.valueOf(assignmentNumber).length() == 1 ? task + "0" + assignmentNumber
                : task + assignmentNumber;
        return fileName.replaceFirst("(ema|EMA|tma|TMA)[0-9]{2}", replacement);
    }

    private String moduleSelectPage(String task) {
        // Get a list of all module titles
        ArrayList<String> moduleTitles = moduleManager.getModuleTitles();

        // Return to main menu if there are no saved modules
        if (moduleTitles.size() == 0) {
            System.out.println("\nNo modules found. Please create a new module structure.");
            System.out.print("Returning to main menu. Press enter to continue...");
            SCANNER.nextLine();
            return null;
        }

        // Hand modules to page and let user select a module.
        PageChoice page = (PageChoice) pages.get("new" + task + 0);
        page.setChoices(moduleTitles);
        int choice = Integer.parseInt(page.getUserInput());
        String selectedModuleTitle = moduleTitles.get(choice - 1);

        // Get the module code for the selected module
        return moduleManager.parseTitleToCode(selectedModuleTitle);

    }

    private void optionsMenu() {
        System.out.println("optionsMenu()");
        // update template file locations
        // add remove folders that are added
        // set tma count
        // set ema count
    }

    private void openDirectory(File aDirectory) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(aDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
