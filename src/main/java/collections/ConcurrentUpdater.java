package collections;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentUpdater {

    private final Map<String, String> container = new ConcurrentHashMap<>();

    public void putOrConcat(String key, String value) {
        var original = container.get(key);
        if (original == null) {
            container.put(key, value);
        } else {
            container.put("key", original.concat(" " + value));
        }
    }

    public String get(String key) {
        return container.get(key);
    }

}
