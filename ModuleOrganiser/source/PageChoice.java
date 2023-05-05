import java.util.ArrayList;

/**
 * The class PageChoice, will display a list of choices to the user and is able
 * to collect a choice from the user.
 * 
 * @author Patrick Macpherson
 * @version 28 March 2023
 */

public class PageChoice extends PageQuestion 
{
    // Instance fields
    // The choices the page will pose to the user
    private ArrayList<String> choices;  

    /**
     * Constructor for the class PageChoice.
     * Calls the constructor of the superclass PageQuestion.
     * Initialises the instance field choices.
     * @param name The name of the page so we can reference it.
     * @param output The textual output the page will display to the user.
     * @param choices The choices the page will pose to the user.
     */
    public PageChoice(String name, String output, ArrayList<String> choices)
    {
        super(name, output);
        this.choices = choices;
    }

    /**
     * A setter method for the instance field choices.
     * Used for situations where the choices cannot be predetermined.
     * @param choices The choices the page will pose to the user.
     */
    public void setChoices(ArrayList<String> choices)
    {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < choices.size(); i++) {
            temp.add((i + 1) + ". " + choices.get(i));
        }
        this.choices = temp;
    }

    @Override
    public String getUserInput() 
    {
        boolean inputValid = false;
        while (!inputValid) {
            super.display();
        
            // Display the list of choices
            System.out.println("");
            for (String choice : choices) {
                System.out.println(choice);
            }

            // Get and validate input from user
            System.out.print(PROMPT);
            userInput = scanner.nextLine();
            inputValid = userInputIsValid();
        }
        
        return userInput;
    }

    /**
     * Determines whether the user entered a valid selection from the given choices.
     * A valid selection is a number from 1 to n, where n is the number of given choices.
     * @return A boolean, stating whether the users input was valid.
     */
    @Override
    protected boolean userInputIsValid() 
    {
        // No user input exists
        if (userInput == null) {
            return false;
        }

        String invalidMessage = "The text you entered was invalid. Please type a number from 1 to " 
                                + choices.size() + " (inclusive)";
        
        boolean isValid = true;
        try {
            int i = Integer.parseInt(userInput);

            if (i < 1 || i > choices.size()) {
                // The user has entered a choice that is out of bounds
                displayErrorMessage(invalidMessage);
                isValid = false;
            }
        } catch (NumberFormatException e) {
            // The user has entered a non integer value
            displayErrorMessage(invalidMessage);
            isValid = false;
        }
        
        // return result.
        return isValid;
    }  
}
