import java.util.ArrayList;

public class TestBench2 
{
    
    public static void main(String[] args) 
    {

        System.out.println(getClass
)
        
        

        

       

        
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




    private void printString(String aString)
    {
        System.out.println(aString);
    }



    

    /**
     * Prints the given string to the console.
     *
     * @param aString The string to be printed.
     */
    private void printString(String aString)
    {
        // Print the string to the console
        System.out.println(aString);
    }











}
