
import com.sun.jdi.IntegerType;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.*;
import javax.management.RuntimeErrorException;
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
                    throw new JSONParseException(String.format("Expected char not found instead %c", contents.charAt(position)), position);                    
                }

                position++;
                skipWhiteSpace();

                if (position >= contents.length()) {
                    throw new JSONParseException("Excpeted a value got end of file", position);
                } 

                switch (contents.charAt(position)) {
                    case '"' -> {
                        System.out.println("calling string parser");
                        fileObj.put(newKey, parseString());
                    }
                    case '[' -> {
                        System.out.println("calling array parser");
                    }
                    case '{' -> {
                        System.out.println("calling object parser");
                    }
                    default -> {
                        if (contents.charAt(position) == '-' || contents.charAt(position) >= '0' && contents.charAt(position) <= '9') {
                            System.out.println("calling int parser");
                            fileObj.put(newKey, parseInteger());
                        } else {

                            throw new UnknownType(String.format("Type at char %c not a match for any known types", contents.charAt(position)), position);

                        }
                    }
                }
            }
        }

            return fileObj;
    }
    
    private String parseString() {

        StringBuilder value = new StringBuilder();

        position++;

        while (contents.charAt(position) != '"') {
            value.append(contents.charAt(position));
            position++;
        }

        return value.toString();
    }

    private int parseInteger() {

        int start = position;

        if (contents.charAt(position) == '-') {
            position++;
        }   

        while (position < contents.length() && contents.charAt(position) >= '0' && contents.charAt(position) <= '9') {
            position++;
        }

        return Integer.parseInt(contents.substring(start, position));
        
    }

    public static void main(String[] args) {
        String file = "C:\\Users\\USER\\Desktop\\Juan-Parser\\testing.json";
        JsonRead reader = new JsonRead(file);
        System.out.println(reader.fromJson());
    }

}

class JSONParseException extends RuntimeException {
    public JSONParseException(String message, int position) {
        super(String.format("Failure | %s | at position | %d |", message, position));

    }
}

class UnknownType extends RuntimeException {
    public UnknownType(String message, int position) {
        super(String.format("Failure | %s | at position | %d |", message, position));
    }
}