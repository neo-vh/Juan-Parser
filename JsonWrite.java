import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.reflect.*;
public class JsonWrite {

String name = "juan";
public static void main(String[] args)
{
    HashMap<String, Object> test = new HashMap<>();
    test.put("j", 7);
    test.put("rope", "laalal");                                                                                                                                                                                                                                                                                                                             
    HashMap<String, Object> map = new HashMap<>();                                                                      
    map.put("juan", 16);
    map.put("neo", 15);
    map.put("liam", 56 );
    map.put("jan", test);
    map.put("scores", new int[] {12, 40, 60});
    try{
        writes(map);
    }catch(IOException e){}

}
public static void writes(HashMap<String, Object> structure) throws IOException
{ 

Set<String> kns = structure.keySet();
// kns is keys and values

String[] na = new String[kns.size()];
// na new array
for(int i = 0; i< kns.size(); i++){
    na[i] = String.valueOf(kns.toArray()[i]);
    String qkns = "\"" + na[i] + "\"";
    na[i] = qkns;
    String keys = String.valueOf(kns.toArray()[i]);
    String maptojson = new String( qkns + ":" + structure.get(keys) + "}");
    String filepath = "testing.json";
    try{
    Files.writeString(Paths.get(filepath), "{" + maptojson);
    System.out.println("Succesfully converted map to jsonn");
    } catch(IOException e) {
        System.out.println("Encountered Error");
    }
    
// qkns quote keys and values    
}
/* Files.writeString(Path.of("testing.json"), json.toString()); */
 
String json = convertMaptoJson(structure);
Files.writeString(Path.of("testing.json"), json);



/* StringJoiner json = new StringJoiner(",", "{", "}");
for(Map.Entry<String, Object> entry : structure.entrySet()) {
   if(entry.getValue() instanceof Integer ){ 
    json.add("\"" + entry.getKey() + "\"" + ":" + entry.getValue());
   } else if(entry.getValue() instanceof Boolean){
    json.add("\"" + entry.getKey() + "\"" + ":" + String.valueOf(entry.getValue()).toLowerCase());
   }else if(entry.getValue() == null) {
     json.add("\"" + entry.getKey() + "\"" + ": null");
 
   }else if(entry.getValue() instanceof String){
    json.add("\"" + entry.getKey() + "\"" + ":" + "\"" + entry.getValue()+ "\"");
   
   } */
}
public static String convertMaptoJson(Map<String, Object> map){
    StringJoiner entries = new StringJoiner(",", "{", "}");

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
        StringJoiner entries = new StringJoiner(",", "{", "}");
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


