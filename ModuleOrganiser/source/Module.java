import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The class Module is used to represent a single module of study.
 * 
 * @Author Patrick Macpherson
 * @Version 26 December 2022
 */
public class Module {
    // Class fields
    private static final String[] COMMON_MODULE_SUBFOLDERS = { "notes", "TMA", "EMA" }; // Subfolders of every module
                                                                                        // directory.

    // Instance fields
    private String code; // The module code
    private String name; // The name of the module
    private String noteTemplatePath; // The path to the users notes template document
    private String tmaTemplatePath; // The path to the users TMA template document
    private int TMAcount; // The current TMA count
    private int EMAcount; // The current EMA count

    /**
     * Constructs a new Module object with the specified code, name, note template
     * path, TMA template path, TMA count, and EMA count. This constructor will also
     * create the file structure for the module in the current working directory.
     * 
     * @param code             The code for the module
     * @param name             The name of the module
     * @param noteTemplatePath The file path to the notes template for the module
     * @param tmaTemplatePath  The file path to the TMA template for the module
     * @param TMAcount         The current number of TMAs for the module
     * @param EMAcount         The current number of EMAs for the module
     */
    public Module(String code, String name, String noteTemplatePath, String tmaTemplatePath, int TMAcount, int EMAcount) {
        this.code = code;
        this.name = name;
        this.noteTemplatePath = noteTemplatePath;
        this.tmaTemplatePath = tmaTemplatePath;
        this.TMAcount = TMAcount;
        this.EMAcount = EMAcount;
        createDirectoryStructure();
    }

    /**
     * The method getName returns the name of the module.
     * 
     * @return A String, representing the name of the module.
     */
    public String getName() {
        return name;
    }

    /**
     * The method getCode returns the code of the module.
     * 
     * @return A String, representing the code of the module.
     */
    public String getCode() {
        return code;
    }

    /**
     * The method getTitle returns the title of the module.
     * The title is formatted as "moduleCode - moduleName"
     * 
     * @return A String, representing the title of the module.
     */
    public String getTitle() {
        return code + " - " + name;
    }

    /**
     * Returns a File object representing the module directory for the current
     * instance of the class.
     * The module directory is determined by concatenating the current working
     * directory with the title of the module.
     * 
     * @return a File object representing the module directory
     */
    public File getModuleDirectory() {
        return new File(Main.CURRENT_WORKING_DIRECTORY + "\\" + getTitle());
    }

    /**
     * getNoteTemplatePath will return the path of the modules notes directory.
     * 
     * @return A File object, representing the path of the modules notes directory.
     */
    public String getNoteTemplatePath() {
        return noteTemplatePath;
    }

    /**
     * Will return the path of the TMA template document.
     * 
     * @return A File object, reprsenting the path to the TMA template document.
     */
    public String gettmaTemplatePath() {
        return tmaTemplatePath;
    }

    /**
     * Returns the modules notes directory.
     * 
     * @return A File object, representing the path to the modules notes directory.
     */
    public File getNotesDirectory() {
        return new File(getModuleDirectory(), "\\" + COMMON_MODULE_SUBFOLDERS[0]);
    }

    /**
     * Will return the path to the modules TMA directory.
     * 
     * @return A File object, representing the path to the TMA directory.
     */
    public File getTMADirectory() {
        return new File(getModuleDirectory(), "\\" + COMMON_MODULE_SUBFOLDERS[1]);
    }

    /**
     * Will return the path to the modules EMA directory.
     * 
     * @return A File object, representing the path to the EMA directory.
     */
    public File getEMADirectory() {
        return new File(getModuleDirectory(), "\\" + COMMON_MODULE_SUBFOLDERS[2]);
    }

    /**
     * Will return the TMA count.
     * 
     * @return An int, representing the count of the current TMA
     */
    public int getTMACount() {
        return TMAcount;
    }

    /**
     * Will return the EMA count.
     * 
     * @return An int, representing the count of the current EMA
     */
    public int getEMACount() {
        return EMAcount;
    }

    /**
     * Increments the TMA count by one.
     */
    public void incrementTMACount() {
        TMAcount++;
    }

    /**
     * Increments the EMA count by one.
     */
    public void incrementEMACount() {
        EMAcount++;
    }

    /**
     * Check if there is a stored TMA template document.
     * 
     * @return A boolean, stating whether a TMA template document exists.
     */
    public boolean isTMATemplatePath() {
        if (tmaTemplatePath.equals("")) {
            return false;
        } else {
            return Files.exists(Paths.get(tmaTemplatePath));
        }
    }

    /**
     * Check if there is a stored note template document.
     * 
     * @return A boolean, stating whether a note template document exists.
     */
    public boolean isNoteTemplatePath() {
        if (noteTemplatePath.equals("")) {
            return false;
        } else {
            return Files.exists(Paths.get(noteTemplatePath));
        }
    }

    /**
     * The method createDirectoryStructure will create a new module directory and
     * its subfolders
     */
    private void createDirectoryStructure() {
        // Check if the moduleDirectory exists
        File moduleDirectory = getModuleDirectory();
        if (!moduleDirectory.exists()) {
            // moduleDirectory doesn't exist, create it
            moduleDirectory.mkdir();

            // Create the subfolders that are common to every module
            for (int i = 0; i < COMMON_MODULE_SUBFOLDERS.length; i++) {
                File directory = new File(moduleDirectory, "\\" + COMMON_MODULE_SUBFOLDERS[i]);
                directory.mkdir();
            }

            // Create any additional subfolders that are given in the configuration file
            String[] subFolders = AppConfiguration.getProperty("moduleSubFolders").split(" ");
            for (int i = 0; i < subFolders.length; i++) {
                File directory = new File(moduleDirectory, "\\" + subFolders[i]);
                directory.mkdir();
            }
        }
    }

    /**
     * Overrides the hashCode() method of the Object class to return the hash code
     * value
     * for this object based on the code and name fields.
     * 
     * @return an integer representing the hash code value for this object
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Overrides the equals() method of the Object class to compare this object with
     * the specified object for equality.
     * 
     * @param obj the object to be compared for equality with this object
     * @return true if the specified object is equal to this object; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Module other = (Module) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
