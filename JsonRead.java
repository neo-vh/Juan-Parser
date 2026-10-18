
import java.util.*;

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


    private Map<String, Object> parseValue(Map<String, Object> fileObj) {
        
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
                        } else if (contents.charAt(position) == 'f' || contents.charAt(position) == 't' || contents.charAt(position) == 'n') {
                            System.out.println("calling boolean parser");
                            fileObj.put(newKey, parseBoolean());
                        } else {

                            throw new UnknownType(String.format("Type at char %c not a match for any known types", contents.charAt(position)), position);

                        }
                    }
                }
            }
        }

            return fileObj;
    }

    private Boolean parseBoolean() {

        Boolean value = null;

        // check if value is true false or null

        int start = position;

        if (contents.charAt(position) == 'n') {

            position++;

            while (position < contents.length() && contents.charAt(position) != ',' && contents.charAt(position) != '}' && contents.charAt(position) != ']') {
                position++;
            }

            if (position >= contents.length()) {
                throw new JSONParseException("Reached end of file while checking for null value", position);
            }

            if (contents.substring(start, position).equals("null")) {
                value = null;
            }
            else {
                throw new UnknownType(String.format("Unknown type encountered expected null -> %s", contents.substring(start, position)), position);
            }

        } else if (contents.charAt(position) == 't') {

            position++;

            while (position < contents.length() && contents.charAt(position) != ',' && contents.charAt(position) != '}' && contents.charAt(position) != ']') {
                position++;
            }

            if (position >= contents.length()) {
                throw new JSONParseException("Expected true but reached end of file while parsing", position);
            }

            if (contents.substring(start, position).equals("true")) {
                value = true;
            } else {
                throw new UnknownType(String.format("Unknown type encountered expected true -> %s", contents.substring(position)), position);
            }

        } else if (contents.charAt(position) == 'f') {

            position++;

            while (position < contents.length() && contents.charAt(position) != ']' && contents.charAt(position) != ',' && contents.charAt(position) != '}') {
                position++;
            }

            if (position >= contents.length()) {
                throw new JSONParseException("Encountered end of file while parsing for false", position);
            }

            if (contents.substring(start, position).equals("false")) {
                value = false;
            } else {
                throw new UnknownType(String.format("Unknown type encountered while parsing for false -> %s", contents.substring(start, position)), position);
            }

        }

        return value;

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

        if (position >= contents.length()) {
            throw new JSONParseException("Encountered end of file while parsing int", position);
        }

        return Integer.parseInt(contents.substring(start, position));
        
    }

    public static void main(String[] args) {
        String file = "read_testing.json";
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