package rabbit;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RabbitCache {
    private static final ConcurrentHashMap<String, CompletableFuture> map = new ConcurrentHashMap<>();

    public static boolean isExist(String correlationId) {
        return get(correlationId) != null;
    }

    public static CompletableFuture get(String correlationId) {
        return map.get(correlationId);
    }

    public static CompletableFuture getAndRemove(String correlationId) {
        return map.remove(correlationId);
    }

    public static void put(String correlationId, CompletableFuture future) {
        map.put(correlationId, future);
    }
}
