import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.reflect.*;
public class JsonWrite {

String name = "juan";
public static void main(String[] args)
{
/*     HashMap<String, Object> test = new HashMap<>();
    test.put("j", 60679067);
    test.put("rope", "sadawad");    
     HashMap<String, Object> test3 = new HashMap<>();        
     test3.put("juan", "pielsize1");
     test3.put("neo", "peelsize0");                                                                                                                                                                                                                                                                                                                 
    HashMap<String, Object> map = new HashMap<>();                                                                      
    map.put("juan", 16);
    map.put("neo", 15);
    map.put("liam", test3 );
    map.put("jan", test);
    map.put("scores", new int[] {12, 40, 60}); */

    JsonRead reader = new JsonRead("testing.json");
    HashMap<String, Object> nananan = new HashMap<>(reader.fromJson());
    try{
        writes(nananan);
    }catch(IOException e){}

}
public static void writes(HashMap<String, Object> structure) throws IOException
{ 
    String json = convertMaptoJson(structure);
Files.writeString(Path.of("testing.json"), json);
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


