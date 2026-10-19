
import java.util.*;


public class JsonRead {

    final String contents;
    int position = 0;

    public static void main(String[] args) {
        JsonRead reader = new JsonRead("testing.json");
        System.out.println(reader.fromJson());
    }


    public JsonRead(String filePath) {

        this.contents = JsonParse.readFile(filePath);

    }

    private char decodeEscape() { // only called when a backslash is encountered
        
        if (contents.charAt(position) != '\\') {
            throw new JSONParseException(String.format("Decoder expected backslash got %c", contents.charAt(position)), position);
        }

        int backslashPos = position;

        position++;

        String escapedLiteral = contents.substring(backslashPos, position); // contains the entire escaped character
        char decoded; 
        
        


        decoded = '0';
        return decoded;


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

        parseKeys(fileObj);

        return fileObj;

    }

    private Object dispatchParser() {

        Object value = new Object();
        char c = contents.charAt(position);

        switch (c) {

            case '"' -> {
                value = parseString();
            }
            case '[' -> {  
                // parse array
            }
            case '{' -> {
                // parse dict
            }
            default -> {

                if (c == '-' || c >= '0' && c <= '9') {
                    // parse int
                } else if (c == 'n' || c == 'f' || c == 't') {
                    // parse bool
                } else {
                    throw new UnknownType("Unknown type while attempting to create value", position); // doesnt follow any type patterns
                }



            }

        }

        return value;

    }
    
    private Map<String, Object> parseKeys(Map<String, Object> fileObjMap) {

        String newKey;


        for (this.position = 0; this.position < contents.length(); this.position++) { // for char in file

            char currentChar = contents.charAt(position);

            if (currentChar == '"') { // start of key

               
                position++;
                int startOfKey = position;


                while (position < contents.length() && contents.charAt(position) != '"') { // run until end of key (ends on the closing quote)

                    position++;

                }   

                if (position >= contents.length()) { // check if loop exited because of bad file
                    throw new JSONParseException("Reached end of file while attempting to parse key", position);
                }

                if (contents.charAt(position) != '"') { // check if loop exited but didint end on end quote
                    throw new JSONParseException(String.format("Unexpected char found instead of end quote %c", contents.charAt(position)), position);
                }

                if (contents.charAt(position) == '"') { // ensure loop exited and stayed on end quote (edge case could mess up key name if position is off)
                    newKey = contents.substring(startOfKey, position); // position still on end quote bcs substring is exclusive (start - position - 1)

                } else {
                    throw new JSONParseException(String.format("Expected end quote got %c instead", contents.charAt(position)), startOfKey);
                }

                position++; // move past end quote
                skipWhiteSpace(); // lands on :

                if (position >= contents.length() || contents.charAt(position) != ':') { // if end of file reached or character not :
                    if (position >= contents.length()) {
                        throw new JSONParseException("End of file Unexpectadly reached", position);
                    } else if (contents.charAt(position) != ':') {
                        throw new JSONParseException(String.format("Expected : got %c", contents.charAt(position)), position);
                    }

                }

                position++; // move to char after colon
                skipWhiteSpace(); // move to start of value

                if (!fileObjMap.containsKey(newKey)) {
                    fileObjMap.put(newKey, dispatchParser()); // consumes value and leaves on the next non white space char after the value
                }

                
            }


        }


        return fileObjMap;
    }

    private String parseString() { // current position is opening quote
        
        StringBuilder value = new StringBuilder();


        if (contents.charAt(position) == '"') { // ensure position is start quote (edge case)
            
            position ++; // move to first character of the string value

            while (position < contents.length() && contents.charAt(position) != '"') {
                
                if (contents.charAt(position) != '\\') { // if not escaped character add value and move position to next char
                    value.append(contents.charAt(position));
                    position++;
                } else { // if escaped character call decoder from backslash and move position to next character after escaped character
                    value.append(decodeEscape());
                }


            }

        }

        return value;

    }
    


}

class JSONParseException extends RuntimeException {

    public JSONParseException(String message, int position) {

        super(String.format("Fail message | %s | at position | %d |", message, position));

    }
    
}

class UnknownType extends RuntimeException {
    public UnknownType(String message, int position) {
        super(String.format("Fail message | %s | at position | %d |", message, position));
    }
}


