/**
 * The class PageQuestion acts as a base class for any pages that will 
 * pose a question to the user.
 * 
 * @author Patrick Macpherson
 * @version 28 March 2023
 */

public abstract class PageQuestion extends Page 
{
    protected final String PROMPT = "\n>>> ";  // A prompt that will be used for user input
    protected String userInput;  // Holds user input that the page has collected 

    /**
     * Constructor for the class PageQuestion.
     * Calls the constructor of the superclass Page.
     * Initialises userInput to null.
     * @param name The name of the page so we can reference it.
     * @param output The textual output the page will display to the user.
     */
    public PageQuestion(String name, String output)
    {
        super(name, output);
        userInput = null;
    }

    /**
     * Used to validate user input that a page has collected.
     * upon completion. This means collected data can be validated only once.
     * @return A boolean, that is true if the input collected is valid otherwise 
     * returns false if no data has been collected by the page or the data has
     * already been validated
     */
    protected abstract boolean userInputIsValid();

    /**
     * Used to display any error messages to the user.
     */
    protected void displayErrorMessage(String message)
    {
        refreshConsole();
        System.out.println("");
        System.out.println(message);
        System.out.print("Press enter to continue...");
        scanner.nextLine();
    }
}
