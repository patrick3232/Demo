import java.util.ArrayList;

public class DirectoryParser 
{
    public static void main(String[] args) {
        String in = "Block 2, Part 6: Client server interaction: A basic web application";
        DirectoryParser directoryParser = new DirectoryParser();
        ArrayList<String> dirs = directoryParser.parse(in);
        System.out.println(dirs);
    }


    // Fields
    private ArrayList<String> directories;
    private String title;

    public DirectoryParser()
    {
        directories = new ArrayList<>();
        title = "";
    }

    public ArrayList<String> parse(String input)
    {
        directories.clear();
        title = "";

        // Split the input string into words using the space character as the delimiter
        String[] words = input.split(" ");

        // Iterate over the list of words and extract the directories and the module title
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i + 1 < words.length) {
                String nextWord = removePunctuation(words[i + 1]);
                // Check if the next word is a number
                if (isNumeric(nextWord)) {
                    // It is a number, so it is a directory
                    // A directory will always contain a number
                    String directory = word + " " + nextWord;
                    directories.add(directory);
                    i++;
                } else {
                    // It is not a number, so it is part of the title
                    title += words[i] + " ";
                }
            } else {
                // We are on the last word, so it is part of the title
                title += words[i] + " ";
                title = title.trim();
            }
        }

        // For the last directory concatenate the title
        if (!directories.isEmpty()) {
            // Get the last element of the directories ArrayList
            String lastDirectory = directories.get(directories.size() - 1);
          
            // Concatenate the last directory with the title
            lastDirectory += " - " + title;
          
            // Update the last element of the directories ArrayList
            directories.set(directories.size() - 1, lastDirectory);
        } else {
            directories.add(title);
        }

        return directories;
    }

    // Helper method to check if a string is numeric
    private static boolean isNumeric(String aString) 
    {
        for (int i = 0; i < aString.length(); i++) {
            if (!Character.isDigit(aString.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Helper method to remove punctuation and non-letter/non-digit characters from a string
    private static String removePunctuation(String s) 
    {
        return s.replaceAll("[^\\p{L}\\p{Nd}]+", "");
    } 
}
