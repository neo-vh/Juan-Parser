package juanparser;

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

        position += 2; // lands after escaped character jumps from \ over character to next char since substring is exclusive for last value

        String escapedLiteral = contents.substring(backslashPos, position); // contains the entire escaped character
        char decoded; 

        switch (escapedLiteral) {
        case "\\\"" -> decoded = '"';   // JSON: \"
        case "\\\\" -> decoded = '\\';  // JSON: \\
        case "\\/"  -> decoded = '/';   // JSON: \/
        case "\\b"  -> decoded = '\b';
        case "\\f"  -> decoded = '\f';
        case "\\n"  -> decoded = '\n';
        case "\\r"  -> decoded = '\r';
        case "\\t"  -> decoded = '\t';
        default -> throw new JSONParseException(
            String.format("Invalid escaped character %s", escapedLiteral), backslashPos);
    }

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
                value = parseArray();
            }
            case '{' -> {
                value = parseDict();

            }
            default -> {

                if (c == '-' || c >= '0' && c <= '9') {
                    value = parseInteger();
                } else if (c == 'n' || c == 'f' || c == 't') {
                    value = parseBool();
                } else {
                    throw new UnknownType(String.format("Unknown type while attempting to create value %c", contents.charAt(position)), position); // doesnt follow any type patterns
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

        position++;

        return value.toString();

    }

    private Number parseInteger() { // current position is first number or digit of int value make sure it can parse decimals too

        int startOfNum;
        Number value;

        if (contents.charAt(position) == '-') { // skip - and land on first number
            startOfNum = position;
            position++;

            if (position >= contents.length()) {
                throw new JSONParseException("End of file reached while parsing Number", startOfNum);
            }

            if (contents.charAt(position) < '0' || contents.charAt(position) > '9') {
                throw new JSONParseException("Expected digit after minus sign", position);
            }

        } else { // lands on first number
            startOfNum = position;
        }

        while (position < contents.length() && contents.charAt(position) >= '0' && contents.charAt(position) <= '9') {

            position++; // advance position for each digit in value

            if (position < contents.length() && contents.charAt(position) == '.') { // if decimal encountered skip past it and make sure its the only decimal in the current version of the substring also consume the decimal and land on the char after
                
               if (!contents.substring(startOfNum, position).contains(".")) { // check if its the only decimal

                    position++; // consume decimal

               } else {

                    throw new JSONParseException("Multiple decimals found in value", startOfNum);

               }

               if (!(position < contents.length() && contents.charAt(position) >= '0' && contents.charAt(position) <= '9')) { // check next digit is valid and make sure new position is not end of file (ends on digit after decimal)

                    if (position >= contents.length()) { // end of file reached

                        throw new JSONParseException("End of file reached while checking decimal next digit", startOfNum);

                    } else { // invalid digit

                        throw new JSONParseException("No valid digit found after decimal", startOfNum);

                    }

               }


            }   

        }

        if (contents.substring(startOfNum, position).contains(".")) {

            value = Double.valueOf(contents.substring(startOfNum, position));

        } else {

            value = Integer.valueOf(contents.substring(startOfNum, position));

        }


        return value;

    }

    private List<Object> parseArray() { // starts on opening [

        List<Object> value = new ArrayList<>();

        if (contents.charAt(position) != '[') {
            
            throw new JSONParseException(String.format("Expected [ got %c", contents.charAt(position)), position);

        }

        position++; // lands on opener of contained value
        skipWhiteSpace(); // ensures position isnt random space

        if (position < contents.length() && contents.charAt(position) == ']') {
            
            position++; // consume entire value
            return value;

        } else {
            if (position >= contents.length()) {
                throw new JSONParseException("Reached end of file parsing array", position);
            }
        }



        while (position < contents.length() && contents.charAt(position) != ']') { // checks until end of array


            if (contents.charAt(position) == ',') { // if on seperator
                
                position++; // moves off seperator
                skipWhiteSpace();

                value.add(dispatchParser()); // consumes rest of value and moves forward
                continue;

            }

            value.add(dispatchParser()); // consumes first value and moves past white space
            skipWhiteSpace();

            if (position >= contents.length()) {
                throw new JSONParseException("End of file reached", position);
            }

        }

        skipWhiteSpace();

        if (contents.charAt(position) == ']') {
            position++; // ensure value is completely consumed
        } 

        return value;


    }

    private Map<String, Object> parseDict() { // starts on opening {
        
        Map<String, Object> finalValue = new HashMap<>();

        String newKey;
        Object newValue;

        position++; // move to first non white space in dict
        skipWhiteSpace();

        if (position < contents.length() && contents.charAt(position) == '}') { // check for empty dict
            
            position++;
            return finalValue;

        }

        if (position >= contents.length()) {
            throw new JSONParseException("End of file reached while attempting to parse dictionary", position);
        }

        while (position < contents.length() && contents.charAt(position) != '}') { // loop till end of file or end of dict

            if (contents.charAt(position) == '"') {
                newKey = parseString(); // consumes key and moves to colon
                skipWhiteSpace();

                if (position >= contents.length()) {
                    throw new JSONParseException("end of file reached while parsing dictionary", position);
                }

            } else {
                throw new JSONParseException(String.format("Expected start of string (double quote) got %c", contents.charAt(position)), position);
            }

            if (contents.charAt(position) != ':') {

                throw new JSONParseException(String.format("Expected Colon got %c", contents.charAt(position)), position);

            }

            position++; // move off colon to start of value
            skipWhiteSpace();

            if (position >= contents.length()) {
                throw new JSONParseException("Reached end of file while parsing for a value", position);
            }

            newValue = dispatchParser(); // dispatch parser of first char of value (will be recursive and consume and return entire value) lands on char after value

            if (position >= contents.length()) {
                throw new JSONParseException("End of file reached while parsing dictionary", position);
            }

            finalValue.put(newKey, newValue); // add value to list position still on char after last value (expected end of dictionary or comma)
            skipWhiteSpace();

            if (contents.charAt(position) == ',') { // check for seperator

                position++;
                skipWhiteSpace();

            } else if (contents.charAt(position) != '}') { // check for end of dict
                throw new JSONParseException(String.format("Expected comma or end of dict got %c", contents.charAt(position)), position);
            }

        }

        if (contents.charAt(position) == '}') {
            position++; // ensure entire dict gets consumed 
        }

        return finalValue;

    }

    private Object parseBool() { // first char of bool t f or n

        Object value = null;

        if (contents.startsWith("true", position)) {

            position += 4; // move to char right after bool value (startswith is offset by 0-3 thus length is only 3 and moving 4 positions is right after bool)
            value = true;

        } else if (contents.startsWith("false", position)) {

            position += 5; // move to char right after bool value
            value = false;

        } else if (contents.startsWith("null", position)) {

            position += 4; // move to char right after bool value 
            value = null;

        } else {
            throw new JSONParseException("Expected true false or null got none", position);
        }

        if (position >= contents.length()) {
            throw new JSONParseException("Reached end of file while consuming bool", position);
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

