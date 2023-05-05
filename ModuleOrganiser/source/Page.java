import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * The class Page is a base class for all pages.
 * A page can be displayed to the user and will always output text that is 
 * either a question or information for the user. 
 * 
 * @author Patrick Macpherson
 * @version 28 March 2023
 */

public abstract class Page 
{
    // Fields  
    private String name;  // The name of the page so we can reference it
    private String output;  // Textual output displayed to the user 
    protected Scanner scanner;  // A scanner for reading in user input

    /**
     * Constructor for the class Page.
     * Initialises the fields name, output and scanner.
     * @param name The name of the page so we can reference it.
     * @param output The textual output the page will display to the user.
     */
    public Page(String name, String output) {
        this.name = name;
        this.output = output;
        scanner = new Scanner(System.in);
    }

    /**
     * The method getName is a getter method that will return the name of the page. 
     * @return A string representing the name of the page.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Compares this object to another object to determine if they are equal.
     * @param obj the object to compare to this object for equality
     * @return {@code true} if this object is equal to the specified object; {@code false} otherwise.
     * Two objects are considered equal if they have the same reference (i.e. they are the same object)
     * or if they are both instances of the Page class and have the same name field.
     */
    @Override
    public boolean equals(Object obj) {
        // Perform a reference check
        if (this == obj) {
            return true;
        }

        // Perform an instance of check
        if (!(obj instanceof Page)) {
            return false;
        }

        // Cast the object and check for content equality
        Page newObj = (Page) obj;
        return name.equals(newObj.name);
    }

    /**
     * Returns a hash code value for the object.
     * The hash code value is computed using the name field of the object.
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Displays the pages output to the user.
     */
    public void display()
    {
        // Refresh the page and display the pages output
        refreshConsole();
        System.out.println(output);
    }

    /**
    * Prompts the user for input and returns the input as a string.
    * @return a string representing the user's input
    */
    public abstract String getUserInput();
    
    /**
     * The method refreshConsole will refresh the console by clearing the console and inserting the application title.
     */
    protected void refreshConsole()
    {
        // Clear the screen
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");  // The program and command
        pb.inheritIO();  // Get control of stdin and stdout
        Process startProcess;
        try {
            startProcess = pb.start();  // Execute the program and command
            startProcess.waitFor();  // Wait for it to finish before continuing
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        // Display application title.
        for (int i = 0; i < 8; i++) {
            String appTitle = AppConfiguration.getProperty("appTitle" + i);
            System.out.println(appTitle);
        }
        System.out.println("");
    }
}
