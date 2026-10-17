
import java.util.*;


// Goal
// create a main value parser that calls each variable types parser
// each parser should be recursive to check for multi level objects
// each variable type should have a parser

// example object to read

// {
//      "something": {
//          "age": 12,
//          "height": "5'4"
//           }
// }


public class JsonRead {

    private final String contents;
    private int position;

    public JsonRead(String contents) {
        this.contents = contents;
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


    private void parseValue() {
        while (position < contents.length()) {
            skipWhiteSpace();

            if (position >= contents.length()) {
                break;
            }

            char currentChar = contents.charAt(position);
            switch (currentChar) {
                case '"' -> {
                    System.out.println(String.format("String %c", currentChar));
                }
                case '{' -> {
                    System.out.println(String.format("Object %c", currentChar));
                }
                case '[' -> {
                    System.out.println(String.format("List %c", currentChar));
                }
                default -> {
                    if (currentChar == '-' || currentChar >= '0' && currentChar <= '9') {
                        System.out.println(String.format("Int %c", currentChar));
                    } else {
                        System.out.println(currentChar);
                    }

                }
            }
            position++;
        }
    }

    public Object parse() { // final external call for reading and returning the json file turned to an object

        // Object result = parseValue();

        return new Object();
    }

    private String parseString() {

        String value = new String();

        return value;
    }

    public static void main(String[] args) {
        String fileContents = JsonParse.readFile("C:\\Users\\USER\\Desktop\\Juan-Parser\\testing.json");
        JsonRead reader = new JsonRead(fileContents);
        reader.parseValue();
    }

}