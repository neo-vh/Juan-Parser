import java.io.*;
import java.util.*;

public class JsonWrite {

public static void main(String[] args)
{
    HashMap<String, Integer> map = new HashMap<>();
    map.put("juan", 16);
    Scanner sc = new Scanner(System.in);
    System.out.println("hello world !");
    String blegh = sc.nextLine();
    System.out.println(blegh);
    writes(map);

}
public static void writes(HashMap<String, Integer> structure)
{ 
LinkedList<String> chad = new LinkedList<>();
Set<String> structure3 = structure.keySet();
chad.addAll(structure3);
for(int i = 0; i< chad.size(); i++){
    String neo = chad.get(i);
    chad.set(i, "i" + neo);

    
}
structure3.clear();
structure3.addAll(chad);
System.out.println(structure3);
}
}
