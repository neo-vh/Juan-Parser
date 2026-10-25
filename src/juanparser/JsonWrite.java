package juanparser;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.reflect.*;
public class JsonWrite {

public static void writes(HashMap<String, Object> structure, String path) throws IOException
{ 
    String json = convertMaptoJson(structure);
Files.writeString(Path.of(path), json);
}

public static String convertMaptoJson(Map<String, Object> map){
    StringJoiner entries = new StringJoiner(",\n    ", "{\n    ", "\n}");

    for ( Map.Entry<String, Object> entry : map.entrySet()) { 

        String key = "\"" + escapeJsonString(entry.getKey()) + "\"";
        entries.add(key + ":" + formatValue(entry.getValue()));

    }
    return entries.toString();

}
private static String formatValue(Object value) {
    if( value == null){
        return "null";
    }
    if (value instanceof String){
        return "\"" + escapeJsonString((String) value) + "\"";
    }
    if (value instanceof Number || value instanceof Boolean){
        return String.valueOf(value);
    }
    if (value.getClass().isArray()) {
    StringJoiner elements = new StringJoiner(",", "[", "]");
    for (int i = 0; i < Array.getLength(value); i++) {
        elements.add(formatValue(Array.get(value, i)));
    }
    return elements.toString();
}
if(value instanceof ArrayList){
    ((ArrayList<?>) value).size();
    StringJoiner element = new StringJoiner(",", "[", "]");
    for(int i = 0; i < ((ArrayList<?>) value).size(); i++){
        element.add(formatValue(((ArrayList<?>)value).get(i)));
    }
    return element.toString();
}
    if ( value instanceof Map<?, ?>){
        Map<?, ?> nestedkMap = (Map<?, ?>) value;
        StringJoiner entries = new StringJoiner(",\n        ", "{\n        ", "\n    }");
        for (Map.Entry<?, ?> entry : nestedkMap.entrySet()){
            String key = "\"" + escapeJsonString(String.valueOf(entry.getKey())) + "\"";
            entries.add(key + ":" + formatValue(entry.getValue()));
        }
        return entries.toString();
    }
    throw new IllegalArgumentException(
        "Unsupported value type:" + value.getClass()
        .getName()    );


}
private static String escapeJsonString(String text){
    return text
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", "\\n")
    .replace("\t", "\\t")
    .replace("\r", "\\r");
}

}


