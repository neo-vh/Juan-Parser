
import java.util.*;

public class JsonRead {

    public static void readToObj(String file) throws BadFileException {
        String contents = JsonParse.readFile(file);

        if (contents.charAt(0) != '{' || contents.charAt(-1) != '}') {
            throw new BadFileException("Bad file provided, file must start with { and end with }");
        }

        for (int n = 0; n <= 0; n++) {
            if (contents.charAt(n) == '"') {

                String newObjKey = "";

                while (contents.charAt(n + 1) != '"') {

                }
            } 
        }
        return ;
    }

}

class fileObj<K, V> {

    K key;
    V value;
    HashMap<K, V> map = new HashMap<>();

    void set(K key, V value) {
        this.key = key;
        this.value = value;

        
    }

    void get() {
        return;
    }


}

class BadFileException extends Exception {
    public BadFileException(String m) {
        super(m);
    }
}
