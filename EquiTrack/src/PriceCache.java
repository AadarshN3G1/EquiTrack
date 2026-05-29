import java.util.*;

public class PriceCache {
    private Map<String, Double> cache = new HashMap<>();
    private Map<String, Long> timestamps = new HashMap<>();
    private final long TTL = 30000; // 30 seconds
    public Double get(String symbol) {
        if (!cache.containsKey(symbol)) return null;
        long now = System.currentTimeMillis();
        if (now - timestamps.get(symbol) > TTL) {
            cache.remove(symbol);
            timestamps.remove(symbol);
            return null;
        }
        return cache.get(symbol);
    }
    public void put(String symbol, double price) {
        cache.put(symbol, price);
        timestamps.put(symbol, System.currentTimeMillis());
    }
}