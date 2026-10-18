import java.io.*;
import java.util.*;

public class JsonWrite {

public static void main(String[] args)
{
    HashMap<String, Integer> map = new HashMap<>();
    map.put("juan", 16);
    System.out.println("hello world !");
    writes(map);

}
public static void writes(HashMap<String, Integer> structure)
{ 
Set<String> structure3 = structure.keySet();

String[] chad = new String[structure3.size()];

for(int i = 0; i< structure3.size(); i++){
    System.out.println(structure3.toArray()[i]);
    chad[i] = String.valueOf(structure3.toArray()[i]);
    String neo = "\"" + chad[i] + "\"";
  
    chad[i] = neo;

    
}
structure3.clear();
System.out.println(Arrays.toString(chad));
System.out.println(structure3);
}
}
