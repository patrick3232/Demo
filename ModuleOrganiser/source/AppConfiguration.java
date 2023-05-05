import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A utility class that holds the apps configuration.
 * 
 * @Author Patrick Macpherson
 * @Version 28 March 2023
 */

public class AppConfiguration {

    // Class fields
    private static Properties configuration; // Properties object holding the applications configuration data
    private static final String CONFIGURATION_PATH = Main.CURRENT_WORKING_DIRECTORY + "\\" + "resources" + "\\"
            + "AppConfiguration.properties"; // The path to the applications configuration file

    /**
     * Will read in the data stored in the AppConfiguartion file.
     */
    public static void loadAppConfiguration() {
        // We only read in the configurtion file once
        if (AppConfiguration.configuration == null) {
            configuration = new Properties();
            try {
                // Create a connection to the file we wish to read.
                FileInputStream inStream = new FileInputStream(CONFIGURATION_PATH);
                // Read data from the input stream.
                configuration.load(inStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Will return the given property from the applications configuration file.
     * 
     * @param aProperty The property that must be retrieved
     * @return A String, representing the configuration associated with the
     *         given property
     */
    public static String getProperty(String aProperty) {
        return configuration.getProperty(aProperty);
    }
}
