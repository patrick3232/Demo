/**test
 * The class Main is used as the entry
 * point to the application.
 * 
 * Author: Patrick Macpherson
 * Version: 17 December 2022
 */

public class Main
{
    // Class fields
    // The path of the apps execution
    public static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir") + "\\";  
    public static void main(String[] args) {
        // Set up the application configuration
        AppConfiguration.loadAppConfiguration();

        // Execute the application
        App app = new App();
        app.mainMenu();
    }

}