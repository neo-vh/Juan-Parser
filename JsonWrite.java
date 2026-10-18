import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class JsonWrite {

public static void main(String[] args)
{
    HashMap<String, Integer> map = new HashMap<>();
    map.put("juan", 16);
    map.put("neo", 15);
    writes(map);

}
public static void writes(HashMap<String, Integer> structure)
{ 
Set<String> kns = structure.keySet();
// kns is keys and values
String[] na = new String[kns.size()];
// na new array
Map<String, Integer> finalmap = new HashMap<>();
for(int i = 0; i< kns.size(); i++){
    System.out.println(kns.toArray()[i]);
    na[i] = String.valueOf(kns.toArray()[i]);
    String qkns = "\"" + na[i] + "\"";
    na[i] = qkns;
    String keys = String.valueOf(kns.toArray()[i]);
    String maptojson = new String("{" + String.valueOf(qkns)+ ":" + structure.get(keys) + "}");
    String filepath = "testing.json";
    try{
    Files.writeString(Paths.get(filepath), maptojson);
    System.out.println("Succesfully converted map to jsonn");
    } catch(IOException e) {
        System.out.println("Encountered Error");
    }
// qkns quote keys and values

    
}
System.out.println(Arrays.toString(na));
System.out.println(String.valueOf(finalmap));
 
}
}
