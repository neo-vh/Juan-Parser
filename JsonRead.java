
import java.nio.file.attribute.PosixFileAttributes;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;


public class JsonRead {

    private final String contents;
    private int position;

    public JsonRead(String file) {
        this.contents = JsonParse.readFile(file);
    }

    private void skipWhiteSpace() {
        while (position < contents.length()) {
            char c = contents.charAt(position);

            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                position++;
            } else {
                break;
            }
        }
    }

    public Map<String, Object> fromJson() {

        Map<String, Object> fileObj = new HashMap<>();

        fileObj = parseValue(fileObj);

        return fileObj;

    }


    private Map<String, Object> parseValue(Map<String, Object> fileObj) throws JSONParseException {
        
        char currentChar;
        String newKey = new String();

        for (this.position = 0; this.position < contents.length(); this.position++) { // for every char in the file
            
            skipWhiteSpace();
            currentChar = contents.charAt(position);
            
            if (currentChar == '"') { // start of new key
                
                int start_string = position + 1;
                position++;

                while (position < contents.length()) {

                    char c = contents.charAt(position);
                    
                    if (c == '"') {
                        // end of string found
                        String key = contents.substring(start_string, position); // position is on end quote
                        position++; // char after end quote
                        
                        newKey = key;
                        break;
                    }

                    position++;

                }

                skipWhiteSpace();
                
                if (contents.charAt(position) != ':') {  
                    throw new JSONParseException(String.format("Expected char not found instead %c", currentChar), position);                    
                }

                position++;

                switch (contents.charAt(position)) {
                    case '"' -> {

                    }
                    case '[' -> {
                        
                    }
                    default -> {

                    }
                }


            }
            
        }

            return fileObj;
        }

    public static void main(String[] args) {
        String file = "C:\\Users\\USER\\Desktop\\Juan-Parser\\testing.json";
        JsonRead reader = new JsonRead(file);
        reader.fromJson();
    }

}

class JSONParseException extends RuntimeException {
    public JSONParseException(String message, int position) {
        super(String.format("Failure | %s | at position | %d |", message, position));

    }
}