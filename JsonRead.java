
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

    public Object fromJson() {

        Object fileObj = new Object();

        fileObj = parseValue(fileObj);

        return fileObj;

    }


    private Object parseValue(Object fileObj) {
        
        Character currentChar;
        String newKey = new String();

        for (this.position = 0; this.position < contents.length(); this.position++) {
            
            skipWhiteSpace();
            currentChar = contents.charAt(position);

            if (currentChar == '"') {
                position++;
                while (contents.charAt(position) != '"') {
                    newKey = newKey + contents.charAt(position);

                    switch (currentChar) {
                        case '"' -> {
                            // fileObj.put(newKey, parseString()); 
                        }
                        default -> {

                        }
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