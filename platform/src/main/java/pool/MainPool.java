package pool;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MainPool {
    private static final ConcurrentHashMap<String, WorkerPool> poolMap = new ConcurrentHashMap();

    public static void init(List<String> poolNames) {
        poolNames.forEach(name -> poolMap.put(name, new WorkerPool(name)));
    }

    public static void submitJob(String workerName, Runnable job) {
        WorkerPool pool = poolMap.get(workerName);
        if (pool == null) {
            System.out.println("Not exist pool with name: " + workerName);
            return;
        }
        pool.submit(job);
    }
}
