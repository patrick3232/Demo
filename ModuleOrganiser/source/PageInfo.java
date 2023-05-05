/**
 * The class PageInfo can be used to display only textual output to the user.
 * 
 * @author Patrick Macpherson
 * @version 28 March 2023
 */

public class PageInfo extends Page {

    /**
     * Constructor for the class PageInfo.
     * Calls the constructor of the superclass Page.
     * @param name The name of the page so we can reference it.
     * @param output The textual output the page will display to the user.
     */
    public PageInfo(String name, String output)
    {
        super(name, output);
    }

    /**
     * Prompts the user for input and returns the input as a string.
     * Since PageInfo only displays output, this should be used to allow the
     * program to sleep and give user time to read the output.
     * @return a string representing the user's input
     */
    @Override
    public String getUserInput()
    {
        return scanner.nextLine();
    }
}
