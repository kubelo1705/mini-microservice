package pool;

import java.util.concurrent.*;

public class WorkerPool extends ThreadPoolExecutor {
    private final String workerName;
    private static final int DEFAULT_SIZE = 5;
    private static final int KEEP_ALIVE = 5;

    public WorkerPool(String workerName) {
        super(DEFAULT_SIZE, DEFAULT_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        this.workerName = workerName;
    }
}
