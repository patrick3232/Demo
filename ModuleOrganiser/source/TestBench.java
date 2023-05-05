import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TestBench {

    
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("-1");
        System.out.println("path: " + path);
        String l;
        
        System.out.println("willTrue " + Files.exists(path));
        l = path.toAbsolutePath().toString();
        System.out.println("l: " + l);
        
        System.out.println("pathreal: " + path.toString());
        
    }
    

    

    

}


