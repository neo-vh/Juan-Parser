import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class JsonParse {

    public static String readFile(String filePath) {

        String file_contents = new String();

        try {
            file_contents = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            System.out.println(String.format("Error %s Ecountered", e));
        }

        return file_contents;
    }

}