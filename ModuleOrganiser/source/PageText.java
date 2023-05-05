import java.util.regex.Pattern;

/**
 * The class PageText will display a question to the user and is able to collect 
 * textual input from the user.
 * 
 * @author Patrick Macpherson
 * @version 28 March 2023
 */

public class PageText extends PageQuestion
{
    /**
     * Constructor for the class PageText.
     * Calls the constructor of the superclass PageQuestion.
     * @param name The name of the page so we can reference it.
     * @param output The textual output the page will display to the user.
     */
    public PageText(String name, String output)
    {
        super(name, output);
    }

    /**
     * Prompts the user for input and returns the input as a string.
     * @return a string representing the user's input
     */
    @Override
    public String getUserInput()
    {
        boolean inputValid = false;
        while (!inputValid) {
            super.display();

            // Display a prompt and get users input
            System.out.print(PROMPT);
            userInput = scanner.nextLine(); 

            // Remove puncuation
            userInput = userInput.replaceAll("[^a-zA-Z0-9 ]", "");
            // Remove any extra whitespace that may have been created
            userInput = userInput.replaceAll("\\s+", " ");
            // Check input is valid
            inputValid = userInputIsValid();

        }
        
        
        return userInput;
    }

    /**
     * Validate user input by ensuring that it contains at least one letter or number.
     * @return A boolean, stating the validity of the users input.
     */
    @Override
    protected boolean userInputIsValid() {
        // No user input exists
        if (userInput == null) {
            return false;
        }

        // Set a message for invalid input
        String invalidMessage = "The text you entered was invalid. Please enter a string with at least one character or number"; 

        // Create the pattern we will search for in the input
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{1}[a-zA-Z0-9 ]*$");

        boolean isValid = false;
        if (pattern.matcher(userInput).matches()) {
            // The input matches the pattern
            isValid = true;
        } else {
            // The input does not match the pattern
            displayErrorMessage(invalidMessage);
            isValid = false;
        }

        // Set userInput to null since it has completed validation and return result.
        return isValid;
    }   
}
