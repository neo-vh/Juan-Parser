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
    try{
        writes(map);
    }catch(IOException e){}

}
public static void writes(HashMap<String, Integer> structure) throws IOException
{ 

Set<String> kns = structure.keySet();
// kns is keys and values

String[] na = new String[kns.size()];
// na new array

Map<String, Integer> finalmap = new HashMap<>();
for(int i = 0; i< kns.size(); i++){
    na[i] = String.valueOf(kns.toArray()[i]);
    String qkns = "\"" + na[i] + "\"";
    na[i] = qkns;
    String keys = String.valueOf(kns.toArray()[i]);
    String maptojson = new String( qkns + ":" + structure.get(keys) + "}");
    String filepath = "write_testing.json";
    try{
    Files.writeString(Paths.get(filepath), "{" + maptojson);
    System.out.println("Succesfully converted map to jsonn");
    } catch(IOException e) {
        System.out.println("Encountered Error");
    }
    
// qkns quote keys and values    
}
StringJoiner json = new StringJoiner(",", "{", "}");
for(Map.Entry<String, Integer> entry : structure.entrySet()) {
    json.add("\"" + entry.getKey() + "\"" + ":" + entry.getValue());
}
Files.writeString(Path.of("write_testing.json"), json.toString()); // gebruik op die oomblik n relative path beter om absolute te gebruik (C:\Users\USER\Desktop\Juan-Parser\write_testing.json) haat jou :)


}
}
