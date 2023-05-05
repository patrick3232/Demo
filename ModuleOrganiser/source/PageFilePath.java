import java.awt.FileDialog;
import java.awt.Frame;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The class PageFilePath, will allow a user to select a file using the
 * file explorer.
 * 
 * @author Patrick Macpherson
 * @version 28 March 2023
 */
public class PageFilePath extends PageQuestion {
    /**
     * Constructor for the class PageFilePath.
     * Calls the constructor of the superclass PageQuestion.
     * 
     * @param name   The name of the page so we can reference it.
     * @param output The textual output the page will display to the user.
     */
    public PageFilePath(String name, String output) {
        super(name, output);
    }

    /**
     * Allows the user to select a file via the GUI file explorer.
     * 
     * @return A String, representing the path of the chosen file.
     */
    @Override
    public String getUserInput() {
        boolean inputValid = false;
        while (!inputValid) {
            super.display();
            // Open the file explorer
            Frame frame = new Frame();
            FileDialog fileDialog = new FileDialog(frame, "Select a file", FileDialog.LOAD);
            fileDialog.toFront();
            fileDialog.setVisible(true);

            // Get users file selection
            // TODO: read getFile API, it seems we are forcing the user to select a file.
            // cancel == null. later validation wont accept null
            userInput = fileDialog.getFile();

            // Relaese any resources used and return the file path
            frame.dispose();
            fileDialog.dispose();

            // Check input is valid
            inputValid = userInputIsValid();
        }

        return userInput;
    }

    /**
     * Validates that the selected file is valid by ensuring it exists.
     * 
     * @return A boolean, stating whether the user input is valid.
     */
    @Override
    protected boolean userInputIsValid() {
        // No user input exists
        if (userInput == null) {
            return false;
        }

        // Create a Path object from the user input and check it exists
        Path path = Paths.get(userInput);
        boolean isValid = Files.exists(path) ? true : false;

        // Set userInput to null since it has completed validation and return result.
        return isValid;
    }
}
