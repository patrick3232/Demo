import java.io.File;
import java.util.ArrayList;

/**
 * The class DirectoryCreator is a utility class that can creates various directory structures.
 * @author Patrick Macpherson
 * @version 31 January 2023
 */
public class DirectoryCreator {

    public static void main(String[] args) {
        File af = new File("C:\\Users\\pat\\Documents\\test\\aa\\bb\\dd\\ee");
        af.mkdirs();
    }


    /**
     * Creates a nested directory structure from a given root and directories that must be nested.
     *
     * @param root the root directory where the structure will be created.
     * @param directories the list of directories to be created, these will be nested one after the other.
     * @return the File object representing the created directory structure.
     */
    public static File createNestedDirectories(File rootDirectory, ArrayList<String> directories)
    {
        // Concatenate the child directories
        String childDirectory = "";
        for (String directory : directories) {
            childDirectory += "\\" + directory;
        }

        // Create a File object for the full directory
        File fullDirectory = new File(rootDirectory, childDirectory);

        // If the directory does not exist, create it and all its parent directories
        if (!fullDirectory.exists()) {
            if (!fullDirectory.mkdirs()) {
                // TODO: place message if directory not created
                // TODO: could be because of forbidden characters in the directory name
            }
        }

        // Return the File object representing the created directory structure
        return fullDirectory;
    }

    /**
     * Creates a root directory and any given subdirectories.
     * @param rootDirectory A File object, representing the root directory.
     * @param subDirectories An ArrayList of String, representing the subdirectories of the given root directory.
     */
    public static void createRootAndSubDirectories(File rootDirectory, ArrayList<String> subDirectories)
    {
        // Create the root directory.
        if (!rootDirectory.exists()) {
            rootDirectory.mkdir();
        }

        // Create the sub directories.
        for (String subDirectory : subDirectories) {
            File file = new File(rootDirectory, subDirectory);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }
    
}
